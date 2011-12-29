package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.ERROR_MESSAGE_ICON;
import static org.beeblos.bpm.core.util.Constants.FATAL_MESSAGE_ICON;
import static org.beeblos.bpm.core.util.Constants.INFO_MESSAGE_ICON;
import static org.beeblos.bpm.core.util.Constants.WARN_MESSAGE_ICON;

import java.text.MessageFormat;

public class FGPException {
	
	public final static int INFO = 0;
	public final static int WARN = 1;
	public final static int ERROR = 2;
	public final static int FATAL = 3;
	
	private String imagePath;
	private String code;
	private String message;
	private String[] params;
	private int level;
	
	public FGPException(String code,String message,String[] params,int level){
		this.code = code;		
		this.params = params;
		this.level = level;	 
		
		if( HelperUtil.getErrorResourceBundle().containsKey(code) ){
			this.message = HelperUtil.getErrorResourceBundle().getString(code);
			
			// Wil 20100824
			if(params != null){
				MessageFormat mf = new MessageFormat(code);
				this.message = mf.format(this.message,params).toString();
			}
			
		}else{
			this.message = message;
		}
		
		switch (level) {
			case INFO:
				imagePath = HelperUtil.getContextParameter(INFO_MESSAGE_ICON);
			break;
			case WARN:
				imagePath = HelperUtil.getContextParameter(WARN_MESSAGE_ICON);
			break;
			case ERROR:
				imagePath = HelperUtil.getContextParameter(ERROR_MESSAGE_ICON);
			break;
			case FATAL:
				imagePath = HelperUtil.getContextParameter(FATAL_MESSAGE_ICON);
			break;
			
			default:
			break;
		}		
	}	
	
	private boolean isEmpty(String str){
		return str == null || str.trim().equals("");
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String[] getParams() {
		return params;
	}
	public void setParams(String[] params) {
		this.params = params;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
