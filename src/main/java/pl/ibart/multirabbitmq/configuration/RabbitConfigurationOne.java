package pl.ibart.multirabbitmq.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.ibart.multirabbitmq.Marker;

@Configuration
public class RabbitConfigurationOne {

    private static final Logger log = LoggerFactory.getLogger(RabbitConfigurationOne.class);

    @Bean
    public ConnectionFactory connectionFactoryOne() {
        log.info(Marker.RABITTMQ_ONE, "Create connection factory");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public DirectExchange exchangeOne() {
        DirectExchange directExchange = new DirectExchange("");
        amqpAdminOne().declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding bindingOne() {
        Binding binding = BindingBuilder.bind(myQueueOne()).to(exchangeOne()).withQueueName();
        amqpAdminOne().declareBinding(binding);
        return binding;
    }

    @Bean
    public Queue myQueueOne() {
        Queue one = new Queue("ONE");
        amqpAdminOne().declareQueue(one);
        return one;
    }

    @Bean
    public RabbitTemplate rabbitTemplateOne() {
        RabbitTemplate template = amqpAdminOne().getRabbitTemplate();
        template.setRoutingKey(myQueueOne().getName());
        return template;
    }

    private RabbitAdmin amqpAdminOne() {
        return new RabbitAdmin(connectionFactoryOne());
    }
}
