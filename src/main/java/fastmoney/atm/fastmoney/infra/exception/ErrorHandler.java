package fastmoney.atm.fastmoney.infra.exception;

import fastmoney.atm.fastmoney.domain.exception.InvalidBalanceException;
import fastmoney.atm.fastmoney.domain.exception.InvalidPinException;
import fastmoney.atm.fastmoney.domain.exception.InvalidValueException;
import fastmoney.atm.fastmoney.domain.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleError400(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(validationDataError::new).toList());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException exception){
        var error = exception.getMessage();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidBalanceException.class)
    public ResponseEntity handleInvalidBalanceException(InvalidBalanceException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(InvalidPinException.class)
    public ResponseEntity handleInvalidPinException(InvalidPinException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity handleInvalidValueException(InvalidValueException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    private record validationDataError(String field, String message) {
        public validationDataError(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
