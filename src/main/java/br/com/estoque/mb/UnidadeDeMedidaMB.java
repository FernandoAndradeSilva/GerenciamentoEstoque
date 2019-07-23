package br.com.estoque.mb;


import br.com.estoque.model.UnidadeDeMedida;
import br.com.estoque.service.UnidadeDeMedidaService;
import br.com.estoque.util.FacesUtil;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UnidadeDeMedidaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    UnidadeDeMedidaService unidadeDeMedidaService;

    private UnidadeDeMedida unidadeDeMedida = new UnidadeDeMedida();


    public List<UnidadeDeMedida> retornaUnidadesDeMedida() {

        return unidadeDeMedidaService.listAll();
    }

    public void salvarUnidadeDeMedida() {

        unidadeDeMedidaService.salvar(unidadeDeMedida);
        FacesUtil.addInfoMessageRedirect("UNIDADE CADASTRADA");

        this.limpaUnidadeDeMedida();
    }


    public void limpaUnidadeDeMedida() {
        this.unidadeDeMedida = new UnidadeDeMedida();
    }
    public UnidadeDeMedida getUnidadeDeMedida() {
        return unidadeDeMedida;
    }

    public void setUnidadeDeMedida(UnidadeDeMedida unidadeDeMedida) {
        this.unidadeDeMedida = unidadeDeMedida;
    }
}
