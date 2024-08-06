package ru.gb.timesheet.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain noSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(it -> it.anyRequest().permitAll())
                .build();
    }


////    @Bean
//SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    return http
//            .authorizeHttpRequests(requests -> requests
////                        .requestMatchers("/home/timesheets/**").hasAuthority("user")
////                        .requestMatchers("/home/projects/**").hasAuthority("admin")
//                            .requestMatchers("/projects/**", "/timesheets/**").hasAuthority("rest")
//                            .anyRequest().authenticated()
//            )
//            .formLogin(Customizer.withDefaults())
//            .build();
//}

@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
}
