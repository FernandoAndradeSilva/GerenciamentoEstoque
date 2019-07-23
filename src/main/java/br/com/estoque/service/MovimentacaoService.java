package br.com.estoque.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.com.estoque.dao.EstoqueDAO;
import br.com.estoque.dao.MovimentacaoDAO;
import br.com.estoque.model.Estoque;
import br.com.estoque.model.Movimentacao;
import br.com.estoque.util.Transacional;

public class MovimentacaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MovimentacaoDAO movimentacaoDAO;


	@Inject
	private EstoqueDAO estoqueDAO;

	@Transacional
	public void salvar(Movimentacao movimentacao) {

		movimentacao.setData(new Date());
		movimentacaoDAO.salvar(movimentacao);
	}

	@Transacional
	public void excluir(Movimentacao movimentacao) {
		movimentacaoDAO.excluir(movimentacao);
	}

	public List<Movimentacao> listAll() {

		return movimentacaoDAO.listAll();
	}

	public Movimentacao porId(Long id) {
		return movimentacaoDAO.porId(id);
	}

	public List<Movimentacao> listAll(String tipoMovimentacao, Long id) {
		return movimentacaoDAO.listAll(tipoMovimentacao, id);
	}

	public List<Movimentacao> listAll(String tipoMovimentacao, String tipoItem, Long id) {
		return movimentacaoDAO.listAll(tipoMovimentacao, tipoItem, id);
	}


	public List<Movimentacao> listAll(String tipoMovimentacao, String tipoItemNavegacao, Long id, String dataInicial,
									  String dataFinal) {
		return movimentacaoDAO.listAll(tipoMovimentacao, tipoItemNavegacao, id , dataInicial , dataFinal);
	}


	public List<Movimentacao> listAll(String tipoMovimentacao, Long id, String dataInicial, String dataFinal ) {
		return movimentacaoDAO.listAll(tipoMovimentacao, id , dataInicial , dataFinal);
	}


	public List<Movimentacao> listarEntradasAtivas(Long idItem) {
		return movimentacaoDAO.listarEntradasAtivas(idItem);
	}

}
