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
package org.esupportail.papercut.web;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.domain.PayBoxForm;
import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.esupportail.papercut.security.ContextHelper;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/{papercutContext}/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {
	

    final static Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Resource 
	EsupPaperCutService esupPaperCutService;
	
	@Resource
	EsupPapercutConfig config;
	
	@Autowired
	PapercutDaoService papercutDaoService;
	   
	@GetMapping(produces = "text/html")
    public String userView(HttpServletRequest request, Model uiModel) {	

		EsupPapercutContext context = config.getContext(ContextHelper.getCurrentContext());
		if(context == null) {
			return "redirect:/";
		}
    	
    	double papercutSheetCost = Double.parseDouble(context.getPapercutSheetCost());
    	double papercutColorSheetCost = Double.parseDouble(context.getPapercutColorSheetCost());
        
        String uid = getUid();
        String userMail = getUserMail();
        
        // check if the user can make a transaction
        int transactionNbMax = Integer.parseInt(context.getTransactionNbMax());
        BigDecimal transactionMontantMax  = new BigDecimal(context.getTransactionMontantMax());
        boolean canMakeTransaction = true;
	
        // constraints via transactionNbMax
    	if(transactionNbMax>-1) {
    		long nbTransactionsNotArchived = papercutDaoService.countByUidAndArchived(uid, false);   			
    		if(transactionNbMax<=nbTransactionsNotArchived) {
    			canMakeTransaction = false;
    		}
    	}		
        
    	BigDecimal payboxMontantMin = new BigDecimal(context.getPayboxMontantMin());
    	BigDecimal payboxMontantMax  = new BigDecimal(context.getPayboxMontantMax());
    	BigDecimal payboxMontantStep  = new BigDecimal(context.getPayboxMontantStep());	
        // constraints on the slider via transactionMontantMax
        if(canMakeTransaction && transactionMontantMax.intValue() > -1) {  	
			Page<PayboxPapercutTransactionLog> transactionsNotArchived = papercutDaoService.findPayboxPapercutTransactionLogsByUidAndArchived(uid, false, PageRequest.of(0, Integer.MAX_VALUE));
			BigDecimal montantTotalTransactionsNotArchived = new BigDecimal("0");
			for(PayboxPapercutTransactionLog txLog: transactionsNotArchived.getContent()) {
				montantTotalTransactionsNotArchived = montantTotalTransactionsNotArchived.add(new BigDecimal(txLog.getMontant()));
			}
			transactionMontantMax = transactionMontantMax.multiply(new BigDecimal("100")).subtract(montantTotalTransactionsNotArchived);
        	if(transactionMontantMax.doubleValue() < payboxMontantMax.doubleValue()*100) {
        		payboxMontantMax = transactionMontantMax.divide(payboxMontantStep).multiply(payboxMontantStep);
        		payboxMontantMax = payboxMontantMax.divide(new BigDecimal("100"));
        		if(payboxMontantMax.compareTo(payboxMontantMin) == -1) {
        			canMakeTransaction = false;
        		}
            	uiModel.addAttribute("payboxMontantMax", payboxMontantMax.doubleValue());
        	}
        }
	       
        // generation de l'ensemble des payboxForm :  payboxMontantMin -> payboxMontantMax par pas de payboxMontantStep
        String contextPath = request.getContextPath();
        Map<Integer, PayBoxForm> payboxForms = new HashMap<Integer, PayBoxForm>(); 
        for(BigDecimal montant=payboxMontantMin; montant.compareTo(payboxMontantMax)<=0; montant = montant.add(payboxMontantStep)) {
        	PayBoxForm payBoxForm = esupPaperCutService.getPayBoxForm(context, uid, userMail, montant.doubleValue(), contextPath);
        	if(payBoxForm != null) {
	        	if(papercutSheetCost > 0) {
	        		int nbSheets = (int)(montant.doubleValue()/papercutSheetCost);
	        		payBoxForm.setNbSheets(nbSheets);
	        	}
	        	if(papercutColorSheetCost > 0) {
	        		int nbColorSheets = (int)(montant.doubleValue()/papercutColorSheetCost);
	        		payBoxForm.setNbColorSheets(nbColorSheets);
	        	}
	        	payboxForms.put(montant.multiply(new BigDecimal(100)).intValue(), payBoxForm);
        	}
        }
        Map<Integer, PayBoxForm> sortedMap = new TreeMap<Integer, PayBoxForm>(payboxForms);
        
        uiModel.addAttribute("payboxForms", sortedMap);
        
        uiModel.addAttribute("canMakeTransaction", canMakeTransaction);

    	UserPapercutInfos userPapercutInfos = esupPaperCutService.getUserPapercutInfos(context, uid);   		
		uiModel.addAttribute("userPapercutInfos", userPapercutInfos);

    	uiModel.addAttribute("isAdmin", WebUtils.isAdmin());
    	uiModel.addAttribute("isManager", WebUtils.isManager());
    	uiModel.addAttribute("active", "home");
    	
    	uiModel.addAttribute("papercutContext", context);
    	
    	return "user/index";
    }

    
    @GetMapping(value = "/logs", produces = "text/html")
    public String myhistoryList(@PageableDefault(size = 10, direction = Direction.DESC, sort = "transactionDate") Pageable pageable,
    		Model uiModel) {
    	
        String uid = getUid();
  
        uiModel.addAttribute("pageLogs", papercutDaoService.findPayboxPapercutTransactionLogsByUid(uid, pageable));
       
        uiModel.addAttribute("isAdmin", WebUtils.isAdmin());
        uiModel.addAttribute("isManager", WebUtils.isManager());
        uiModel.addAttribute("active", "history");
    	
        uiModel.addAttribute("active", "logs"); 	
        return "user/history";
    }
	
    
    @GetMapping(value = "/logs/{i}", produces = "text/html")
    public String viewTransactionLog(@PathVariable("id") Long id, Model uiModel) {
    	uiModel.addAttribute("payboxpapercuttransactionlog", papercutDaoService.findById(id).get());
    	uiModel.addAttribute("itemId", id);
    	uiModel.addAttribute("isAdmin", WebUtils.isAdmin());
    	uiModel.addAttribute("isManager", WebUtils.isManager());
    	uiModel.addAttribute("active", "logs"); 	
        return "user/show-transactionlog";
    }

    private String getUid() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

    
	private String getUserMail() {
		//String mailAttr = request.getPreferences().getValue(PREF_PAPERCUT_USER_MAIL_ATTR, null);
		//Map<String,String> userinfo = (Map<String,String>)request.getAttribute(PortletRequest.USER_INFO);
		//String mail = userinfo.get(mailAttr);
		// TODO
		return "toto@univ-ville.fr";
	}

		
    /**
     * When user is redirected on esup-papercut after the paybox process, 
     * and if validatePayboxJustWithRedirection is true,
     * we validate the transaction regarding informations on queryString 
     * (there is a paybox signature that secures this, 
     * so we can do that even if we can consider it "less" secure than the direct call of paybox) 
     */
    @RequestMapping(params="signature")
    public String renderViewAfterPaybox(@PathVariable String papercutContext, @RequestParam(required=false) String montant, @RequestParam String reference, @RequestParam(required=false) String auto, 
    		@RequestParam String erreur, @RequestParam String idtrans, @RequestParam String signature) {	
    	
    	EsupPapercutContext context = config.getContext(papercutContext);
    	
    	if("true".equals(context.getValidatePayboxJustWithRedirection())) {
	    	String uid = getUid();
	    	String queryString = getQueryString(montant, reference, auto, erreur, idtrans, signature);

	    	log.debug(queryString);
	    	
	    	// TODO ajouter context.paperCutContext dans payboxCallback
	    	esupPaperCutService.payboxCallback(context, montant, reference, auto, erreur, idtrans, signature, queryString, null, uid);
    	}
    	
    	return "redirect:/user";
    }


    private static String getQueryString(String montant, String reference,
    		String auto, String erreur, String idtrans, String signature) {
    	StringBuilder sb = new StringBuilder();

    	if(montant != null) {
	    	sb.append(String.format("%s=%s", "montant", montant));
	    	sb.append("&");
    	}

    	sb.append(String.format("%s=%s", "reference", reference));
    	sb.append("&");

    	if(auto != null) {
	    	sb.append(String.format("%s=%s", "auto", auto));
	    	sb.append("&");
    	}

    	sb.append(String.format("%s=%s", "erreur", erreur));
    	sb.append("&");	

    	sb.append(String.format("%s=%s", "idtrans", idtrans));
    	sb.append("&");	

    	sb.append(String.format("%s=%s", "signature", signature));

    	return sb.toString();
    }
    
} 
