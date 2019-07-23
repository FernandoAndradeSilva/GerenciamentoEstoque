package br.com.test;


import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@FacesConverter(value = "clienteConverter")
@Named
@ViewScoped
public class ClienteConverter implements Converter , Serializable {

	private static final long serialVersionUID = 1L;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		System.out.println("OBJETO");
		
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		System.out.println("STRING");
		

		
		return value.toString();
	}
	
	
	
}
