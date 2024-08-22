package com.spring.msvcnotifiaciones.dto;

import java.util.List;

public record KafkaMessageDto(
        String emailUsuario,
        String nombre,
        String apellido,
        List<String> urlImagesList
) {

}