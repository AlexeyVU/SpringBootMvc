package springBoot.SpringBootMVC.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageError> handleResourceNotFound(ResourceNotFoundException e) {
        MessageError messageError = new MessageError("Server error", e.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(messageError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageError> handleBadRequest(Exception e) {
        MessageError messageError = new MessageError("Bad Request", e.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messageError);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageError> handleServerError(Exception e) {
        MessageError messageError = new MessageError("Bad Request", e.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(messageError);
    }
}
