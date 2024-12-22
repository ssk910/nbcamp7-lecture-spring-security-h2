package com.sparta.nbcamp7lecturespringsecurityh2.controller;

import com.sparta.nbcamp7lecturespringsecurityh2.dto.JwtAuthResponse;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.AccessTokenReissueRequest;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.common.CommonResponseBody;
import com.sparta.nbcamp7lecturespringsecurityh2.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create on 2024. 12. 22. create by IntelliJ IDEA.
 *
 * <p> access token 재발급을 위한 API. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/token")
@RequiredArgsConstructor
public class TokenController {

  private final RefreshTokenService refreshTokenService;

  /**
   * access token 재발급.
   *
   * @param accessTokenReissueRequest DTO.
   * @return {@code ResponseEntity<CommonResponseBody<JwtAuthResponse>>}
   */
  @PostMapping("/reissue")
  public ResponseEntity<CommonResponseBody<JwtAuthResponse>> reissueRefreshToken(
      @Valid @RequestBody AccessTokenReissueRequest accessTokenReissueRequest) {
    JwtAuthResponse jwtAuthResponse = this.refreshTokenService.reissueAccessToken(
        accessTokenReissueRequest);

    return ResponseEntity.ok(new CommonResponseBody<>("재발급 성공", jwtAuthResponse));
  }
}
