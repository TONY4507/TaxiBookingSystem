//package com.project.TaxiBookingSystem;
//
//
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.AuthenticationManagerBuilder;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfiguration {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Bean
//    public JwtFilter jwtFilter() {
//        return new JwtFilter(jwtUtil);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()
//            .authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/customer/**", "/driver/**", "/cab/**").authenticated()
//                .anyRequest().permitAll()
//            .and()
//            .formLogin()
//            .and()
//            .httpBasic(); // Enable basic authentication for testing
//
//        return http.build();
//    }
//  
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//            .withUser("admin").password("{noop}admin123").roles("ADMIN")
//            .and()
//            .withUser("user").password("{noop}user123").roles("USER");
//    }
//    
//
//}
