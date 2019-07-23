package br.com.estoque.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.estoque.dao.TipoDAO;
import br.com.estoque.model.Tipo;
import br.com.estoque.util.Transacional;

public class TipoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoDAO tipoDAO;

	@Transacional
	public void salvar(Tipo tipo) {
		tipo.setSigla(tipo.getSigla().toUpperCase());
		tipo.setDescricao(tipo.getDescricao().toUpperCase());
		tipoDAO.salvar(tipo);
	}
	
	@Transacional
	public Tipo salvaRetorna(Tipo tipo) {
		tipo.setSigla(tipo.getSigla().toUpperCase());
		tipo.setDescricao(tipo.getDescricao().toUpperCase());
		return tipoDAO.salvar(tipo);
	}
	
	@Transacional
	public void excluir(Tipo tipo) {
		tipoDAO.excluir(tipo);
	}

	public List<Tipo> listAll() {

		return tipoDAO.listAll();
	}

	public List<Tipo> listPorCategoria(Long id) {
		return tipoDAO.listPorCategoria(id);
	}

	public Tipo porId(Long id) {
		return tipoDAO.porId(id);
	}

	public boolean verificaTipoCadastrado(String sigla , String descricao , Long idCategoria) {
		return tipoDAO.verificaTipoCadastrado(sigla , descricao , idCategoria);
	}

}
