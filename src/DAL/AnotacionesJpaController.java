/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.IllegalOrphanException;
import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import Mundo.Anotaciones;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Anotacionxpartido;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class AnotacionesJpaController implements Serializable {

    public AnotacionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Anotaciones anotaciones) throws PreexistingEntityException, Exception {
        if (anotaciones.getAnotacionxpartidoCollection() == null) {
            anotaciones.setAnotacionxpartidoCollection(new ArrayList<Anotacionxpartido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Anotacionxpartido> attachedAnotacionxpartidoCollection = new ArrayList<Anotacionxpartido>();
            for (Anotacionxpartido anotacionxpartidoCollectionAnotacionxpartidoToAttach : anotaciones.getAnotacionxpartidoCollection()) {
                anotacionxpartidoCollectionAnotacionxpartidoToAttach = em.getReference(anotacionxpartidoCollectionAnotacionxpartidoToAttach.getClass(), anotacionxpartidoCollectionAnotacionxpartidoToAttach.getAnotacionxpartidoPK());
                attachedAnotacionxpartidoCollection.add(anotacionxpartidoCollectionAnotacionxpartidoToAttach);
            }
            anotaciones.setAnotacionxpartidoCollection(attachedAnotacionxpartidoCollection);
            em.persist(anotaciones);
            for (Anotacionxpartido anotacionxpartidoCollectionAnotacionxpartido : anotaciones.getAnotacionxpartidoCollection()) {
                Anotaciones oldAnotacionesOfAnotacionxpartidoCollectionAnotacionxpartido = anotacionxpartidoCollectionAnotacionxpartido.getAnotaciones();
                anotacionxpartidoCollectionAnotacionxpartido.setAnotaciones(anotaciones);
                anotacionxpartidoCollectionAnotacionxpartido = em.merge(anotacionxpartidoCollectionAnotacionxpartido);
                if (oldAnotacionesOfAnotacionxpartidoCollectionAnotacionxpartido != null) {
                    oldAnotacionesOfAnotacionxpartidoCollectionAnotacionxpartido.getAnotacionxpartidoCollection().remove(anotacionxpartidoCollectionAnotacionxpartido);
                    oldAnotacionesOfAnotacionxpartidoCollectionAnotacionxpartido = em.merge(oldAnotacionesOfAnotacionxpartidoCollectionAnotacionxpartido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnotaciones(anotaciones.getTipoan()) != null) {
                throw new PreexistingEntityException("Anotaciones " + anotaciones + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Anotaciones anotaciones) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Anotaciones persistentAnotaciones = em.find(Anotaciones.class, anotaciones.getTipoan());
            List<Anotacionxpartido> anotacionxpartidoCollectionOld = persistentAnotaciones.getAnotacionxpartidoCollection();
            List<Anotacionxpartido> anotacionxpartidoCollectionNew = anotaciones.getAnotacionxpartidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Anotacionxpartido anotacionxpartidoCollectionOldAnotacionxpartido : anotacionxpartidoCollectionOld) {
                if (!anotacionxpartidoCollectionNew.contains(anotacionxpartidoCollectionOldAnotacionxpartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Anotacionxpartido " + anotacionxpartidoCollectionOldAnotacionxpartido + " since its anotaciones field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Anotacionxpartido> attachedAnotacionxpartidoCollectionNew = new ArrayList<Anotacionxpartido>();
            for (Anotacionxpartido anotacionxpartidoCollectionNewAnotacionxpartidoToAttach : anotacionxpartidoCollectionNew) {
                anotacionxpartidoCollectionNewAnotacionxpartidoToAttach = em.getReference(anotacionxpartidoCollectionNewAnotacionxpartidoToAttach.getClass(), anotacionxpartidoCollectionNewAnotacionxpartidoToAttach.getAnotacionxpartidoPK());
                attachedAnotacionxpartidoCollectionNew.add(anotacionxpartidoCollectionNewAnotacionxpartidoToAttach);
            }
            anotacionxpartidoCollectionNew = attachedAnotacionxpartidoCollectionNew;
            anotaciones.setAnotacionxpartidoCollection(anotacionxpartidoCollectionNew);
            anotaciones = em.merge(anotaciones);
            for (Anotacionxpartido anotacionxpartidoCollectionNewAnotacionxpartido : anotacionxpartidoCollectionNew) {
                if (!anotacionxpartidoCollectionOld.contains(anotacionxpartidoCollectionNewAnotacionxpartido)) {
                    Anotaciones oldAnotacionesOfAnotacionxpartidoCollectionNewAnotacionxpartido = anotacionxpartidoCollectionNewAnotacionxpartido.getAnotaciones();
                    anotacionxpartidoCollectionNewAnotacionxpartido.setAnotaciones(anotaciones);
                    anotacionxpartidoCollectionNewAnotacionxpartido = em.merge(anotacionxpartidoCollectionNewAnotacionxpartido);
                    if (oldAnotacionesOfAnotacionxpartidoCollectionNewAnotacionxpartido != null && !oldAnotacionesOfAnotacionxpartidoCollectionNewAnotacionxpartido.equals(anotaciones)) {
                        oldAnotacionesOfAnotacionxpartidoCollectionNewAnotacionxpartido.getAnotacionxpartidoCollection().remove(anotacionxpartidoCollectionNewAnotacionxpartido);
                        oldAnotacionesOfAnotacionxpartidoCollectionNewAnotacionxpartido = em.merge(oldAnotacionesOfAnotacionxpartidoCollectionNewAnotacionxpartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = anotaciones.getTipoan();
                if (findAnotaciones(id) == null) {
                    throw new NonexistentEntityException("The anotaciones with id " + id + " no longer exists.");
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
            Anotaciones anotaciones;
            try {
                anotaciones = em.getReference(Anotaciones.class, id);
                anotaciones.getTipoan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The anotaciones with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Anotacionxpartido> anotacionxpartidoCollectionOrphanCheck = anotaciones.getAnotacionxpartidoCollection();
            for (Anotacionxpartido anotacionxpartidoCollectionOrphanCheckAnotacionxpartido : anotacionxpartidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Anotaciones (" + anotaciones + ") cannot be destroyed since the Anotacionxpartido " + anotacionxpartidoCollectionOrphanCheckAnotacionxpartido + " in its anotacionxpartidoCollection field has a non-nullable anotaciones field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(anotaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Anotaciones> findAnotacionesEntities() {
        return findAnotacionesEntities(true, -1, -1);
    }

    public List<Anotaciones> findAnotacionesEntities(int maxResults, int firstResult) {
        return findAnotacionesEntities(false, maxResults, firstResult);
    }

    private List<Anotaciones> findAnotacionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Anotaciones.class));
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

    public Anotaciones findAnotaciones(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Anotaciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnotacionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Anotaciones> rt = cq.from(Anotaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
