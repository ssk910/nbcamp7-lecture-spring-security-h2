package com.sparta.nbcamp7lecturespringsecurityh2.controller;

import com.sparta.nbcamp7lecturespringsecurityh2.dto.MemberResponse;
import com.sparta.nbcamp7lecturespringsecurityh2.dto.common.CommonResponseBody;
import com.sparta.nbcamp7lecturespringsecurityh2.service.StaffService;
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
 * <p> STAFF 권한으로 접근할 수 있는 API. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Slf4j(topic = "Security::StaffController")
@RestController
@RequestMapping(value = "/staff")
@RequiredArgsConstructor
public class StaffController {

  private final StaffService staffService;

  /**
   * STAFF 권한으로 접근 테스트.
   *
   * @return {@code ResponseEntity<CommonResponseBody<MemberResponse>>}
   */
  @GetMapping("/something")
  public ResponseEntity<CommonResponseBody<MemberResponse>> doSomethingAsStaff() {
    MemberResponse memberResponse = this.staffService.doSomethingAsStaff();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new CommonResponseBody<>("USER 권한으로 접근 성공.", memberResponse));
  }
}
