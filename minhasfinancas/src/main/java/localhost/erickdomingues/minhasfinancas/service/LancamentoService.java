package localhost.erickdomingues.minhasfinancas.service;

import java.util.List;
import java.util.Optional;

import localhost.erickdomingues.minhasfinancas.model.entity.Lancamento;
import localhost.erickdomingues.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	public Lancamento salvar(Lancamento lancamento);
	
	public Lancamento atualizar(Lancamento lancamento);
	
	public void excluir(Lancamento lancamento);
	
	public List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	public void validar(Lancamento lancamento);
	
	public Optional<Lancamento> buscarPorID(Long id);
}
