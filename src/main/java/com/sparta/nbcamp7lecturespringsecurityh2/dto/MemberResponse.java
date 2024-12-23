package com.sparta.nbcamp7lecturespringsecurityh2.dto;

import lombok.Getter;

/**
 * create on 2024. 12. 23. create by IntelliJ IDEA.
 *
 * <p> 사용자 정보를 담은 response DTO 정의. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
public class MemberResponse {

  /**
   * 식별자.
   */
  private final Long id;
  /**
   * 사용자 이메일.
   */
  private final String email;
  /**
   * 사용자 권한.
   */
  private final String role;

  /**
   * 생성자.
   */
  public MemberResponse(Long id, String email, String role) {
    this.id = id;
    this.email = email;
    this.role = role;
  }
}
