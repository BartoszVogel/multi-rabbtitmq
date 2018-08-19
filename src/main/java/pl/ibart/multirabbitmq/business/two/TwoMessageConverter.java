package pl.ibart.multirabbitmq.business.two;

import org.springframework.amqp.core.Message;
import pl.ibart.multirabbitmq.business.common.AbstractMessageConverter;
import pl.ibart.multirabbitmq.business.common.dto.Holder;
import pl.ibart.multirabbitmq.business.common.dto.Meta;

import java.io.IOException;

public class TwoMessageConverter extends AbstractMessageConverter<Two> {

    @Override
    protected Holder<Two> getHolder(Message message, Meta meta) throws IOException {
        Two two = getObjectMapper().readValue(message.getBody(), Two.class);
        return new Holder<>(two, meta);
    }
}
