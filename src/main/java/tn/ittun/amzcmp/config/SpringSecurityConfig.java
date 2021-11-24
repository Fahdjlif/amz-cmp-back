
package tn.ittun.amzcmp.config;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import tn.ittun.amzcmp.jwt.JwtTokenFilterConfigurer;
import tn.ittun.amzcmp.jwt.JwtTokenProvider;
import tn.ittun.amzcmp.service.UserDetailService;


@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired private UserDetailService	userDetailsService;
	@Autowired private JwtTokenProvider		jwtTokenProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
	{
		authenticationManagerBuilder.userDetailsService( userDetailsService);
	}

	@Override
	@Bean(name = "authenticationManager")
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		// Disable CSRF (cross site request forgery)
		http.csrf().disable();

		// No session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS);

		// Entry points
		http.cors().and().authorizeRequests()//
			.antMatchers( "/api/login/**").permitAll()//
			.antMatchers( "/api/create/user").permitAll()//
			.antMatchers( "/api/search/**").permitAll().antMatchers( "/api/account/getall").permitAll()//
			.and().authorizeRequests()//
			.antMatchers( "/api/email/**").permitAll()//
			// Entry points
			.and().authorizeRequests()//
			.antMatchers( "/api/signup/**").permitAll()//
			.and().authorizeRequests()//
			.antMatchers( "/api/activate/**").permitAll().and().authorizeRequests()////
			.antMatchers( "/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
			// Entry points
			.and().authorizeRequests()//
			// Disallow everything else..
			.anyRequest().authenticated();

		// If a user try to access a resource without having enough permissions
		http.exceptionHandling().accessDeniedPage( "/login");

		// Apply JWT
		http.apply( new JwtTokenFilterConfigurer( jwtTokenProvider));
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource()
	{
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins( new ArrayList<>( Arrays.asList( "*")));
		configuration.setAllowedMethods( new ArrayList<>( Arrays.asList( "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")));
		// setAllowCredentials(true) is important, otherwise:
		// The value of the 'Access-Control-Allow-Origin' header in the response must
		// not be the wildcard '*' when the
		// request's credentials mode is 'include'.
		configuration.setAllowCredentials( true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		// will fail with 403 Invalid CORS request
		configuration.setAllowedHeaders( new ArrayList<>( Arrays.asList( "Authorization", "Cache-Control", "Content-Type")));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration( "/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}