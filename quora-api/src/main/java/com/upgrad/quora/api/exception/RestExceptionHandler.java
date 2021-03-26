package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException afe, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(afe.getCode()).message(afe.getErrorMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException userNF, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(userNF.getCode()).message(userNF.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException signUpE, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(signUpE.getCode()).message(signUpE.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException afe, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(afe.getCode()).message(afe.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException signOut, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(signOut.getCode()).message(signOut.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException iqe, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(iqe.getCode()).message(iqe.getErrorMessage()), HttpStatus.NOT_FOUND);
    }
}
