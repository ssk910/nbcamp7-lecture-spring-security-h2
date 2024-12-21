package com.sparta.nbcamp7lecturespringsecurityh2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

/**
 * create on 2024. 12. 19. create by IntelliJ IDEA.
 *
 * <p> 서비스의 사용자를 정의. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
@Entity
public class Member extends BaseEntity {

  /**
   * ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 사용자 이메일.
   */
  @Column(unique = true)
  private String email;

  /**
   * 비밀번호.
   */
  @Column
  private String password;

  /**
   * 사용자의 권한. 기본값은 {@code Role.USER}입니다.
   */
  @Enumerated(value = EnumType.STRING)
  private Role role = Role.USER;

  /**
   * 기본 생성자.
   */
  public Member() {
  }

  /**
   * 생성자.
   *
   * @param email 사용자 이메일
   * @param password 비밀번호
   */
  public Member(String email, String password) {
    this.email = email;
    this.password = password;
  }

  /**
   * 생성자.
   *
   * @param email 사용자 이메일
   * @param password 비밀번호
   * @param role     사용자의 권한
   */
  public Member(String email, String password, Role role) {
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
