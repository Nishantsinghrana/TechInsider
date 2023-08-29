package com.blog.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.blog.Security.CustomUserdetailService;
import com.blog.Security.JwtAuthenticationEntryPoint;
import com.blog.Security.JwtAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebMvc
public class SecurityConfig  {
	public static final String[] PUBLIC_URLS = {  "/api/v1/auth/**","/v3/api-docs","/swagger-resources/**","/webjars/**","/v3/api-docs"};
	@Autowired
	private  CustomUserdetailService customUserDetailService;
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
	http.csrf() 
	.disable() 
	.authorizeHttpRequests()
	.requestMatchers(PUBLIC_URLS).permitAll()
	.requestMatchers(HttpMethod.GET).permitAll()
	.anyRequest().authenticated() 
	.and().exceptionHandling() 
	.authenticationEntryPoint (this.jwtAuthenticationEntryPoint)
	.and()
	.sessionManagement()   
	.sessionCreationPolicy(SessionCreationPolicy. STATELESS);
	http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	http.authenticationProvider(daoAuthenticationProvider());
	  
	DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
	return defaultSecurityFilterChain;
}
@Bean
public DaoAuthenticationProvider daoAuthenticationProvider()
{

	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

	provider.setUserDetailsService(this.customUserDetailService);

	provider.setPasswordEncoder(passwordEncoder());

	return provider;

}
	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.csrf().disable().authorizeHttpRequests().anyRequest().authenticated().
	 * and().httpBasic(); }
	 */
	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception{auth.userDetailsService
	 * (this.customerUserDetailService).passwordEncoder(passwordEncoder()); }
	 */
@Bean 
public PasswordEncoder passwordEncoder() { 
	return new BCryptPasswordEncoder();
}
@Bean
public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception
{

	return configuration.getAuthenticationManager();

}
@Bean
public FilterRegistrationBean coresFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.addAllowedOriginPattern("*");
    corsConfiguration.addAllowedHeader("authorization");
    corsConfiguration.addAllowedHeader("Content-Type");
    corsConfiguration.addAllowedHeader("Accept");
    corsConfiguration.addAllowedMethod("POST");
    corsConfiguration.addAllowedMethod("GET");
    corsConfiguration.addAllowedMethod("DELETE");
    corsConfiguration.addAllowedMethod("PUT");
    corsConfiguration.addAllowedMethod("OPTIONS");
    corsConfiguration.setMaxAge(3600L);
    source.registerCorsConfiguration("/**", corsConfiguration);

    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

    bean.setOrder(-110);

    return bean;
}
}