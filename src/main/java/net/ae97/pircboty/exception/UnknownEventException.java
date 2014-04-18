package net.ae97.pircboty.exception;

import net.ae97.pircboty.PircBotY;
import net.ae97.pircboty.hooks.Event;
import org.apache.commons.lang3.Validate;

public class UnknownEventException extends RuntimeException {

    private static final long serialVersionUID = 40292L;

    public UnknownEventException(Event<? extends PircBotY> event, Throwable cause) {
        super("Unknown Event " + (event == null ? null : event.getClass().toString()), cause);
        Validate.notNull(event, "Event cannot be null");
    }

    public UnknownEventException(Event<? extends PircBotY> event) {
        this(event, null);
    }
}