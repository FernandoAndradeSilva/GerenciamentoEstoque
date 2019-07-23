package br.com.estoque.service;

import br.com.estoque.dao.ModificacaoDAO;
import br.com.estoque.model.Modificacao;
import br.com.estoque.util.Transacional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ModificacaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ModificacaoDAO modificacaoDAO;

    @Transacional
    public void salvar(Modificacao modificacao) {

        modificacao.setData(new Date());
        modificacaoDAO.salvar(modificacao);
    }

    @Transacional
    public void excluir(Modificacao modificacao) {
        modificacaoDAO.excluir(modificacao);
    }

    public Modificacao porId(Long id) {
        return modificacaoDAO.porId(id);
    }

    public List<Modificacao> listAll() {
        return modificacaoDAO.listAll();
    }

    public List<Modificacao> listAll(Long idItem) {
        return modificacaoDAO.listAll(idItem);
    }
}
