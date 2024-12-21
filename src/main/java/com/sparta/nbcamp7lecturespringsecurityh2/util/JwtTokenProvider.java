package com.sparta.nbcamp7lecturespringsecurityh2.util;

import com.sparta.nbcamp7lecturespringsecurityh2.entity.Member;
import com.sparta.nbcamp7lecturespringsecurityh2.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p>JWT 제공자.</p>
 * <p>토큰의 생성, 추출, 만료 확인 등의 기능.</p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

  /**
   * JWT 시크릿 키.
   */
  @Value("${jwt.secret}")
  private String jwtSecret;

  /**
   * 토큰 만료시간(밀리초).
   */
  @Getter
  @Value("${jwt.expiry-millis}")
  private long jwtExpirationMillis;

  /**
   * Member repository.
   */
  private final MemberRepository memberRepository;

  /**
   * <p>토큰 생성 후 리턴.</p>
   * 입력받은 {@link Authentication}에서 추출한 {@code username}으로 {@link #generateTokenBy(String)} 메소드를
   * 이용한다.
   *
   * @param authentication 인증 완료된 후 세부 정보
   * @return 생성된 토큰
   * @throws EntityNotFoundException 입력받은 이메일에 해당하는 사용자를 찾지 못했을 경우
   */
  public String generateToken(Authentication authentication) throws EntityNotFoundException {
    String username = authentication.getName();
    return this.generateTokenBy(username);
  }

  /**
   * 입력받은 토큰에서 {@link Authentication}의 {@code username}을 리턴.
   *
   * @param token 토큰
   * @return username
   */
  public String getUsername(String token) {
    Claims claims = this.getClaims(token);
    return claims.getSubject();
  }

  /**
   * 토큰이 유효한지 확인.
   *
   * @param token 토큰
   * @return 유효 여부.
   * <ul>
   *   <li>{@code true} - 유효함.</li>
   *   <li>{@code false} - 유효하지 않음.</li>
   * </ul>
   */
  public boolean isValidToken(String token) {
    try {
      return !this.isTokenExpired(token);
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  /**
   * <p>이메일 주소를 이용해 토큰을 생성한 후 리턴.</p>
   * <p>토큰 생성에는 HS256 알고리즘을 이용.</p>
   *
   * @param email 이메일
   * @return 생성된 토큰
   * @throws EntityNotFoundException 입력받은 이메일에 해당하는 사용자를 찾지 못했을 경우
   */
  private String generateTokenBy(String email) throws EntityNotFoundException {
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationMillis);
    Member member = this.memberRepository.findMemberByEmail(email);

    return Jwts.builder()
        .subject(email)
        .issuedAt(currentDate)
        .expiration(expireDate)
        .claim("Role", member.getRole())
        .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
        .compact();
  }

  /**
   * JWT의 claim 부분을 추출.
   *
   * @param token 토큰
   * @return {@link Claims}
   * @see <a href="https://ko.wikipedia.org/wiki/JSON_%EC%9B%B9_%ED%86%A0%ED%81%B0">JSON 웹 토큰</a>
   */
  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * 입력받은 토큰의 만료 여부.
   *
   * @param token 토큰
   * @return 만료 여부
   * <ul>
   *   <li>{@code true} - 만료됨.</li>
   *   <li>{@code false} - 만료되지 않음.</li>
   * </ul>
   */
  private Boolean isTokenExpired(String token) {
    if (!StringUtils.hasText(token)) {
      throw new IllegalArgumentException("Token string is empty.");
    }

    final Date expiration = this.getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * 입력 받은 토큰의 만료일을 리턴.
   *
   * @param token 토큰
   * @return 만료일
   */
  private Date getExpirationDateFromToken(String token) {
    return this.resolveClaims(token, Claims::getExpiration);
  }

  /**
   * 토큰에 입력 받은 로직을 적용하고 그 결과를 리턴.
   *
   * @param token          토큰
   * @param claimsResolver 토큰에 적용할 로직.
   * @param <T>            {@code claimsResolver}의 리턴 타입.
   * @return {@code T}
   */
  private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = this.getClaims(token);
    return claimsResolver.apply(claims);
  }
}
