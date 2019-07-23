package br.com.estoque.service;

import br.com.estoque.dao.EstoqueDAO;
import br.com.estoque.model.Estoque;
import br.com.estoque.model.Estorno;
import br.com.estoque.model.Movimentacao;
import br.com.estoque.model.Usuario;
import br.com.estoque.util.Transacional;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

public class EstoqueService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EstoqueDAO estoqueDAO;

	@Inject
    private MovimentacaoService movimentacaoService;

	@Transacional
	public void salvar(Estoque estoque) {

		estoqueDAO.salvar(estoque);
	}

	@Transacional
	public Estoque salvaRetorna(Estoque estoque) {
		return estoqueDAO.salvar(estoque);
	}

	@Transacional
	public void excluir(Estoque estoque) {
		estoqueDAO.excluir(estoque);
	}

	public List<Estoque> listAll() {
		return estoqueDAO.listAll();
	}

	public Estoque porId(Long id) {
		return estoqueDAO.porId(id);
	}

	@Transacional
	public void atualizaEstoque(Movimentacao movimentacao) {

		Estoque estoqueItem = estoqueDAO.retornaEstoqueDoItem(movimentacao.getItem().getId() ,
				this.retornaUsuarioDaSessao().getSetor().getUnidade().getId());

		int quantidade = movimentacao.getQuantidade();

		if(movimentacao.getTipo().equals("ENTRADA")) { // ENTRADA

			estoqueItem.setEntradas(estoqueItem.getEntradas() + quantidade);

			if(estoqueItem.getCusto() == 0.0) { // CUSTO ZERADO
				estoqueItem.setCusto(movimentacao.getValor() / quantidade); // CONFIGURA PRIMEIRO CUSTO
				estoqueItem.setSaldo(estoqueItem.getSaldo() + quantidade);
			} else {
				float totalSaldoItem = estoqueItem.getSaldo() * estoqueItem.getCusto();
				estoqueItem.setSaldo(estoqueItem.getSaldo() + quantidade); // ATUALIZA SALDO
				estoqueItem.setCusto((totalSaldoItem + movimentacao.getValor()) / (estoqueItem.getSaldo()));
			}
		} else if(movimentacao.getTipo().equals("SAIDA")) { // SAÍDA
			estoqueItem.setSaidas(estoqueItem.getSaidas() + quantidade);
			estoqueItem.setSaldo(estoqueItem.getSaldo() - quantidade);

		}

		estoqueDAO.salvar(estoqueItem);
	}

	@Transacional
	public void atualizaEstoqueEstorno(Estoque estoque , Movimentacao movimentacao) {
		if(movimentacao.getTipo().equals("ENTRADA")) {
			estoque.setEntradas(estoque.getEntradas() - movimentacao.getQuantidade());
			estoque.setSaldo(estoque.getSaldo() - movimentacao.getQuantidade());
            estoque.setCusto(this.recalculaCusto(movimentacao.getItem().getId()));

		} else if (movimentacao.getTipo().equals("SAIDA")) {
			estoque.setSaidas(estoque.getSaidas() - movimentacao.getQuantidade());
			estoque.setSaldo(estoque.getSaldo() + movimentacao.getQuantidade());
		}

		estoqueDAO.salvar(estoque);

	}

    public float recalculaCusto(Long idItem) {

	    int quantidadeTotal = 0;
	    float valorTotal = 0;

        for(Movimentacao movimentacao :  movimentacaoService.listarEntradasAtivas(idItem)) {
            quantidadeTotal += movimentacao.getQuantidade();
            valorTotal += movimentacao.getValor();
        }
        if(quantidadeTotal == 0) {
            return 0;
        } else {
            return valorTotal / quantidadeTotal;
        }
    }


	public Estoque retornaEstoqueDoItem(Long idItem , Long idUnidade) {
		return estoqueDAO.retornaEstoqueDoItem(idItem , idUnidade);
	}

	public List<Estoque> retornaEstoqueDoItem(Long idItem) {
		return estoqueDAO.retornaEstoqueDoItem(idItem);
	}


	public Usuario retornaUsuarioDaSessao() {
		return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}

	public boolean verificaSaldoSuficiente(int quantidade , Long idItem ) {
		return estoqueDAO.verificaSaldoSuficiente(quantidade , idItem);
	}
}
