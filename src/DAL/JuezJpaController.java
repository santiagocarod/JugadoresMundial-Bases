/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.IllegalOrphanException;
import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import Mundo.Juez;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Pais;
import Mundo.PartidosJueces;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class JuezJpaController implements Serializable {

    public JuezJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Juez juez) throws PreexistingEntityException, Exception {
        if (juez.getPartidosJuecesCollection() == null) {
            juez.setPartidosJuecesCollection(new ArrayList<PartidosJueces>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais codpais = juez.getCodpais();
            if (codpais != null) {
                codpais = em.getReference(codpais.getClass(), codpais.getCodpais());
                juez.setCodpais(codpais);
            }
            Collection<PartidosJueces> attachedPartidosJuecesCollection = new ArrayList<PartidosJueces>();
            for (PartidosJueces partidosJuecesCollectionPartidosJuecesToAttach : juez.getPartidosJuecesCollection()) {
                partidosJuecesCollectionPartidosJuecesToAttach = em.getReference(partidosJuecesCollectionPartidosJuecesToAttach.getClass(), partidosJuecesCollectionPartidosJuecesToAttach.getPartidosJuecesPK());
                attachedPartidosJuecesCollection.add(partidosJuecesCollectionPartidosJuecesToAttach);
            }
            juez.setPartidosJuecesCollection(attachedPartidosJuecesCollection);
            em.persist(juez);
            if (codpais != null) {
                codpais.getJuezCollection().add(juez);
                codpais = em.merge(codpais);
            }
            for (PartidosJueces partidosJuecesCollectionPartidosJueces : juez.getPartidosJuecesCollection()) {
                Juez oldJuezOfPartidosJuecesCollectionPartidosJueces = partidosJuecesCollectionPartidosJueces.getJuez();
                partidosJuecesCollectionPartidosJueces.setJuez(juez);
                partidosJuecesCollectionPartidosJueces = em.merge(partidosJuecesCollectionPartidosJueces);
                if (oldJuezOfPartidosJuecesCollectionPartidosJueces != null) {
                    oldJuezOfPartidosJuecesCollectionPartidosJueces.getPartidosJuecesCollection().remove(partidosJuecesCollectionPartidosJueces);
                    oldJuezOfPartidosJuecesCollectionPartidosJueces = em.merge(oldJuezOfPartidosJuecesCollectionPartidosJueces);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJuez(juez.getCodj()) != null) {
                throw new PreexistingEntityException("Juez " + juez + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Juez juez) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juez persistentJuez = em.find(Juez.class, juez.getCodj());
            Pais codpaisOld = persistentJuez.getCodpais();
            Pais codpaisNew = juez.getCodpais();
            Collection<PartidosJueces> partidosJuecesCollectionOld = persistentJuez.getPartidosJuecesCollection();
            Collection<PartidosJueces> partidosJuecesCollectionNew = juez.getPartidosJuecesCollection();
            List<String> illegalOrphanMessages = null;
            for (PartidosJueces partidosJuecesCollectionOldPartidosJueces : partidosJuecesCollectionOld) {
                if (!partidosJuecesCollectionNew.contains(partidosJuecesCollectionOldPartidosJueces)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PartidosJueces " + partidosJuecesCollectionOldPartidosJueces + " since its juez field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codpaisNew != null) {
                codpaisNew = em.getReference(codpaisNew.getClass(), codpaisNew.getCodpais());
                juez.setCodpais(codpaisNew);
            }
            Collection<PartidosJueces> attachedPartidosJuecesCollectionNew = new ArrayList<PartidosJueces>();
            for (PartidosJueces partidosJuecesCollectionNewPartidosJuecesToAttach : partidosJuecesCollectionNew) {
                partidosJuecesCollectionNewPartidosJuecesToAttach = em.getReference(partidosJuecesCollectionNewPartidosJuecesToAttach.getClass(), partidosJuecesCollectionNewPartidosJuecesToAttach.getPartidosJuecesPK());
                attachedPartidosJuecesCollectionNew.add(partidosJuecesCollectionNewPartidosJuecesToAttach);
            }
            partidosJuecesCollectionNew = attachedPartidosJuecesCollectionNew;
            juez.setPartidosJuecesCollection(partidosJuecesCollectionNew);
            juez = em.merge(juez);
            if (codpaisOld != null && !codpaisOld.equals(codpaisNew)) {
                codpaisOld.getJuezCollection().remove(juez);
                codpaisOld = em.merge(codpaisOld);
            }
            if (codpaisNew != null && !codpaisNew.equals(codpaisOld)) {
                codpaisNew.getJuezCollection().add(juez);
                codpaisNew = em.merge(codpaisNew);
            }
            for (PartidosJueces partidosJuecesCollectionNewPartidosJueces : partidosJuecesCollectionNew) {
                if (!partidosJuecesCollectionOld.contains(partidosJuecesCollectionNewPartidosJueces)) {
                    Juez oldJuezOfPartidosJuecesCollectionNewPartidosJueces = partidosJuecesCollectionNewPartidosJueces.getJuez();
                    partidosJuecesCollectionNewPartidosJueces.setJuez(juez);
                    partidosJuecesCollectionNewPartidosJueces = em.merge(partidosJuecesCollectionNewPartidosJueces);
                    if (oldJuezOfPartidosJuecesCollectionNewPartidosJueces != null && !oldJuezOfPartidosJuecesCollectionNewPartidosJueces.equals(juez)) {
                        oldJuezOfPartidosJuecesCollectionNewPartidosJueces.getPartidosJuecesCollection().remove(partidosJuecesCollectionNewPartidosJueces);
                        oldJuezOfPartidosJuecesCollectionNewPartidosJueces = em.merge(oldJuezOfPartidosJuecesCollectionNewPartidosJueces);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = juez.getCodj();
                if (findJuez(id) == null) {
                    throw new NonexistentEntityException("The juez with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juez juez;
            try {
                juez = em.getReference(Juez.class, id);
                juez.getCodj();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The juez with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PartidosJueces> partidosJuecesCollectionOrphanCheck = juez.getPartidosJuecesCollection();
            for (PartidosJueces partidosJuecesCollectionOrphanCheckPartidosJueces : partidosJuecesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Juez (" + juez + ") cannot be destroyed since the PartidosJueces " + partidosJuecesCollectionOrphanCheckPartidosJueces + " in its partidosJuecesCollection field has a non-nullable juez field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pais codpais = juez.getCodpais();
            if (codpais != null) {
                codpais.getJuezCollection().remove(juez);
                codpais = em.merge(codpais);
            }
            em.remove(juez);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Juez> findJuezEntities() {
        return findJuezEntities(true, -1, -1);
    }

    public List<Juez> findJuezEntities(int maxResults, int firstResult) {
        return findJuezEntities(false, maxResults, firstResult);
    }

    private List<Juez> findJuezEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Juez.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Juez findJuez(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Juez.class, id);
        } finally {
            em.close();
        }
    }

    public int getJuezCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Juez> rt = cq.from(Juez.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
