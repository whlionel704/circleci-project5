package sg.edu.ntu.javaproject.Exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullException.class)
    public ResponseEntity<ErrorResponse> handleNullException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AccountNumberExistsException.class, AccountTypeIsExistException.class,
            InsufficientBalanceException.class, EmailIsExistException.class })
    public ResponseEntity<ErrorResponse> handleResourceIsExistException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ AccountNotFoundException.class, AccountTypeIsNotExistException.class,
            CustomerAndAccountNotFoundException.class, CustomerNotFoundException.class,
            AccountNumberIsNotExistException.class })
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<ObjectError> validationErrors = e.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();

        for (ObjectError error : validationErrors) {
            sb.append(error.getDefaultMessage() + ". ");
        }
        log.error(sb.toString());
        ErrorResponse errorResponse = new ErrorResponse(sb.toString(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse("Unique constraint violation occurred. Details: " + message,
                LocalDateTime.now());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), LocalDateTime.now()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ ForbiddenAccessException.class })
    public ResponseEntity<Object> handleForbiddenAccessException(RuntimeException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), LocalDateTime.now()), HttpStatus.FORBIDDEN);
    }
}
