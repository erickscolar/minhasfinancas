package localhost.erickdomingues.minhasfinancas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import localhost.erickdomingues.minhasfinancas.exceptions.ErroAutenticacao;
import localhost.erickdomingues.minhasfinancas.exceptions.RegraNegocioException;
import localhost.erickdomingues.minhasfinancas.model.entity.Usuario;
import localhost.erickdomingues.minhasfinancas.model.repository.UsuarioRepository;
import localhost.erickdomingues.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	private UsuarioServiceImpl service;
	
	@MockBean
	private UsuarioRepository repository;
	
	@Test
	public void deveSalvarUmUsuario() {
		//cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = new Usuario();
		usuario.setId(1l);
		usuario.setNome("Usuario");
		usuario.setEmail("usuario@email.com.br");
		usuario.setSenha("usuariosenha");
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//ação
		Assertions.assertDoesNotThrow(() -> {
			Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
			
			//verificação
			Assertions.assertNotNull(usuarioSalvo);
			Assertions.assertNotNull(usuarioSalvo.getId().equals(1l));
			Assertions.assertNotNull(usuarioSalvo.getNome().equals("Usuario"));
			Assertions.assertNotNull(usuarioSalvo.getEmail().equals("usuario@email.com.br"));
			Assertions.assertNotNull(usuarioSalvo.getSenha().equals("usuariosenha"));
		});
	}
	
	@Test
	public void naoDeveCadastrarUmUsuarioComEmailJaCadastrado() {
		String email = "usuario@email.com.br";
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//ação
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			service.salvarUsuario(usuario);
		});
		
		//verificação
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test
	public void deveValidarUsuarioComSucesso() {
		//cenário
		String email = "usuario@email.com.br";
		String senha = "senhausuario";
		
		Usuario usuario = new Usuario();
		usuario.setId(1l);
		usuario.setNome("Usuario");
		usuario.setEmail(email);
		usuario.setSenha(senha);
		
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//ação
		Assertions.assertDoesNotThrow(() -> {
			Usuario result = service.autenticar(email, senha);
			
		//verificação
			Assertions.assertNotNull(result);
		});
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		//cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//ação e verificação
		
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.autenticar("usuario@email.com.br", "1234"));
		
		org.assertj.core.api.Assertions.assertThat(exception)
			.isInstanceOf(ErroAutenticacao.class)
			.hasMessage("Usuário não encontrado para este e-mail.");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaEstiverIncorreta() {
		//cenário
		String email = "usuario@email.com.br";
		String senha = "senhausuario";

		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(senha);
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//ação
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.autenticar(email, "123"));
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
	}
	
	@Test
	public void deveValidarEmail() {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação e verificação
		Assertions.assertDoesNotThrow(() -> {
			service.validarEmail("usuario@email.com.br");
		});
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
			service.validarEmail("usuario@email.com.br");
		});
	}
}
