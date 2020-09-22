package localhost.erickdomingues.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import localhost.erickdomingues.minhasfinancas.api.dto.LancamentoDTO;
import localhost.erickdomingues.minhasfinancas.exceptions.RegraNegocioException;
import localhost.erickdomingues.minhasfinancas.model.entity.Lancamento;
import localhost.erickdomingues.minhasfinancas.model.entity.Usuario;
import localhost.erickdomingues.minhasfinancas.model.enums.StatusLancamento;
import localhost.erickdomingues.minhasfinancas.model.enums.TipoLancamento;
import localhost.erickdomingues.minhasfinancas.service.LancamentoService;
import localhost.erickdomingues.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

	private LancamentoService service;
	private UsuarioService usuarioService;
	
	public LancamentoController(LancamentoService service, UsuarioService usuarioService) {
		this.service = service;
		this.usuarioService = usuarioService;
	}
	
	@GetMapping
	public ResponseEntity buscar(
		@RequestParam(value = "descricao", required = false) String descricao,
		@RequestParam(value = "mes", required = false) Integer mes,
		@RequestParam(value = "ano", required = false) Integer ano,
		@RequestParam(value = "usuario") Long idUsuario
	){
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.buscarPorId(idUsuario);
		if (usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível encontrar o usuário.");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento lancamento = converter(dto);
			lancamento = service.salvar(lancamento);
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("id")
	public ResponseEntity atualizar(@PathVariable("id") Long id, LancamentoDTO dto) {
		return service.buscarPorID(id).map(entity ->  {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("id")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		return service.buscarPorID(id).map(entity -> {
			service.excluir(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(
				() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST)
			);
	}
	
	public Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService.buscarPorId(dto.getUsuario())
			.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o ID informado"));
		
		lancamento.setUsuario(usuario);
		if (dto.getTipo() != null ) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		if (dto.getStatus() != null ) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}

		return lancamento;
	}
}
