package com.afb.dpd.mobilemoney.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONException;

import com.afb.dpd.orangemoney.jpa.exception.DAOAPIException;
import com.afb.dpd.orangemoney.jpa.tools.Entries;
import com.afb.dpd.orangemoney.jpa.tools.bkeve;
import com.afb.dpd.orangemoney.jpa.tools.bkmvti;

public class SendEventToCbsDAO extends AbstractDAOAPI<SendEventToCbsDAO>{
	
	public SendEventToCbsDAO() {
		super(SendEventToCbsDAO.class);
	}

	@Override
	public String getUrl() {
		return null;
	}
	
	public bkeve sendEventToCoreBanking(String url, bkeve eve) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		return this.sendPostEvent(url, eve);
	}
	
	public boolean sendEventToCoreBankingEOD(String url, List<String> eve) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		return this.sendPostEOD(url, eve);
	}
	
	public bkeve reverseEventFromCoreBanking(String url, String bkeve) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		return this.sendPostReverseEvent(url, bkeve);
	}
	
	public bkeve checkEventToCoreBanking(String url, String bkeve) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		return this.getEvent(url, bkeve);
	}
	
	public Double getSoldeToCoreBanking(String url, String ncp) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		return this.getBalance(url, ncp);
	}
	
	public boolean accountingentries(String url, Entries entry) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		return this.postingEntries(url, entry);
	}
	
	public boolean sendEntriesToCoreBanking(String url, List<bkmvti> bkmvti) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		return this.sendAccountings(url, bkmvti);
	}
	
	public boolean ignoreEventFromCoreBanking(String url, String bkeve) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		return this.ignoreEventFromCoreBanking(url, bkeve);
	}
}
