package com.hansol.common.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.hansol.common.security.JwtAccessDeniedHandler;
import com.hansol.common.security.JwtAuthenticationEntryPoint;
import com.hansol.common.security.JwtAuthenticationFilter;
import com.hansol.common.security.JwtAuthenticationProvider;
import com.hansol.common.security.MemberDetailsService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final MemberDetailsService memeberDetailsService;
	private final JwtAuthenticationProvider tokenProvider;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final JwtAccessDeniedHandler accessDeniedHandler;
	
	public static final String[] SWAGGER_URLS = {
			"/api-test/**", "/swagger-ui/**", "/v3/api-docs/**"
	};
	
	public static final String[] PERMIT_ALL_URLS = {
			"/", "/error/**",
			"/api/members/username-check",
			"/api/members/email-check",
			"/api/members/register",
			"/api/members/login"
	};
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring()
	    	.antMatchers(SWAGGER_URLS)
	    	.antMatchers(PERMIT_ALL_URLS);
	    web.httpFirewall(defaultHttpFirewall());
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.cors()
				.configurationSource(corsConfigurationSource())
		    .and()
		        .csrf().disable()
		        .httpBasic().disable()
		        .formLogin().disable()
		        .headers().frameOptions().disable()
	        .and()
	        	.sessionManagement()
		    	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		    .and()
		    	.exceptionHandling()
		        .authenticationEntryPoint(authenticationEntryPoint)
		        .accessDeniedHandler(accessDeniedHandler)
		    .and()
		        .authorizeRequests()
		            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
		            .antMatchers(SWAGGER_URLS).permitAll()
		            .antMatchers(PERMIT_ALL_URLS).permitAll()
		            .anyRequest().authenticated();
			
			http.addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
	}
	
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(memeberDetailsService)
               .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(Arrays.asList("http://localhost:8080"));
        corsConfig.setAllowedMethods(Arrays.asList("HEAD", "OPTIONS", "GET", "POST", "PUT", "DELETE"));
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Content-Length", "Cache-Control"));
        corsConfig.setAllowCredentials(false);
        corsConfig.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration("/**", corsConfig);

        return corsConfigSource;
    }
    
    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
}