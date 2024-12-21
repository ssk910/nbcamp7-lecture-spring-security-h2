package com.sparta.nbcamp7lecturespringsecurityh2.controller;

import com.sparta.nbcamp7lecturespringsecurityh2.dto.AccountRequest;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.JwtAuthResponse;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.common.CommonResponseBody;
import com.sparta.nbcamp7lecturespringsecurityh2.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 가입 및 로그인을 위한 API. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequestMapping(value = "/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  /**
   * 회원가입.
   *
   * @param accountRequest {@link AccountRequest}
   * @param role           권한
   * @return {@code ResponseEntity<CommonResponseBody<String>>}
   */
  @PostMapping("/join")
  public ResponseEntity<CommonResponseBody<String>> join(
      @Valid @RequestBody AccountRequest accountRequest,
      @NotBlank @RequestParam String role) {
    this.accountService.createAccount(accountRequest, role);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new CommonResponseBody<>("회원가입 완료."));
  }

  /**
   * 로그인.
   *
   * @param accountRequest {@link AccountRequest}
   * @return {@code ResponseEntity<CommonResponseBody<JwtAuthResponse>>}
   */
  @PostMapping("/login")
  public ResponseEntity<CommonResponseBody<JwtAuthResponse>> login(
      @Valid @RequestBody AccountRequest accountRequest) {
    JwtAuthResponse authResponse = this.accountService.login(accountRequest);

    return ResponseEntity.ok(new CommonResponseBody<>("로그인 성공", authResponse));
  }

  /**
   * 로그아웃.
   *
   * @param request        {@link HttpServletRequest}
   * @param response       {@link HttpServletResponse}
   * @param authentication 인증 객체 (Authentication은 controller에서 주입받을 수 있음)
   * @return {@code ResponseEntity<CommonResponseBody<String>>}
   * @throws UsernameNotFoundException 인증되지 않은 경우
   */
  @PostMapping("/logout")
  public ResponseEntity<CommonResponseBody<String>> logout(HttpServletRequest request,
      HttpServletResponse response, Authentication authentication)
      throws UsernameNotFoundException {
    // 인증 정보가 있다면 로그아웃 처리.
    if (authentication != null && authentication.isAuthenticated()) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
      return ResponseEntity.ok(new CommonResponseBody<>("로그아웃 성공."));
    }

    // 인증 정보가 없다면 인증되지 않았기 때문에 로그인 필요.
    throw new UsernameNotFoundException("로그인이 먼저 필요합니다.");
  }

  /**
   * 현재 사용자의 인증 정보를 확인하기 위한 엔드 포인트.
   *
   * @param authentication 인증 객체 (Authentication은 controller에서 주입받을 수 있음)
   * @return {@code ResponseEntity<CommonResponseBody<?>>}
   */
  @GetMapping("/me")
  public ResponseEntity<CommonResponseBody<?>> aboutMe(Authentication authentication) {
    return ResponseEntity.ok(
        new CommonResponseBody<>(
            "현재 사용자의 인증 객체에 대한 정보입니다.",
            authentication.getPrincipal()
        ));
  }
}
