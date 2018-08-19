package pl.ibart.multirabbitmq.business.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Holder<T> implements WithValue<T> ,WithCorrelationId, WithRequestId{
    private T value;
    private Meta meta;

    @Override
    public String getCorrelationId() {
        return meta.getCorrelationId();
    }

    @Override
    public String getRequestId() {
        return meta.getRequestId();
    }
}
