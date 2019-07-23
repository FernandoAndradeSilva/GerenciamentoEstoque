package br.com.estoque.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Usuario implements Serializable {	

	
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false)
	private String nome;
	
	@Column(unique=true)
	private String email;
	
	private String senha;
	private int nivelDeAcesso;
	private boolean ativo = false;
	
	
	@ManyToOne
	@JoinColumn(name="setor",  nullable= true)
	private Setor setor = new Setor();
	 
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_categoria", joinColumns = @JoinColumn(name= "usuario_id"),
				inverseJoinColumns = @JoinColumn(name = "categoria_id"))	
	private List<Categoria> categorias;
	
	@OneToMany(mappedBy="usuario" , orphanRemoval=true , fetch = FetchType.LAZY)
	private List<Estorno> estornos = new ArrayList<>();
	
	@OneToMany(mappedBy="usuario" , orphanRemoval=true , fetch = FetchType.LAZY)
	private List<Movimentacao> movimentacoes = new ArrayList<>();
	
	@OneToMany(mappedBy="usuario" , orphanRemoval=true , fetch = FetchType.LAZY)
	private List<Item> itensCadastrados = new ArrayList<>();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Usuario other = (Usuario) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public int getNivelDeAcesso() {
		return nivelDeAcesso;
	}
	public void setNivelDeAcesso(int nivelDeAcesso) {
		this.nivelDeAcesso = nivelDeAcesso;
	}
	public Setor getSetor() {
		return setor;
	}
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	public List<Categoria> getCategorias() {
		return categorias;
	}
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Estorno> getEstornos() {
		return estornos;
	}
	public void setEstornos(List<Estorno> estornos) {
		this.estornos = estornos;
	}
	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}
	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	public List<Item> getItensCadastrados() {
		return itensCadastrados;
	}
	public void setItensCadastrados(List<Item> itensCadastrados) {
		this.itensCadastrados = itensCadastrados;
	}
	
	
	
	
	
	
	
//	private Setor setor;
	
	
	
	
//	private List<Categoria> categoriasDisponiveis;
//	private List<Estorno> estornosEfetuados;
//	private List<Movimentacao> movimentacoesEfetuadas;
//	private List<Item> itensCadastrados;
	
	
}
