package org.pablog.bookdb.web.datatables;

import java.io.Serializable;
import java.util.List;

import flexjson.JSONSerializer;

public class DataTablesResponse<T extends Object> implements Serializable {
	/*
	 * Questo parametro viene cambiato dal client per ogni chiamata ajax.
	 * Doviamo fare atenzione a fare il cast a integer per evitare Xss attacks
	 */       
	private String sEcho;

	/*
	 * Numero totale di records
	 */
	private int iTotalRecords;
	
	/*
	 * Numero totale di records dopo filtrare. Siccome non filtriamo, per noi e' lo stesso di iTotalRecords. 
	 * Pero' datatables si aspetta questo parametro
	 */
	private int  iTotalDisplayRecords;
	
	/*
	 * Datasource.
	 * Come default, datatables aspetta un array 2D pero flexjson converte la lista come un array di oggeti json.
	 * Usando il parametro 'aoColumns' nella chiamata di datatables possiamo specificare le proprieta' corrispondenti alle colonne
	 */
	private List<T> aaData;
	
	/*
	 * Proprieta' usata per inviare messagi (errori, info, etc) dal server nel ritorno delle chiamate json.
	 */
//	private String serverMessage;
//	
	private String sColumns;

	
	public DataTablesResponse(String sEcho, int iTotalRecords, int iTotalDisplayRecords,
			List<T> aaData, String sColumns) {
		super();
		this.sEcho = sEcho;
		this.iTotalRecords = iTotalRecords;
		this.iTotalDisplayRecords = iTotalDisplayRecords;
		this.aaData = aaData;
//		this.serverMessage = serverMessage;
		this.sColumns = sColumns;
	}
	
	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	public int getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public List<T> getAaData() {
		return aaData;
	}

	public void setAaData(List<T> aaData) {
		this.aaData = aaData;
	}

//	public String getServerMessage() {
//		return serverMessage;
//	}
//
//	public void setServerMessage(String serverMessage) {
//		this.serverMessage = serverMessage;
//	}
//
	public String getsColumns() {
		return sColumns;
	}

	public void setsColumns(String sColumns) {
		this.sColumns = sColumns;
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
}
