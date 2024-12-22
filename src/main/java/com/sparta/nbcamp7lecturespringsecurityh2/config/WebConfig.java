package com.sparta.nbcamp7lecturespringsecurityh2.config;

import com.sparta.nbcamp7lecturespringsecurityh2.config.filter.JwtAuthFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 웹 설정. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig {

  /**
   * JWT 인증 Filter.
   */
  private final JwtAuthFilter jwtAuthFilter;

  /**
   * AuthenticationProvider.
   */
  private final AuthenticationProvider authenticationProvider;

  /**
   * AuthenticationEntryPoint.
   */
  private final AuthenticationEntryPoint authEntryPoint;

  /**
   * 화이트 리스트.
   */
  private static final String[] WHITE_LIST = {"/accounts/login", "/favicon.ico", "/error"};

  /**
   * security 필터.
   *
   * @param http {@link HttpSecurity}
   * @return {@link SecurityFilterChain} 필터 체인
   */
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth ->
                auth.requestMatchers(WHITE_LIST).permitAll()
                    // static 경로
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll()
//                    .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
//                .requestMatchers("/auth/admin").hasRole("ADMIN")
                    .requestMatchers("/accounts/logout").hasAnyRole("ADMIN", "STAFF", "USER", "GUEST")
                    .anyRequest().authenticated()
        )
        // Spring Security 예외에 대한 처리를 핸들러에 위임.
        .exceptionHandling(handler ->
            handler.authenticationEntryPoint(authEntryPoint)
        )
        // SecurityContext를 가져올 때 HttpSession을 사용하지 않도록 설정.
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * 사용자 권한의 계층을 설정.
   * @return {@link RoleHierarchy}
   */
  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy(
        // "ROLE_ADMIN > ROLE_STAFF > ROLE_GUEST\nROLE_ADMIN > ROLE_USER > ROLE_GUEST"
        """
            ROLE_ADMIN > ROLE_STAFF > ROLE_GUEST
            ROLE_ADMIN > ROLE_USER > ROLE_GUEST
            """);
  }

  /**
   * h2-console 접속은 Spring Security를 거치지 않도록 설정.
   *
   * @return {@link WebSecurityCustomizer}
   * @see <a
   * href="https://dukcode.github.io/spring/h2-console-with-spring-security/">spring-security에서-h2-console-사용하기</a>
   */
  @Bean
  @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
  public WebSecurityCustomizer configureH2ConsoleEnable() {
    return web -> web.ignoring().requestMatchers(PathRequest.toH2Console());
  }
}
