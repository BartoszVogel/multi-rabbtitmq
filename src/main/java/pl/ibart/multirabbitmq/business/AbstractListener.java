package pl.ibart.multirabbitmq.business;

import pl.ibart.multirabbitmq.business.common.WithCorrelationId;
import pl.ibart.multirabbitmq.business.common.WithRequestId;
import pl.ibart.multirabbitmq.business.common.WithValue;
import pl.ibart.multirabbitmq.util.MDCContext;

public abstract class AbstractListener<T extends WithCorrelationId & WithRequestId & WithValue> {

    public static final String METHOD = "receiveMessage";

    public void receiveMessage(T holder) {
        MDCContext.setCorrelationId(holder.getCorrelationId());
        MDCContext.setRequestId(holder.getRequestId());
        try {
            handle(holder);
        } finally {
            MDCContext.clearMDC();
        }
    }

    protected abstract void handle(T holder);
}
