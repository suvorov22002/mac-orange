package com.afb.dpd.orangemoney.jsf.models;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.afb.dpd.orangemoney.jsf.tools.OMTools;

public class ReportViewerDialog extends AbstractPortalForm {
	
	/**
	 * Type mime du flux de sortie
	 */
	private String mimeType = "";
	
	/**
	 * Flux a afficher dans le visualisateur
	 */
	private byte[] streamData = null;
	
	/**
	 * default constructor
	 */
	public ReportViewerDialog(){}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.persocheque.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Portal Report Viewer";
	}

	public void paint(OutputStream stream, Object object) {
    	
    	try{
    		
    		// Si le tableau de données n'est pas vide
    		if(streamData != null && streamData.length>0){
    			
    			// Affichage du flux
    			stream.write(streamData);
    		}
    		
    	} catch(Exception e) {
    		
			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
    	}
    }
    
	public void loadStream() {
		
		try{
    		
    		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    		response.setContentType(mimeType);
    		response.getOutputStream().write(streamData);
			
		}catch(Exception e ){
			PortalExceptionHelper.threatException(e);
		}
	}

	/**
	 * @return the streamData
	 */
	public byte[] getStreamData() {
		return streamData;
	}

	/**
	 * @param streamData the streamData to set
	 */
	public void setStreamData(byte[] streamData){
		this.streamData = streamData;
	}
	
	
	/**
	 * Methode de construction de l'etat
	 */
	public void buildReport(Collection<?> data, HashMap<Object, Object> map){
		
		try{
			// Construction de l'etat
			this.streamData = OMTools.getReportPDFBytes(OMTools.getReportsDir().concat("recuSouscription.jasper"), map, data); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Methode de construction de l'etat
	 */
	public void buildReport(String reportFileName,Collection<?> data, HashMap<Object, Object> map){
		
		try{
			// Construction de l'etat
			this.streamData = OMTools.getReportPDFBytes(OMTools.getReportsDir().concat(reportFileName), map, data); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.persocheque.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();
		
		mimeType = "";
		streamData = null;
	}
}
