package br.com.estoque.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.estoque.dao.UnidadeDAO;
import br.com.estoque.model.Unidade;
import br.com.estoque.util.Transacional;

public class UnidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeDAO UnidadeDAO;

	@Transacional
	public void salvar(Unidade Unidade) {

		UnidadeDAO.salvar(Unidade);
	}

	@Transacional
	public void excluir(Unidade Unidade) {
		UnidadeDAO.excluir(Unidade);
	}

	public List<Unidade> listAll() {

		return UnidadeDAO.listAll();
	}

	public Unidade porId(Long id) {
		return UnidadeDAO.porId(id);
	}

}
