package proj.bbs.exception.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionDTO {
    private LocalDateTime timestamp;
    private String message;
}
