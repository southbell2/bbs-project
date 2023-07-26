package proj.bbs.exception.advice;

import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import proj.bbs.exception.AccessTokenExpiredException;
import proj.bbs.exception.dto.ExceptionDTO;

@ControllerAdvice(basePackages = "proj.bbs.security.filter")
public class ExceptionAdvice {
    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> notFoundEx(AccessTokenExpiredException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(LocalDateTime.now(), e.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("WWW-Authenticate", "error='token_expired', error_description='The access token expired'");
        return new ResponseEntity<>(exceptionDTO, headers, HttpStatus.UNAUTHORIZED);
    }
}
