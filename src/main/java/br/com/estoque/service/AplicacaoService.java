package br.com.estoque.service;

import br.com.estoque.dao.AplicacaoDAO;
import br.com.estoque.model.Aplicacao;
import br.com.estoque.util.Transacional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

public class AplicacaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AplicacaoDAO aplicacaoDAO;

	@Transacional
	public void salvar(Aplicacao aplicacao) {

		aplicacao.setNome(aplicacao.getNome().toUpperCase());
		aplicacaoDAO.salvar(aplicacao);
	}

	@Transacional
	public Aplicacao salvaRetorna(Aplicacao aplicacao) {
		return aplicacaoDAO.salvar(aplicacao);
	}

	@Transacional
	public void excluir(Aplicacao aplicacao) {
		aplicacaoDAO.excluir(aplicacao);
	}

	public List<Aplicacao> listAll() {

		return aplicacaoDAO.listAll();
	}

	public Aplicacao porId(Long id) {
		return aplicacaoDAO.porId(id);
	}
	
	
	


}
