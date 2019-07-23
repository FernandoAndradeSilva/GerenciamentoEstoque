package br.com.estoque.dao;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Estoque;
import br.com.estoque.model.Usuario;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

public class EstoqueDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Estoque salvar(Estoque estoque) {
		return manager.merge(estoque);
	}
	
	public void excluir(Estoque estoque) {
		try {
			estoque = porId(estoque.getId());
			manager.remove(estoque);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Estoque porId(Long id) {
		return manager.find(Estoque.class, id);
	}
	
	
	public List<Estoque> listAll() {
		return manager.createNativeQuery("SELECT * FROM Estoque", Estoque.class).getResultList();
	}


	public Estoque retornaEstoqueDoItem(Long idItem , Long idUnidade) {
		Query query = manager.createNativeQuery(
		"SELECT id, custo , entradas , saidas , saldo , unidade , item " +
				" FROM estoque where item = :bindItem " +
                " AND unidade = :bindUnidade ",Estoque.class)
                .setParameter("bindItem" , idItem)
                .setParameter("bindUnidade" , idUnidade);
		Estoque estoque = null;
		try {
			return (Estoque) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Estoque> retornaEstoqueDoItem(Long idItem) {
		return  manager.createNativeQuery(
				"SELECT id, custo , entradas , saidas , saldo , unidade , item " +
						" FROM estoque where item = :bindItem ",Estoque.class)
				.setParameter("bindItem" , idItem).getResultList();


	}


	public boolean verificaSaldoSuficiente(int quantidade , Long idItem) {

		Query query = manager.createNativeQuery(
				"SELECT id, custo , entradas , saidas , saldo , unidade , item "
						+" FROM estoque "
						+" WHERE item = :bindItem "
						+" AND estoque.unidade = :bindIdUnidade" , Estoque.class)
				.setParameter("bindItem" , idItem)
				.setParameter("bindIdUnidade" , this.retornaUsuarioDaSessao().getSetor().getUnidade().getId());

		Estoque estoque = (Estoque) query.getSingleResult();

		if (quantidade < estoque.getSaldo()) {
			return true;
		} else
			return false;
	}




	public Usuario retornaUsuarioDaSessao() {
		return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}


}	
