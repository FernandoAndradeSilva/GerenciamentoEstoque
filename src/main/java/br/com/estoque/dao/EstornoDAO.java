package br.com.estoque.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Estorno;

public class EstornoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Estorno salvar(Estorno estorno) {
		return manager.merge(estorno);
	}

	public void excluir(Estorno estorno) {
		try {
			estorno = porId(estorno.getId());
			manager.remove(estorno);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Estorno porId(Long id) {
		return manager.find(Estorno.class, id);
	}

	public List<Estorno> listAll() {
		return manager.createNativeQuery("SELECT * FROM Estorno", Estorno.class).getResultList();
	}

	public List<Estorno> listAll(Long id) {
		return manager
				.createNativeQuery("SELECT * FROM Estorno "
					+ "inner join movimentacao on estorno.movimentacao_id = movimentacao.id "
					+ "inner join item on movimentacao.item = item.id "
					+ "inner join tipo on item.tipo = tipo.id "
					+ "inner join categoria on tipo.categoria = categoria.id "
					+ "where categoria.id in ("
					+ "select categoria_id from usuario_categoria "
					+ "where usuario_id = :paramId)", Estorno.class)
				.setParameter("paramId", id)
				.getResultList();

	}

	public List<Estorno> listAll(String tipoItem, Long id) {

		return manager
				.createNativeQuery("SELECT * FROM Estorno "
					+ "inner join movimentacao on estorno.movimentacao_id = movimentacao.id "
					+ "inner join item on movimentacao.item = item.id "
					+ "inner join tipo on item.tipo = tipo.id "
					+ "inner join categoria on tipo.categoria = categoria.id "
					+ "where categoria.id in ("
					+ "select categoria_id from usuario_categoria "
					+ "where usuario_id = :paramId) "
					+ "and tipo.descricao = :bindTipoItem", Estorno.class)
				.setParameter("paramId", id)
				.setParameter("bindTipoItem", tipoItem)
				.getResultList();
	}

	public List<Estorno> listAll(Long id, Date dataInicial, Date dataFinal) {
		return manager
				.createNativeQuery("SELECT * FROM Estorno "
					+ "inner join movimentacao on estorno.movimentacao_id = movimentacao.id "
					+ "inner join item on movimentacao.item = item.id "
					+ "inner join tipo on item.tipo = tipo.id "
					+ "inner join categoria on tipo.categoria = categoria.id "
					+ "where categoria.id in ("
					+ "select categoria_id from usuario_categoria "
					+ "where usuario_id = :paramId) "
					+ "and estorno.data >= :paramMinData "
					+ "and estorno.data <= :paramMaxData ", Estorno.class)
				.setParameter("paramId", id)
				.setParameter("paramMinData", dataInicial)
				.setParameter("paramMaxData", dataFinal)
				.getResultList();

	}

	public List<Estorno> listAll(String tipoItemNavegacao, Long id, Date dataInicial, Date dataFinal) {
		return manager
				.createNativeQuery("SELECT * FROM Estorno "
					+ "inner join movimentacao on estorno.movimentacao_id = movimentacao.id "
					+ "inner join item on movimentacao.item = item.id "
					+ "inner join tipo on item.tipo = tipo.id "
					+ "inner join categoria on tipo.categoria = categoria.id "
					+ "where categoria.id in ("
					+ "select categoria_id from usuario_categoria "
					+ "where usuario_id = :paramId) "
					+ "and tipo.descricao = :bindTipoItem "
					+ "and estorno.data >= :paramMinData "
					+ "and estorno.data <= :paramMaxData ", Estorno.class)
				.setParameter("paramId", id)
				.setParameter("bindTipoItem", tipoItemNavegacao)
				.setParameter("paramMinData", dataInicial)
				.setParameter("paramMaxData", dataFinal)
				.getResultList();
	}

	public List<Estorno> listAll(Long id, String dataInicial, String dataFinal) {
		return manager
				.createNativeQuery("SELECT * FROM Estorno "
					+ "inner join movimentacao on estorno.movimentacao_id = movimentacao.id "
					+ "inner join item on movimentacao.item = item.id "
					+ "inner join tipo on item.tipo = tipo.id "
					+ "inner join categoria on tipo.categoria = categoria.id "
					+ "where categoria.id in ("
					+ "select categoria_id from usuario_categoria "
					+ "where usuario_id = :paramId) "
					+ "and estorno.data >= :paramMinData "
					+ "and estorno.data <= :paramMaxData ", Estorno.class)
				.setParameter("paramId", id)
				.setParameter("paramMinData", dataInicial  + " 00:00:00")
				.setParameter("paramMaxData", dataFinal  + " 23:59:59")
				.getResultList();

	}

	public List<Estorno> listAll(String tipoItemNavegacao, Long id, String dataInicial, String dataFinal) {
		return manager
				.createNativeQuery("SELECT * FROM Estorno "
					+ "inner join movimentacao on estorno.movimentacao_id = movimentacao.id "
					+ "inner join item on movimentacao.item = item.id "
					+ "inner join tipo on item.tipo = tipo.id "
					+ "inner join categoria on tipo.categoria = categoria.id "
					+ "where categoria.id in ("
					+ "select categoria_id from usuario_categoria "
					+ "where usuario_id = :paramId) "
					+ "and tipo.descricao = :bindTipoItem "
					+ "and estorno.data >= :paramMinData "
					+ "and estorno.data <= :paramMaxData ", Estorno.class)
				.setParameter("paramId", id)
				.setParameter("bindTipoItem", tipoItemNavegacao)
				.setParameter("paramMinData", dataInicial  + " 00:00:00")
				.setParameter("paramMaxData", dataFinal  + " 23:59:59")
				.getResultList();
	}


}
