package br.com.estoque.service;

import br.com.estoque.dao.UnidadeDeMedidaDAO;
import br.com.estoque.model.UnidadeDeMedida;
import br.com.estoque.util.Transacional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

public class UnidadeDeMedidaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeDeMedidaDAO unidadeDeMedidaDAO;

	@Transacional
	public void salvar(UnidadeDeMedida unidadeDeMedida) {
		unidadeDeMedidaDAO.salvar(unidadeDeMedida);
	}

	@Transacional
	public UnidadeDeMedida salvaRetorna(UnidadeDeMedida unidadeDeMedida) {
		return unidadeDeMedidaDAO.salvar(unidadeDeMedida);
	}

	@Transacional
	public void excluir(UnidadeDeMedida unidadeDeMedida) {
		unidadeDeMedidaDAO.excluir(unidadeDeMedida);
	}

	public List<UnidadeDeMedida> listAll() {

		return unidadeDeMedidaDAO.listAll();
	}

	public List<UnidadeDeMedida> listPorId(Long idSetor) {

		return unidadeDeMedidaDAO.lisPorId(idSetor);
	}

	public UnidadeDeMedida porId(Long id) {
		return unidadeDeMedidaDAO.porId(id);
	}
	
	
	


}
