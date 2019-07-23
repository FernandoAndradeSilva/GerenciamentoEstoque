package br.com.estoque.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Setor;

public class SetorDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Setor salvar(Setor setor) {
		return manager.merge(setor);		
	}
	
	public void excluir(Setor setor) {
		try {
			setor = porId(setor.getId());
			manager.remove(setor);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Setor porId(Long id) {
		return manager.find(Setor.class, id);	
	}
	
	
	public List<Setor> listAll() {
		return manager.createNativeQuery("SELECT * FROM Setor", Setor.class).getResultList();
	}

	
}	
