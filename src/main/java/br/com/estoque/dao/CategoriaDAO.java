package br.com.estoque.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Categoria;

public class CategoriaDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Categoria salvar(Categoria categoria) {
		return manager.merge(categoria);		
	}
	
	public void excluir(Categoria categoria) {
		try {
			categoria = porId(categoria.getId());
			manager.remove(categoria);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Categoria porId(Long id) {
		return manager.find(Categoria.class, id);	
	}
	
	
	public List<Categoria> listAll() {
		return manager.createNativeQuery("SELECT * FROM Categoria", Categoria.class).getResultList();
	}
	
	
	public List<Categoria> categoriasUsuario(Long id) {
		return manager.createNativeQuery("SELECT categoria.id, categoria.descricao, categoria.nome, categoria.sigla from usuario_categoria "
				+ "inner join usuario on usuario.id = usuario_categoria.usuario_id "
				+ "inner join categoria on categoria.id = usuario_categoria.categoria_id where usuario.id = :bindIdUsuario ", Categoria.class)
				.setParameter("bindIdUsuario", id).getResultList();
	}

	public boolean verificaCategoriaCadastrada(String sigla , String nome) {
		Query query = manager.createNativeQuery(
				"SELECT * FROM categoria where sigla = :bindSigla " +
						 "and nome = :bindNome ",Categoria.class)
				.setParameter("bindSigla", sigla)
				.setParameter("bindNome" , nome);

		try {
			query.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	
}	
