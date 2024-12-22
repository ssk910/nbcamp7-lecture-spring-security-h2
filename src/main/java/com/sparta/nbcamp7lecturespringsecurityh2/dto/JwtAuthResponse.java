package com.sparta.nbcamp7lecturespringsecurityh2.dto;

import lombok.AccessLevel;
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
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtAuthResponse {

  /**
   * access token 인증 방식.
   */
  private String accessTokenAuthScheme;

  /**
   * access token.
   */
  private String accessToken;

  /**
   * refresh token.
   */
  private String refreshToken;

  /**
   * 생성자.
   */
  public JwtAuthResponse(String accessTokenAuthScheme, String accessToken, String refreshToken) {
    this.accessTokenAuthScheme = accessTokenAuthScheme;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
