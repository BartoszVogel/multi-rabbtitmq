package pl.ibart.multirabbitmq;

import org.slf4j.MarkerFactory;

public interface Marker {

    org.slf4j.Marker RABITTMQ_ONE = MarkerFactory.getMarker("RABITTMQ_ONE");
    org.slf4j.Marker RABITTMQ_TWO = MarkerFactory.getMarker("RABITTMQ_TWO");

}
