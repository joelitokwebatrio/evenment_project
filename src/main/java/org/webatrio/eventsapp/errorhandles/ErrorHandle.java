package org.webatrio.eventsapp.errorhandles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webatrio.eventsapp.enums.ErrorCode;
import org.webatrio.eventsapp.exceptions.*;
import org.webatrio.eventsapp.models.ErrorMessage;

import java.time.LocalDate;

@RestControllerAdvice
public class ErrorHandle {

    @ExceptionHandler(EventErrorException.class)
    public ResponseEntity<ErrorMessage> handleException(EventErrorException exception) {
        final HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorMessage errorMessage = ErrorMessage.builder()
                .status(ErrorCode.INTERNAL_SERVE_ERROR.value)
                .httpStatus(internalServerError)
                .message(exception.getMessage())
                .timestamp(LocalDate.now())
                .build();
        return new ResponseEntity<>(errorMessage, internalServerError);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(EventNotFoundException exception) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;

        final ErrorMessage errorMessage = ErrorMessage.builder()
                .status(ErrorCode.NOT_FOUND.value)
                .httpStatus(notFound)
                .message(exception.getMessage())
                .timestamp(LocalDate.now())
                .build();
        return new ResponseEntity<>(errorMessage, notFound);
    }

    @ExceptionHandler(EventAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> handleException(EventAlreadyExistException exception) {
        final HttpStatus alreadyReported = HttpStatus.ALREADY_REPORTED;
        final ErrorMessage errorMessage = ErrorMessage.builder()
                .status(ErrorCode.ALREADY_EXIST.value)
                .httpStatus(alreadyReported)
                .message(exception.getMessage())
                .timestamp(LocalDate.now())
                .build();
        return new ResponseEntity<>(errorMessage, alreadyReported);
    }

    @ExceptionHandler(ParticipantAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> handleException(ParticipantAlreadyExistException exception) {
        final HttpStatus alreadyReported = HttpStatus.ALREADY_REPORTED;
        final ErrorMessage errorMessage = ErrorMessage.builder()
                .status(ErrorCode.ALREADY_EXIST.value)
                .httpStatus(alreadyReported)
                .message(exception.getMessage())
                .timestamp(LocalDate.now())
                .build();
        return new ResponseEntity<>(errorMessage, alreadyReported);
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(ParticipantNotFoundException exception) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        final ErrorMessage errorMessage = ErrorMessage.builder()
                .status(ErrorCode.ALREADY_EXIST.value)
                .httpStatus(notFound)
                .message(exception.getMessage())
                .timestamp(LocalDate.now())
                .build();
        return new ResponseEntity<>(errorMessage, notFound);
    }
}
