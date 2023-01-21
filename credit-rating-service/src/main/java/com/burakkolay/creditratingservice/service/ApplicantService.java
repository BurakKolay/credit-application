package com.burakkolay.creditratingservice.service;

import com.burakkolay.creditratingservice.config.RabbitMQConfig;
import com.burakkolay.creditratingservice.model.Applicant;
import com.burakkolay.creditratingservice.model.Credit;
import com.burakkolay.creditratingservice.repository.ApplicantRepository;
import com.burakkolay.creditratingservice.repository.CreditRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final CreditRepository creditRepository;

    private RabbitTemplate rabbitTemplate;

    public ApplicantService(ApplicantRepository applicantRepository, CreditRepository creditRepository, RabbitTemplate rabbitTemplate) {
        this.applicantRepository = applicantRepository;
        this.creditRepository = creditRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "credit-queue")
    public void processMessage(Applicant applicant){
        applicant.setCreditRating(750);

        Applicant copyApplicant = applicant.deepCopy(applicant);

        creditRepository.saveAll(applicant.getCredit());

        applicantRepository.save(copyApplicant);

        sendMessage(applicant);
    }

    public void sendMessage(Applicant applicant){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,applicant);
    }
}
