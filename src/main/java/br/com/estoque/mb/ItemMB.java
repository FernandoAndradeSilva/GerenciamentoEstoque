package br.com.estoque.mb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.estoque.model.*;
import br.com.estoque.service.*;
import br.com.estoque.util.BuscaCep;
import br.com.estoque.util.Relatorio;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.primefaces.context.RequestContext;

import br.com.estoque.util.FacesUtil;

/**
 * @author fernando.silva
 */
@Named
@ViewScoped
public class ItemMB implements Serializable {

    private static final long serialVersionUID = 1L;

    ////////////////// INJECTS //////////////////

    @Inject
    ItemService itemService = new ItemService();
    @Inject
    UsuarioService usuarioService = new UsuarioService();
    @Inject
    TipoService tipoService = new TipoService();
    @Inject
    CategoriaService categoriaService = new CategoriaService();
    @Inject
    ModificacaoService modificacaoService = new ModificacaoService();
    @Inject
    EstoqueService estoqueService = new EstoqueService();


    ///////////// ---------------- /////////////

    private List<NaturezaMaterial> naturezasDoTipo;
    private List<Unidade> unidades;


    private Long idteste;

    private List<Item> itens = new ArrayList<Item>();

    private List<Object> itensHash = new ArrayList<>();

    private Tipo novoTipo = new Tipo();
    private Categoria novaCategoria = new Categoria();

    private Modificacao modificacao = new Modificacao();

    private Categoria categoria = new Categoria();
    private List <Tipo> tiposSelecao = new ArrayList<>();

    private String codigoUpdate;
    private String preCodigo = null;

    private String tipoItemNavegacao = null;
    private Long categoriaSelecionada;

    private Long idItem;
    private Item itemOriginal = new Item();

    private Unidade unidadeUsuario = this.retornaUsuarioDaSessao().getSetor().getUnidade();

    // Item que será utilizado para Cadastro / Update
    private Item item = new Item();

    public void carregaItemObservacoes(Long idItem) {
        this.item = itemService.porId(idItem);
    }

    public void inicializar() {
        if (idItem != null) {
            this.item = itemService.porId(idItem);
            this.itemOriginal = item;
            this.codigoUpdate = item.getCodigo();
            modificacao.setDescricaoAntiga(itemOriginal.getDescricao());
            modificacao.setEspecificacaoAntiga(itemOriginal.getEspecificacao());
            modificacao.setEstoqueMinimoAntigo(itemOriginal.getEstoqueMinimo());
            modificacao.setObservacaoAntiga(itemOriginal.getObservacoes());
            this.categoria = item.getTipo().getCategoria();
            this.selecionaCategoria();

        }
    }

    public boolean verificaMovimentacoes() {

        Estoque estoqueItem = estoqueService.retornaEstoqueDoItem(this.item.getId() ,
                this.retornaUsuarioDaSessao().getSetor().getUnidade().getId());


        if (this.idItem != null && estoqueItem.getEntradas() > 0)
            return true;
        else
            return false;
    }




    public void selecionaCategoria() {

        if(this.getCategoria() != null) {
            this.tiposSelecao = this.getCategoria().getTipos();
        } else {
            this.tiposSelecao = new ArrayList<Tipo>();
            this.item.setCodigo("");
            this.item.setTipo(new Tipo());
        }

    }

    public void selecionaTipo() {

        if(this.item.getTipo() != null) {
            this.item.setCodigo(item.getTipo().getCategoria().getSigla() + item.getTipo().getSigla());
        } else {
            this.item.setCodigo("");
        }
    }

    public void selecionaCategoriaNovoTipo() {
        if(this.categoria != null) {
            this.novoTipo.setCategoria(this.categoria);
        }
    }

    public void cadastrarTipo() {

        novoTipo.setCategoria(categoria);
        tipoService.salvar(novoTipo);
        this.tiposSelecao =  tipoService.listPorCategoria(categoria.getId());
        FacesUtil.addInfoMessageTicket("CLASSE CADASTRADA");
        this.novoTipo.setSigla("");
        this.novoTipo.setDescricao("");
        this.novoTipo = new Tipo();
    }

    public void cadastrarCategoria() {

        Categoria c = new Categoria();
        c = categoriaService.salvaRetorna(novaCategoria);
        FacesUtil.addInfoMessageTicket("GRUPO CADASTRADO");
        this.atualizaPermisaoCategoriaUsuario(c);
        this.item.setCodigo("");
        this.novaCategoria = new Categoria();
    }

    public void limpaNovaCategoria() {
        this.novaCategoria = new Categoria();
    }

    public void atualizaPermisaoCategoriaUsuario(Categoria c) {
        Usuario user = usuarioService.porId(this.retornaUsuarioDaSessao().getId());
        user.getCategorias().add(c);
        usuarioService.salvar(user);
    }

    public void cadastraItem() {

        Estoque estoqueDoItem = estoqueService.retornaEstoqueDoItem(item.getId() ,
                this.retornaUsuarioDaSessao().getSetor().getUnidade().getId());


        String message = null;
        item.setUsuario(usuarioService.porId(this.retornaUsuarioDaSessao().getId()));

        if (item.getId() != null) {
            if (estoqueDoItem.getEntradas() > 0) {
                this.salvaModificacao();
            }
            message = "Informações Atualizadas";
            this.modificacao = new Modificacao();

        } else {
            message = "Item Cadastrado";
        }
        itemService.salvar(item);
        FacesUtil.addInfoMessageRedirect(message);
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("http://localhost:8080/Projeto_Estoque_war_exploded/pages/index.xhtml?tipo="+item.getTipo().getDescricao());

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.item = new Item();

    }

    public void bloqueiaEdicao() {

        Estoque estoqueDoItem = estoqueService.retornaEstoqueDoItem(item.getId() ,
                this.retornaUsuarioDaSessao().getSetor().getUnidade().getId());

        if ((idItem != null) && (estoqueDoItem.getEntradas() > 0)) {
            if (!(this.retornaUsuarioDaSessao().getNivelDeAcesso() == 2 || this.retornaUsuarioDaSessao().getNivelDeAcesso() == 1)) {

                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('DialogUsuarioNaoPermitido').show();");
            }
        }
    }

    public void salvaModificacao() {

        if (!(this.item.getDescricao().equals(this.modificacao.getDescricaoAntiga()))) {
            this.modificacao.setDescricaoNova(item.getDescricao());
        }
        if (!(this.item.getEspecificacao().equals(this.modificacao.getEspecificacaoAntiga()))) {
            this.modificacao.setEspecificaoNova(item.getEspecificacao());
        }
        if (!(this.item.getObservacoes().equals(this.modificacao.getObservacaoAntiga()))) {
            this.modificacao.setObservacaoNova(item.getObservacoes());
        }
        if (this.item.getEstoqueMinimo() != this.modificacao.getEstoqueMinimoAntigo()) {
            this.modificacao.setEstoqueMinimoNovo(item.getEstoqueMinimo());
        }

        modificacao.setItem(this.item);
        modificacao.setUsuario(this.retornaUsuarioDaSessao());

        modificacaoService.salvar(this.modificacao);
    }

    public void limpaItemObservacoes() {

        this.item = new Item();
    }

    // Carrega os itens do Usuário de acordo com suas cateogrias
    public void carregarItensUsuario() {

        if (this.retornaUsuarioDaSessao() != null) {

                this.configuraListaItens(tipoItemNavegacao);
                //this.configuraListaItens();

                //this.itensHash = itemService.listAllHash(this.retornaUsuarioDaSessao().getId());
//            } else { // CARREGA POR TIPO
//                this.itensHash = itemService.listAllHash(this.retornaUsuarioDaSessao().getId(), this.tipoItemNavegacao);
//            }
        }

    }

    public void configuraListaItens(String tipoItemNavegacao) {

        List<Item> itens = new ArrayList<>();

        if(tipoItemNavegacao == null) {
            itens = itemService.listAll(this.retornaUsuarioDaSessao().getId());
        } else {
            itens = itemService.listAll(this.retornaUsuarioDaSessao().getId() , tipoItemNavegacao);
        }

        HashMap<String , Object> map = new HashMap<>();
        List<Object> itensVisualizacao = new ArrayList<>();

        for(Item item : itens) {

            map.put("id" , item.getId());
            map.put("codigo" , item.getCodigo());
            map.put("descricao" , item.getDescricao());
            map.put("especificacao" , item.getEspecificacao());
            map.put("siglaUnDeMedida" , item.getUnidadeDeMedida().getSigla());
            map.put("nomeUnDeMedida" , item.getUnidadeDeMedida().getDescricao());
            map.put("estoqueMinimo" , item.getEstoqueMinimo());

            List<Estoque> estoques = buscaEstoque(item.getId());

            int entradas = 0;
            int saidas = 0;
            int saldos = 0;
            float custos = 0;


            for(Estoque estoque : estoques) {
                entradas += estoque.getEntradas();
                saidas += estoque.getSaidas();
                saldos += estoque.getSaldo();
                custos += estoque.getCusto();

            }

            map.put("entradas" , entradas);
            map.put("saidas" , saidas);
            map.put("saldo" , saldos);
            map.put("custo" , custos / estoques.size());

            itensVisualizacao.add(map);
            map = new HashMap<>();
        }

        this.itensHash = itensVisualizacao;

    }


    public List<Estoque> buscaEstoque(Long idItem) {

        Estoque estoque;
        List<Estoque> estoques = new ArrayList<>();

        if(this.unidadeUsuario == null) {
            estoques = estoqueService.retornaEstoqueDoItem(idItem);

        } else {
            estoques.add(estoqueService.retornaEstoqueDoItem(idItem , this.unidadeUsuario.getId()));
        }

        return estoques;
    }


//    public List<Object> filtraVisualizacao(List<Object> itens) {
//
//    }




    ////////// VALIDAÇÕES
    public void verificaTipoCadastrado(FacesContext context, UIComponent toValidate, Object value) {

        if(this.getCategoria() != null && novoTipo.getSigla() != null && value.toString() != null) {
            if(tipoService.verificaTipoCadastrado(novoTipo.getSigla() , value.toString() , this.getCategoria().getId())) {
                FacesMessage msg = new FacesMessage(" 'CLASSE' já CADASTRADA", "TIPO' já cadastrado");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

    public void verificaCategorriaCadastrada(FacesContext context, UIComponent toValidate, Object value) {

        if(novaCategoria.getSigla() != null) {
            if(categoriaService.verificaCategoriaCadastrada(novaCategoria.getSigla().toUpperCase() , value.toString().toUpperCase())) {
                FacesMessage msg = new FacesMessage(" 'GRUPO' já cadastrado", "GRUPO' já cadastrado");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }




    public void verificaCodigoCadastrado(FacesContext context, UIComponent toValidate, Object value) {

        String codigo =  value.toString();

        if(codigo.length() == 7) {

            if(this.idItem != null) {

                if(!this.codigoUpdate.equals(codigo)) {
                       if (itemService.verificaCodigoCadastrado(codigo)) {
                       FacesMessage msg = new FacesMessage("'CÓDIGO' já cadastrado", "JÁ existe");
                       msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                       throw new ValidatorException(msg);
                    }

                }
            } else if(itemService.verificaCodigoCadastrado(codigo)) {
                FacesMessage msg = new FacesMessage("'CÓDIGO' já cadastrado", "JÁ existe");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            } {

            }
        } else {
            FacesMessage msg = new FacesMessage("O 'CÓDIGO' é composto por 4 letras e 3 números", "JÁ existe");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }


    }

    public List<Categoria> categoriasDoUsuario() {
        Usuario user = usuarioService.porId(this.retornaUsuarioDaSessao().getId());

        if (user != null) {
            return user.getCategorias();
        } else
            return null;


    }

    public Usuario retornaUsuarioDaSessao() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }



    ////////////////////

    public String getCodigoUpdate() {
        return codigoUpdate;
    }

    public void setCodigoUpdate(String codigoUpdate) {
        this.codigoUpdate = codigoUpdate;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public String getTipoItemNavegacao() {
        return tipoItemNavegacao;
    }

    public void setTipoItemNavegacao(String tipoItemNavegacao) {
        this.tipoItemNavegacao = tipoItemNavegacao;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Unidade> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<Unidade> unidades) {
        this.unidades = unidades;
    }



    public Long getCategoriaSelecionada() {
        return categoriaSelecionada;
    }

    public void setCategoriaSelecionada(Long categoriaSelecionada) {
        this.categoriaSelecionada = categoriaSelecionada;
    }

    public Tipo getNovoTipo() {
        return novoTipo;
    }

    public void setNovoTipo(Tipo novoTipo) {
        this.novoTipo = novoTipo;
    }

    public List<NaturezaMaterial> getNaturezasDoTipo() {
        return naturezasDoTipo;
    }

    public void setNaturezasDoTipo(List<NaturezaMaterial> naturezasDoTipo) {
        this.naturezasDoTipo = naturezasDoTipo;
    }

    public String getPreCodigo() {
        return preCodigo;
    }

    public void setPreCodigo(String preCodigo) {
        this.preCodigo = preCodigo;
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public Item getItemOriginal() {
        return itemOriginal;
    }

    public void setItemOriginal(Item itemOriginal) {
        this.itemOriginal = itemOriginal;
    }

    public Categoria getNovaCategoria() {
        return novaCategoria;
    }

    public void setNovaCategoria(Categoria novaCategoria) {
        this.novaCategoria = novaCategoria;
    }



    public Modificacao getModificacao() {
        return modificacao;
    }

    public void setModificacao(Modificacao modificacao) {
        this.modificacao = modificacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Tipo> getTiposSelecao() {
        return tiposSelecao;
    }

    public void setTiposSelecao(List<Tipo> tiposSelecao) {
        this.tiposSelecao = tiposSelecao;
    }

    public Long getIdteste() {
        return idteste;
    }

    public void setIdteste(Long idteste) {
        this.idteste = idteste;
    }

    public List<Object> getItensHash() {
        return itensHash;
    }

    public void setItensHash(List<Object> itensHash) {
        this.itensHash = itensHash;
    }

    public Unidade getUnidadeUsuario() {
        return unidadeUsuario;
    }

    public void setUnidadeUsuario(Unidade unidadeUsuario) {
        this.unidadeUsuario = unidadeUsuario;
    }
}
