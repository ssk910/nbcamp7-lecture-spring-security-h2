package com.sparta.nbcamp7lecturespringsecurityh2.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 토큰 타입. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum TokenType {
  BEARER("Bearer");

  private final String name;

  /**
   * Authorization 헤더의 값으로 사용될 prefix를 생성.
   *
   * @param tokenType {@link TokenType}
   * @return 생성된 prefix
   */
  public static String generateHeaderPrefix(TokenType tokenType) {
    return tokenType.getName() + " ";
  }
}
