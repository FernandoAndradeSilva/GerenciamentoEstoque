package br.com.estoque.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.NaturezaMaterial;

public class NaturezaMaterialDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public NaturezaMaterial salvar(NaturezaMaterial naturezaMaterial) {
		return manager.merge(naturezaMaterial);		
	}
	
	public void excluir(NaturezaMaterial naturezaMaterial) {
		try {
			naturezaMaterial = porId(naturezaMaterial.getId());
			manager.remove(naturezaMaterial);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public NaturezaMaterial porId(Long id) {
		return manager.find(NaturezaMaterial.class, id);	
	}
	
	
	public List<NaturezaMaterial> listAll() {
		return manager.createNativeQuery("SELECT * FROM NaturezaMaterial", NaturezaMaterial.class).getResultList();
	}

	
}	
