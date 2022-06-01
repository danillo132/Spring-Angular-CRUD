package curso.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Telefone;

@Repository
public interface TelefoneRepository extends CrudRepository<Telefone,Long> {

	
	@Query(value = "INSERT INTO Telefone values(?1,'?2')", nativeQuery = true)
	Telefone saveTel(String numero, Long id);
	
	
}
