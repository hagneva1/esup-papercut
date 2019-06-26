/**
 * Licensed to EsupPortail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * EsupPortail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.papercut.domain;

import java.util.SortedMap;
import java.util.TreeMap;

public class PayBoxForm {

	private String actionUrl;

	private String site;	

	private String rang;

	private String identifiant;

	private String total;

	private String devise;
	
	private String commande;
	
	private String clientEmail;
	
	private String retourVariables;
	
	private String hash;
	
	private String time;
	
	private String callbackUrl;
	
	private String forwardEffectueUrl;
	
	private String forwardRefuseUrl;
	
	private String forwardAnnuleUrl;
	
	private String hmac;
	
	private Integer nbSheets;
	
	private Integer nbColorSheets;
	
	private String toolTip;
	
	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getRang() {
		return rang;
	}

	public void setRang(String rang) {
		this.rang = rang;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public String getCommande() {
		return commande;
	}

	public void setCommande(String commande) {
		this.commande = commande;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	public String getRetourVariables() {
		return retourVariables;
	}

	public void setRetourVariables(String retourVariables) {
		this.retourVariables = retourVariables;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getForwardEffectueUrl() {
		return forwardEffectueUrl;
	}

	public void setForwardEffectueUrl(String forwardEffectueUrl) {
		this.forwardEffectueUrl = forwardEffectueUrl;
	}

	public String getForwardRefuseUrl() {
		return forwardRefuseUrl;
	}

	public void setForwardRefuseUrl(String forwardRefuseUrl) {
		this.forwardRefuseUrl = forwardRefuseUrl;
	}

	public String getForwardAnnuleUrl() {
		return forwardAnnuleUrl;
	}

	public void setForwardAnnuleUrl(String forwardAnnuleUrl) {
		this.forwardAnnuleUrl = forwardAnnuleUrl;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

	public Integer getNbSheets() {
		return nbSheets;
	}

	public void setNbSheets(Integer nbSheets) {
		this.nbSheets = nbSheets;
	}

	public Integer getNbColorSheets() {
		return nbColorSheets;
	}

	public void setNbColorSheets(Integer nbColorSheets) {
		this.nbColorSheets = nbColorSheets;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public SortedMap<String, String> getOrderedParams() {
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("PBX_SITE", site);
		params.put("PBX_RANG", rang);
		params.put("PBX_IDENTIFIANT", identifiant);
		params.put("PBX_TOTAL", total);
		params.put("PBX_DEVISE", devise);
		params.put("PBX_CMD", commande);
		params.put("PBX_PORTEUR", clientEmail);
		params.put("PBX_RETOUR", retourVariables);
		params.put("PBX_HASH", hash);
		params.put("PBX_TIME", time);
		params.put("PBX_REPONDRE_A", callbackUrl);
		params.put("PBX_EFFECTUE", forwardEffectueUrl);
		params.put("PBX_REFUSE", forwardRefuseUrl);
		params.put("PBX_ANNULE", forwardAnnuleUrl);		
		//params.put("PBX_HMAC", hmac);	
		return params;
	}
	
	public String getParamsAsString() {
		String paramsAsString = "";
		 SortedMap<String, String> params = getOrderedParams();
		 for(String key : params.keySet()) {
			 paramsAsString = paramsAsString + key + "=" + params.get(key) + "&";
		 }
		 paramsAsString = paramsAsString.subSequence(0, paramsAsString.length()-1).toString();
		 try {
			 // paramsAsString = URLEncoder.encode(paramsAsString, "utf8");
			 // System.out.println(paramsAsString);
			 return paramsAsString;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getMontant() {
		return new Double(new Double(total)/100.0).toString();
	}
	
}
