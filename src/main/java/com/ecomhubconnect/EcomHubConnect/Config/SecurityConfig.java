package com.ecomhubconnect.EcomHubConnect.Config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.ecomhubconnect.EcomHubConnect.Session.InMemorySessionRegistry;
import com.ecomhubconnect.EcomHubConnect.Session.SessionFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private InMemorySessionRegistry inMemorySessionRegistry;
	
	 @Autowired
	    private SessionFilter sessionFilter;

	@Bean
	public UserDetailsService getDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/logout","/login","/saveUser").permitAll()
        .anyRequest().authenticated();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		http.logout().
		logoutSuccessHandler(new LogoutSuccessHandler() {
		                    @Override
		                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
		                            Authentication authentication) throws IOException, ServletException {
		                    	System.out.println(request.getHeader(HttpHeaders.AUTHORIZATION));
		                    	inMemorySessionRegistry.removeSession(request.getHeader(HttpHeaders.AUTHORIZATION));
//		                    	System.out.println("User logged out: " + authentication.getName());
		                    	response.setStatus(HttpServletResponse.SC_OK);
		                        response.getWriter().write("Logout successful");
		                    }
		                });

		http.addFilterBefore(
                sessionFilter,
                UsernamePasswordAuthenticationFilter.class
        );


		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	            .build();
	}
	

}