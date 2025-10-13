package com.puzzlix.solid_task._global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * AppConfig - 앱 전체 관련된 설정
 * WebConfig - 웹 설정과 관련된 부분으로 만들 예쩡
 */
@Configuration  // IoC 대상
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
