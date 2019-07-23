package br.com.estoque.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.com.estoque.dao.EstornoDAO;
import br.com.estoque.model.Estorno;
import br.com.estoque.util.Transacional;

public class EstornoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EstornoDAO estornoDAO;

	@Transacional
	public void salvar(Estorno estorno) {
		
		estorno.setData(new Date());
		estornoDAO.salvar(estorno);
	}

	@Transacional
	public void excluir(Estorno estorno) {
		estornoDAO.excluir(estorno);
	}

	public List<Estorno> listAll() {

		return estornoDAO.listAll();
	}
	
	public List<Estorno> listAll(Long id) {

		return estornoDAO.listAll(id);
	}
	
	public List<Estorno> listAll(String tipoItem, Long id) {

		return estornoDAO.listAll(tipoItem , id);
	}

	public Estorno porId(Long id) {
		return estornoDAO.porId(id);
	}

	public List<Estorno> listAll(Long id, Date dataInicial, Date dataFinal) {
		return estornoDAO.listAll(id, dataInicial , dataFinal);
	}

	public List<Estorno> listAll(String tipoItemNavegacao, Long id, Date dataInicial, Date dataFinal) {
		return estornoDAO.listAll(tipoItemNavegacao, id, dataInicial , dataFinal);
	}

	public List<Estorno> listAll(Long id, String dataInicial, String dataFinal) {
		return estornoDAO.listAll(id, dataInicial , dataFinal);
	}

	public List<Estorno> listAll(String tipoItemNavegacao, Long id, String dataInicial, String dataFinal) {
		return estornoDAO.listAll(tipoItemNavegacao, id, dataInicial , dataFinal);
	}

}
