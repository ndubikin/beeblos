package org.beeblos.bpm.core.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class File implements Serializable{
	
	private static final long serialVersionUID = 1027098774809408140L;

	private static final Log logger = LogFactory.getLog(File.class);
	
	private String Name;
	private String mime;
	private Long length;
	private byte[] data;
	
	// nes 20111101
	private String absolutePath;
	private boolean loadedInMemory;
	
	
	public File() {
		super();
		_init();

	}

	private void _init() {
		this.Name=null;
		this.mime=null;
		this.length=null;
		this.data=null;
		
		this.absolutePath=null;
		this.loadedInMemory=false;
	}
	
	
	public File(String name, String mime, Long length, byte[] data,
			String absolutePath, boolean loadedInMemory) {
		Name = name;
		this.mime = mime;
		this.length = length;
		this.data = data;
		this.absolutePath = absolutePath;
		this.loadedInMemory = loadedInMemory;
	}

	public byte[] getData() throws IOException {
		
		if ( !isLoadedInMemory() ) return getDataFromDisk();
		return data;
	}
	
	public byte[] getDataFromDisk() throws IOException{
		
		if ( isLoadedInMemory() ) return getData();
		 
		try {
			
	    	 byte[] fileInBytes = new byte[(int) this.getLength()];
	    	 java.io.File tempFile = new java.io.File( this.getAbsolutePath() );
	    	 FileInputStream fileInputStream = new FileInputStream(tempFile);
	    	 fileInputStream.read(fileInBytes);
	    	 fileInputStream.close();
	    	 
	    	 //file.setData(fileInBytes); //
			
			return  fileInBytes;
			
			
		} catch (Exception e) {
			String mensaje = "com.softpoint.bdc.util.File: getDataFromDisk: can't read file : ["+this.getAbsolutePath()+"] "+e.getMessage()+" "+e.getCause();
			logger.error(mensaje);
			e.printStackTrace();
			throw new IOException(mensaje);
		}
		
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		name = name.toLowerCase();
		Name = name;
		int extDot = name.lastIndexOf('.');
		if(extDot > 0 && this.mime==null){
			String extension = name.substring(extDot +1);
			
			if("bmp".equals(extension)){
				mime="image/bmp";
			} else if("jpg".equals(extension)){
				mime="image/jpeg";
			} else if("gif".equals(extension)){
				mime="image/gif";
			} else if("png".equals(extension)){
				mime="image/png";
		//HZC: considerar las sgts mime para los docs. 		
			} else if("doc".equals(extension)){
				mime="application/msword";
			} else if("xls".equals(extension)){
				mime="application/vnd.ms-excel";
			} else if("pdf".equals(extension)){
				mime="application/pdf";
			} else if("rtf".equals(extension)){
				mime="application/rtf";
			} else if("txt".equals(extension)){ // nes 20110125
				mime="text/plain";
			} else {
				mime = "application/octet-stream";
			}
		}
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	
	public String getMime(){
		return mime;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public boolean isLoadedInMemory() {
		return loadedInMemory;
	}

	public void setLoadedInMemory(boolean loadedInMemory) {
		this.loadedInMemory = loadedInMemory;
	}

	
}
