package com.sparta.nbcamp7lecturespringsecurityh2.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * create on 2024. 12. 23. create by IntelliJ IDEA.
 *
 * <p> 인가 실패를 처리하는 핸들러 재정의. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Component
public class DelegatedAccessDeniedHandler implements AccessDeniedHandler {

  /**
   * Spring Security 예외를 처리하기 위한 resolver.
   */
  private final HandlerExceptionResolver resolver;

  /**
   * 생성자.
   */
  public DelegatedAccessDeniedHandler(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    resolver.resolveException(request, response, null, accessDeniedException);
  }
}
