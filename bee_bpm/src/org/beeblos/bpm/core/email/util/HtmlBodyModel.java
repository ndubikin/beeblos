package org.beeblos.bpm.core.email.util;


import java.util.List;

public class HtmlBodyModel {

	private String body; // aqui deberia ir el body ( string ) con los src ajustados
	private List<ImageManagerModel> imageList; // aquí la lista de imagenes que detectaste ( el id que le pusiste ( cid:etc ) y el nombre del fichero ( lo que decia antes en el src="xxx" )
	
	public HtmlBodyModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HtmlBodyModel(String body, List<ImageManagerModel> image) {
		this.body = body;
		this.imageList = image;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public List<ImageManagerModel> getImageList() {
		return imageList;
	}
	public void setImageList(List<ImageManagerModel> imageList) {
		this.imageList = imageList;
	}
	@Override
	public String toString() {
		return "HtmlBodyModel [" + (body != null ? "body=" + body + ", " : "")
				+ (imageList != null ? "image=" + imageList : "") + "]";
	}

	
	
	
	
}
