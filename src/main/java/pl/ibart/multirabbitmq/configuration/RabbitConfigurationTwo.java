package pl.ibart.multirabbitmq.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.ibart.multirabbitmq.Marker;

@Configuration
public class RabbitConfigurationTwo {

    private static final Logger log = LoggerFactory.getLogger(RabbitConfigurationTwo.class);
    public static final String TWO = "TWO";

    @Bean
    public ConnectionFactory connectionFactoryTwo() {
        log.info(Marker.RABITTMQ_TWO, "Create connection factory");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean //can be deleted
    public DirectExchange exchangeTwo() {
        DirectExchange directExchange = DirectExchange.DEFAULT;
        amqpAdminTwo().declareExchange(directExchange);
        return directExchange;
    }


    @Bean //can be deleted
    public Binding bindingTwo() {
        Binding binding = BindingBuilder.bind(myQueueTwo()).to(exchangeTwo()).withQueueName();
        amqpAdminTwo().declareBinding(binding);
        return binding;
    }

    @Bean
    public Queue myQueueTwo() {
        Queue two = new Queue(TWO);
        amqpAdminTwo().declareQueue(two);
        return two;
    }

    @Bean
    public RabbitTemplate rabbitTemplateTwo() {
        RabbitTemplate template = amqpAdminTwo().getRabbitTemplate();
        template.setRoutingKey(TWO);
        return template;
    }

    private RabbitAdmin amqpAdminTwo() {
        return new RabbitAdmin(connectionFactoryTwo());
    }
}
