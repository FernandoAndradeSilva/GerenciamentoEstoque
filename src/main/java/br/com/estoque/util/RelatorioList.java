package br.com.estoque.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioList {


    private HttpServletResponse response;
    private FacesContext context;
    private ByteArrayOutputStream baos;
    private InputStream stream;
    private Connection con;







    public RelatorioList() {

        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();

    }

    public void getRelatorio(String tipoItemRelatorio , String tipoRelatorio, Date dtInicial , Date dtFinal , Long idUsuario, List<Object> itens) throws FileNotFoundException {







        FileInputStream stream = new FileInputStream("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\Projeto_Estoque\\resources\\Jasper\\"+tipoRelatorio+".jasper");

        Map<String, Object> params = new HashMap<String, Object>();

        ImageIcon LogoRelatorioMeio = new ImageIcon("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\Projeto_Estoque\\resources\\images\\LogoRelatorioMeio.png");
        ImageIcon LogoRelatorioLogo = new ImageIcon("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\Projeto_Estoque\\resources\\images\\LogoRelatorioLogo.png");



        params.put("LogoRelatorioMeio", LogoRelatorioMeio.getImage());
        params.put("LogoRelatorioLogo", LogoRelatorioLogo.getImage());
        params.put("tituloRelatorio", "RELATÓRIO "+ tipoRelatorio + " "+tipoItemRelatorio);
        params.put("tipoRelatorio" , tipoRelatorio.toString());
        params.put("tipoItem" , tipoItemRelatorio.toString());
        params.put("dtIni", dtInicial);
        params.put("dtFim", dtFinal);


        baos = new ByteArrayOutputStream();

        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);
            JasperPrint print = JasperFillManager.fillReport(report, params, getConexao());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "inline; filename=relatorio.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();


        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Connection getConexao() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gerenciamentoestoque", "root", "root");
            return con;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return con;
    }


}