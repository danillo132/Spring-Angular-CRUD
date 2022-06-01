package curso.api.rest.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.api.rest.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	
	
	
	@Transactional
	@Modifying
	@Query(value="update usuario set senha = ?1 where id = ?2", nativeQuery=true)
	public void updateSenha(String senha, Long codUser);
	
	
	
	
	
	@Query("Select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
	
	@Query("Select u from Usuario u where u.email = ?1")
	Usuario findUserByEmail(String email);
	

	@Query("Select u from Usuario u where u.nome like %?1%")
	List<Usuario> findUserByNome(String nome);
	
	@Query(value = "select constraint_name  from information_schema.constraint_column_usage where table_name = 'usuarios_role' and column_name = 'role_id'\r\n"
			+ "and constraint_name <> 'unique_role_user'; ", nativeQuery = true)
	String consultaConstraintRole();
	

	@Transactional
	@Modifying
	@Query(value = "insert into usuarios_role (usuario_id, role_id) values (?1,(select id from role where nome_role ='ROLE_USER'));", nativeQuery = true)
	void insereAcesso(Long idUser);
	
	
	
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update Usuario set token = ?1 where login = ?2")
	void atualizaTokenUser(String token,String login);
	
	default Page<Usuario> findUserByNamePage(String nome, PageRequest pageRequest){
		
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		
		ExampleMatcher example =  ExampleMatcher.matchingAny()
				.withMatcher("nome",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<Usuario> lista = Example.of(usuario, example);
		
		Page<Usuario> retorno = findAll(lista, pageRequest);
		return retorno;
	}

}
