package localhost.erickdomingues.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import localhost.erickdomingues.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private TestEntityManager manager;
	
	@Test
	public void deveVerificarExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		manager.persist(usuario);
		
		//ação
		boolean result = repository.existsByEmail("usuario@email.com.br");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHaUsuarioCadastradoComOEmail() {
		//ação
		boolean result = repository.existsByEmail("usuario@email.com.br");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario = criarUsuario();
		
		//ação
		Usuario usuarioSalvo = manager.persist(usuario);
		
		//verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		manager.persist(usuario);
		
		//ação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com.br");
	
		//verificação
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoBuscarUsuarioPorEmailQueNaoExisteNaBase() {
		//ação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com.br");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Usuario criarUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome("usuario");
		usuario.setEmail("usuario@email.com.br");
		usuario.setSenha("senhausuario");
		
		return usuario;
	}
}
