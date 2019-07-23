package br.com.estoque.mb;

import br.com.estoque.model.CentroCusto;
import br.com.estoque.model.Setor;
import br.com.estoque.model.Unidade;
import br.com.estoque.model.Usuario;
import br.com.estoque.service.SetorService;
import br.com.estoque.service.UnidadeService;
import br.com.estoque.util.FacesUtil;
import org.primefaces.context.RequestContext;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named
@ViewScoped
public class SetorMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	SetorService setorService;
	
	private Setor setor;

	public List<Setor> setores() {
		return setorService.listAll();
	}

	private List<Usuario> usuariosSetor = new ArrayList<>();

	private Setor novoSetor = new Setor();

	public void carregaSetoresUsuario(Long idSetor) {

		Setor setor = setorService.porId(idSetor);

		this.usuariosSetor = setor.getUsuarios();
	}

	public void salvarSetor() {
		this.setorService.salvar(novoSetor);

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('DialogNovoSetor').hide();");
		FacesUtil.addInfoMessageTicket("SETOR cadastrado");
	}



	public void excluirSetor(Long idSetor) {

		Setor setor = setorService.porId(idSetor);

		if(setor.getCentrosDeCusto().size() == 0 && setor.getUsuarios().size() == 0) {
			setorService.excluir(setor);
			FacesUtil.addInfoMessageTicket("O SETOR foi excluído");
		} else {
			FacesUtil.addWarningMessageTicket("Impossível excluir SETOR, possui CENTROS DE CUSTO ou USUÁRIOS cadastrados");
		}

	}



	public void limpaNovoSetor() {
		this.novoSetor = new Setor();
	}

	public SetorService getSetorService() {
		return setorService;
	}

	public void setSetorService(SetorService setorService) {
		this.setorService = setorService;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Setor getNovoSetor() {
		return novoSetor;
	}

	public void setNovoSetor(Setor novoSetor) {
		this.novoSetor = novoSetor;
	}

	public List<Usuario> getUsuariosSetor() {
		return usuariosSetor;
	}

	public void setUsuariosSetor(List<Usuario> usuariosSetor) {
		this.usuariosSetor = usuariosSetor;
	}
}
