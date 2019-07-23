package br.com.estoque.dao;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.UnidadeDeMedida;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public class UnidadeDeMedidaDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public UnidadeDeMedida salvar(UnidadeDeMedida unidadeDeMedida) {
		return manager.merge(unidadeDeMedida);
	}
	
	public void excluir(UnidadeDeMedida unidadeDeMedida) {
		try {
			unidadeDeMedida = porId(unidadeDeMedida.getId());
			manager.remove(unidadeDeMedida);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public UnidadeDeMedida porId(Long id) {
		return manager.find(UnidadeDeMedida.class, id);
	}
	
	
	public List<UnidadeDeMedida> listAll() {
		return manager.createNativeQuery("SELECT * FROM UnidadeDeMedida", UnidadeDeMedida.class).getResultList();
	}

	public List<UnidadeDeMedida> lisPorId(Long idSetor) {
		return manager.createNativeQuery("SELECT * FROM UnidadeDeMedida " +
				"WHERE setor = :paramIdSetor",
				UnidadeDeMedida.class).setParameter("paramIdSetor", idSetor).getResultList();
	}

}	
