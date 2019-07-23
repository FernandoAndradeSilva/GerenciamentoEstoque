package br.com.estoque.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.estoque.dao.FornecedorDAO;
import br.com.estoque.model.Fornecedor;
import br.com.estoque.util.Transacional;

public class FornecedorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FornecedorDAO fornecedorDAO;

	@Transacional
	public void salvar(Fornecedor fornecedor) {

		fornecedorDAO.salvar(fornecedor);
	}

	@Transacional
	public void excluir(Fornecedor fornecedor) {
		fornecedorDAO.excluir(fornecedor);
	}

	public List<Fornecedor> listAll() {

		return fornecedorDAO.listAll();
	}

	public Fornecedor porId(Long id) {
		return fornecedorDAO.porId(id);
	}
	
	public Fornecedor verificaCnpj(String cnpj) {

		return fornecedorDAO.verificaCNPJ(cnpj);
	}

	public Fornecedor verificaNomeFornecedor(String nome) {
		
		return fornecedorDAO.verificaNomeFornecedor(nome);
		
	}

	public Fornecedor verificaCpf(String cpf) {

		return fornecedorDAO.verificaCPF(cpf);
	}
}
