package br.com.estoque.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Query;

import br.com.estoque.dao.UsuarioDAO;
import br.com.estoque.model.Tipo;
import br.com.estoque.model.Usuario;
import br.com.estoque.util.Transacional;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDAO usuarioDAO;

	@Transacional
	public void salvar(Usuario usuario) {

		usuarioDAO.salvar(usuario);
	}

	@Transacional
	public void excluir(Usuario usuario) {
		usuarioDAO.excluir(usuario);
	}

	public List<Usuario> listAll() {

		List<Usuario> usuarios = usuarioDAO.listAll();
		List<Usuario> users = new ArrayList<Usuario>();


		if(this.retornaUsuarioDaSessao().getNivelDeAcesso() != 1) {

			for(Usuario usuario : usuarios) {
				if(usuario.getNivelDeAcesso() != 1) {
					users.add(usuario);
				}
			}
			return users;
		} else
			return usuarios;


	}

	public Usuario porId(Long id) {
		return usuarioDAO.porId(id);
	}
	
	public Usuario verificaEmail(String email) {
		
		return usuarioDAO.verificaEmail(email);
		
	
	}
	
	public List<Tipo> verificaTiposDoUsuario(Long id) {
		return usuarioDAO.verificaTiposDoUsuario(id);
	}

	public Usuario retornaUsuarioDaSessao() {
		return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}
}
