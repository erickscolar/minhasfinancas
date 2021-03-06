package localhost.erickdomingues.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import localhost.erickdomingues.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	public boolean existsByEmail(String email);
	
	public Optional<Usuario> findByEmail(String email);
}
