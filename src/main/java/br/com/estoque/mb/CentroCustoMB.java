package br.com.estoque.mb;

import br.com.estoque.model.CentroCusto;
import br.com.estoque.model.Setor;
import br.com.estoque.service.CentroCustoService;
import br.com.estoque.util.FacesUtil;
import org.primefaces.context.RequestContext;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named
@ViewScoped
public class CentroCustoMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	CentroCustoService centroCustoService;
	
	private CentroCusto centroCusto;

	private CentroCusto novoCentroCusto = new CentroCusto();

	private Setor setor = new Setor();

	public List<CentroCusto> centrosDeCusto() {
		return centroCustoService.listAll();
	}
	
	public List<CentroCusto> listarPorSetor() {

		return centroCustoService.listPorId(this.setor.getId());
	}

	public void selecionaSetor(Setor setor) {
		this.setor = setor;
	}



	public void excluiirCentroDeCusto(Long id) {

		CentroCusto cc = centroCustoService.porId(id);

		if(cc.getMovimentacoes().size() !=0) {
			FacesUtil.addWarningMessageTicket("Impossível excluir o Centro de Custo enquanto possui Movimentações");
		} else {
		    centroCustoService.excluir(cc);
            FacesUtil.addInfoMessageRedirect("Centro de Custo excluído");
            this.listarPorSetor();
        }



//		centroCustoService.excluir();

	}


	public void salvarCentroCusto() {
        this.centroCustoService.salvar(novoCentroCusto);

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('DialogNovoCentroCusto').hide();");
        FacesUtil.addInfoMessageTicket("Cento De Custo cadastrado");

	}

	public void limpaCentroCusto() {
	    this.novoCentroCusto = new CentroCusto();
    }

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public CentroCusto getNovoCentroCusto() {
		return novoCentroCusto;
	}

	public void setNovoCentroCusto(CentroCusto novoCentroCusto) {
		this.novoCentroCusto = novoCentroCusto;
	}
}
