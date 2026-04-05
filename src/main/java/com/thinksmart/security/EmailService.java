package com.thinksmart.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email, String otp){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("ThinkSmart Email Verification");

        message.setText("Your OTP for ThinkSmart registration is: " + otp);

        mailSender.send(message);
    }

    public void sendPasswordResetOtp(String email, String otp){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("ThinkSmart Password Reset OTP");

        message.setText("Your OTP to reset your ThinkSmart password is: " + otp);

        mailSender.send(message);
    }
}