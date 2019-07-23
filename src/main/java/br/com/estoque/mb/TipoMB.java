package br.com.estoque.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.estoque.model.Categoria;
import br.com.estoque.model.Tipo;
import br.com.estoque.model.Usuario;

import br.com.estoque.service.TipoService;
import br.com.estoque.service.UsuarioService;



@Named
@ViewScoped
public class TipoMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	TipoService tipoService;
	
	List<SelectItem> tipoLista;
	
	
	@Inject
	private UsuarioService usuarioService;
	
	private Tipo tipo;
	
	private Tipo novoTipo = new Tipo();
	
	

	
	
	public void salvar(Tipo tipo) {
		
		tipoService.salvar(tipo);
	}
	
	

	public void teste() {
		System.out.println(this.retornaUsuarioDaSessao().getCategorias());
		
	}
	
	public void carregaTipos() {
		
		tipoLista = new ArrayList<SelectItem>();
		
		Usuario usuario = null;
		
		usuario = usuarioService.porId(this.retornaUsuarioDaSessao().getId());
		System.out.println(usuario);
		
		
		if(usuario != null) {
			List<Categoria> categorias = usuario.getCategorias();
			List<Tipo> tipos = this.usuarioService.verificaTiposDoUsuario(this.retornaUsuarioDaSessao().getId());

			List<SelectItemGroup> listaGroups = new ArrayList<SelectItemGroup>();
			List<SelectItem> listaTipos = new ArrayList<SelectItem>();

			for (Categoria categoria : categorias) {
				SelectItemGroup group = new SelectItemGroup(categoria.getNome());
				listaGroups.add(group);
				for (Tipo tipo : tipos) {
					if (tipo.getCategoria().getId() == categoria.getId()) {
						SelectItem selected = new SelectItem(new Tipo(tipo.getId(),tipo.getCategoria(), tipo.getDescricao()));
						listaTipos.add(selected);
						selected = new SelectItem();
					}
				}
				for (SelectItem itemSelected : listaTipos) {
					group.setSelectItems(listaTipos.toArray(new SelectItem[listaTipos.size()]));
				}
				tipoLista.add(group);
				listaTipos = new ArrayList<SelectItem>();
			}
		}
		

		
		
		
		
	}
	
	
	public List<SelectItem> tipos() {	

		List<SelectItem> tipoListaFinal = new ArrayList<SelectItem>();
		
		Usuario usuario = null;
		
		usuario = usuarioService.porId(this.retornaUsuarioDaSessao().getId());
		System.out.println(usuario);
		
		
		if(usuario != null) {
			List<Categoria> categorias = usuario.getCategorias();
			List<Tipo> tipos = this.usuarioService.verificaTiposDoUsuario(this.retornaUsuarioDaSessao().getId());

			List<SelectItemGroup> listaGroups = new ArrayList<SelectItemGroup>();
			List<SelectItem> listaTipos = new ArrayList<SelectItem>();

			for (Categoria categoria : categorias) {
				SelectItemGroup group = new SelectItemGroup(categoria.getNome());
				listaGroups.add(group);
				for (Tipo tipo : tipos) {
					if (tipo.getCategoria().getId() == categoria.getId()) {
						SelectItem selected = new SelectItem(new Tipo(tipo.getId(),tipo.getCategoria(), tipo.getDescricao() ));
						listaTipos.add(selected);
						selected = new SelectItem();
					}
				}
				for (SelectItem itemSelected : listaTipos) {
					group.setSelectItems(listaTipos.toArray(new SelectItem[listaTipos.size()]));
				}
				tipoListaFinal.add(group);
				listaTipos = new ArrayList<SelectItem>();
			}
		}
		return tipoListaFinal;

	}
	
	
	public Usuario retornaUsuarioDaSessao() {
		return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}

	public Tipo getNovoTipo() {
		return novoTipo;
	}

	public void setNovoTipo(Tipo novoTipo) {
		this.novoTipo = novoTipo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}


	public List<SelectItem> getTipoLista() {
		return tipoLista;
	}


	public void setTipoLista(List<SelectItem> tipoLista) {
		this.tipoLista = tipoLista;
	}
	
	
	
}
