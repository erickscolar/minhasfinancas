package localhost.erickdomingues.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import localhost.erickdomingues.minhasfinancas.exceptions.RegraNegocioException;
import localhost.erickdomingues.minhasfinancas.model.entity.Lancamento;
import localhost.erickdomingues.minhasfinancas.model.enums.StatusLancamento;
import localhost.erickdomingues.minhasfinancas.model.repository.LancamentoRepository;
import localhost.erickdomingues.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repository.save(lancamento);
		
	}

	@Override
	@Transactional
	public void excluir(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example<Lancamento> example = Example.of(lancamentoFiltro, ExampleMatcher.matching()
																	.withIgnoreCase()
																	.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma descrição válida.");
		}
		
		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("O mês não pode ser vazio e tem de estar entre 1 e 12.");
		}
		
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4 || lancamento.getAno() > Calendar.getInstance().get(Calendar.YEAR)) {
			throw new RegraNegocioException("O ano não pode ser nulo, deve ter 4 caracteres e não pode ser maior que o ano atual.");
		}
		
		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um usuário.");
		}
		
		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioException("O valor deve ser válido.");
		}
		
		if (lancamento.getTipo() == null ) {
			throw new RegraNegocioException("Informe um tipo de lançamento.");
		}
	}

	@Override
	public Optional<Lancamento> buscarPorID(Long id) {
		return repository.findById(id);
	}
}
