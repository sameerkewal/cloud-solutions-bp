package com.example.cloud_solutions_bp.observerpattern;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;

public class SmsNotificationListener implements  EventListener{
    public String ACCOUNT_SID = "";
    public String AUTH_TOKEN = "";


    public SmsNotificationListener(){
        Dotenv dotenv = Dotenv.configure().load();
        ACCOUNT_SID = dotenv.get("ACC_SID");
        AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


    }


    @Override
    public void update(String eventType, String outgoingMessage, String phoneNumber) {

        Message message = Message.creator(
                        new PhoneNumber(phoneNumber),
                        new PhoneNumber("+12017629498"),
                        outgoingMessage)
                .create();

//        System.out.println(message.getSid());
    }
}
