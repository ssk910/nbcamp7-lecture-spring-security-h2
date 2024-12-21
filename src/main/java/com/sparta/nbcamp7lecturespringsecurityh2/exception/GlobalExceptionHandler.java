package com.sparta.nbcamp7lecturespringsecurityh2.exception;

import com.sparta.nbcamp7lecturespringsecurityh2.dto.CommonResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 전역에서 예외를 처리하는 핸들러. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Validation 예외 처리.
   *
   * @param e HandlerMethodValidationException 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<String>>}
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  protected ResponseEntity<CommonResponseBody<String>> handleMethodValidationExceptions(
      HandlerMethodValidationException e) {
    String message = e.getParameterValidationResults().get(0).getResolvableErrors().get(0)
        .getDefaultMessage();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new CommonResponseBody<>(message, null));
  }

  /**
   * Validation 예외 처리.
   *
   * @param e MethodArgumentNotValidException 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<String>>}
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonResponseBody<String>> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new CommonResponseBody<>(message, null));
  }

  /**
   * Security와 관련된 AuthenticationException 예외 처리.
   *
   * @param e AuthenticationException 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<Void>>}
   */
  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<CommonResponseBody<Void>> handleAuthException(
      AuthenticationException e) {
    HttpStatus statusCode = e instanceof BadCredentialsException
        ? HttpStatus.FORBIDDEN
        : HttpStatus.UNAUTHORIZED;

    return ResponseEntity
        .status(statusCode)
        .body(new CommonResponseBody<>(e.getMessage(), null));
  }

  /**
   * 그외의 예외 처리.
   *
   * @param e 예외 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<Void>>}
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<CommonResponseBody<Void>> handleOtherExceptions(Exception e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new CommonResponseBody<>(e.getMessage(), null));
  }
}
