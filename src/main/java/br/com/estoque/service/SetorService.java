package br.com.estoque.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.estoque.dao.SetorDAO;
import br.com.estoque.model.Setor;
import br.com.estoque.util.Transacional;

public class SetorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SetorDAO setorDAO;

	@Transacional
	public void salvar(Setor setor) {

		setorDAO.salvar(setor);
	}

	@Transacional
	public void excluir(Setor setor) {
		setorDAO.excluir(setor);
	}

	public List<Setor> listAll() {

		return setorDAO.listAll();
	}

	public Setor porId(Long id) {
		return setorDAO.porId(id);
	}

}
