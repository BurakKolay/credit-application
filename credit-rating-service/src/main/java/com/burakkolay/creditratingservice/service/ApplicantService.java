package com.burakkolay.creditratingservice.service;

import com.burakkolay.creditratingservice.config.RabbitMQConfig;
import com.burakkolay.creditratingservice.model.Applicant;
import com.burakkolay.creditratingservice.repository.ApplicantRepository;
import com.burakkolay.creditratingservice.repository.CreditRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final CreditRepository creditRepository;

    private final RabbitTemplate rabbitTemplate;

    public ApplicantService(ApplicantRepository applicantRepository, CreditRepository creditRepository, RabbitTemplate rabbitTemplate) {
        this.applicantRepository = applicantRepository;
        this.creditRepository = creditRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "credit-queue")
    public void processMessage(Applicant applicant){
        Random randomRatingGenerator = new Random();
        applicant.setCreditRating(randomRatingGenerator.nextInt(0,2000));

        Applicant copyApplicant = applicant.deepCopy(applicant);

        creditRepository.saveAll(applicant.getCredit());

        applicantRepository.save(copyApplicant);

        sendMessage(applicant);
    }

    public void sendMessage(Applicant applicant){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,applicant);
    }
}
