package br.com.tavares.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/agendar", "/sucesso", "/css/**", "/img/**").permitAll()
        .requestMatchers("/admin/**").authenticated()
        .anyRequest().permitAll()
      )
      .formLogin(Customizer.withDefaults())
      .logout(Customizer.withDefaults())
      .csrf(csrf -> csrf.disable());
    return http.build();
  }

  @Bean
  InMemoryUserDetailsManager users() {
    UserDetails admin = User.withUsername("admin")
      .password("{noop}admin123")
      .roles("ADMIN")
      .build();
    return new InMemoryUserDetailsManager(admin);
  }
}
