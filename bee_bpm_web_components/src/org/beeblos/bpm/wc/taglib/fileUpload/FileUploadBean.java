/**
 * 
 */
package org.beeblos.bpm.wc.taglib.fileUpload;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.util.File;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.richfaces.component.html.HtmlFileUpload;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

/**
 * @author Ilya Shaikovsky
 *
 */
public class FileUploadBean extends CoreManagedBean {
	private static final long serialVersionUID = -7411311691524646787L;
	private ArrayList<File> files = new ArrayList<File>();
    private int MAX_UPLOADS_AVAILABLE = 1000;
    private int uploadsAvailable = MAX_UPLOADS_AVAILABLE;
    private boolean autoUpload = true;
    private boolean useFlash = false; //nes 20110118 - pongo en falso porque en principio no deberiamos usar flash para nada ...
    private boolean disabled = false;
    HtmlFileUpload fileUpload = null; 
    
    private boolean renderImagenHombre = true;
    private boolean renderImagenMujer  = false;
    private boolean mostrarHombre = true;
     
    private boolean unicoUpload = true;
    
    private static final Log logger = LogFactory.getLog(FileUploadBean.class);
    
    public int getSize() {    	    	
    	if (getFiles().size()>0){
            return getFiles().size();
        }else 
        {
            return 0;
        }
    }
    
    public int getNumElementos() {    	    	
    	if (getFiles().size()>0){
            return getFiles().size();
        }else 
        {
            return 0;
        }
    }    

    public FileUploadBean() {
    }
    
	public void limpiarListener(ActionEvent event){
		clearUploadData();
	}    

    public void paint(OutputStream stream, Object object) throws IOException {    	    	    	 	
        stream.write(getFiles().get(0).getData());
        stream.flush(); // rrl 20110517
    }

    // nes 20111101 - cambiado para que trabaje con los
    // ficheros en file system ( en el directorio que haga el upload
    // el tomcat ) en vez de subirlos a memoria y tenerlos en memoria ...
    public void listener(UploadEvent event){

    	
    	 UploadItem item = event.getUploadItem();
    	 File file = new File();
    	 
    	 try{
    	 
    	 file.setName(item.getFileName());
    	 //file.setMime(item.getContentType());
    	 
    	 /*Get File Data*/
    	 if ( item.isTempFile() ) {
    	 
	    	 //byte[] fileInBytes = new byte[(int)item.getFile().length()];
	    	 //java.io.File tempFile = item.getFile();
	    	 //FileInputStream fileInputStream = new FileInputStream(tempFile);
	    	 //fileInputStream.read(fileInBytes);
	    	 //fileInputStream.close();
	    	 
	    	 //file.setData(fileInBytes); //
    	 
    		 file.setData(null);
    		 file.setLength(item.getFile().length());
    		 file.setName(item.getFileName());
    		 file.setAbsolutePath(item.getFile().getAbsolutePath());
    		 file.setLoadedInMemory(false);
    		 
    		 
    	 } else {
    		 file.setData(item.getData());
    		 file.setLoadedInMemory(true);
    		 file.setLength(item.getFile().length());
    		 file.setAbsolutePath(null);
    	 }
   
    	 //HZC: Borrar si es solo se permite un upload,
    	 if(isUnicoUpload()){
    		 getFiles().clear();
    	 }
    	 files.add(file);
    	 //uploadsAvailable--;
    	 }
    	 catch(Exception e){
    	 
    	 }
    	 
    }
    
    // sube al server un fichero por memoria como byte[]
    //rrl 20110802 - nes 20111103
    public void addByteArrayAsFileAttachment(byte[] buffer, String nameFile) {
    	
		File file = new File();

		file.setLoadedInMemory(true);
		file.setAbsolutePath(null);

		file.setName(nameFile);
		file.setData(buffer);
		file.setLength(buffer.length); // nes 20111103

		files.add(file);
		
    }
      
    public String clearUploadData() {
    	getFiles().clear();    	
        setUploadsAvailable(MAX_UPLOADS_AVAILABLE);
        return null;
    }
    
    public long getTimeStamp(){
        return System.currentTimeMillis();
    }
    
    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) { 
        this.files = files;
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isUseFlash() {
        return useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

	public boolean isDisabled() {
		
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isRenderImagenHombre() {
		
		if( getSize() == 0  && mostrarHombre){
			renderImagenHombre = true;
		}else{
			renderImagenHombre = false;
		}
		
		return renderImagenHombre;
	}

	public void setRenderImagenHombre(boolean renderImagenHombre) {
		this.renderImagenHombre = renderImagenHombre;
	}

	public boolean isRenderImagenMujer() {
		
		if( getSize() == 0  && !mostrarHombre){
			renderImagenMujer = true;
		}else{
			renderImagenMujer = false;
		}		
		
		return renderImagenMujer;
	}

	public void setRenderImagenMujer(boolean renderImagenMujer) {
		this.renderImagenMujer = renderImagenMujer;
	}

	public boolean isMostrarHombre() {
		return mostrarHombre;
	}

	public void setMostrarHombre(boolean mostrarHombre) {
		this.mostrarHombre = mostrarHombre;
	}

	public boolean isUnicoUpload() {
		return unicoUpload;
	}

	public void setUnicoUpload(boolean unicoUpload) {
		this.unicoUpload = unicoUpload;
	}




}