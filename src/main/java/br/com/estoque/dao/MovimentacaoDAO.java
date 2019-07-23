package br.com.estoque.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Movimentacao;
import br.com.estoque.model.Usuario;

public class MovimentacaoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Movimentacao salvar(Movimentacao movimentacao) {
		return manager.merge(movimentacao);
	}

	public void excluir(Movimentacao movimentacao) {
		try {
			movimentacao = porId(movimentacao.getId());
			manager.remove(movimentacao);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Movimentacao porId(Long id) {
		return manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> listAll() {
		return manager.createNativeQuery("SELECT * FROM Movimentacao", Movimentacao.class).getResultList();
	}

	public List<Movimentacao> listAll(String tipoMovimentacao, Long id ) {
		return manager
				.createNativeQuery(
						"SELECT * FROM Movimentacao " + "inner join item on movimentacao.item = item.id "
								+ "inner join tipo on item.tipo = tipo.id "
								+ "inner join categoria  on tipo.categoria = categoria.id " + "where categoria.id in ("
								+ "select categoria_id from usuario_categoria "
								+ "where usuario_id = :paramId) and movimentacao.tipo = :paramTipoMov and movimentacao.status = 1 ",
						Movimentacao.class)
				.setParameter("paramId", id).setParameter("paramTipoMov", tipoMovimentacao).getResultList();
	}

	public List<Movimentacao> listAll(String tipoMovimentacao, String tipoItem, Long id) {
		return manager
				.createNativeQuery("SELECT * FROM Movimentacao " + "inner join item on movimentacao.item = item.id "
						+ "inner join tipo on item.tipo = tipo.id "
						+ "inner join categoria  on tipo.categoria = categoria.id " + "where categoria.id in ("
						+ "select categoria_id from usuario_categoria "
						+ "where usuario_id = :paramId) and movimentacao.tipo = :paramTipoMov "
						+ "and tipo.descricao = :paraTipoItem and movimentacao.status = 1", Movimentacao.class)
				.setParameter("paramId", id).setParameter("paramTipoMov", tipoMovimentacao)
				.setParameter("paraTipoItem", tipoItem).getResultList();
	}

	public List<Movimentacao> listAll(String tipoMovimentacao, Long id, String dataInicial, String dataFinal) {
		return manager.createNativeQuery("SELECT * FROM Movimentacao "
				+ "inner join item on movimentacao.item = item.id " + "inner join tipo on item.tipo = tipo.id "
				+ "inner join categoria  on tipo.categoria = categoria.id " + "where categoria.id in ("
				+ "select categoria_id from usuario_categoria "
				+ "where usuario_id = :paramId) "
				+ "and movimentacao.tipo = :paramTipoMov "
				+ "and movimentacao.data >= :paramMinData "
				+ "and movimentacao.data <= :paramMaxData "
				+ "and movimentacao.status = 1",Movimentacao.class)
				.setParameter("paramId", id)
				.setParameter("paramTipoMov", tipoMovimentacao)
				.setParameter("paramMinData", dataInicial + " 00:00:00")
				.setParameter("paramMaxData", dataFinal + " 23:59:59").getResultList();

	}

	public List<Movimentacao> listAll(String tipoMovimentacao, String tipoItemNavegacao, Long id, String dataInicial,
									  String dataFinal) {
		return manager.createNativeQuery("SELECT * FROM Movimentacao "
				+ "inner join item on movimentacao.item = item.id "
				+ "inner join tipo on item.tipo = tipo.id "
				+ "inner join categoria  on tipo.categoria = categoria.id "
				+ "where categoria.id in ("
				+ "select categoria_id from usuario_categoria "
				+ "where usuario_id = :paramId) "
				+ "and movimentacao.tipo = :paramTipoMov "
				+ "and tipo.descricao = :paraTipoItem "
				+ "and movimentacao.data >= :paramMinData "
				+ "and movimentacao.data <= :paramMaxData "
				+ "and movimentacao.status = 1",Movimentacao.class)
				.setParameter("paramId", id).setParameter("paramTipoMov", tipoMovimentacao)
				.setParameter("paraTipoItem", tipoItemNavegacao)
				.setParameter("paramMinData", dataInicial + " 00:00:00")
				.setParameter("paramMaxData", dataFinal + " 23:59:59").getResultList();
	}

	public List<Movimentacao> listarEntradasAtivas(Long idItem) {
		return manager.createNativeQuery("SELECT * FROM Movimentacao "
				+ "where movimentacao.item = :paramIdItem "
				+ "and movimentacao.status =1 "
				+ "and movimentacao.unidadeArmazenada = :paramUnidadeUsuario "
				+ "and movimentacao.tipo = 'ENTRADA'", Movimentacao.class)
				.setParameter("paramIdItem" , idItem)
				.setParameter("paramUnidadeUsuario" , this.retornaUsuarioDaSessao().getSetor().getUnidade().getId())
				.getResultList();


	}


	public Usuario retornaUsuarioDaSessao() {
		return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}



}
