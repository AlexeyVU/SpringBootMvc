package springBoot.SpringBootMVC.handler;

import java.time.LocalDateTime;

public record MessageError(String message, String detailedMessage, LocalDateTime errorTime) {

}
