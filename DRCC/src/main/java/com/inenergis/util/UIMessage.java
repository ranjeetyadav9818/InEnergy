package com.inenergis.util;

import org.apache.commons.collections.CollectionUtils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

@Named
public class UIMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	FacesContext facesContext;
	
	
	public void addMessage(String pattern, Object ... args){
		addMessage(pattern, FacesMessage.SEVERITY_INFO, args);
	}

	public void addMessage(String pattern, FacesMessage.Severity severity, Object ... args){
		String msg = MessageFormat.format(pattern, args);
		facesContext.addMessage(null, new FacesMessage(severity,msg,""));
	}

	public void addMessageWithoutFormatting(String msg) {
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,msg,""));
	}

	public List<FacesMessage> getMessageList(){
		return facesContext.getMessageList();
	}

	public boolean errorMessagesGenerated(){
		if (CollectionUtils.isNotEmpty(getMessageList())) {
			return getMessageList().stream().filter(msg -> FacesMessage.SEVERITY_ERROR.equals(msg.getSeverity())).findFirst().isPresent();
		}
		return false;
	}
}
