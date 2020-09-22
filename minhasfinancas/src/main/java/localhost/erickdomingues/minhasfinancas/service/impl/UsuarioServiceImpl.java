package localhost.erickdomingues.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import localhost.erickdomingues.minhasfinancas.exceptions.ErroAutenticacao;
import localhost.erickdomingues.minhasfinancas.exceptions.RegraNegocioException;
import localhost.erickdomingues.minhasfinancas.model.entity.Usuario;
import localhost.erickdomingues.minhasfinancas.model.repository.UsuarioRepository;
import localhost.erickdomingues.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para este e-mail.");
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		if(repository.existsByEmail(email)) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail!");
		}		
	}

	@Override
	public Optional<Usuario> buscarPorId(Long id) {
		return repository.findById(id);
	}
}
