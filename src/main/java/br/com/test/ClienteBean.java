package br.com.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ClienteBean  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Cliente cli = new Cliente(); 
	
	Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Cliente> lista() {
		
		List<Cliente> lista = new ArrayList<Cliente>();
		
		Cliente c1 = new Cliente();
		Cliente c2 = new Cliente();
		Cliente c3 = new Cliente();
		
		c1.setId(1);
		c1.setNome("Fernando");

		c2.setId(2);
		c2.setNome("Marcelo");

		c3.setId(3);
		c3.setNome("Pedro");
		
		lista.add(c1);
		lista.add(c2);
		lista.add(c3);
		
		return lista;
	}
	
	public void tste() {
		System.out.println(id);
	}


	public Cliente getCli() {
		return cli;
	}



	public void setCli(Cliente cli) {
		this.cli = cli;
	}
	
	
}
