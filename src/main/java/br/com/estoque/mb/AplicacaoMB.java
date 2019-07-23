package br.com.estoque.mb;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Aplicacao;
import br.com.estoque.service.AplicacaoService;
import br.com.estoque.util.FacesUtil;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AplicacaoMB implements Serializable {

    private static final long serialVersionUID = 1L;


    Aplicacao novaAplicacao = null;

    @Inject
    AplicacaoService aplicacaoService;

    Aplicacao aplicacao;

    public List<Aplicacao> retornaAplicacoes() {

        return aplicacaoService.listAll();
    }


    public void configuraNovaAplicacao() {
        this.novaAplicacao = new Aplicacao();
    }

    public void salvaNovaAplicao() {
        aplicacaoService.salvar(novaAplicacao);
        this.novaAplicacao = null;
    }

    // MÉTODO PARA EXCLUIR A APLICAÇÃO
    // VERIFICA SE EXISTE MOVIMENTAÇÕES RELACIONADAS
    public void excluirAplicacao(Long id) {

        try {
            aplicacaoService.excluir(aplicacaoService.porId(id));
        } catch (NegocioException e) {
            FacesUtil.addWarningMessageTicket("Impossível excluir enquanto existir Movimentações relacionadas a Aplicação");
        }


    }

    public void carregaAplicacaoUpdate(Long id) {
        this.novaAplicacao = aplicacaoService.porId(id);
    }


    public Aplicacao getNovaAplicacao() {
        return novaAplicacao;
    }

    public void setNovaAplicacao(Aplicacao novaAplicacao) {
        this.novaAplicacao = novaAplicacao;
    }
}
