package com.spring.msvcnotifiaciones.service;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEmailService {

    void sendEmailWithFile(String[] toUser, String subject, String message, List<String> listFileUrl);

}
