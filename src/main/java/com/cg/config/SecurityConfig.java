package com.cg.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cg.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig 
{
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
	                                               DaoAuthenticationProvider provider) throws Exception {

	    return http.csrf(csrf -> csrf.disable())
	            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	            .authenticationProvider(provider)
	            .authorizeHttpRequests(auth -> auth
	                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                    .requestMatchers("/generateToken").permitAll()
	                    .requestMatchers(HttpMethod.POST, "/auth/register/employee").hasRole("ADMIN")
	                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
	                    .requestMatchers(HttpMethod.POST, "/auth/register/customer").permitAll()
	                    .requestMatchers(HttpMethod.POST, "/auth/register/supplier").permitAll()
	                    .requestMatchers(HttpMethod.GET, "/auth/me").permitAll()

	                    .requestMatchers(HttpMethod.GET, "/api/pets/**").permitAll()
	                    .requestMatchers(HttpMethod.GET, "/api/pet-categories/**").permitAll()
	                    .requestMatchers(HttpMethod.GET, "/api/pet-foods/**").permitAll()
	                    .requestMatchers(HttpMethod.GET, "/api/grooming/**").permitAll()
	                    .requestMatchers(HttpMethod.GET, "/api/vaccinations/**").permitAll()

	                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
	                    .requestMatchers("/api/employees/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.GET, "/api/transactions/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.POST, "/api/transactions/**").hasAnyRole("ADMIN", "EMPLOYEE", "CUSTOMER")
	                    .requestMatchers(HttpMethod.PUT, "/api/transactions/*/status").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers("/api/transactions/**").hasRole("ADMIN")
	                    .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "EMPLOYEE", "CUSTOMER")
	                    .requestMatchers("/api/suppliers/**").hasAnyRole("ADMIN", "EMPLOYEE","SUPPLIER")

	                    .requestMatchers(HttpMethod.POST, "/api/pets/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.PUT, "/api/pets/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.DELETE, "/api/pets/**").hasRole("ADMIN")

	                    .requestMatchers(HttpMethod.POST, "/api/pet-categories/**").hasRole("ADMIN")
	                    .requestMatchers(HttpMethod.PUT, "/api/pet-categories/**").hasRole("ADMIN")
	                    .requestMatchers(HttpMethod.DELETE, "/api/pet-categories/**").hasRole("ADMIN")

	                    .requestMatchers(HttpMethod.POST, "/api/pet-foods/**").hasRole("ADMIN")
	                    .requestMatchers(HttpMethod.PUT, "/api/pet-foods/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.DELETE, "/api/pet-foods/**").hasRole("ADMIN")

	                    .requestMatchers(HttpMethod.POST, "/api/grooming/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.PUT, "/api/grooming/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.DELETE, "/api/grooming/**").hasRole("ADMIN")

	                    .requestMatchers(HttpMethod.POST, "/api/vaccinations/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.PUT, "/api/vaccinations/**").hasAnyRole("ADMIN", "EMPLOYEE")
	                    .requestMatchers(HttpMethod.DELETE, "/api/vaccinations/**").hasRole("ADMIN")

	                    .anyRequest().authenticated()
	            )
	            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	            .build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:4200"));
		config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
