package br.com.estoque.util;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;



public class FacesUtil {
	
	// MESSAGES SEM REDIRECT
	
	public static void addInfoMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));		
	}
	
	public static void addWarningMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg));		
	}
	
	public static void addInfoNo(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg));		
	}
	
	
	// MESSAGES COM REDIRECT
	
	public static void addWarningMessageRedirect(String msg) {
		if (FacesContext.getCurrentInstance().getExternalContext().getFlash().isKeepMessages() == false) {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg));		
	}
	
	public static void addInfoNoRedirect(String msg) {
		if (FacesContext.getCurrentInstance().getExternalContext().getFlash().isKeepMessages() == false) {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg));		
	}
	
	// MESSAGE STICKER
	
	public static void addWarningMessageTicket(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));		
	}
	
	public static void addInfoMessageRedirect(String msg) {
		if (FacesContext.getCurrentInstance().getExternalContext().getFlash().isKeepMessages() == false) {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}		
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));		
	}
	
	public static void addInfoMessageTicket(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));		
	}
	

	
}
