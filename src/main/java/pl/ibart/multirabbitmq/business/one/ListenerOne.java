package pl.ibart.multirabbitmq.business.one;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.ibart.multirabbitmq.business.common.AbstractListener;
import pl.ibart.multirabbitmq.business.common.dto.Holder;
import pl.ibart.multirabbitmq.business.common.dto.Meta;
import pl.ibart.multirabbitmq.business.two.Two;

@Service
public class ListenerOne extends AbstractListener<Holder<One>> {

    private static final Logger log = LoggerFactory.getLogger(ListenerOne.class);

    private final RabbitTemplate rabbitTemplateTwo;

    public ListenerOne(RabbitTemplate rabbitTemplateTwo) {
        this.rabbitTemplateTwo = rabbitTemplateTwo;
    }

    @Override
    protected void handle(Holder<One> holder) {
        One value = holder.getValue();
        log.info("ListenerOne receive: {}", holder);
        if (value.getCount() < 4) {
            Two two = new Two();
            two.setText(value.getText() + ", two");
            int count = value.getCount();
            two.setCount(++count);
            Meta meta = new Meta(holder.getCorrelationId());
            rabbitTemplateTwo.convertAndSend(new Holder<>(two, meta));
        }
    }

}
