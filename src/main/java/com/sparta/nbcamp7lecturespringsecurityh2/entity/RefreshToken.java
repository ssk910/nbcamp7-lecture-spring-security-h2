package com.sparta.nbcamp7lecturespringsecurityh2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import lombok.Getter;

/**
 * create on 2024. 12. 22. create by IntelliJ IDEA.
 *
 * <p> Refresh Token. </p>
 * <p> 일반적으로 refresh token은 캐시를 이용한 DB에 따로 저장합니다. 이 프로젝트는 단순한 예제 코드입니다. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
@Entity
public class RefreshToken {

  /**
   * ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * 토큰.
   */
  private String token;

  /**
   * 토큰 만료일.
   */
  private Instant expiryDate;

  /**
   * 사용자 정보.
   */
  @OneToOne
  @JoinColumn(name = "member_id", referencedColumnName = "id")
  private Member member;

  public RefreshToken() {
  }

  public RefreshToken(String token, Instant expiryDate, Member member) {
    this.token = token;
    this.expiryDate = expiryDate;
    this.member = member;
  }

  public void updateToken(String token, Instant expiryDate) {
    this.token = token;
    this.expiryDate = expiryDate;
  }
}
