package pl.ibart.multirabbitmq.business.two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.ibart.multirabbitmq.business.AbstractListener;
import pl.ibart.multirabbitmq.business.common.Holder;
import pl.ibart.multirabbitmq.business.common.Meta;
import pl.ibart.multirabbitmq.business.one.One;

@Service
public class ListenerTwo extends AbstractListener<Holder<Two>> {

    private static final Logger log = LoggerFactory.getLogger(ListenerTwo.class);

    private final RabbitTemplate rabbitTemplateOne;

    public ListenerTwo(RabbitTemplate rabbitTemplateOne) {
        this.rabbitTemplateOne = rabbitTemplateOne;
    }

    @Override
    protected void handle(Holder<Two> holder) {
        Two two = holder.getValue();
        log.info("ListenerTwo receive: {}", two);
        if (two.getCount() < 4) {
            One one = new One();
            one.setText(two.getText() + ", one");
            int count = two.getCount();
            one.setCount(++count);
            Meta meta = new Meta(holder.getCorrelationId());
            rabbitTemplateOne.convertAndSend(new Holder<>(one, meta));
        }
    }

}
