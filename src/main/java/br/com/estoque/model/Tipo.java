package br.com.estoque.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Tipo implements Serializable {	

	
	private static final long serialVersionUID = 1L;	
	
	public Tipo() {
		
	}
	public Tipo(long id, Categoria categoria, String descricao) {	
		this.id = id;
		this.categoria = categoria;
		this.descricao = descricao;
	}
	
	
	
	public Tipo(long id, Categoria categoria, String descricao , List<NaturezaMaterial> naturezasMaterial) {
		
		this.id = id;

		this.categoria = categoria;
		this.descricao = descricao;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;	

	@ManyToOne
	@JoinColumn(name="categoria",  nullable= false)
	private Categoria categoria;
		
	
	private String descricao;
	private String sigla;
	
	
	
	
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
		Tipo other = (Tipo) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	



	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	@Override
	public String toString() {
		return this.sigla + " - " + this.descricao;
	}


	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


}
