package br.com.estoque.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.estoque.dao.EstoqueDAO;
import br.com.estoque.dao.ItemDAO;
import br.com.estoque.model.Estoque;
import br.com.estoque.model.Item;
import br.com.estoque.model.Unidade;
import br.com.estoque.model.Usuario;
import br.com.estoque.util.Transacional;

public class ItemService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ItemDAO itemDAO;

	@Inject
	private EstoqueDAO estoqueDAO;

	@Inject
    private UnidadeService unidadeService;

	@Transacional
	public void salvar(Item item) {

		item.setDescricao(item.getDescricao().toUpperCase());
		if(item.getEspecificacao() != "") {
			item.setEspecificacao(item.getEspecificacao().toUpperCase());
		}
		if(item.getId()== null) {
			item.setCriacao(new Date());
		} else {
			item.setModificacao(new Date());
		}

		Item itemSalvo;

		itemSalvo = itemDAO.salvar(item);

		// CRIA os ETOQUES DE TODAS AS UNIDADES
		if(itemSalvo != null && itemSalvo.getModificacao() == null) {

		    List<Unidade> unidades = unidadeService.listAll();

		    for(Unidade unidade : unidades) {
                Estoque estoque = new Estoque();
                estoque.setUnidade(unidade);
                estoque.setItem(itemSalvo);
                estoqueDAO.salvar(estoque);
		    }
		}
	}

	@Transacional
	public void excluir(Item item) {
		itemDAO.excluir(item);
	}

	public List<Item> listAll() {

		return itemDAO.listAll();
	}

	public Item porId(Long id) {
		return itemDAO.porId(id);
	}



	public List<Item> listAll(String tipo) {

		return itemDAO.listAll(tipo);
	}

	public boolean verificaCodigoCadastrado(String codigo) {
		return itemDAO.verificaCodigoCadastrado(codigo);
	}


	public Usuario retornaUsuarioDaSessao() {
		return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}


	public List<Item> listAll(Long idUsuario) {
		return itemDAO.listAll(idUsuario);
	}

	public List<Item> listAll(Long idUsuario, String tipoItemNavegacao) {
		return itemDAO.listAll(idUsuario , tipoItemNavegacao);
	}


	//
//	public List<Object> listAllHash(Long id) {
//	    return itemDAO.listAllHash(id);
//    }
//
//
//    public List<Object> listAllHash(Long id, String tipoItemNavegacao) {
//        return itemDAO.listAllHash(id , tipoItemNavegacao);
//    }




}
