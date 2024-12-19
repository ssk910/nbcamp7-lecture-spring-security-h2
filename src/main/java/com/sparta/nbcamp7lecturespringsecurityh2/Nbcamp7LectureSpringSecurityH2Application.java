package com.sparta.nbcamp7lecturespringsecurityh2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 메인 클래스.
 */
@EnableJpaAuditing
@SpringBootApplication
public class Nbcamp7LectureSpringSecurityH2Application {

  public static void main(String[] args) {
    SpringApplication.run(Nbcamp7LectureSpringSecurityH2Application.class, args);
  }

}
