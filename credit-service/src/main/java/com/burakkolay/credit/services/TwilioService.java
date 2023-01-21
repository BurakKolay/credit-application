package com.burakkolay.credit.services;

import com.burakkolay.credit.model.entity.Applicant;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {
    public static final String ACCOUNT_SID="AC5d320aeddc050e5b70eeb89ada338967";
    public static final String AUTH_TOKEN="9618763b529db90847cce8811ae0dfc6";

    public void sendSMS(Applicant applicant){
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Dear"+applicant.getFirstName()+" "+ applicant.getLastName()+", your credit rating is"+applicant.getCreditRating());

        Message message = Message.creator(new PhoneNumber("+905369378309"),new PhoneNumber("+17695532440"),stringBuilder.toString()).create();

    }
}
