package br.com.estoque.dao;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Modificacao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public class ModificacaoDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Modificacao salvar(Modificacao modificacao) {
		return manager.merge(modificacao);
	}
	
	public void excluir(Modificacao modificacao) {
		try {
			modificacao = porId(modificacao.getId());
			manager.remove(modificacao);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("MODIFICAÇÃO NAO PODE SER EXCLUÍDO");
		}
	}

	public Modificacao porId(Long id) {
		return manager.find(Modificacao.class, id);
	}
	
	
	public List<Modificacao> listAll() {
		return manager.createNativeQuery("SELECT * FROM Modificacao", Modificacao.class).getResultList();
	}


    public List<Modificacao> listAll(Long idItem) {

		return manager.createNativeQuery("SELECT * FROM Modificacao WHERE modificacao.item = :paramItem", Modificacao.class).setParameter("paramItem" , idItem).getResultList();
    }
}
