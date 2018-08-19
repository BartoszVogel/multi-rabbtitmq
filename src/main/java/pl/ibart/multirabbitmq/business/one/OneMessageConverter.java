package pl.ibart.multirabbitmq.business.one;

import org.springframework.amqp.core.Message;
import pl.ibart.multirabbitmq.business.common.AbstractMessageConverter;
import pl.ibart.multirabbitmq.business.common.dto.Holder;
import pl.ibart.multirabbitmq.business.common.dto.Meta;

import java.io.IOException;

public class OneMessageConverter extends AbstractMessageConverter<One> {

    protected Holder<One> getHolder(Message message, Meta meta) throws IOException {
        One one = getObjectMapper().readValue(message.getBody(), One.class);
        return new Holder<>(one, meta);
    }
}
