/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.IllegalOrphanException;
import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import Mundo.Efectivo;
import Mundo.EfectivoPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class EfectivoJpaController implements Serializable {

    public EfectivoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Efectivo efectivo) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (efectivo.getEfectivoPK() == null) {
            efectivo.setEfectivoPK(new EfectivoPK());
        }
        efectivo.getEfectivoPK().setTipopago(efectivo.getPago().getPagoPK().getTipopago());
        efectivo.getEfectivoPK().setIdpago(efectivo.getPago().getPagoPK().getIdpago());
        List<String> illegalOrphanMessages = null;
        Pago pagoOrphanCheck = efectivo.getPago();
        if (pagoOrphanCheck != null) {
            Efectivo oldEfectivoOfPago = pagoOrphanCheck.getEfectivo();
            if (oldEfectivoOfPago != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pago " + pagoOrphanCheck + " already has an item of type Efectivo whose pago column cannot be null. Please make another selection for the pago field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago = efectivo.getPago();
            if (pago != null) {
                pago = em.getReference(pago.getClass(), pago.getPagoPK());
                efectivo.setPago(pago);
            }
            em.persist(efectivo);
            if (pago != null) {
                pago.setEfectivo(efectivo);
                pago = em.merge(pago);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEfectivo(efectivo.getEfectivoPK()) != null) {
                throw new PreexistingEntityException("Efectivo " + efectivo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Efectivo efectivo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        efectivo.getEfectivoPK().setTipopago(efectivo.getPago().getPagoPK().getTipopago());
        efectivo.getEfectivoPK().setIdpago(efectivo.getPago().getPagoPK().getIdpago());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Efectivo persistentEfectivo = em.find(Efectivo.class, efectivo.getEfectivoPK());
            Pago pagoOld = persistentEfectivo.getPago();
            Pago pagoNew = efectivo.getPago();
            List<String> illegalOrphanMessages = null;
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                Efectivo oldEfectivoOfPago = pagoNew.getEfectivo();
                if (oldEfectivoOfPago != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pago " + pagoNew + " already has an item of type Efectivo whose pago column cannot be null. Please make another selection for the pago field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pagoNew != null) {
                pagoNew = em.getReference(pagoNew.getClass(), pagoNew.getPagoPK());
                efectivo.setPago(pagoNew);
            }
            efectivo = em.merge(efectivo);
            if (pagoOld != null && !pagoOld.equals(pagoNew)) {
                pagoOld.setEfectivo(null);
                pagoOld = em.merge(pagoOld);
            }
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                pagoNew.setEfectivo(efectivo);
                pagoNew = em.merge(pagoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                EfectivoPK id = efectivo.getEfectivoPK();
                if (findEfectivo(id) == null) {
                    throw new NonexistentEntityException("The efectivo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EfectivoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Efectivo efectivo;
            try {
                efectivo = em.getReference(Efectivo.class, id);
                efectivo.getEfectivoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The efectivo with id " + id + " no longer exists.", enfe);
            }
            Pago pago = efectivo.getPago();
            if (pago != null) {
                pago.setEfectivo(null);
                pago = em.merge(pago);
            }
            em.remove(efectivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Efectivo> findEfectivoEntities() {
        return findEfectivoEntities(true, -1, -1);
    }

    public List<Efectivo> findEfectivoEntities(int maxResults, int firstResult) {
        return findEfectivoEntities(false, maxResults, firstResult);
    }

    private List<Efectivo> findEfectivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Efectivo.class));
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

    public Efectivo findEfectivo(EfectivoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Efectivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEfectivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Efectivo> rt = cq.from(Efectivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
