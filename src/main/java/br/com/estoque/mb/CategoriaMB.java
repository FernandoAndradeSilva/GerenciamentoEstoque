package br.com.estoque.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.estoque.model.Categoria;
import br.com.estoque.service.CategoriaService;

@Named
@ViewScoped
public class CategoriaMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoriaService categoriaService;
	
	private Categoria categoria;
	

	
	public void buscarCategorias() {
		
		
		List <Categoria> categorias = categoriaService.listAll();
	}
	
	
	public List<Categoria> categorias() {
		
		return this.categoriaService.listAll();
	}
	

	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	
	
}
