package pl.ibart.multirabbitmq.business.common;

import lombok.Data;
import pl.ibart.multirabbitmq.util.UUIDGenerator;

@Data
public class Meta implements WithCorrelationId, WithRequestId {
    private String correlationId;
    private String requestId;

    public Meta(String correlationId) {
        this.correlationId = correlationId;
        this.requestId = UUIDGenerator.generate();
    }

    public Meta(String correlationId, String requestId) {
        this.correlationId = correlationId;
        this.requestId = requestId;
    }
}
