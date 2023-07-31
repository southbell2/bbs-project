package proj.bbs.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import proj.bbs.exception.UnauthorizedException;
import proj.bbs.exception.dto.ExceptionDTO;

import java.time.LocalDateTime;

@ControllerAdvice(basePackages = "proj.bbs.security.filter")
public class ExceptionAdvice {
    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> unAuthorizedException(UnauthorizedException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.UNAUTHORIZED);
    }
}
