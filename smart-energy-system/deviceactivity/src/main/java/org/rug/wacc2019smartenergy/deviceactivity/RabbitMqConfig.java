package org.rug.wacc2019smartenergy.deviceactivity;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RabbitMqConfig {

    @Value("${jsa.rabbitmq.queue_energy_activity}")
    String queueEnergyActivity;

    @Value("${jsa.rabbitmq.exchange_single}")
    String exchangeSingleName;

    @Value("${jsa.rabbitmq.exchange_fanout")
    String exchangeFanOutName;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Queue queueEnergyActivity() {
        return new Queue(queueEnergyActivity, false);
    }

    @Bean
    public TopicExchange exchangeSingle() {
        return new TopicExchange(exchangeSingleName);
    }

    @Bean
    public FanoutExchange exchangeFanOut() {
        return new FanoutExchange(exchangeFanOutName);
    }

    @Bean
    public Binding bindingEnergyActivity() {
        return BindingBuilder.bind(queueEnergyActivity()).to(exchangeSingle()).with(queueEnergyActivity().getName());
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
