package localhost.erickdomingues.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import localhost.erickdomingues.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
}
