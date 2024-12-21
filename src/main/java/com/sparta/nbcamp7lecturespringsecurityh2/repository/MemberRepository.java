package com.sparta.nbcamp7lecturespringsecurityh2.repository;

import com.sparta.nbcamp7lecturespringsecurityh2.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> Member 엔티티의 repository. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  /**
   * 입력받은 id에 해당하는 Member를 리턴.
   *
   * @param id 가져올 Member의 id
   * @return {@link Member}
   */
  default Member findMemberById(Long id) {
    return this.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
  }
}
