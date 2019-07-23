package br.com.estoque.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.estoque.model.Usuario;

@Entity
public class Movimentacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	

	private Date data;	
	private String notaFiscal;
	private String cupomFiscal;
	private String motivo;
	private String observacoes;
	private String tipo;	
	private int quantidade;	
	private int rm;
	private String solicitante;


	private float valor;
	private boolean status = true;
	private boolean ajusteDeEstoque = false;

	@ManyToOne
	@JoinColumn(name="centroCusto",  nullable = true)
	private CentroCusto centroCusto = new CentroCusto();

	@ManyToOne
	@JoinColumn(name = "unidadeArmazenada" , nullable = true)
	private Unidade unidadeArmazenada = new Unidade();

	@ManyToOne
	@JoinColumn(name="aplicacao",  nullable= true)
	private Aplicacao aplicacao = new Aplicacao();

	@ManyToOne
	@JoinColumn(name="usuario",  nullable= false)
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="fornecedor",  nullable= true)
	private Fornecedor fornecedor = new Fornecedor();
	
	@ManyToOne
	@JoinColumn(name="item",  nullable= true)
	@Cascade(CascadeType.ALL)
	private Item item = new Item();
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movimentacao other = (Movimentacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isAjusteDeEstoque() {
		return ajusteDeEstoque;
	}

	public void setAjusteDeEstoque(boolean ajusteDeEstoque) {
		this.ajusteDeEstoque = ajusteDeEstoque;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getCupomFiscal() {
		return cupomFiscal;
	}

	public void setCupomFiscal(String cupomFiscal) {
		this.cupomFiscal = cupomFiscal;
	}

	public int getRm() {
		return rm;
	}

	public void setRm(int rm) {
		this.rm = rm;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Aplicacao getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(Aplicacao aplicacao) {
		this.aplicacao = aplicacao;
	}

	public Unidade getUnidadeArmazenada() {
		return unidadeArmazenada;
	}

	public void setUnidadeArmazenada(Unidade unidadeArmazenada) {
		this.unidadeArmazenada = unidadeArmazenada;
	}
}
