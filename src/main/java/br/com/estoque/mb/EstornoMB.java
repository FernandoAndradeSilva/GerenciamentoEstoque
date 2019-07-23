package br.com.estoque.mb;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.estoque.model.*;
import br.com.estoque.service.*;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.com.estoque.util.FacesUtil;

@Named
@ViewScoped
public class EstornoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EstornoService estornoService = new EstornoService();

	@Inject
	private MovimentacaoService movimentacaoService = new MovimentacaoService();

	@Inject
	private ItemService itemService = new ItemService();

	@Inject
	private UsuarioService usuarioService = new UsuarioService();

	@Inject
	private EstoqueService estoqueService = new EstoqueService();


	private String tipoItemNavegacao = null;

	private Estorno estorno = new Estorno();

	private Date minDatePesquisa = null;
	private Date dataInicial = null;
	private Date dataFinal = null;

	private List<Estorno> estornos = new ArrayList<Estorno>();



	private Unidade unidadeVisualizacao = this.retornaUsuarioDaSessao().getSetor().getUnidade();

	public void estornarMovimentacao() {

		this.estorno.setUsuario(usuarioService.porId(this.retornaUsuarioDaSessao().getId()));
		this.estorno.getMovimentacao().setStatus(false);
		this.estorno.getMovimentacao().setCentroCusto(null);

		Estoque estoqueItem = estoqueService.retornaEstoqueDoItem(this.estorno.getMovimentacao().getItem().getId() ,
				this.retornaUsuarioDaSessao().getSetor().getUnidade().getId());

		if(estorno.getMovimentacao().getTipo().equals("ENTRADA")) {
			if(estorno.getMovimentacao().getQuantidade() > estoqueItem.getSaldo()) { // se houverem saidas a serem canceladas primeiro
				FacesUtil.addWarningMessageTicket("Impossível cancelar! O item possui operações de SAÍDA ");
			} else {
				estornoService.salvar(estorno);
				estoqueService.atualizaEstoqueEstorno(estoqueItem , this.estorno.getMovimentacao());
			}
		} else if(estorno.getMovimentacao().getTipo().equals("SAIDA")) {
			estornoService.salvar(estorno);
			estoqueService.atualizaEstoqueEstorno(estoqueItem , this.estorno.getMovimentacao());
		}


		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('DialogEstorno').hide();");
		FacesUtil.addInfoMessageTicket(this.estorno.getMovimentacao().getTipo() + " ESTORNADA");

		this.estorno = new Estorno();

	}


	public void tabChange(TabChangeEvent event) {

		this.dataFinal = null;
		this.dataInicial = null;

		TabView tabView = (TabView) event.getComponent();
		int activeIndex = tabView.getChildren().indexOf(event.getTab());

		if (activeIndex == 3) {

		}

	}
	
	public void limpaEstorno() {
		this.estorno = new Estorno();
	}
	
	public List<Estorno> estornos() {

		List<Estorno> estornos = new ArrayList<Estorno>();

		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

		if(this.dataFinal != null && this.dataInicial != null) { // ENTÃO FOI FEITO UMA BUSCA
			if (tipoItemNavegacao == null) {
				estornos = estornoService.listAll(this.retornaUsuarioDaSessao().getId(), formato.format(this.dataInicial),
						formato.format(this.dataFinal));
			} else {
				estornos = estornoService.listAll(tipoItemNavegacao, this.retornaUsuarioDaSessao().getId(),
						formato.format(this.dataInicial), formato.format(this.dataFinal));
			}

		} else {
			if (tipoItemNavegacao == null) {
				estornos = estornoService.listAll(this.retornaUsuarioDaSessao().getId());
			} else {
				estornos = estornoService.listAll(tipoItemNavegacao, this.retornaUsuarioDaSessao().getId());
			}
		}

		return this.filtraVisualizacao(estornos);
	}


	public List<Estorno> filtraVisualizacao(List<Estorno> estornos) {

		List<Estorno> listFinal = new ArrayList<>();

		for(Estorno estorno : estornos) {
			if(estorno.getMovimentacao().getUnidadeArmazenada().equals(this.unidadeVisualizacao)) {
				listFinal.add(estorno);
			}
		}
		return listFinal;
	}


	public Usuario retornaUsuarioDaSessao() {
		return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}

	public void limpaBusca() {
		this.setDataFinal(null);
		this.dataInicial = null;
		this.setMinDatePesquisa(null);
	}


	public void configuraMinData() {
		this.setMinDatePesquisa(dataInicial);
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getTipoItemNavegacao() {
		return tipoItemNavegacao;
	}

	public void setTipoItemNavegacao(String tipoItemNavegacao) {
		this.tipoItemNavegacao = tipoItemNavegacao;
	}

	public List<Estorno> getEstornos() {
		return estornos;
	}

	public void setEstornos(List<Estorno> estornos) {
		this.estornos = estornos;
	}

	public Date getMinDatePesquisa() {
		return minDatePesquisa;
	}

	public void setMinDatePesquisa(Date minDatePesquisa) {
		this.minDatePesquisa = minDatePesquisa;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Estorno getEstorno() {
		return estorno;
	}

	public void setEstorno(Estorno estorno) {
		this.estorno = estorno;
	}

	public Unidade getUnidadeVisualizacao() {
		return unidadeVisualizacao;
	}

	public void setUnidadeVisualizacao(Unidade unidadeVisualizacao) {
		this.unidadeVisualizacao = unidadeVisualizacao;
	}
}
