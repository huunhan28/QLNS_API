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
    public static final String[] twilio = {"990f5d247e1710c7589ac8afe6307ce4"};//6824fd93f0de908cf99b7aa0ec7b227b
    																			//6824fd93f0de908cf99b7aa0ec7b227b
 
    public static void send(String phoneNumber, String messageString) { 
        Twilio.init(ACCOUNT_SID, twilio[0]); 
        Message message = Message.creator( 
                new com.twilio.type.PhoneNumber(phoneNumber),  
                "MGd7591077f48b9bb44046d63489183f76", 
                messageString)      
            .create(); 
 
        System.out.println(message.getSid()); 
    } 
}