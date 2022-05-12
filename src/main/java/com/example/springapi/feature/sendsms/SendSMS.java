package com.example.springapi.feature.sendsms;

import com.twilio.Twilio; 
import com.twilio.converter.Promoter; 
import com.twilio.rest.api.v2010.account.Message; 
import com.twilio.type.PhoneNumber; 
 
import java.net.URI; 
import java.math.BigDecimal; 
 
public class SendSMS { 
    // Find your Account Sid and Token at twilio.com/console 
    public static final String ACCOUNT_SID = "AC3de7bece048e8fedacd4e45436b6f790"; 
    public static final String AUTH_TOKEN = "b1f97f85a0141d60cea96df4f0407255";//
 
    public static void send(String phoneNumber, String messageString) { 
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN); 
        Message message = Message.creator( 
                new com.twilio.type.PhoneNumber(phoneNumber),  
                "MGd7591077f48b9bb44046d63489183f76", 
                messageString)      
            .create(); 
 
        System.out.println(message.getSid()); 
    } 
}