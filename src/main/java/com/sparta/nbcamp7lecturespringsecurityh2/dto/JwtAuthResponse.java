package com.sparta.nbcamp7lecturespringsecurityh2.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> JWT 정보가 담긴 response DTO. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtAuthResponse {

  /**
   * access token.
   */
  private String accessToken;

  /**
   * 토큰 종류.
   */
  private String tokenType;

  /**
   * 토큰 만료 시간.
   */
  private Long expiresIn;
}
