package br.com.estoque.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.estoque.model.Categoria;
import br.com.estoque.model.Tipo;
import br.com.estoque.model.Unidade;
import br.com.estoque.model.Usuario;
import br.com.estoque.service.ItemService;
import br.com.estoque.service.TipoService;
import br.com.estoque.service.UnidadeService;
import br.com.estoque.service.UsuarioService;



@Named
@ViewScoped
public class UnidadeMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	UnidadeService unidadeService;
	
	private Unidade unidade;

	public List<Unidade> unidades() {
		return unidadeService.listAll();
	}
	
	
	
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}
	
}
