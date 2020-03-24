/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.IllegalOrphanException;
import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import Mundo.Tarjeta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Tarjetaspartido;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class TarjetaJpaController implements Serializable {

    public TarjetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjeta tarjeta) throws PreexistingEntityException, Exception {
        if (tarjeta.getTarjetaspartidoCollection() == null) {
            tarjeta.setTarjetaspartidoCollection(new ArrayList<Tarjetaspartido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Tarjetaspartido> attachedTarjetaspartidoCollection = new ArrayList<Tarjetaspartido>();
            for (Tarjetaspartido tarjetaspartidoCollectionTarjetaspartidoToAttach : tarjeta.getTarjetaspartidoCollection()) {
                tarjetaspartidoCollectionTarjetaspartidoToAttach = em.getReference(tarjetaspartidoCollectionTarjetaspartidoToAttach.getClass(), tarjetaspartidoCollectionTarjetaspartidoToAttach.getTarjetaspartidoPK());
                attachedTarjetaspartidoCollection.add(tarjetaspartidoCollectionTarjetaspartidoToAttach);
            }
            tarjeta.setTarjetaspartidoCollection(attachedTarjetaspartidoCollection);
            em.persist(tarjeta);
            for (Tarjetaspartido tarjetaspartidoCollectionTarjetaspartido : tarjeta.getTarjetaspartidoCollection()) {
                Tarjeta oldTarjetaOfTarjetaspartidoCollectionTarjetaspartido = tarjetaspartidoCollectionTarjetaspartido.getTarjeta();
                tarjetaspartidoCollectionTarjetaspartido.setTarjeta(tarjeta);
                tarjetaspartidoCollectionTarjetaspartido = em.merge(tarjetaspartidoCollectionTarjetaspartido);
                if (oldTarjetaOfTarjetaspartidoCollectionTarjetaspartido != null) {
                    oldTarjetaOfTarjetaspartidoCollectionTarjetaspartido.getTarjetaspartidoCollection().remove(tarjetaspartidoCollectionTarjetaspartido);
                    oldTarjetaOfTarjetaspartidoCollectionTarjetaspartido = em.merge(oldTarjetaOfTarjetaspartidoCollectionTarjetaspartido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTarjeta(tarjeta.getTipot()) != null) {
                throw new PreexistingEntityException("Tarjeta " + tarjeta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarjeta tarjeta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjeta persistentTarjeta = em.find(Tarjeta.class, tarjeta.getTipot());
            Collection<Tarjetaspartido> tarjetaspartidoCollectionOld = persistentTarjeta.getTarjetaspartidoCollection();
            Collection<Tarjetaspartido> tarjetaspartidoCollectionNew = tarjeta.getTarjetaspartidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Tarjetaspartido tarjetaspartidoCollectionOldTarjetaspartido : tarjetaspartidoCollectionOld) {
                if (!tarjetaspartidoCollectionNew.contains(tarjetaspartidoCollectionOldTarjetaspartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjetaspartido " + tarjetaspartidoCollectionOldTarjetaspartido + " since its tarjeta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Tarjetaspartido> attachedTarjetaspartidoCollectionNew = new ArrayList<Tarjetaspartido>();
            for (Tarjetaspartido tarjetaspartidoCollectionNewTarjetaspartidoToAttach : tarjetaspartidoCollectionNew) {
                tarjetaspartidoCollectionNewTarjetaspartidoToAttach = em.getReference(tarjetaspartidoCollectionNewTarjetaspartidoToAttach.getClass(), tarjetaspartidoCollectionNewTarjetaspartidoToAttach.getTarjetaspartidoPK());
                attachedTarjetaspartidoCollectionNew.add(tarjetaspartidoCollectionNewTarjetaspartidoToAttach);
            }
            tarjetaspartidoCollectionNew = attachedTarjetaspartidoCollectionNew;
            tarjeta.setTarjetaspartidoCollection(tarjetaspartidoCollectionNew);
            tarjeta = em.merge(tarjeta);
            for (Tarjetaspartido tarjetaspartidoCollectionNewTarjetaspartido : tarjetaspartidoCollectionNew) {
                if (!tarjetaspartidoCollectionOld.contains(tarjetaspartidoCollectionNewTarjetaspartido)) {
                    Tarjeta oldTarjetaOfTarjetaspartidoCollectionNewTarjetaspartido = tarjetaspartidoCollectionNewTarjetaspartido.getTarjeta();
                    tarjetaspartidoCollectionNewTarjetaspartido.setTarjeta(tarjeta);
                    tarjetaspartidoCollectionNewTarjetaspartido = em.merge(tarjetaspartidoCollectionNewTarjetaspartido);
                    if (oldTarjetaOfTarjetaspartidoCollectionNewTarjetaspartido != null && !oldTarjetaOfTarjetaspartidoCollectionNewTarjetaspartido.equals(tarjeta)) {
                        oldTarjetaOfTarjetaspartidoCollectionNewTarjetaspartido.getTarjetaspartidoCollection().remove(tarjetaspartidoCollectionNewTarjetaspartido);
                        oldTarjetaOfTarjetaspartidoCollectionNewTarjetaspartido = em.merge(oldTarjetaOfTarjetaspartidoCollectionNewTarjetaspartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tarjeta.getTipot();
                if (findTarjeta(id) == null) {
                    throw new NonexistentEntityException("The tarjeta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjeta tarjeta;
            try {
                tarjeta = em.getReference(Tarjeta.class, id);
                tarjeta.getTipot();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjeta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Tarjetaspartido> tarjetaspartidoCollectionOrphanCheck = tarjeta.getTarjetaspartidoCollection();
            for (Tarjetaspartido tarjetaspartidoCollectionOrphanCheckTarjetaspartido : tarjetaspartidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarjeta (" + tarjeta + ") cannot be destroyed since the Tarjetaspartido " + tarjetaspartidoCollectionOrphanCheckTarjetaspartido + " in its tarjetaspartidoCollection field has a non-nullable tarjeta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarjeta> findTarjetaEntities() {
        return findTarjetaEntities(true, -1, -1);
    }

    public List<Tarjeta> findTarjetaEntities(int maxResults, int firstResult) {
        return findTarjetaEntities(false, maxResults, firstResult);
    }

    private List<Tarjeta> findTarjetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjeta.class));
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

    public Tarjeta findTarjeta(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjeta> rt = cq.from(Tarjeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
