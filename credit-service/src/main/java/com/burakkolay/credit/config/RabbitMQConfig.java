package com.burakkolay.credit.config;


import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.convert.TypeMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "credit-queue";
    public static final String EXCHANGE = "credit-exchange";
    public static final String ROUTING_KEY = "credit-routing-key";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        Jackson2JsonMessageConverter converter= new Jackson2JsonMessageConverter();
//        converter.setClassMapper(classMapper());
        return converter;
    }

//    @Bean
//    public DefaultClassMapper classMapper() {
//        DefaultClassMapper classMapper = new DefaultClassMapper();
//        Map<String, Class<?>> idClassMapping = new HashMap<>();
//        idClassMapping.put("Applicant", Applicant.class);
//        idClassMapping.put("Credit", Credit.class);
//        classMapper.setIdClassMapping(idClassMapping);
//        classMapper.setTrustedPackages("*");
//        return classMapper;
//    }
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
