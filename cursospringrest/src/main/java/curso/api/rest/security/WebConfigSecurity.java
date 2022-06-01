package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private ImplementacaoUserDetailsService detailsService;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//Ativando a proteção contra usuários que não estão validados por token
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		//Ativando a permissão para a página inicial do sistema
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index", "/recuperar/**").permitAll()
		
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		//Url de logout - redireciona após o user deslogar do sistema
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		//Filtra as requisições de login para autenticação 
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		
		//Filtra demais requisições para verificar a presença do token JWT 
	}
	

	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(detailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		
	}
}
