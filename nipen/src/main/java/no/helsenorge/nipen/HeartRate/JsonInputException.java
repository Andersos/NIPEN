package no.helsenorge.nipen.HeartRate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class JsonInputException extends RuntimeException {
    public JsonInputException(Throwable cause) {
        super(cause);
    }
}