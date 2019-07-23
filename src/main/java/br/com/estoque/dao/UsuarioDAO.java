package br.com.estoque.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Item;
import br.com.estoque.model.Tipo;
import br.com.estoque.model.Usuario;

public class UsuarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Usuario salvar(Usuario usuario) {
		return manager.merge(usuario);
	}

	public void excluir(Usuario usuario) {
		try {
			usuario = porId(usuario.getId());
			manager.remove(usuario);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Usuario porId(Long id) {
		return manager.find(Usuario.class, id);
	}

	public List<Usuario> listAll() {
		return manager.createNativeQuery("SELECT * FROM Usuario", Usuario.class).getResultList();
	}

	public Usuario verificaEmail(String email) {

		Query query = manager.createNativeQuery(
				"SELECT id, email, nivelDeAcesso,  nome, senha, setor, ativo FROM Usuario where email = :bindEmail",
				Usuario.class);
		query.setParameter("bindEmail", email);

		Usuario user = null;
		
		try {
			return (Usuario) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}		
		


	}
	
	public List<Tipo> verificaTiposDoUsuario(Long id) {
		
		return manager.createNativeQuery("SELECT * FROM tipo "
				+ "inner join categoria on tipo.categoria = categoria.id "
				+ "where categoria.id in (" 
				+ "select categoria_id from usuario_categoria " 
				+ "where usuario_id = :paramId) ", Tipo.class)
				.setParameter("paramId", id).getResultList();
	}

//	public Object verificaSenha(String senha , String email) {
//		Query query = manager.createNativeQuery(
//				"SELECT id, email, nivelDeAcesso,  nome, senha, setor, ativo FROM Usuario where email = :bindEmail",
//				Usuario.class);
//		query.setParameter("bindEmail", email);
//		query.setParameter("bindSenha", senha);
//		return null;
//	}

}
