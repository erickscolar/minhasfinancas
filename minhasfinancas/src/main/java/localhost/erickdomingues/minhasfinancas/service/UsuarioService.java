package localhost.erickdomingues.minhasfinancas.service;

import java.util.Optional;

import localhost.erickdomingues.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	public Usuario autenticar(String email, String senha);
	
	public Usuario salvarUsuario(Usuario usuario);
	
	public void validarEmail(String email);
	
	public Optional<Usuario> buscarPorId(Long id);
}
