package com.sparta.nbcamp7lecturespringsecurityh2.service;

import com.sparta.nbcamp7lecturespringsecurityh2.dto.AccountRequest;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.JwtAuthResponse;
import com.sparta.nbcamp7lecturespringsecurityh2.entity.Member;
import com.sparta.nbcamp7lecturespringsecurityh2.entity.Role;
import com.sparta.nbcamp7lecturespringsecurityh2.repository.MemberRepository;
import com.sparta.nbcamp7lecturespringsecurityh2.util.AuthenticationScheme;
import com.sparta.nbcamp7lecturespringsecurityh2.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 사용자 서비스. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "Security::AccountService")
public class AccountService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  /**
   * 이메일이 중복되지 않으면 가입처리.
   *
   * @param accountRequest {@link AccountRequest}
   * @param role           {@link Role}
   * @throws DuplicateKeyException 입력받은 이메일에 대한 사용자가 이미 있을 경우
   */
  @Transactional
  public void createAccount(AccountRequest accountRequest, String role)
      throws DuplicateKeyException {
    boolean duplicated = this.memberRepository.findByEmail(accountRequest.getEmail()).isPresent();
    if (duplicated) {
      throw new DuplicateKeyException("중복된 이메일입니다.");
    }

    this.memberRepository.save(new Member(
        accountRequest.getEmail(),
        passwordEncoder.encode(accountRequest.getPassword()),
        Role.of(role)
    ));
  }

  /**
   * 로그인.
   *
   * @param accountRequest {@link AccountRequest} DTO 객체
   * @return {@link JwtAuthResponse}
   */
  public JwtAuthResponse login(AccountRequest accountRequest) {    // 사용자 확인.
    // 사용자 확인.
    Member member = this.memberRepository.findByEmail(accountRequest.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("이메일에 해당하는 사용자를 찾을 수 없습니다."));
    this.validatePassword(accountRequest.getPassword(), member.getPassword());

    // 사용자 인증 후 인증 객체를 저장
    Authentication authentication = this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            accountRequest.getEmail(),
            accountRequest.getPassword())
    );
    log.info("SecurityContext에 Authentication 저장.");
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 토큰 생성
    String accessToken = this.jwtProvider.generateToken(authentication);
    log.info("토큰 생성: {}", accessToken);

    return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken);
  }

  /**
   * 암호를 검증한다. 인코딩 전후의 암호를 입력받아 결과가 일치하는지 확인한다.
   *
   * @param rawPassword     인코딩 전의 암호
   * @param encodedPassword 인코딩 된 암호
   * @throws IllegalArgumentException 암호가 일치하지 않을 때
   */
  private void validatePassword(String rawPassword, String encodedPassword)
      throws IllegalArgumentException {
    boolean notValid = !this.passwordEncoder.matches(rawPassword, encodedPassword);
    if (notValid) {
      throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
    }
  }
}
