package br.com.estoque.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Unidade;

public class UnidadeDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Unidade salvar(Unidade unidade) {
		return manager.merge(unidade);		
	}
	
	public void excluir(Unidade unidade) {
		try {
			unidade = porId(unidade.getId());
			manager.remove(unidade);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Unidade porId(Long id) {
		return manager.find(Unidade.class, id);	
	}
	
	
	public List<Unidade> listAll() {
		return manager.createNativeQuery("SELECT * FROM Unidade", Unidade.class).getResultList();
	}

	
}	
