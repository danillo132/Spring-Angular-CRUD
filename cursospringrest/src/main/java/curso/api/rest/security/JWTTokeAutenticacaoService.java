package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokeAutenticacaoService {

	// Tempo de validade do token
	private static final Long EXPIRATION_TIME = (long) 172800000;

	// uma senha punica para compor a autenticacao
	private static final String SECRET = "SenhaExtremamenteSecreta";

	// Prefix padrão de token
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	// Gerando o token de autenticacao e adicionando ao cabeçalho e a resposta http

	public void addAuthentication(HttpServletResponse response, String username) throws IOException {

		// Montagem do token
		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		// Junta o token com o prefixo
		String token = TOKEN_PREFIX + " " + JWT;

		// Adiciona ao cabeçalho http
		response.addHeader(HEADER_STRING, token);
		
		ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
		.atualizaTokenUser(JWT, username);

		liberacaoCors(response);
		
		

		// Escreve o token como resposta no corpo do http
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}

	// Retorna o usuario validado com token ou caso nao seja valido retorna null

	public Authentication getAuthetication(HttpServletRequest request, HttpServletResponse response) {

		// Pega o token enviado no cabecalho HTTP

		String token = request.getHeader(HEADER_STRING);

		
		try {
		if (token != null) {

			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

			// Faz a validacoa do token do usuario na requisicao
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenLimpo).getBody().getSubject(); // Ex:
																													// Joao
																													// Silva

			if (user != null) {

				Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
						.findUserByLogin(user);

				// Retorna o usuario logado
				if (usuario != null) {

					if (tokenLimpo.equalsIgnoreCase(usuario.getToken())) {

						return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
								usuario.getAuthorities());

					}
				}

			}
		}

		}catch (ExpiredJwtException e) {
			try {
				response.getOutputStream().println("Seu token está expirado, faça o login novamente!");
			} catch (IOException e1) {
			}
		}
		liberacaoCors(response);
		return null; // nao autorizado
 
	}

	private void liberacaoCors(HttpServletResponse response) {
		if(response.getHeader("Access-Control-Allow-Origin") == null)	{
			//libeirando resposta para porta diferente do projeto angular
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Headers") == null)	{
			//libeirando resposta para porta diferente do projeto angular
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Request-Headers") == null)	{
			//libeirando resposta para porta diferente do projeto angular
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Methods") == null)	{
			//libeirando resposta para porta diferente do projeto angular
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
		
		
		
	}

	

}
