package br.com.estoque.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


@Entity
public class Categoria implements Serializable {	

	
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToMany(mappedBy="categoria" , orphanRemoval=true , fetch = FetchType.LAZY)
	private List<Tipo> tipos;	
	
	private String nome;
	private String descricao;	
	private String sigla;

	@ManyToMany(mappedBy = "categorias")	
	private List<Usuario> usuarios;
	
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
		Categoria other = (Categoria) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

	
	public List<Tipo> getTipos() {
		return tipos;
	}
	public void setTipos(List<Tipo> tipos) {
		this.tipos = tipos;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}


	@Override
	public String toString() {
		return  this.sigla + " - " + this.nome;
	}

	
	
	
	
}
