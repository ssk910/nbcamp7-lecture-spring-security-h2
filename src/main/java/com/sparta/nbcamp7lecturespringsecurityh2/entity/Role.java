package com.sparta.nbcamp7lecturespringsecurityh2.entity;

import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 사용자의 권한을 나타내는 Enum 클래스. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum Role {

  /**
   * user 권한.
   */
  USER("user"),

  /**
   * staff 권한.
   */
  STAFF("staff"),

  /**
   * admin 권한.
   */
  ADMIN("admin");

  /**
   * 권한 이름.
   */
  private final String name;

  /**
   * 생성자.
   *
   * @param name 권한 이름.
   */
  Role(String name) {
    this.name = name;
  }

  /**
   * 입력받은 값에 해당하는 {@link Role}을 찾아 리턴합니다.
   *
   * @param roleName 권한 이름.
   * @return {@link Role}
   * @throws IllegalArgumentException 입력받은 값에 해당하는 권한을 찾을 수 없는 경우
   */
  public static Role of(String roleName) throws IllegalArgumentException {
    for (Role role : values()) {
      if (role.getName().equals(roleName.toLowerCase())) {
        return role;
      }
    }

    throw new IllegalArgumentException("해당하는 이름의 권한을 찾을 수 없습니다: " + roleName);
  }

  /**
   * {@link org.springframework.security.core.userdetails.UserDetails}에 담길 권한을 리턴.
   *
   * @return 권한 리스트. {@code List<}{@link SimpleGrantedAuthority}{@code >}
   */
  public List<SimpleGrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
  }
}
