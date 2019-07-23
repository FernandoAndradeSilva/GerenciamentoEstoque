package br.com.estoque.util;

import br.com.estoque.service.UsuarioService;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

/**
 *
 * @author marcelosiedler
 */
public class JavaMail {

    private String emailDestinatario;

    private String assunto;
    private String msg;

    public String getEmailDestinatario() {
        return emailDestinatario;
    }

    public void setEmailDestinatario(String emailDestinatario) {
        this.emailDestinatario = emailDestinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String resetarSenha(String email)  {

        JavaMail jm = new JavaMail();

        jm.setEmailDestinatario(email);
        jm.setAssunto("NOVA SENHA");

        Random rand = new java.util.Random();
        String novaSenha = String.valueOf(Long.toHexString(rand.nextLong()).substring(0,8));

        jm.setMsg("SUA NOVA SENHA É: " + novaSenha);

        jm.enviarGmail();

        return novaSenha;

        //this.redefineSenhaUsuario(novaSenha);

    }

    public boolean enviarGmail() {
        boolean retorno = false;
        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.cienciasnauticas.org.br");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session s = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication("webmaster@cienciasnauticas.org.br", "icn3nstc");//email e senha usuário
                    }
                });

        s.setDebug(true);

        //compose message
        try {
            MimeMessage message = new MimeMessage(s);
            message.setFrom(new InternetAddress("webmaster@cienciasnauticas.org.br"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.emailDestinatario));

            message.setSubject(this.assunto);
            message.setContent(this.msg, "text/html; charset=utf-8");

            //send message
            Transport.send(message);

            retorno = true;

        } catch (MessagingException e) {
            retorno = false;
            e.printStackTrace();
        }
        return retorno;
    }






}