package curso.api.rest.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.ObjetoErro;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import curso.api.rest.service.EnviaEmailService;
import net.bytebuddy.agent.builder.AgentBuilder.FallbackStrategy.Simple;

@RestController
@RequestMapping(value = "/recuperar")
public class RecuperaController {

	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EnviaEmailService emailService;
	
	@ResponseBody
	@PostMapping(value = "/")
	public ResponseEntity<ObjetoErro> recuperar(@RequestBody Usuario login) throws Exception{
		
		ObjetoErro erro = new ObjetoErro();
		
		
		
		Usuario usuario = usuarioRepository.findUserByLogin(login.getLogin());
		
		if(usuario == null) {
			erro.setCode("404"); //Não encontrado
			erro.setError("Usuário não encontrado!");
		}else {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime());
			
			String senhaCriptografada = new BCryptPasswordEncoder().encode(senhaNova);
			
			usuarioRepository.updateSenha(senhaCriptografada, usuario.getId());
			
			emailService.enviarEmail("Recuperação de senha", usuario.getLogin(), "Sua nova senha é: " + senhaNova);
			
			//Rotina de envio de e-mail
			erro.setCode("200"); //Não encontrado
			erro.setError("Acesso enviado para o seu e-mail!");
		}
		
		return new ResponseEntity<ObjetoErro>(erro, HttpStatus.OK);
	}
}
