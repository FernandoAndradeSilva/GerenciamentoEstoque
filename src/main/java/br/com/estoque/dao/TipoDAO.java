package br.com.estoque.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Tipo;

public class TipoDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Tipo salvar(Tipo tipo) {
		return manager.merge(tipo);		
	}
	
	public void excluir(Tipo tipo) {
		try {
			tipo = porId(tipo.getId());
			manager.remove(tipo);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Tipo porId(Long id) {
		return manager.find(Tipo.class, id);	
	}

	public List<Tipo> listPorCategoria(Long idCat) {
		return manager.createNativeQuery("SELECT * FROM Tipo " +
				"WHERE tipo.categoria = :paramCategoria ", Tipo.class)
				.setParameter("paramCategoria", idCat).getResultList();
	}
	
	
	public List<Tipo> listAll() {
		return manager.createNativeQuery("SELECT * FROM Tipo", Tipo.class).getResultList();
	}



	public boolean verificaTipoCadastrado(String sigla , String descricao , Long idCategoria) {
		Query query = manager.createNativeQuery(
				"SELECT * FROM tipo where sigla = :bindSigla " +
						"and descricao = :bindDescricao " +
						"and categoria = :bindIdCategoria ",Tipo.class)
				.setParameter("bindSigla", sigla.toUpperCase())
				.setParameter("bindDescricao" , descricao.toUpperCase())
				.setParameter("bindIdCategoria" , idCategoria);

		try {
			query.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

}	
