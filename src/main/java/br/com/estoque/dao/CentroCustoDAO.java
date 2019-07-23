package br.com.estoque.dao;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.CentroCusto;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public class CentroCustoDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public CentroCusto salvar(CentroCusto centroCusto) {
		return manager.merge(centroCusto);
	}
	
	public void excluir(CentroCusto centroCusto) {
		try {
			centroCusto = porId(centroCusto.getId());
			manager.remove(centroCusto);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public CentroCusto porId(Long id) {
		return manager.find(CentroCusto.class, id);
	}
	
	
	public List<CentroCusto> listAll() {
		return manager.createNativeQuery("SELECT * FROM CentroCusto", CentroCusto.class).getResultList();
	}

	public List<CentroCusto> lisPorId(Long idSetor) {
		return manager.createNativeQuery("SELECT * FROM CentroCusto " +
				"WHERE setor = :paramIdSetor",
				CentroCusto.class).setParameter("paramIdSetor", idSetor).getResultList();
	}

}	
