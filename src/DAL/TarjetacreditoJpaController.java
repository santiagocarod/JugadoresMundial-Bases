/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.IllegalOrphanException;
import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Pago;
import Mundo.Tarjetacredito;
import Mundo.TarjetacreditoPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class TarjetacreditoJpaController implements Serializable {

    public TarjetacreditoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjetacredito tarjetacredito) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (tarjetacredito.getTarjetacreditoPK() == null) {
            tarjetacredito.setTarjetacreditoPK(new TarjetacreditoPK());
        }
        tarjetacredito.getTarjetacreditoPK().setTipopago(tarjetacredito.getPago().getPagoPK().getTipopago());
        tarjetacredito.getTarjetacreditoPK().setIdpago(tarjetacredito.getPago().getPagoPK().getIdpago());
        List<String> illegalOrphanMessages = null;
        Pago pagoOrphanCheck = tarjetacredito.getPago();
        if (pagoOrphanCheck != null) {
            Tarjetacredito oldTarjetacreditoOfPago = pagoOrphanCheck.getTarjetacredito();
            if (oldTarjetacreditoOfPago != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pago " + pagoOrphanCheck + " already has an item of type Tarjetacredito whose pago column cannot be null. Please make another selection for the pago field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago = tarjetacredito.getPago();
            if (pago != null) {
                pago = em.getReference(pago.getClass(), pago.getPagoPK());
                tarjetacredito.setPago(pago);
            }
            em.persist(tarjetacredito);
            if (pago != null) {
                pago.setTarjetacredito(tarjetacredito);
                pago = em.merge(pago);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTarjetacredito(tarjetacredito.getTarjetacreditoPK()) != null) {
                throw new PreexistingEntityException("Tarjetacredito " + tarjetacredito + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarjetacredito tarjetacredito) throws IllegalOrphanException, NonexistentEntityException, Exception {
        tarjetacredito.getTarjetacreditoPK().setTipopago(tarjetacredito.getPago().getPagoPK().getTipopago());
        tarjetacredito.getTarjetacreditoPK().setIdpago(tarjetacredito.getPago().getPagoPK().getIdpago());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjetacredito persistentTarjetacredito = em.find(Tarjetacredito.class, tarjetacredito.getTarjetacreditoPK());
            Pago pagoOld = persistentTarjetacredito.getPago();
            Pago pagoNew = tarjetacredito.getPago();
            List<String> illegalOrphanMessages = null;
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                Tarjetacredito oldTarjetacreditoOfPago = pagoNew.getTarjetacredito();
                if (oldTarjetacreditoOfPago != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pago " + pagoNew + " already has an item of type Tarjetacredito whose pago column cannot be null. Please make another selection for the pago field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pagoNew != null) {
                pagoNew = em.getReference(pagoNew.getClass(), pagoNew.getPagoPK());
                tarjetacredito.setPago(pagoNew);
            }
            tarjetacredito = em.merge(tarjetacredito);
            if (pagoOld != null && !pagoOld.equals(pagoNew)) {
                pagoOld.setTarjetacredito(null);
                pagoOld = em.merge(pagoOld);
            }
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                pagoNew.setTarjetacredito(tarjetacredito);
                pagoNew = em.merge(pagoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TarjetacreditoPK id = tarjetacredito.getTarjetacreditoPK();
                if (findTarjetacredito(id) == null) {
                    throw new NonexistentEntityException("The tarjetacredito with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TarjetacreditoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjetacredito tarjetacredito;
            try {
                tarjetacredito = em.getReference(Tarjetacredito.class, id);
                tarjetacredito.getTarjetacreditoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjetacredito with id " + id + " no longer exists.", enfe);
            }
            Pago pago = tarjetacredito.getPago();
            if (pago != null) {
                pago.setTarjetacredito(null);
                pago = em.merge(pago);
            }
            em.remove(tarjetacredito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarjetacredito> findTarjetacreditoEntities() {
        return findTarjetacreditoEntities(true, -1, -1);
    }

    public List<Tarjetacredito> findTarjetacreditoEntities(int maxResults, int firstResult) {
        return findTarjetacreditoEntities(false, maxResults, firstResult);
    }

    private List<Tarjetacredito> findTarjetacreditoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjetacredito.class));
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

    public Tarjetacredito findTarjetacredito(TarjetacreditoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjetacredito.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetacreditoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjetacredito> rt = cq.from(Tarjetacredito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
