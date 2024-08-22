package com.spring.msvcnotifiaciones.service.impl;

import com.spring.msvcnotifiaciones.service.IEmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service

public class EmailServiceImpl implements IEmailService {

    @Autowired
    private  JavaMailSender javaMailSender;
    @Value("${username.email}")
    private String emailuser;



    @Override
    public void sendEmailWithFile(String[] toUser, String subject, String message, List<String> listFileUrl) {

        List<File> tempFiles = new ArrayList<>();
        try {
            for (String fileUrl : listFileUrl) {
                URL url = new URL(fileUrl);
                InputStream inputStream = url.openStream();
                File tempFile = File.createTempFile("cloudinary-", ".jpg");
                FileOutputStream outputStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                tempFiles.add(tempFile);  // Agregar el archivo temporal a la lista
            }

            // Configuración del correo
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            mimeMessageHelper.setFrom(emailuser);
            mimeMessageHelper.setTo(toUser);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);

            // Adjuntar todos los archivos descargados al correo
            for (File tempFile : tempFiles) {
                mimeMessageHelper.addAttachment(tempFile.getName(), tempFile);
            }

            // Enviar el correo
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // Eliminar todos los archivos temporales después de enviarlos
            for (File tempFile : tempFiles) {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
    }

}
