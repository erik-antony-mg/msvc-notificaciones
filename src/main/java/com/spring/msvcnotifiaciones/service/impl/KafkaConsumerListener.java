package com.spring.msvcnotifiaciones.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.msvcnotifiaciones.dto.KafkaMessageDto;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerListener {

    private final Logger LOGGER= LoggerFactory.getLogger(KafkaConsumerListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private EmailServiceImpl emailService;

    @KafkaListener(topics = "${kafka.topic.name}",groupId = "${kafka.group-id}")
    public void listener(@NonNull JsonNode jsonNode){
        try {
            KafkaMessageDto mensajeKafka = objectMapper.treeToValue(jsonNode, KafkaMessageDto.class);

            String[] destinatario = { mensajeKafka.emailUsuario() };
            String asunto= "factura de la tienda SportFlex del cliente "+mensajeKafka.nombre()+" "+mensajeKafka.apellido();
            String mensaje="factura por las compras realizadas en la tienda SportFlex si tiene algun problema con problema con la factura enviar un mensaje al correo sportFlex.gmail.com";

            emailService.sendEmailWithFile(destinatario,asunto,mensaje,mensajeKafka.urlImagesList());

            LOGGER.info("Mensaje Recibido el mensaje es :{}", mensajeKafka.toString());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error a la hora de deserealizar el mensaje", e);
        }
    }

}