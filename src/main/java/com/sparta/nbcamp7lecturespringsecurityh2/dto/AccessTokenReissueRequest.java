package com.sparta.nbcamp7lecturespringsecurityh2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * create on 2024. 12. 22. create by IntelliJ IDEA.
 *
 * <p> access token 재발급 DTO. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
public class AccessTokenReissueRequest {

  /**
   * refresh token.
   */
  @NotBlank(message = "refreshToken은 빈 값이 될 수 없습니다.")
  private final String refreshToken;

  /**
   * 생성자.
   */
  public AccessTokenReissueRequest(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
