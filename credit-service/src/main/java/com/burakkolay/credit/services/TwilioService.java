package com.burakkolay.credit.services;

import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;

    public void sendSMS(Applicant applicant, Credit credit){
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber(applicant.getPhoneNumber()),new PhoneNumber("+17695532440"),
                "Dear" + applicant.getFirstName() + " " + applicant.getLastName() + ", your credit result is " +
                credit.getCreditResult()+" and your credit limit is "+ credit.getCreditBalance()).create();
    }
}
