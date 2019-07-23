package br.com.estoque.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.estoque.dao.NaturezaMaterialDAO;
import br.com.estoque.model.NaturezaMaterial;
import br.com.estoque.util.Transacional;

public class NaturezaMaterialService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NaturezaMaterialDAO naturezaMaterialDAO;

	@Transacional
	public void salvar(NaturezaMaterial naturezaMaterial) {

		naturezaMaterialDAO.salvar(naturezaMaterial);
	}
	
	@Transacional
	public NaturezaMaterial salvaRetorna(NaturezaMaterial naturezaMaterial) {

		naturezaMaterial.setDescricao(naturezaMaterial.getDescricao().toUpperCase());
		naturezaMaterial.setSilga(naturezaMaterial.getSilga().toUpperCase());

		return naturezaMaterialDAO.salvar(naturezaMaterial);
	}

	@Transacional
	public void excluir(NaturezaMaterial naturezaMaterial) {
		naturezaMaterialDAO.excluir(naturezaMaterial);
	}

	public List<NaturezaMaterial> listAll() {

		return naturezaMaterialDAO.listAll();
	}

	public NaturezaMaterial porId(Long id) {
		return naturezaMaterialDAO.porId(id);
	}

}