package br.com.estoque.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.estoque.dao.CategoriaDAO;
import br.com.estoque.model.Categoria;
import br.com.estoque.util.Transacional;

public class CategoriaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaDAO categoriaDAO;

	@Transacional
	public void salvar(Categoria categoria) {

		categoria.setSigla(categoria.getSigla().toUpperCase());
		categoria.setNome(categoria.getNome().toUpperCase());
		categoriaDAO.salvar(categoria);
	}

	@Transacional
	public Categoria salvaRetorna(Categoria categoria) {

		categoria.setSigla(categoria.getSigla().toUpperCase());
		categoria.setNome(categoria.getNome().toUpperCase());
		return categoriaDAO.salvar(categoria);
	}

	@Transacional
	public void excluir(Categoria categoria) {
		categoriaDAO.excluir(categoria);
	}

	public List<Categoria> listAll() {

		return categoriaDAO.listAll();
	}

	public Categoria porId(Long id) {
		return categoriaDAO.porId(id);
	}
	

	public boolean verificaCategoriaCadastrada(String sigla, String nome) {

	    return categoriaDAO.verificaCategoriaCadastrada(sigla , nome);
    }

	
	public List<Categoria> categoriasUsuario(Long id) {
		return categoriaDAO.categoriasUsuario(id);
	}
}
