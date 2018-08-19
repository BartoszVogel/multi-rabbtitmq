package pl.ibart.multirabbitmq.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.StringUtils;
import pl.ibart.multirabbitmq.util.Const;
import pl.ibart.multirabbitmq.business.common.Holder;
import pl.ibart.multirabbitmq.business.common.Meta;
import pl.ibart.multirabbitmq.util.UUIDGenerator;
import pl.ibart.multirabbitmq.util.MDCContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractMessageConverter<T> implements MessageConverter {
    private static Logger logger = LoggerFactory.getLogger(AbstractMessageConverter.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected abstract Holder<T> getHolder(Message message, Meta meta) throws IOException;

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        if (!(o instanceof Holder)) {
            throw new RuntimeException("Not supported class " + o.getClass().getName());
        }
        Holder holder = (Holder) o;
        Meta meta = holder.getMeta();
        MDCContext.setCorrelationId(meta.getCorrelationId());
        MDCContext.setRequestId(meta.getRequestId());
        try {
            byte[] body = getObjectMapper().writeValueAsBytes(holder.getValue());
            logger.info("Outbound message as string {}", asString(body));
            updateMessageProperties(messageProperties, meta);
            return new Message(body, messageProperties);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            MDCContext.clearMDC();
        }
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String correlationId = getProperty(message, Const.CORRELATION_ID_HEADER);
        String requestId = getProperty(message, Const.REQUEST_ID_HEADER);
        MDCContext.setCorrelationId(correlationId);
        MDCContext.setRequestId(requestId);
        try {
            logger.info("Inbound message {}", message);
            Holder holder = getHolder(message, new Meta(correlationId, requestId));
            logger.info("Converted to: {}", holder.getValue());
            return holder;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            MDCContext.clearMDC();
        }
    }

    private void updateMessageProperties(MessageProperties messageProperties, Meta meta) {
        messageProperties.getHeaders().put(Const.CORRELATION_ID_HEADER, meta.getCorrelationId());
        messageProperties.getHeaders().put(Const.REQUEST_ID_HEADER, meta.getRequestId());
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
    }

    private String getProperty(Message message, String correlationIdHeader) {
        String property = (String) message.getMessageProperties().getHeaders().get(correlationIdHeader);
        if (StringUtils.isEmpty(property)) {
            property = UUIDGenerator.generate();
            logger.warn("Missing {}. Create new: {}.", correlationIdHeader, property);
        }
        return property;
    }


    private String asString(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}
