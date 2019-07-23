package br.com.estoque.mb;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.estoque.model.Unidade;
import br.com.estoque.util.JavaMail;
import org.primefaces.context.RequestContext;

import br.com.estoque.util.FacesUtil;
import br.com.estoque.model.Categoria;
import br.com.estoque.model.Usuario;
import br.com.estoque.service.CategoriaService;
import br.com.estoque.service.UsuarioService;

@Named
@SessionScoped
public class UsuarioMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    UsuarioService usuarioService = new UsuarioService();

    @Inject
    CategoriaService categoriaService = new CategoriaService();

    private Usuario usuarioParaLiberacao;

    private Usuario usuario = new Usuario();
    private Usuario novoUsuario = new Usuario();



    private String email = null;
    private String senha = null;
    private String senhaAntiga;
    private String novaSenha;
    private String emailNovaSenha;

    private List<SelectItem> menu;
    private List<Usuario> usuariosDoSistema;

    private List<Categoria> categoriasLiberacao;

    public List<Categoria> categoriasDoUsuario() {
        Usuario user = usuarioService.porId(this.usuario.getId());

        if (user != null) {
            return user.getCategorias();
        } else
            return null;


    }

    public void inicializar() {
        this.usuariosDoSistema = usuarioService.listAll();
    }

    public void redirecionaUsuarioLogadoIndex() {
            if(this.retornaUsuarioDaSessao() != null ) {

                try {
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect("http://localhost:8080/Projeto_Estoque_war_exploded/pages/index.xhtml");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public List<Usuario> usuarios() {
        return this.usuariosDoSistema = usuarioService.listAll();
    }

    public String logar() throws NoSuchAlgorithmException {

        this.usuario = usuarioService.verificaEmail(this.email);
        if (usuario != null) { // USUARIO EXISTE

            if (this.verificaSenha()) {

                if (this.usuario.isAtivo()) {
                    this.criarSessao();
                    FacesUtil.addInfoMessageRedirect("Bem Vindo " + usuario.getNome());
                    return "pages/index.xhtml?faces-redirect=true";

                } else
                    System.out.println("Usuario Inativo");
                FacesUtil.addWarningMessageTicket("Usuário inativo, contate a Administração");
                return "";
            } else {
                FacesUtil.addWarningMessageTicket("Senha incorreta");
                return "";
            }
        } else {
            FacesUtil.addWarningMessageTicket("Usuário não cadastrado");
            return "";
        }

    }

    public void lerCategorias(Long id) {

        System.out.println(categoriaService.categoriasUsuario(id));
        this.categoriasLiberacao = categoriaService.categoriasUsuario(id);
    }

    public int retornaNivelUsuarioDaSessao() {

        return Integer.valueOf(this.retornaUsuarioDaSessao().getNivelDeAcesso());

    }

    public void verificaPermissaoCadastrar() {
        if(this.retornaNivelUsuarioDaSessao() == 4) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("http://localhost:8080/Projeto_Estoque_war_exploded/pages/index.xhtml");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verificaPermissaoModificar(int entradas) {

        if ((entradas > 0) && (this.verificaPermissaoAdministativa() == false)) {
            return false;
        } else if ((entradas > 0 && ((this.retornaUsuarioDaSessao().getNivelDeAcesso() == 3) || (this.retornaUsuarioDaSessao().getNivelDeAcesso() == 4))))
            return false;
        else
            return true;
    }

    public boolean verificaPermissaoAdministativa() {

        boolean result = false;

        if(this.retornaUsuarioDaSessao() != null) {
            if (this.retornaUsuarioDaSessao().getNivelDeAcesso() == 1 || this.retornaUsuarioDaSessao().getNivelDeAcesso() == 2 ||
            this.retornaUsuarioDaSessao().getNivelDeAcesso() == 5) {
                result = true;
            } else
                result = false;
        }
        return result;
    }

    public boolean verificaPermissaoColaborador() {

        boolean result = false;

        if(this.retornaUsuarioDaSessao() != null) {
            if (this.retornaUsuarioDaSessao().getNivelDeAcesso() == 1 || this.retornaUsuarioDaSessao().getNivelDeAcesso() == 2 ||
                    this.retornaUsuarioDaSessao().getNivelDeAcesso() == 5 || this.retornaUsuarioDaSessao().getNivelDeAcesso() == 3) {
                result = true;
            } else
                result = false;
        }
        return result;
    }


    public void verificaUsuarioAdministrativo() {
        if(!this.verificaPermissaoAdministativa()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('DialogUsuarioNaoPermitido').show();");
        }
    }

    public String retornaNivelUsuario(int num) {

        String nivel = null;

        switch (num) {
            case 1:
                nivel = "MASTER";
                break;
            case 2:
                nivel = "ADMIN";
                break;
            case 3:
                nivel = "COLABORADOR";
                break;
            case 4:
                nivel = "CONVIDADO";
                break;
            case 5:
                nivel = "DIRETORIA";
                break;
        }
        return nivel;
    }

    public void salvarAlteracoesUsuario() {

        this.usuario.setCategorias(this.categoriasLiberacao);
        this.usuarioService.salvar(usuario);

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('DialogLiberacaoUsuario').hide();");
        FacesUtil.addInfoMessageRedirect("Usuário Atualizado");
    }


    public void criarSessao() throws NoSuchAlgorithmException {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("usuario", usuario);
    }

    public void logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        usuario = new Usuario();
    }

    public void sairDoSistema() {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("http://localhost:8080/Projeto_Estoque_war_exploded/login.xhtml");

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.logout();
    }

    private boolean verificaSenha() throws NoSuchAlgorithmException {
        if (this.Sha(this.senha).equals(this.usuario.getSenha())) {
            return true;
        } else {
            return false;
        }
    }

    public String Sha(String senha) throws NoSuchAlgorithmException {

        String s = senha;
        MessageDigest m = MessageDigest.getInstance("SHA-256");
        m.update(s.getBytes(), 0, s.length());
        return new BigInteger(1, m.digest()).toString(16);
    }

    public void verificaUsuarioLogado() {
        if (this.retornaUsuarioDaSessao() == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("http://localhost:8080/Projeto_Estoque_war_exploded/login.xhtml");
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public Usuario retornaUsuarioDaSessao() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }

    public void cadastrarUsuario() throws NoSuchAlgorithmException {
        // DEFINIÇÕES PADRÃO USUÁRIO CADASTRADO
        this.novoUsuario.setAtivo(false);
        this.novoUsuario.setNivelDeAcesso(4);

        novoUsuario.setSenha(this.Sha(this.novoUsuario.getSenha()));
        this.usuarioService.salvar(novoUsuario);

        this.limpaUsuario();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('DialogNovoUsuario').hide();");
        FacesUtil.addInfoMessageRedirect("Usuário cadastrado");
    }

    public void validaEmailCadastrado(FacesContext context, UIComponent toValidate, Object value) {

        if (usuarioService.verificaEmail(value.toString()) != null) {
            FacesMessage msg = new FacesMessage("'E-MAIL' já utilizado", "JÁ existe");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        } else {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(value.toString());

            if (matcher.matches() == false) {
                FacesMessage msg = new FacesMessage("Preencha com um 'E-MAIL' válido", "JÁ existe");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }

        }

    }

    public void verificaSenhaAntiga(FacesContext context, UIComponent toValidate, Object value) throws NoSuchAlgorithmException {

        if(!(this.Sha(value.toString()).equals(usuario.getSenha()))) {
            FacesMessage msg = new FacesMessage("Senha atual incorreta", "Senha atual incorreta");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public void verificaNovaSenha(FacesContext context, UIComponent toValidate, Object value) throws NoSuchAlgorithmException {

        if((this.Sha(value.toString()).equals(usuario.getSenha()))) {

            FacesMessage msg = new FacesMessage("A 'nova senha' deve ser diferente da 'senha atual'", "A 'nova senha' deve ser diferente da 'senha atual'");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public void redifineSenha() throws NoSuchAlgorithmException {

        Usuario user = usuarioService.porId(this.retornaUsuarioDaSessao().getId());
        user.setSenha(this.Sha(this.novaSenha));
        usuarioService.salvar(user);

        FacesUtil.addInfoMessageRedirect("Senha Redefinida");

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('DialogRedefinirSenha').hide();");

    }

    public void enviaNovaSenha() throws NoSuchAlgorithmException {
        Usuario usuario = usuarioService.verificaEmail(this.emailNovaSenha);

        if(usuario != null) {
            JavaMail jm = new JavaMail();
            String senhaRedefinida = jm.resetarSenha(this.emailNovaSenha);

            usuario.setSenha(this.Sha(senhaRedefinida));
            System.out.println(senhaRedefinida);
            usuarioService.salvar(usuario);

            FacesUtil.addInfoMessageRedirect("Verifique seu E-MAIL");
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('DialogRedefinirSenha').hide();");

        } else {
            FacesUtil.addWarningMessageTicket("Usuário não cadastrado");
        }
        this.emailNovaSenha = "";

    }

    public void selecionaUnidadeNovoUsuario() {
        System.out.println(novoUsuario.getSetor().getUnidade());
    }



    public void limpaUsuario() {
        this.novoUsuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Usuario> getUsuariosDoSistema() {
        return usuariosDoSistema;
    }

    public void setUsuariosDoSistema(List<Usuario> usuariosDoSistema) {
        this.usuariosDoSistema = usuariosDoSistema;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario getNovoUsuario() {
        return novoUsuario;
    }

    public void setNovoUsuario(Usuario novoUsuario) {
        this.novoUsuario = novoUsuario;
    }


    public List<SelectItem> getMenu() {
        return menu;
    }

    public void setMenu(List<SelectItem> menu) {
        this.menu = menu;
    }

    public Usuario getUsuarioParaLiberacao() {
        return usuarioParaLiberacao;
    }

    public void setUsuarioParaLiberacao(Usuario usuarioParaLiberacao) {
        this.usuarioParaLiberacao = usuarioParaLiberacao;
    }


    public List<Categoria> getCategoriasLiberacao() {
        return categoriasLiberacao;
    }

    public void setCategoriasLiberacao(List<Categoria> categoriasLiberacao) {
        this.categoriasLiberacao = categoriasLiberacao;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getEmailNovaSenha() {
        return emailNovaSenha;
    }

    public void setEmailNovaSenha(String emailNovaSenha) {
        this.emailNovaSenha = emailNovaSenha;
    }
}
