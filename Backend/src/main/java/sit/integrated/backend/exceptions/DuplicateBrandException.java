package sit.integrated.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateBrandException extends RuntimeException {
    public DuplicateBrandException(String message) {
        super(message);
    }
}
