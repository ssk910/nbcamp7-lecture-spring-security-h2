package com.sparta.nbcamp7lecturespringsecurityh2.exception;

import com.sparta.nbcamp7lecturespringsecurityh2.dto.common.CommonResponseBody;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 전역에서 예외를 처리하는 핸들러. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
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
    String message = e.getParameterValidationResults().getFirst().getResolvableErrors().getFirst()
        .getDefaultMessage();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new CommonResponseBody<>(message));
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
    String message = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new CommonResponseBody<>(message));
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
        .body(new CommonResponseBody<>(e.getMessage()));
  }

  /**
   * Security와 관련된 AccessDeniedException 예외 처리.
   *
   * @param e AccessDeniedException 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<Void>>}
   */
  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<CommonResponseBody<Void>> handleAccessDeniedException(
      AccessDeniedException e) {

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(new CommonResponseBody<>(e.getMessage()));
  }

  /**
   * Security와 관련된 AuthorizationDeniedException 예외 처리.
   *
   * @param e AuthorizationDeniedException 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<Void>>}
   */
  @ExceptionHandler(AuthorizationDeniedException.class)
  protected ResponseEntity<CommonResponseBody<Void>> handleAuthorizationDeniedException(
      AuthorizationDeniedException e) {

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(new CommonResponseBody<>(e.getMessage()));
  }

  /**
   * JWT와 관련된 JwtException 예외 처리.
   *
   * @param e JwtException 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<Void>>}
   */
  @ExceptionHandler(JwtException.class)
  protected ResponseEntity<CommonResponseBody<Void>> handleJwtException(JwtException e) {
    HttpStatus httpStatus = e instanceof ExpiredJwtException
        ? HttpStatus.UNAUTHORIZED
        : HttpStatus.FORBIDDEN;

    return ResponseEntity
        .status(httpStatus)
        .body(new CommonResponseBody<>(e.getMessage()));
  }

  /**
   * ResponseStatusException 예외 처리.
   *
   * @param e ResponseStatusException 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<Void>>}
   */
  @ExceptionHandler(ResponseStatusException.class)
  protected ResponseEntity<CommonResponseBody<Void>> handleResponseStatusExceptions(
      ResponseStatusException e) {
    return ResponseEntity
        .status(e.getStatusCode())
        .body(new CommonResponseBody<>(e.getMessage()));
  }

  /**
   * 그외의 예외 처리.
   *
   * @param e 예외 인스턴스
   * @return {@code ResponseEntity<CommonResponseBody<Void>>}
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<CommonResponseBody<Void>> handleOtherExceptions(Exception e) {
    log.error(e.getMessage());
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new CommonResponseBody<>(e.getMessage()));
  }
}
