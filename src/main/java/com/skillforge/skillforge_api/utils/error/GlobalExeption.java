package com.skillforge.skillforge_api.utils.error;


import com.skillforge.skillforge_api.entity.RestResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExeption {

    @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
    public ResponseEntity<Object> handleException(Exception e) {
        RestResponse<Object> rest = new RestResponse<Object>();
        rest.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rest.setError("Call api Failed");
        rest.setMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(rest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        final List<FieldError> result = bindingResult.getFieldErrors();

        RestResponse<Object> rest = new RestResponse<>();
        rest.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rest.setError(e.getBody().getDetail());

        List<String> errors = result.stream().map(f -> f.getDefaultMessage())
                .collect(Collectors.toList());

        rest.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(rest);
    }

    @ExceptionHandler({IllegalArgumentException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> handleBadRequest(Exception e) {
        return buildError(HttpStatus.BAD_REQUEST, "Bad Request", e.getMessage());
    }

//    @ExceptionHandler(Exception.class) // fallback
//    public ResponseEntity<Object> handleOther(Exception e) {
//        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", e.getMessage());
//    }


    private ResponseEntity<Object> buildError(HttpStatus status, String error, Object message) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status.value());
        res.setError(error);
        res.setMessage(message);
        return ResponseEntity.status(status).body(res);
    }


}
