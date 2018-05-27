package pl.ibart.multirabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiRabbitmqApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MultiRabbitmqApplication.class);

    @Autowired
    RabbitTemplate rabbitTemplateOne;

    @Autowired
    RabbitTemplate rabbitTemplateTwo;


    public static void main(String[] args) {
        SpringApplication.run(MultiRabbitmqApplication.class, args);
    }

    @Override
    public void run(String... args) {
        rabbitTemplateTwo.convertAndSend("Hello Two");
        rabbitTemplateOne.convertAndSend("Hello One");

        log.info("Receive from One: {}", rabbitTemplateOne.receive("ONE"));
        log.info("Receive from Two: {}", rabbitTemplateTwo.receive("TWO"));
    }
}
