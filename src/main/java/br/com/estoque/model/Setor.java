package br.com.estoque.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
public class Setor implements Serializable {	

	
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int codigo;

	private String nome;
	private String gestor;
	private int andar;
	
	@OneToMany(mappedBy="setor" , orphanRemoval=true)
	private List<Usuario> usuarios;

	@OneToMany(mappedBy="setor" , orphanRemoval=true , fetch = FetchType.LAZY)
	private List<CentroCusto> centrosDeCusto;

	@ManyToOne
	@JoinColumn(name="unidade",  nullable= false)
	private Unidade unidade = new Unidade();
	

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
		Setor other = (Setor) obj;
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getGestor() {
		return gestor;
	}
	public void setGestor(String gestor) {
		this.gestor = gestor;
	}
	public int getAndar() {
		return andar;
	}
	public void setAndar(int andar) {
		this.andar = andar;
	}
	public Unidade getUnidade() {
		return unidade;
	}
	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}
	@Override
	public String toString() {
		return  id + nome;
	}


	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public List<CentroCusto> getCentrosDeCusto() {
		return centrosDeCusto;
	}

	public void setCentrosDeCusto(List<CentroCusto> centrosDeCusto) {
		this.centrosDeCusto = centrosDeCusto;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
