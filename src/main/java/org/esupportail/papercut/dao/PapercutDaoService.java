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
package org.esupportail.papercut.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PapercutDaoService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private PayboxPapercutTransactionLogRepository txRepository;
	
    @Transactional
    public void persist(PayboxPapercutTransactionLog txLog) {
    	txRepository.save(txLog);
    }
    
    @Transactional
    public void remove(PayboxPapercutTransactionLog txLog) {       
        if (em.contains(txLog)) {
            em.remove(txLog);
        } else {
            PayboxPapercutTransactionLog attached = txRepository.findById(txLog.getId()).get();
            em.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {       
        em.flush();
    }
    
    @Transactional
    public void clear() {        
        em.clear();
    }
    
    @Transactional
    public PayboxPapercutTransactionLog merge(PayboxPapercutTransactionLog txLog) {        
        PayboxPapercutTransactionLog merged = em.merge(txLog);
        em.flush();
        return merged;
    }
    
	/*
	 * 
	 * Requêtes pour PostgreSQL : corespondent aux préférences "requetes***" positionnées à la valeur "useOriginal"
	Pour adapter les requêtes à un autre SGBD, changer la valeur "useOriginal" par la requête directement
	*
	*/
    public List<Object>  countNumberTranscationsBydate(String requete) {
    	if(requete.equals("useOriginal")){
    		requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, count(*) as count FROM paybox_papercut_transaction_log GROUP BY year, month ORDER BY year,month";
    	}

		Query q = em.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public List<Object>  countMontantTranscationsBydate(String requete) {
    	if(requete.equals("useOriginal")){
    		requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, sum(CAST(montant AS decimal)) as totalMois FROM paybox_papercut_transaction_log GROUP BY year, month ORDER BY year,month";
    	}

		Query q = em.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public List<Object>  countCumulNombreTranscationsBydate(String requete) {
    	if(requete.equals("useOriginal")){
    		requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, sum(count(* )) OVER (PARTITION BY date_part('year',transaction_date) ORDER BY  date_part('year',transaction_date),date_part('month',transaction_date)) as cumul FROM paybox_papercut_transaction_log Where date_part('year',transaction_date) in (select distinct date_part('year',transaction_date) from paybox_papercut_transaction_log) GROUP BY year, month ORDER BY year,month";
    	}

		Query q = em.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public List<Object>  countCumulMontantTranscationsBydate(String requete) {
    	if(requete.equals("useOriginal")){
    		requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month ,sum( sum(CAST(montant AS decimal))) OVER (PARTITION BY date_part('year',transaction_date) ORDER BY  date_part('year',transaction_date),date_part('month',transaction_date)) as cumul FROM paybox_papercut_transaction_log Where date_part('year',transaction_date) in (select distinct date_part('year',transaction_date) from paybox_papercut_transaction_log) GROUP BY year, month ORDER BY year,month";
    	} 	

		Query q = em.createNativeQuery(requete);

        return q.getResultList();
    }

    
}
