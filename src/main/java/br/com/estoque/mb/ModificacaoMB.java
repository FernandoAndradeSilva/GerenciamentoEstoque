package br.com.estoque.mb;

import br.com.estoque.model.Item;
import br.com.estoque.model.Modificacao;
import br.com.estoque.service.ItemService;
import br.com.estoque.service.ModificacaoService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ModificacaoMB implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private ItemService itemService;

    private Modificacao modificacao;

    private Item item;

    private Long idItem;

    public void inicializar() {

        this.item = itemService.porId(idItem);
    }




    public List<Modificacao> retornaModificaoesItem() {


        return item.getModificacoes();


    }

    public Modificacao getModificacao() {
        return modificacao;
    }

    public void setModificacao(Modificacao modificacao) {
        this.modificacao = modificacao;
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
