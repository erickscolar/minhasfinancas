package localhost.erickdomingues.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import localhost.erickdomingues.minhasfinancas.model.enums.StatusLancamento;
import localhost.erickdomingues.minhasfinancas.model.enums.TipoLancamento;

@Entity
public class Lancamento {
	private Long id;
	private String descricao;
	private Integer dia;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private LocalDate dataCadastro;
	private Usuario usuario;
	private TipoLancamento tipo;
	private StatusLancamento status;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	@Column(name = "data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	public LocalDate getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	@JoinColumn(name = "id_usuario")
	@ManyToOne
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "tipo_lancamento")
	public TipoLancamento getTipo() {
		return tipo;
	}
	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "status_lancamento")
	public StatusLancamento getStatus() {
		return status;
	}
	public void setStatus(StatusLancamento status) {
		this.status = status;
	}	
}
