package br.com.estoque.dao;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Aplicacao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public class AplicacaoDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Aplicacao salvar(Aplicacao aplicacao) {
		return manager.merge(aplicacao);
	}
	
	public void excluir(Aplicacao aplicacao) {
		try {
			aplicacao = porId(aplicacao.getId());
			manager.remove(aplicacao);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Aplicacao porId(Long id) {
		return manager.find(Aplicacao.class, id);
	}
	
	
	public List<Aplicacao> listAll() {
		return manager.createNativeQuery("SELECT * FROM Aplicacao", Aplicacao.class).getResultList();
	}



}	
