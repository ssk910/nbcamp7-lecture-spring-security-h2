package com.sparta.nbcamp7lecturespringsecurityh2.controller;

import com.sparta.nbcamp7lecturespringsecurityh2.dto.MemberResponse;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.common.CommonResponseBody;
import com.sparta.nbcamp7lecturespringsecurityh2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create on 2024. 12. 23. create by IntelliJ IDEA.
 *
 * <p> USER 권한으로 접근할 수 있는 API. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Slf4j(topic = "Security::UserController")
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * USER 권한으로 접근 테스트.
   *
   * @return {@code ResponseEntity<CommonResponseBody<MemberResponse>>}
   */
  @GetMapping("/something")
  public ResponseEntity<CommonResponseBody<MemberResponse>> doSomethingAsUser() {
    MemberResponse memberResponse = this.userService.doSomethingAsUser();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new CommonResponseBody<>("USER 권한으로 접근 성공.", memberResponse));
  }
}
