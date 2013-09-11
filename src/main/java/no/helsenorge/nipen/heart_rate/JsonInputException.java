package main.java.no.helsenorge.nipen.heart_rate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class JsonInputException extends RuntimeException {
    public JsonInputException(Throwable cause) {
        super(cause);
    }
}