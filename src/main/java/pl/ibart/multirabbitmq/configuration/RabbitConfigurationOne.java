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
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.ibart.multirabbitmq.business.AbstractListener;
import pl.ibart.multirabbitmq.business.one.ListenerOne;
import pl.ibart.multirabbitmq.business.one.OneMessageConverter;

@Configuration
public class RabbitConfigurationOne {

    private static final Logger log = LoggerFactory.getLogger(RabbitConfigurationOne.class);

    public static final String ONE = "ONE";

    @Bean
    public ConnectionFactory connectionFactoryOne() {
        log.info("Create connection factory {}", ONE);
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
        Queue one = new Queue(ONE);
        amqpAdminOne().declareQueue(one);
        return one;
    }

    @Bean
    public RabbitTemplate rabbitTemplateOne() {
        RabbitTemplate template = amqpAdminOne().getRabbitTemplate();
        template.setRoutingKey(myQueueOne().getName());
        template.setMessageConverter(oneMessageConverter());
        return template;
    }

    private RabbitAdmin amqpAdminOne() {
        return new RabbitAdmin(connectionFactoryOne());
    }

    @Bean
    public MessageConverter oneMessageConverter() {
        return new OneMessageConverter();
    }

    @Bean
    SimpleMessageListenerContainer containerOne(@Qualifier("connectionFactoryOne") ConnectionFactory connectionFactory,
                                             @Qualifier("listenerAdapterOne") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(myQueueOne().getName());
        container.setMessageListener(listenerAdapter);
        container.setDefaultRequeueRejected(true);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapterOne(ListenerOne receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, AbstractListener.METHOD);
        adapter.setMessageConverter(oneMessageConverter());
        return adapter;
    }
}
