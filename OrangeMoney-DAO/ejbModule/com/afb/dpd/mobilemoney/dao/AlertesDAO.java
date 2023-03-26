package com.afb.dpd.mobilemoney.dao;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.afb.dpd.orangemoney.jpa.exception.DAOAPIException;
import com.afb.dpd.orangemoney.jpa.tools.ClientProduit;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlertesDAO{
	
	
	public static final int TIMEOUT = 120;//seconds
	ObjectMapper mapper;
	

	public AlertesDAO() {;
	}

	
	public HttpHost getTarget(String host, String protocole, String port) {
		// specify the host, protocol, and port
		if(StringUtils.isNotBlank(host) && StringUtils.isNotBlank(port) && StringUtils.isNotBlank(protocole))
			return new HttpHost(host, Integer.parseInt(port), protocole);
		else
			return null;
	}
	
	
	public HttpClient getHttpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(TIMEOUT*1000)
				.setConnectionRequestTimeout(TIMEOUT*1000)
				.setSocketTimeout(TIMEOUT*1000)
				.build();
		//*** System.out.println("Connection Timeout , " + requestConfig.getConnectionRequestTimeout());
		HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		return client;
	}
	
	
	public int sendSimpleSMS(String host, String protocole, String port, String url, String message, String phone) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		URIBuilder builder = new URIBuilder(url);
		
		builder.setParameter("message", message);	
		builder.setParameter("phone", phone);	
		
		builder.build();
		
		System.out.println("request url " +builder.build());
		
		HttpGet getRequest = new HttpGet(builder.build());		
		
		HttpResponse httpResponse = this.getHttpClient().execute(getTarget(host, protocole, port), getRequest);
		return httpResponse.getStatusLine().getStatusCode();
		
	}	
	
	
	public int sendSimpleMail(String host, String protocole, String port, String url, String message, String mails, String subject, String from) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		URIBuilder builder = new URIBuilder(url);
		
		builder.setParameter("message", message);	
		builder.setParameter("format", "txt");	
		builder.setParameter("to", mails);	
		builder.setParameter("subject", subject);	
		builder.setParameter("from", from);	
		
		builder.build();
		
		System.out.println("request url " +builder.build());
		
		HttpGet getRequest = new HttpGet(builder.build());		
		
		HttpResponse httpResponse = this.getHttpClient().execute(getTarget(host, protocole, port), getRequest);
		return httpResponse.getStatusLine().getStatusCode();
		
	}
	
	
	
	public ClientProduit mapToObject(JSONObject obj) throws JsonParseException, JsonMappingException, IOException, JSONException {
		ObjectMapper map = new ObjectMapper();
		StringReader reader = new StringReader(obj.toString());
		ClientProduit o = map.readValue(reader, ClientProduit.class);
		return o;
	}
	
	
	protected void checkStatusResponse(HttpResponse response) throws DAOAPIException {
		int status = response.getStatusLine().getStatusCode();		
		System.out.println("******** status ********* : " + status);;
		if(status != 200 && status != 201 && status != 202) {
			throw new DAOAPIException(" HTTP : "+status + " result "+response.getStatusLine().getReasonPhrase());
		}
	}
	
	
	public List<ClientProduit> resiliations(String host, String protocole, String port, String url, String module) throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		URIBuilder builder = new URIBuilder(url + "/resiliations");		
		builder.setParameter("module", module);			
		builder.build();
		
		System.out.println("request url " +builder.build());

		List<ClientProduit> results = null;
		try {
			HttpGet getRequest = new HttpGet(builder.build());		
			
			HttpResponse httpResponse = this.getHttpClient().execute(getTarget(host, protocole, port), getRequest);
			this.checkStatusResponse(httpResponse);
			results = new ArrayList<ClientProduit>();
					
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity != null) {
				String retSrc = EntityUtils.toString(entity);				
				if(retSrc.length() == 0) {
					retSrc = new JSONArray().toString();
				}
				// CONVERT RESPONSE STRING TO JSON ARRAY
				JSONArray ja = new JSONArray(retSrc);
				int n = ja.length();
				for(int i=0 ; i< n ; i++) {
					JSONObject jo = ja.getJSONObject(i);
					results.add(this.mapToObject(jo));
				}
			}
		} catch (ParseException e) {
		}
		
		System.out.println("******** results.size() ********* : " + results.size());
		return results;		
	}
	

}
