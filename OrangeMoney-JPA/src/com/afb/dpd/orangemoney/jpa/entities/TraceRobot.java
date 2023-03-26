package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Trace Robot
 * @author Yves LABO 
 * @version 1.0
 */
@Entity
@Table(name = "OGMO_TraceRobot")
public class TraceRobot implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static final Long Default = 01l;

	
	
	/**
	 * Id auto genere
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "DATETIME_TRACE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetimeTrace = new Date();
	
	@Column(name = "executedMethod")
	private boolean executedMethod = Boolean.FALSE;
	
	@Column
	private String typeExecution;
	
	@Column(length=1000)
	private String trace;
	
	
	
	public TraceRobot() {}
	
	
	public TraceRobot(Long id) {
		super();
		this.id = id;
	}
	
	


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	public Date getDatetimeTrace() {
		return datetimeTrace;
	}


	public boolean isExecutedMethod() {
		return executedMethod;
	}


	public void setExecutedMethod(boolean executedMethod) {
		this.executedMethod = executedMethod;
	}


	public void setDatetimeTrace(Date datetimeTrace) {
		this.datetimeTrace = datetimeTrace;
	}

	public String getFormattedDatetimeTrace() {
		return datetimeTrace == null ? null : new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(datetimeTrace); 
	}


	/**
	 * @return the typeExecution
	 */
	public String getTypeExecution() {
		return typeExecution;
	}


	/**
	 * @param typeExecution the typeExecution to set
	 */
	public void setTypeExecution(String typeExecution) {
		this.typeExecution = typeExecution;
	}


	/**
	 * @return the trace
	 */
	public String getTrace() {
		return trace;
	}


	/**
	 * @param trace the trace to set
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}
	
    
}
