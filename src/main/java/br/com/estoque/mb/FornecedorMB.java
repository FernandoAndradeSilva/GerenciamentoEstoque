package br.com.estoque.mb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.estoque.model.Movimentacao;
import br.com.estoque.model.TipoFornecedor;
import br.com.estoque.util.BuscaCep;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.primefaces.context.RequestContext;

import br.com.estoque.model.Fornecedor;
import br.com.estoque.service.FornecedorService;
import br.com.estoque.util.FacesUtil;

@Named
@ViewScoped
public class FornecedorMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    FornecedorService fornecedorService = new FornecedorService();

    private List<Fornecedor> fornecedores;

    private Fornecedor fornecedor = new Fornecedor();

    private String nomeTemp = null;
    private String cnpjTemp = null;
    private String cpfTemp = null;


    public void selecionaTipoFornecedor(int num) {
        if (num == 1) {
            this.fornecedor.setTipoFornecedor(TipoFornecedor.PessoaJuridica);
        } else if (num == 2) {
            this.fornecedor.setTipoFornecedor(TipoFornecedor.PessoaFisica);
        } else if (num == 3) {
            this.fornecedor.setTipoFornecedor(TipoFornecedor.Outros);
        }
    }

    public String retornaTipo() {

        if(this.fornecedor.getTipoFornecedor() != null) {
            if(this.fornecedor.getTipoFornecedor().equals(TipoFornecedor.PessoaJuridica)) {
                return "PESSOA JURÍDICA";
            } else if (this.fornecedor.getTipoFornecedor().equals(TipoFornecedor.PessoaFisica)) {
                return "PESSOA FÍSICA";
            } else
                return "OUTROS";
        } else
            return "";



    }


    public void inicializar() {
        if (fornecedor.getId() != null) {
            this.nomeTemp = fornecedor.getNome();
            this.cnpjTemp = fornecedor.getCnpj();
            this.cpfTemp = fornecedor.getCpf();
        }
    }

    public void verificaNomeFornecedor(FacesContext context, UIComponent toValidate, Object value) {

        String nome = value.toString();

        Fornecedor fornecedor = null;
        fornecedor = fornecedorService.verificaNomeFornecedor(nome);
        if (fornecedor != null) {

            if (!(nome.equals(this.nomeTemp))) {
                FacesMessage msg = new FacesMessage("NOME já cadastrado", "Campo obrigatório");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }

        }
    }

    public void verificaCnpjFornecedor(FacesContext context, UIComponent toValidate, Object value) {

        String cnpj = value.toString();

        String validarCnpj = value.toString();
        validarCnpj = validarCnpj.replace(".", "");
        validarCnpj = validarCnpj.replace("/", "");
        validarCnpj = validarCnpj.replace("-", "");


        if (this.isCNPJ(validarCnpj)) {
            Fornecedor fornecedor = null;

            if (cnpj != "") {
                fornecedor = fornecedorService.verificaCnpj(cnpj);
            }

            if (fornecedor != null) {

                if ((!(cnpj.equals(this.cnpjTemp))) && value.toString() != "") {
                    FacesMessage msg = new FacesMessage("CNPJ já cadastrado", "CNPJ já cadastrado");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                } else {
                    System.out.println("VALIDO->" + value.toString());
                }


            }
        } else if (cnpj != "") {
            FacesMessage msg = new FacesMessage("CNPJ inválido", "CNPJ inválido");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }


    }


    public boolean isCNPJ(String CNPJ) {
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return (false);

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char) ((11 - r) + 48);

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return (true);
            else return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }


    public void verificaCpfFornecedor(FacesContext context, UIComponent toValidate, Object value) {

        String cpf = value.toString();

        String cpfValida = value.toString().replace("-", "");


        if (this.isCPF(cpfValida)) {
            Fornecedor fornecedor = null;

            if (cpf != "") {
                fornecedor = fornecedorService.verificaCpf(cpf);
            }

            if (fornecedor != null) {

                if ((!(cpf.equals(this.cpfTemp))) && value.toString() != "") {
                    FacesMessage msg = new FacesMessage("CPF já cadastrado", "Campo obrigatório");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }
        } else if (cpf != "") {
            FacesMessage msg = new FacesMessage("CPF inválido", "Campo obrigatório");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }


    }


    public boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return (false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return (true);
            else return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }


    public void verificaNomeFornecedor1(FacesContext context, UIComponent toValidate, Object value) {

        System.out.println(this.nomeTemp);

        String nome = value.toString();

        Fornecedor fornecedor = null;

        fornecedor = fornecedorService.verificaNomeFornecedor(nome);
        if (fornecedor != null) {

            System.out.println(this.getFornecedor().getNome());
            System.out.println(value.toString());

            if (this.fornecedor.getId() != null && this.fornecedor.getNome().equals(nome)) {
                System.out.println("entoru aqui");
            } else {
                FacesMessage msg = new FacesMessage("NOME já cadastrado", "Campo obrigatório");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }

        }
    }

    public void validarCep(FacesContext context, UIComponent toValidate, Object value) throws UnirestException, IllegalAccessException {

        String cep = value.toString();

        System.out.println(cep);

        if (cep != null) {
            cep = cep.replace(".", "");
            cep = cep.replace("-", "");
            BuscaCep busca = new BuscaCep();
            HashMap<String, Object> endereco = new HashMap<>();
            endereco = busca.retornaEndereco(cep);

            if (endereco == null) {
                FacesMessage msg = new FacesMessage("Verifique o 'CEP' digitado", "Verifique o 'CEP' digitado");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }


    }



    public void salvar() {

        String message = null;

        if (this.fornecedor.getId() != null) {
            message = "FORNECEDOR ATUALIZADO";
        } else {
            message = "FORNECEDOR CADASTRADO";
        }


        fornecedorService.salvar(fornecedor);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('DialogNovoFornecedor').hide();");
        FacesUtil.addInfoMessageTicket(message);
        fornecedor = new Fornecedor();


    }


    public void excluirFornecedor(Long id) {

        Fornecedor fornecedor = fornecedorService.porId(id);

        if(fornecedor.getMovimentacoes().size() > 0) {

            boolean movimentacoesAtivas = false;

            for(Movimentacao movimentacao : fornecedor.getMovimentacoes()) {
                if(movimentacao.isStatus()) {
                    movimentacoesAtivas = true;
                    break;
                }
            }

            if(movimentacoesAtivas == false) {
                fornecedorService.excluir(fornecedor);
                FacesUtil.addInfoMessageTicket("FORNECEDOR EXCLUÍDO");


            } else {
                FacesUtil.addWarningMessageTicket("Impossível excluir , existem Movimentações com o FORNCEDOR");
            }

        } else {
            fornecedorService.excluir(fornecedor);
            FacesUtil.addInfoMessageTicket("FORNECEDOR EXCLUÍDO");
        }

    }


    public void buscaCEP() throws UnirestException, IllegalAccessException {


        String cep = this.fornecedor.getCep();

        if (cep != "") {
            cep = cep.replace(".", "");
            cep = cep.replace("-", "");


            BuscaCep busca = new BuscaCep();


            HashMap<String, Object> endereco = new HashMap<>();
            endereco = busca.retornaEndereco(cep);

            if (endereco != null) {
                fornecedor.setEstado(endereco.get("estado").toString());
                fornecedor.setCidade(endereco.get("cidade").toString());
                fornecedor.setEndereco(endereco.get("logradouro").toString());
                fornecedor.setBairro(endereco.get("bairro").toString());
            } else {
                this.fornecedor.setEstado("");
                this.fornecedor.setCidade("");
                this.fornecedor.setEndereco("");
                this.fornecedor.setBairro("");
                this.fornecedor.setNumero("");
                this.fornecedor.setComplemento("");
            }
        } else {

            this.fornecedor.setEstado("");
            this.fornecedor.setCidade("");
            this.fornecedor.setEndereco("");
            this.fornecedor.setBairro("");
            this.fornecedor.setNumero("");
            this.fornecedor.setComplemento("");
        }


    }

    public void resetaFornecedor() {
        this.fornecedor = new Fornecedor();
    }

    public List<Fornecedor> fornecedores() {
        return this.fornecedores = fornecedorService.listAll();
    }

    public List<Fornecedor> getFornecedores() {
        return fornecedores;
    }

    public void setFornecedores(List<Fornecedor> fornecedores) {
        this.fornecedores = fornecedores;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getNomeTemp() {
        return nomeTemp;
    }

    public String getCpfTemp() {
        return cpfTemp;
    }

    public void setCpfTemp(String cpfTemp) {
        this.cpfTemp = cpfTemp;
    }

    public void setNomeTemp(String nomeTemp) {
        this.nomeTemp = nomeTemp;
    }
}
