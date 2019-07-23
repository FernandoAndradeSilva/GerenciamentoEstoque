package br.com.estoque.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.estoque.model.*;
import br.com.estoque.service.CentroCustoService;
import br.com.estoque.service.EstoqueService;
import br.com.estoque.service.ItemService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.com.estoque.service.MovimentacaoService;
import br.com.estoque.util.FacesUtil;
import java.text.SimpleDateFormat;

@Named
@ViewScoped
public class MovimentacaoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	MovimentacaoService movimentacaoService;

	@Inject
	CentroCustoService centroCustoService;

	@Inject
	EstoqueService estoqueService;

	@Inject
	ItemService itemService;



	private Movimentacao movimentacao = new Movimentacao();
	private List<Movimentacao> movimentacoes;

	private Movimentacao movimentacaoSelecionada;

	private Setor setorSaida = null;
	private String tipoItemNavegacao = null;

	private Date minDatePesquisa = null;
	private Date dataInicial = null;
	private Date dataFinal = null;

	private Long idAplicacao;

	private Unidade unidadeUsuario = this.retornaUsuarioDaSessao().getSetor().getUnidade();


	public void configuraTipoMovimentacao(String tipoMovimentacao , Long idItem) {
		this.movimentacao.setTipo(tipoMovimentacao);
		movimentacao.setItem(itemService.porId(idItem));

	}

	public void tabChange(TabChangeEvent event) {

		this.dataFinal = null;
		this.dataInicial = null;

		TabView tabView = (TabView) event.getComponent();
		int activeIndex = tabView.getChildren().indexOf(event.getTab());

		if (activeIndex == 1) {
			tabView.setActiveIndex(1);
		} else if (activeIndex == 2) {
			tabView.setActiveIndex(2);
		} else if (activeIndex == 3) {
			tabView.setActiveIndex(3);
		} else if (activeIndex == 0) {
			tabView.setActiveIndex(0);
		}

	}

	public List<CentroCusto> retornaCentrosDeCusto(){

		List<CentroCusto> centros = new ArrayList<CentroCusto>();
		if(setorSaida != null) {
			centros = centroCustoService.listPorId(setorSaida.getId());
		}
		return centros;

	}

	public List<Movimentacao> movimentacoes(String tipoMovimentacao) {

		List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
		
		if(this.dataFinal != null && this.dataInicial != null) { // ENTÃO FOI FEITO UMA BUSCA

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			
			if (this.tipoItemNavegacao == null) {
				movimentacoes = movimentacaoService.listAll(tipoMovimentacao, this.retornaUsuarioDaSessao().getId(),
						formato.format(this.dataInicial), formato.format(this.dataFinal));
			} else {
				movimentacoes = movimentacaoService.listAll(tipoMovimentacao, tipoItemNavegacao,
						this.retornaUsuarioDaSessao().getId(), formato.format(this.dataInicial), formato.format(this.dataFinal));
			}
		} else { // NÃO FOI FEITO BUSCA
			
			if (this.tipoItemNavegacao == null) {
				movimentacoes = movimentacaoService.listAll(tipoMovimentacao, this.retornaUsuarioDaSessao().getId());
			} else {
				movimentacoes = movimentacaoService.listAll(tipoMovimentacao, tipoItemNavegacao,
						this.retornaUsuarioDaSessao().getId());
			}
		}
		return this.filtraVisual(movimentacoes);
	
	}


	public List<Movimentacao> filtraVisual(List<Movimentacao> movimentacoes) {

		List<Movimentacao> listFinal = new ArrayList<>();

		for(Movimentacao movimentacao : movimentacoes) {
			if(movimentacao.getUnidadeArmazenada().equals(this.unidadeUsuario)) {
				listFinal.add(movimentacao);
			}
		}

		return listFinal;

	}
	
	public void mediaAjusteDeEstoque() {

		this.movimentacao.setValor(estoqueService.retornaEstoqueDoItem(this.movimentacao.getItem().getId() ,
				this.retornaUsuarioDaSessao().getSetor().getUnidade().getId()).getCusto());
	}



	public void salvarMovimentacao() {

		if(movimentacao.getCupomFiscal() == "" && movimentacao.getNotaFiscal() == "" && !this.movimentacao.isAjusteDeEstoque()) {

			FacesUtil.addWarningMessageTicket("Preencha 'NOTA FISCAL' ou 'CUPOM FISCAL'");

		} else {
			movimentacao.setData(new Date());
			movimentacao.setStatus(true);
			movimentacao.setUsuario(this.retornaUsuarioDaSessao());

			if(movimentacao.getTipo().equals("ENTRADA")) {

				movimentacao.setAplicacao(null);
				movimentacao.setCentroCusto(null);
				movimentacao.setValor(movimentacao.getQuantidade() * movimentacao.getValor());

			} else if(movimentacao.getTipo().equals("SAIDA")) {

				movimentacao.setFornecedor(null);

				Estoque estoqueDoItem = estoqueService.retornaEstoqueDoItem(this.movimentacao.getItem().getId() ,
						this.retornaUsuarioDaSessao().getSetor().getUnidade().getId());

				movimentacao.setValor(movimentacao.getQuantidade() * estoqueDoItem.getCusto());
			}

			movimentacao.setUnidadeArmazenada(this.retornaUsuarioDaSessao().getSetor().getUnidade());

			movimentacaoService.salvar(movimentacao);
			estoqueService.atualizaEstoque(movimentacao);

			this.setorSaida = new Setor();

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('DialogMovimentacao').hide();");
			FacesUtil.addInfoMessageRedirect("Cadastrado com sucesso");

			this.movimentacao = new Movimentacao();
		}
	}



	public void salvaNF() {
		movimentacaoService.salvar(this.movimentacaoSelecionada);

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('DialogNF').hide();");
		FacesUtil.addInfoMessageRedirect("Nota Fiscal Adiocionada");

		this.limpaMovimentacaoSelecionada();
	}

	public void limpaBusca() {
		this.dataFinal = null;
		this.dataInicial = null;
		this.minDatePesquisa = null;
	}


	public void limpaMovimentacao() {
		this.movimentacao = new Movimentacao();
		this.movimentacao.setMotivo("");
		this.movimentacao.setAplicacao(new Aplicacao());
		this.movimentacao.setCentroCusto(new CentroCusto());
		this.movimentacao.setSolicitante("");


	}

	public void limpaMovimentacaoSelecionada() {
		this.movimentacaoSelecionada = new Movimentacao();
	}

	public void configuraMinData() {
		this.minDatePesquisa = dataInicial;
	}

	public void validaQuantidade(FacesContext context, UIComponent toValidate, Object value) {

		int quantidadeSolicitada = ((Integer) value).intValue();

		if (quantidadeSolicitada <= 0) {
			FacesMessage msg = new FacesMessage("'VALOR' precisa ser maior que 0 ", "'VALOR' precisa ser maior que 0");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		} else {
			if (this.movimentacao.getTipo().equals("SAIDA")) {
				if(!(this.estoqueService.verificaSaldoSuficiente(quantidadeSolicitada , movimentacao.getItem().getId()))) {
					FacesMessage msg = new FacesMessage(
							"Não há saldo suficiente",
							"Não há saldo suficiente");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}


			}

		}

	}


	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();
	}

	// MÉTODO QUE RETORNA O USUÁRIO DA SESSÃO ATIVA
	public Usuario retornaUsuarioDaSessao() {
		return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}

	public List<SelectItem> carregaMenuMotivoSaida() {

		List<SelectItem> menuMotivoSaida;
		menuMotivoSaida = new ArrayList<SelectItem>();

		SelectItemGroup cat1 = new SelectItemGroup("INTERNO");
		SelectItemGroup cat2 = new SelectItemGroup("EXTERNO");
		SelectItemGroup cat3 = new SelectItemGroup("GERENCIAMENTO");

		SelectItem tipo1 = new SelectItem("USO FUNCIONÁRIO", "USO FUNCIONÁRIO");
		SelectItem tipo2 = new SelectItem("COMSUMÍVEIS", "COMSUMÍVEIS");
		SelectItem tipo3 = new SelectItem("CTO", "CTO");
		SelectItem tipo4 = new SelectItem("AJUSTE DE ESTOQUE", "AJUSTE DE ESTOQUE");
		SelectItem tipo5 = new SelectItem("OUTROS", "OUTROS");

		cat1.setSelectItems(new SelectItem[] { tipo1, tipo2, tipo5 });
		cat2.setSelectItems(new SelectItem[] { tipo3, tipo5 });
		cat3.setSelectItems(new SelectItem[] { tipo4, tipo5 });

		menuMotivoSaida.add(cat1);
		menuMotivoSaida.add(cat2);
		menuMotivoSaida.add(cat3);

		return menuMotivoSaida;

	}

	public List<String> motivoSaida() {

		List<String> menu = new ArrayList<String>();

		menu.add("USO E CONSUMO");
		menu.add("AJUSTE DE INVENTÁRIO");
		menu.add("SAÍDA PARA REPARO");
		menu.add("ICN");
		menu.add("CTO");

		return menu;

	}

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public List<Movimentacao> setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
		return movimentacoes;
	}

	public String getTipoItemNavegacao() {
		return tipoItemNavegacao;
	}

	public void setTipoItemNavegacao(String tipoItemNavegacao) {
		this.tipoItemNavegacao = tipoItemNavegacao;
	}

	public Date getMinDatePesquisa() {
		return minDatePesquisa;
	}

	public void setMinDatePesquisa(Date minDatePesquisa) {
		this.minDatePesquisa = minDatePesquisa;
	}

	public Movimentacao getMovimentacaoSelecionada() {
		return movimentacaoSelecionada;
	}

	public void setMovimentacaoSelecionada(Movimentacao movimentacaoSelecionada) {
		this.movimentacaoSelecionada = movimentacaoSelecionada;
	}


	public Long getIdAplicacao() {
		return idAplicacao;
	}

	public void setIdAplicacao(Long idAplicacao) {
		this.idAplicacao = idAplicacao;
	}

	public Setor getSetorSaida() {
		return setorSaida;
	}

	public void setSetorSaida(Setor setorSaida) {
		this.setorSaida = setorSaida;
	}

	public Unidade getUnidadeUsuario() {
		return unidadeUsuario;
	}

	public void setUnidadeUsuario(Unidade unidadeUsuario) {
		this.unidadeUsuario = unidadeUsuario;
	}
}
