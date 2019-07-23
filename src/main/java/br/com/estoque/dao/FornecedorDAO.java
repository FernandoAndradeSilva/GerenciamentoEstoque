package br.com.estoque.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Fornecedor;
import br.com.estoque.model.Usuario;

public class FornecedorDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Fornecedor salvar(Fornecedor fornecedor) {
		return manager.merge(fornecedor);	
	}
	
	public void excluir(Fornecedor fornecedor) {
		try {
			fornecedor = porId(fornecedor.getId());
			manager.remove(fornecedor);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Fornecedor porId(Long id) {
		return manager.find(Fornecedor.class, id);	
	}


	public List<Fornecedor> listAll() {
		return manager.createNativeQuery("SELECT * FROM Fornecedor", Fornecedor.class).getResultList();
	}



	public Fornecedor verificaCNPJ(String cnpj) {

		Query query = manager.createNativeQuery(
				"SELECT id, cel, cnpj, cpf, tipoFornecedor , cep , nomeFantasia ,  contato, email, nome, observacao, tel, bairro, cidade, complemento, endereco, estado, numero " +
						" FROM fornecedor where cnpj = :bindCnpj",
				Fornecedor.class);
		query.setParameter("bindCnpj", cnpj);

		Fornecedor fornecedor = null;



		try {
			return (Fornecedor) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}



	}

	public Fornecedor verificaCPF(String cpf) {

		Query query = manager.createNativeQuery(
				"SELECT id, cel, cnpj, cpf, tipoFornecedor ,  cep , nomeFantasia ,  contato, email, nome, observacao, tel, bairro, cidade, complemento, endereco, estado, numero " +
						" FROM fornecedor where cpf = :bindCpf",
				Fornecedor.class);
		query.setParameter("bindCpf", cpf);

		Fornecedor fornecedor = null;



		try {
			return (Fornecedor) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}



	}


	public Fornecedor verificaNomeFornecedor(String nome) {
		
		Query query = manager.createNativeQuery(
				"SELECT id, cel, cnpj, cpf , tipoFornecedor, cep , nomeFantasia ,  contato, email, nome, observacao, tel, bairro, cidade, complemento, endereco, estado, numero " +
						" FROM Fornecedor where nome = :bindnome",
				Fornecedor.class);
		query.setParameter("bindnome", nome);

		Fornecedor user = null;
		
		try {
			return (Fornecedor) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}	
	}
	
}	
