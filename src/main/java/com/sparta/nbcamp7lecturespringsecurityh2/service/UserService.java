package com.sparta.nbcamp7lecturespringsecurityh2.service;

import com.sparta.nbcamp7lecturespringsecurityh2.config.auth.UserDetailsImpl;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.MemberResponse;
import com.sparta.nbcamp7lecturespringsecurityh2.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * create on 2024. 12. 23. create by IntelliJ IDEA.
 *
 * <p> USER 권한 관련 서비스. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "Security::UserService")
public class UserService {

  /**
   * USER 권한으로 처리할 로직을 정의합니다.
   *
   * @return {@link MemberResponse} 객체
   */
  public MemberResponse doSomethingAsUser() {
    // 인증 객체를 이용해 로그인 한 사용자의 정보를 가져온다.
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Member member = userDetails.getMember();

    log.info("USER 로직 실행.");

    return new MemberResponse(
        member.getId(),
        member.getEmail(),
        member.getRole().getName()
    );
  }
}
