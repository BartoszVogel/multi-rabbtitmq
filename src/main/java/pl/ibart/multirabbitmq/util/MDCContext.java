package pl.ibart.multirabbitmq.util;


import org.slf4j.MDC;

import static pl.ibart.multirabbitmq.util.Const.CORRELATION_ID;
import static pl.ibart.multirabbitmq.util.Const.REQUEST_ID;


public class MDCContext {

    public static void setCorrelationId(String value) {
        MDC.put(CORRELATION_ID, value);
    }

    public static void setRequestId(String value) {
        MDC.put(REQUEST_ID, value);
    }

    public static void clearMDC() {
        MDC.clear();
    }
}
