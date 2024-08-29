package com.omm.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 설정관련
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled =  true)
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**"))
                        .permitAll()) // 로그인 안해도 모든 페이지 접근 가능하게 한다. (위 세 줄이 한 줄)
                .csrf(AbstractHttpConfigurer::disable) // csrf 는 암호화의 일정인데 여기서는 정하지 않았다(disable)
                .formLogin((formLogin) -> formLogin.loginPage("/login").defaultSuccessUrl("/")) // defaultSuccessUrl("/")는 /login 이후 어느 페이지로 이동하게 지정해주는 곳
                .logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/").invalidateHttpSession(true))
        ;
        return http.build();
    }
    @Bean // UserSecurityService에 있는 껍데기 PasswordEncoder를 객체를 주입시켜준다.
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean // 권한 관리하는 기능
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

