package com.sparta.nbcamp7lecturespringsecurityh2.dto;

import lombok.Getter;

/**
 * create on 2024. 12. 23. create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
public class MemberResponse {

  private final Long id;
  private final String email;
  private final String role;

  public MemberResponse(Long id, String email, String role) {
    this.id = id;
    this.email = email;
    this.role = role;
  }
}
