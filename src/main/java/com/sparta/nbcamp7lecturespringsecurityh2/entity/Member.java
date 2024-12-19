package com.sparta.nbcamp7lecturespringsecurityh2.entity;

import jakarta.persistence.Entity;
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
   * 기본 생성자.
   */
  public Member() {
  }

}
