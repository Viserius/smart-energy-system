package org.rug.wacc2019smartenergy.powerinfrastructureservice;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${jsa.rabbitmq.queue_energy_activity}")
    String queueEnergyActivity;

    @Value("${jsa.rabbitmq.exchange_single}")
    String exchangeSingleName;

    @Value("${jsa.rabbitmq.exchange_fanout")
    String exchangeFanOutName;

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
    public Queue autoDeleteQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding autoDeleteBinding() {
        return BindingBuilder.bind(autoDeleteQueue()).to(exchangeFanOut());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory (
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer
    ) {
      SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
      configurer.configure(factory, connectionFactory);
      factory.setMessageConverter(jsonMessageConverter());
      return factory;
    }
}
