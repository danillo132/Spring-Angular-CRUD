package curso.api.rest.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Telefone;
import curso.api.rest.model.UserChart;
import curso.api.rest.model.UserReport;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.TelefoneRepository;
import curso.api.rest.repository.UsuarioRepository;
import curso.api.rest.service.ImplementacaoUserDetailsService;
import curso.api.rest.service.RelatorioService;


@CrossOrigin
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TelefoneRepository repository;
	
	@Autowired
	private ImplementacaoUserDetailsService detailsService;
	
	@Autowired
	private RelatorioService relatorioService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> initV1(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario>  usuario = usuarioRepository.findById(id);
		
		
		
		return  new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	


	
	
	@CrossOrigin
	@GetMapping(value = "/listar", produces = "application/json")
	public ResponseEntity<Page<Usuario>> TodosUsers() throws InterruptedException{
		
		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
		
		Page<Usuario> lista = usuarioRepository.findAll(page);
		
		
		
		return new ResponseEntity<Page<Usuario>>(lista, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> TodosUsersPage(@PathVariable("pagina") int pagina) throws InterruptedException{
		
		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));
		
		Page<Usuario> lista = usuarioRepository.findAll(page);
		
		
		
		return new ResponseEntity<Page<Usuario>>(lista, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> usuarioPorNome(@PathVariable("nome") String nome) throws InterruptedException{
		
		PageRequest pageRequest = null;
		Page<Usuario> list = null;		
				if(nome == null || (nome != null && nome.trim().isEmpty()) 
						|| (nome.equalsIgnoreCase("undefined"))) {
				pageRequest = PageRequest.of(0,5,Sort.by("nome"));
				list = usuarioRepository.findAll(pageRequest);
				}else {
					pageRequest = PageRequest.of(0,5,Sort.by("nome"));
					list = usuarioRepository.findUserByNamePage(nome,pageRequest );
				}
		
	
		
		
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value = "/usuarioPorNome/{nome}/page/{page}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> usuarioPorNome(@PathVariable("nome") String nome,@PathVariable("page") int page) throws InterruptedException{
		
		PageRequest pageRequest = null;
		Page<Usuario> list = null;		
				if(nome == null || (nome != null && nome.trim().isEmpty()) 
						|| (nome.equalsIgnoreCase("undefined"))) {
				pageRequest = PageRequest.of(page,5,Sort.by("nome"));
				list = usuarioRepository.findAll(pageRequest);
				}else {
					pageRequest = PageRequest.of(page,5,Sort.by("nome"));
					list = usuarioRepository.findUserByNamePage(nome,pageRequest );
				}
		
	
		
		
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/relatorio", produces = "application/text")
	public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception{
		
		byte[] pdf = relatorioService.gerarRelatorio("relatorio-usuario", new HashMap(), request.getServletContext());
	
		String base64pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64pdf, HttpStatus.OK);
	}
	
	@PostMapping(value = "/relatorio/", produces = "application/text")
	public ResponseEntity<String> downloadRelatorioParam(HttpServletRequest request, @RequestBody UserReport userReport) throws Exception{
		

		System.out.println(userReport.getDataInicio());
		System.out.println(userReport.getDataFim());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dateFormatParam = new SimpleDateFormat("yyyy-MM-dd");
		
		String dataInicio = dateFormatParam.format(dateFormat.parse(userReport.getDataInicio()));
		
		String dataFim = dateFormatParam.format(dateFormat.parse(userReport.getDataFim()));
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("DATA_INICIO", dataInicio);
		params.put("DATA_FIM", dataFim);
		
		
		byte[] pdf = relatorioService.gerarRelatorio("relatorio-usuario-param", params, request.getServletContext());
	
		String base64pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64pdf, HttpStatus.OK);
	}
	
	
	
	@PostMapping(value = "/cadastrar", produces = "application/json")
	public ResponseEntity<Usuario> cadastrarUser(@RequestBody Usuario usuario) throws Exception{
		for (Telefone telefone : usuario.getTelefones()) {
			System.out.println("ID USER: " + telefone.getUsuario().getId() + " ID TEL: " + telefone.getId());
			
		}
		
		/*URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
		URLConnection connection = url.openConnection();
		
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		String cep  = "";
		
		StringBuilder jsonCep = new StringBuilder();
		
		while((cep = br.readLine()) != null) {
			jsonCep.append(cep);
		}
		
		Usuario usuarioAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		
		
		usuario.setCep(usuarioAux.getCep());
		usuario.setLogradouro(usuarioAux.getLogradouro());
		usuario.setBairro(usuarioAux.getBairro());
		usuario.setComplemento(usuarioAux.getComplemento());
		usuario.setLocalidade(usuarioAux.getLocalidade());
		usuario.setUf(usuarioAux.getUf());*/
		
	
		String senhacrip = new BCryptPasswordEncoder().encode(usuario.getSenha());
		
		usuario.setSenha(senhacrip);
		
		for(int pos = 0 ;pos < usuario.getTelefones().size();pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		
		
		
		Usuario usuariosalvo = usuarioRepository.save(usuario);
		detailsService.insereAcessoPadrao( usuariosalvo.getId());
		
		return new ResponseEntity<Usuario>(usuariosalvo, HttpStatus.OK); 
	}
	
	
	
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable("id") Long id) {
		
		usuarioRepository.deleteById(id);
		
		return "ok";
		
	}

	@PutMapping(value = "/atualizar", produces = "application/json")
	public ResponseEntity<Usuario> atualizarUser(@RequestBody Usuario usuario){
		
	
			
		for(int pos = 0 ;pos < usuario.getTelefones().size();pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
	for (Telefone telefone : usuario.getTelefones()) {
		System.out.println("ID USER: " + telefone.getUsuario().getId() + " ID TEL: " + telefone.getId());
		
	}
			
		Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();
		
		if(!userTemporario.getSenha().equals(usuario.getSenha())) {
			String senhacrip = new BCryptPasswordEncoder().encode(usuario.getSenha());
			
			usuario.setSenha(senhacrip);
		}
		
		
		
		Usuario usuariosalvo = usuarioRepository.save(usuario);
		
		
		return new ResponseEntity<Usuario>(usuariosalvo, HttpStatus.OK); 
		
	}
	
	@DeleteMapping(value = "/removerTelefone/{id}", produces = "application/text")
	public String deletetelefone(@PathVariable("id") Long id) {
	
			repository.deleteById(id);
			
		return "Ok";
	}
	
	@GetMapping(value = "/grafico", produces = "application/json")
	public ResponseEntity<UserChart> grafico(){
		
		UserChart chart = new UserChart();
		
	List<String> resultado = jdbcTemplate.queryForList("select array_agg(nome) from usuario where salario > 0 union all select cast(array_agg(salario) as character varying[])  from usuario where salario > 0",String.class);
		
	if(!resultado.isEmpty()) {
		String nomes = resultado.get(0).replaceAll("\\{", "").replaceAll("\\}", "");
		String salario = resultado.get(1).replaceAll("\\{", "").replaceAll("\\}", "");
		
		chart.setNome(nomes);
		chart.setSalario(salario);
	}
		
		
		return new ResponseEntity<UserChart>(chart,HttpStatus.OK);
	}
		
}
