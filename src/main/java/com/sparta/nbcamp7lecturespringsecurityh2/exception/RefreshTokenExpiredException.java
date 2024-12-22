package com.sparta.nbcamp7lecturespringsecurityh2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * create on 2024. 12. 22. create by IntelliJ IDEA.
 *
 * <p> refresh token이 만료된 경우 발생하는 예외. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
public class RefreshTokenExpiredException extends ResponseStatusException {

  /**
   * 생성자.
   *
   * @param reason 예외 발생 원인
   */
  public RefreshTokenExpiredException(String reason) {
    super(HttpStatus.UNAUTHORIZED, reason);
  }
}
