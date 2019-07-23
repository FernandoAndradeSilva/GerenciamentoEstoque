package br.com.estoque.mb;


import br.com.estoque.model.Item;
import br.com.estoque.model.Movimentacao;
import br.com.estoque.model.Usuario;
import br.com.estoque.service.ItemService;
import br.com.estoque.service.MovimentacaoService;
import br.com.estoque.util.FacesUtil;
import br.com.estoque.util.Relatorio;
import org.primefaces.context.RequestContext;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Named
@ViewScoped
public class RelatorioMB implements Serializable {

    private static final long serialVersionUID = 1L;


    private Relatorio relatorio = new Relatorio();

    private Date dataInicial;
    private Date dataFinal;


    private String tipoItemRelatorio;
    private String tipoRelatorio;

    @Inject
    private ItemService itemService;

    @Inject
    private MovimentacaoService movimentacaoService;


    public boolean verificaCamposRelatorio() {
        if (this.dataFinal != null && this.dataInicial != null && this.tipoItemRelatorio != null && this.tipoRelatorio != null) {
            return false;
        } else
            return true;
    }

    public void resetaDataFinal() {
        this.dataFinal = new Date();
    }


    public void gerarRelatorio() throws FileNotFoundException {


        Relatorio relatorio = new Relatorio();
        relatorio.getRelatorio (this.tipoItemRelatorio, this.tipoRelatorio, this.dataInicial, this.dataFinal, this.retornaUsuarioDaSessao().getId());

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('DialogRelatorio').hide();");


    }


    public void limpaCamposRelatorio() {
        this.tipoRelatorio = "";
        this.tipoItemRelatorio = "";
        this.dataInicial = new Date();
        this.dataFinal = new Date();
    }



    public String getTipoItemRelatorio() {
        return tipoItemRelatorio;
    }

    public void setTipoItemRelatorio(String tipoItemRelatorio) {
        this.tipoItemRelatorio = tipoItemRelatorio;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }


    public Usuario retornaUsuarioDaSessao() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }


    public void construriRelatorioSaldo() {

        List<Object> objetos = new ArrayList<Object>();

        String tipo = "COMPUTADOR";



        List<Item> itens = itemService.listAll(tipo);

        System.out.println(itens);


        for(Item item : itens) {
            HashMap<String , Object > map = new HashMap<>();
            int totalEntradas = 0;
            int totalSaidas = 0;

            float valorTotal = 0;

            for(Movimentacao movimentacao : item.getMovimentacoes()) {
                if(movimentacao.getTipo().equals("ENTRADA")) {
                    valorTotal += movimentacao.getQuantidade() * movimentacao.getValor();
                    totalEntradas+=movimentacao.getQuantidade();
                } else if(movimentacao.getTipo().equals("SAIDA")) {
                    totalSaidas+=movimentacao.getQuantidade();
                }

            }
            map.put("ESTOQUE MÍNIMO" , item.getEstoqueMinimo());
            map.put("ESPECIFICAÇÃO" , item.getEspecificacao());
            map.put("DESCRIÇÃO" , item.getDescricao());
            map.put("CÓDIGO" , item.getCodigo());

            map.put("VALOR" , valorTotal / totalEntradas);

            map.put("SALDO" , (totalEntradas - totalSaidas));
            map.put("TOTAL ENTRADAS", totalEntradas);
            map.put("TOTAL SAÍDAS", totalSaidas);


            objetos.add(map);
        }

        System.out.println(objetos);

    }


}
