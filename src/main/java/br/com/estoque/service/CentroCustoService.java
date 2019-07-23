package br.com.estoque.service;

import br.com.estoque.dao.CentroCustoDAO;
import br.com.estoque.model.CentroCusto;
import br.com.estoque.util.Transacional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

public class CentroCustoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CentroCustoDAO centroCustoDAO;

	@Transacional
	public void salvar(CentroCusto centroCusto) {
		centroCustoDAO.salvar(centroCusto);
	}

	@Transacional
	public CentroCusto salvaRetorna(CentroCusto centroCusto) {
		return centroCustoDAO.salvar(centroCusto);
	}

	@Transacional
	public void excluir(CentroCusto centroCusto) {
		centroCustoDAO.excluir(centroCusto);
	}

	public List<CentroCusto> listAll() {

		return centroCustoDAO.listAll();
	}

	public List<CentroCusto> listPorId(Long idSetor) {

		return centroCustoDAO.lisPorId(idSetor);
	}

	public CentroCusto porId(Long id) {
		return centroCustoDAO.porId(id);
	}
	
	
	


}
