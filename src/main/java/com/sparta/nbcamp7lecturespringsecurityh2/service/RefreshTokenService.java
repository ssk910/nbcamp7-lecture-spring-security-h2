package com.sparta.nbcamp7lecturespringsecurityh2.service;

import com.sparta.nbcamp7lecturespringsecurityh2.dto.AccessTokenReissueRequest;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.JwtAuthResponse;
import com.sparta.nbcamp7lecturespringsecurityh2.entity.Member;
import com.sparta.nbcamp7lecturespringsecurityh2.entity.RefreshToken;
import com.sparta.nbcamp7lecturespringsecurityh2.exception.RefreshTokenExpiredException;
import com.sparta.nbcamp7lecturespringsecurityh2.repository.MemberRepository;
import com.sparta.nbcamp7lecturespringsecurityh2.repository.RefreshTokenRepository;
import com.sparta.nbcamp7lecturespringsecurityh2.util.AuthenticationScheme;
import com.sparta.nbcamp7lecturespringsecurityh2.util.JwtProvider;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * create on 2024. 12. 22. create by IntelliJ IDEA.
 *
 * <p> refresh token 관련 서비스. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  /**
   * 토큰 만료시간(밀리초).
   */
  @Value("${jwt.refresh.expiry-millis}")
  private long tokenExpiryMillis;
  private final MemberRepository memberRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtProvider jwtProvider;

  /**
   * refresh token 생성. {@code RefreshToken}을 먼저 조회하고 만료되었다면 갱신, 조회되지 않는다면 생성한다.
   *
   * @param email 사용자 이메일.
   * @return 생성된 토큰
   */
  @Transactional
  public String generateRefreshToken(String email) {
    Member member = this.memberRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("해당 email에 맞는 값이 존재하지 않습니다."));

    // 토큰 생성 및 갱신.
    RefreshToken refreshToken = refreshTokenRepository.findByMember_Id(member.getId())
        .map(this::updateTokenIfExpired)
        .orElseGet(() -> createNewToken(member));

    this.refreshTokenRepository.save(refreshToken);
    return refreshToken.getToken();
  }

  /**
   * access token 재발급.
   * <ol>
   *   <li>인증 객체를 이용하여 저장된 refresh token 조회.</li>
   *   <li>만료일 확인. (만료된 토큰은 삭제)</li>
   *   <li>access token 재발급.</li>
   * </ol>
   *
   * @param accessTokenReissueRequest {@link AccessTokenReissueRequest}
   * @return {@link JwtAuthResponse}
   * @throws EntityNotFoundException 저장된 refresh token을 찾을 수 없는 경우
   */
  @Transactional
  public JwtAuthResponse reissueAccessToken(AccessTokenReissueRequest accessTokenReissueRequest)
      throws EntityNotFoundException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return this.refreshTokenRepository.findByToken(accessTokenReissueRequest.getRefreshToken())
        .map(this::verifyExpiration)
        .map(RefreshToken::getMember)
        .map(member -> {
          String accessToken = this.jwtProvider.generateToken(authentication);
          return new JwtAuthResponse(
              AuthenticationScheme.BEARER.getName(),
              accessToken,
              accessTokenReissueRequest.getRefreshToken()
          );
        })
        .orElseThrow(() -> new EntityNotFoundException("Refresh Token을 찾을 수 없습니다. 다시 로그인이 필요합니다."));
  }

  /**
   * RefreshToken이 만료되었다면 갱신하여 리턴한다. (만료되지 않았다면 기존 토큰을 리턴)
   *
   * @param existingToken 기존 토큰
   * @return {@link RefreshToken}
   */
  private RefreshToken updateTokenIfExpired(RefreshToken existingToken) {
    if (this.isTokenExpired(existingToken)) {
      existingToken.updateToken(
          UUID.randomUUID().toString(),
          Instant.now().plusMillis(this.tokenExpiryMillis)
      );
    }

    return existingToken;
  }

  /**
   * 입력받은 member에 해당하는 새로운 RefreshToken 엔티티를 생성.
   *
   * @param member {@link Member}
   * @return {@link RefreshToken}
   */
  private RefreshToken createNewToken(Member member) {
    return new RefreshToken(
        UUID.randomUUID().toString(),
        Instant.now().plusMillis(this.tokenExpiryMillis),
        member
    );
  }

  /**
   * refresh token의 만료 여부 확인. 만료된 토큰은 삭제 후 예외를 던집니다.
   *
   * @param refreshToken {@link RefreshToken}
   * @return 토큰(만료되지 않은 경우)
   * @throws RefreshTokenExpiredException refresh token이 만료된 경우
   */
  private RefreshToken verifyExpiration(RefreshToken refreshToken)
      throws RefreshTokenExpiredException {
    if (this.isTokenExpired(refreshToken)) {
      this.refreshTokenRepository.delete(refreshToken);
      throw new RefreshTokenExpiredException("refresh token이 만료되었습니다. 다시 로그인이 필요합니다.");
    }

    return refreshToken;
  }

  /**
   * refresh token의 만료 여부 확인.
   *
   * @param refreshToken {@link RefreshToken}
   * @return 만료 여부
   * <ul>
   *   <li>{@code true} - 만료됨.</li>
   *   <li>{@code false} - 만료되지 않음.</li>
   * </ul>
   */
  private boolean isTokenExpired(RefreshToken refreshToken) {
    return refreshToken.getExpiryDate().compareTo(Instant.now()) < 0;
  }
}