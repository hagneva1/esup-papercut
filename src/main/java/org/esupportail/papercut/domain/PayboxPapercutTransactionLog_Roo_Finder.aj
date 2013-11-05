// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.esupportail.papercut.domain;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;

privileged aspect PayboxPapercutTransactionLog_Roo_Finder {
    
    public static Long PayboxPapercutTransactionLog.countFindPayboxPapercutTransactionLogsByIdtransEquals(String idtrans) {
        if (idtrans == null || idtrans.length() == 0) throw new IllegalArgumentException("The idtrans argument is required");
        EntityManager em = PayboxPapercutTransactionLog.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PayboxPapercutTransactionLog AS o WHERE o.idtrans = :idtrans", Long.class);
        q.setParameter("idtrans", idtrans);
        return ((Long) q.getSingleResult());
    }
    
    public static Long PayboxPapercutTransactionLog.countFindPayboxPapercutTransactionLogsByPaperCutContextEquals(String paperCutContext) {
        if (paperCutContext == null || paperCutContext.length() == 0) throw new IllegalArgumentException("The paperCutContext argument is required");
        EntityManager em = PayboxPapercutTransactionLog.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PayboxPapercutTransactionLog AS o WHERE o.paperCutContext = :paperCutContext", Long.class);
        q.setParameter("paperCutContext", paperCutContext);
        return ((Long) q.getSingleResult());
    }
    
    public static Long PayboxPapercutTransactionLog.countFindPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEquals(String uid, String paperCutContext) {
        if (uid == null || uid.length() == 0) throw new IllegalArgumentException("The uid argument is required");
        if (paperCutContext == null || paperCutContext.length() == 0) throw new IllegalArgumentException("The paperCutContext argument is required");
        EntityManager em = PayboxPapercutTransactionLog.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PayboxPapercutTransactionLog AS o WHERE o.uid = :uid  AND o.paperCutContext = :paperCutContext", Long.class);
        q.setParameter("uid", uid);
        q.setParameter("paperCutContext", paperCutContext);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<PayboxPapercutTransactionLog> PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByIdtransEquals(String idtrans, String sortFieldName, String sortOrder) {
        if (idtrans == null || idtrans.length() == 0) throw new IllegalArgumentException("The idtrans argument is required");
        EntityManager em = PayboxPapercutTransactionLog.entityManager();
        String jpaQuery = "SELECT o FROM PayboxPapercutTransactionLog AS o WHERE o.idtrans = :idtrans";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        TypedQuery<PayboxPapercutTransactionLog> q = em.createQuery(jpaQuery, PayboxPapercutTransactionLog.class);
        q.setParameter("idtrans", idtrans);
        return q;
    }
    
    public static TypedQuery<PayboxPapercutTransactionLog> PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByPaperCutContextEquals(String paperCutContext, String sortFieldName, String sortOrder) {
        if (paperCutContext == null || paperCutContext.length() == 0) throw new IllegalArgumentException("The paperCutContext argument is required");
        EntityManager em = PayboxPapercutTransactionLog.entityManager();
        String jpaQuery = "SELECT o FROM PayboxPapercutTransactionLog AS o WHERE o.paperCutContext = :paperCutContext";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        TypedQuery<PayboxPapercutTransactionLog> q = em.createQuery(jpaQuery, PayboxPapercutTransactionLog.class);
        q.setParameter("paperCutContext", paperCutContext);
        return q;
    }
    
    public static TypedQuery<PayboxPapercutTransactionLog> PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEquals(String uid, String paperCutContext, String sortFieldName, String sortOrder) {
        if (uid == null || uid.length() == 0) throw new IllegalArgumentException("The uid argument is required");
        if (paperCutContext == null || paperCutContext.length() == 0) throw new IllegalArgumentException("The paperCutContext argument is required");
        EntityManager em = PayboxPapercutTransactionLog.entityManager();
        String jpaQuery = "SELECT o FROM PayboxPapercutTransactionLog AS o WHERE o.uid = :uid  AND o.paperCutContext = :paperCutContext";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        TypedQuery<PayboxPapercutTransactionLog> q = em.createQuery(jpaQuery, PayboxPapercutTransactionLog.class);
        q.setParameter("uid", uid);
        q.setParameter("paperCutContext", paperCutContext);
        return q;
    }
    
}
