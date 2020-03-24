/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.IllegalOrphanException;
import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import Mundo.Fase;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Precio;
import java.util.ArrayList;
import java.util.Collection;
import Mundo.Partidos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author mafeg
 */
public class FaseJpaController1 implements Serializable {

    public FaseJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fase fase) throws PreexistingEntityException, Exception {
        if (fase.getPrecioCollection() == null) {
            fase.setPrecioCollection(new ArrayList<Precio>());
        }
        if (fase.getPartidosCollection() == null) {
            fase.setPartidosCollection(new ArrayList<Partidos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Precio> attachedPrecioCollection = new ArrayList<Precio>();
            for (Precio precioCollectionPrecioToAttach : fase.getPrecioCollection()) {
                precioCollectionPrecioToAttach = em.getReference(precioCollectionPrecioToAttach.getClass(), precioCollectionPrecioToAttach.getPrecioPK());
                attachedPrecioCollection.add(precioCollectionPrecioToAttach);
            }
            fase.setPrecioCollection(attachedPrecioCollection);
            List<Partidos> attachedPartidosCollection = new ArrayList<Partidos>();
            for (Partidos partidosCollectionPartidosToAttach : fase.getPartidosCollection()) {
                partidosCollectionPartidosToAttach = em.getReference(partidosCollectionPartidosToAttach.getClass(), partidosCollectionPartidosToAttach.getCodigopartido());
                attachedPartidosCollection.add(partidosCollectionPartidosToAttach);
            }
            fase.setPartidosCollection(attachedPartidosCollection);
            em.persist(fase);
            for (Precio precioCollectionPrecio : fase.getPrecioCollection()) {
                Fase oldFase1OfPrecioCollectionPrecio = precioCollectionPrecio.getFase1();
                precioCollectionPrecio.setFase1(fase);
                precioCollectionPrecio = em.merge(precioCollectionPrecio);
                if (oldFase1OfPrecioCollectionPrecio != null) {
                    oldFase1OfPrecioCollectionPrecio.getPrecioCollection().remove(precioCollectionPrecio);
                    oldFase1OfPrecioCollectionPrecio = em.merge(oldFase1OfPrecioCollectionPrecio);
                }
            }
            for (Partidos partidosCollectionPartidos : fase.getPartidosCollection()) {
                Fase oldFaseOfPartidosCollectionPartidos = partidosCollectionPartidos.getFase();
                partidosCollectionPartidos.setFase(fase);
                partidosCollectionPartidos = em.merge(partidosCollectionPartidos);
                if (oldFaseOfPartidosCollectionPartidos != null) {
                    oldFaseOfPartidosCollectionPartidos.getPartidosCollection().remove(partidosCollectionPartidos);
                    oldFaseOfPartidosCollectionPartidos = em.merge(oldFaseOfPartidosCollectionPartidos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFase(fase.getFase()) != null) {
                throw new PreexistingEntityException("Fase " + fase + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fase fase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fase persistentFase = em.find(Fase.class, fase.getFase());
            Collection<Precio> precioCollectionOld = persistentFase.getPrecioCollection();
            Collection<Precio> precioCollectionNew = fase.getPrecioCollection();
            List<Partidos> partidosCollectionOld = persistentFase.getPartidosCollection();
            List<Partidos> partidosCollectionNew = fase.getPartidosCollection();
            List<String> illegalOrphanMessages = null;
            for (Precio precioCollectionOldPrecio : precioCollectionOld) {
                if (!precioCollectionNew.contains(precioCollectionOldPrecio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Precio " + precioCollectionOldPrecio + " since its fase1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Precio> attachedPrecioCollectionNew = new ArrayList<Precio>();
            for (Precio precioCollectionNewPrecioToAttach : precioCollectionNew) {
                precioCollectionNewPrecioToAttach = em.getReference(precioCollectionNewPrecioToAttach.getClass(), precioCollectionNewPrecioToAttach.getPrecioPK());
                attachedPrecioCollectionNew.add(precioCollectionNewPrecioToAttach);
            }
            precioCollectionNew = attachedPrecioCollectionNew;
            fase.setPrecioCollection(precioCollectionNew);
            List<Partidos> attachedPartidosCollectionNew = new ArrayList<Partidos>();
            for (Partidos partidosCollectionNewPartidosToAttach : partidosCollectionNew) {
                partidosCollectionNewPartidosToAttach = em.getReference(partidosCollectionNewPartidosToAttach.getClass(), partidosCollectionNewPartidosToAttach.getCodigopartido());
                attachedPartidosCollectionNew.add(partidosCollectionNewPartidosToAttach);
            }
            partidosCollectionNew = attachedPartidosCollectionNew;
            fase.setPartidosCollection(partidosCollectionNew);
            fase = em.merge(fase);
            for (Precio precioCollectionNewPrecio : precioCollectionNew) {
                if (!precioCollectionOld.contains(precioCollectionNewPrecio)) {
                    Fase oldFase1OfPrecioCollectionNewPrecio = precioCollectionNewPrecio.getFase1();
                    precioCollectionNewPrecio.setFase1(fase);
                    precioCollectionNewPrecio = em.merge(precioCollectionNewPrecio);
                    if (oldFase1OfPrecioCollectionNewPrecio != null && !oldFase1OfPrecioCollectionNewPrecio.equals(fase)) {
                        oldFase1OfPrecioCollectionNewPrecio.getPrecioCollection().remove(precioCollectionNewPrecio);
                        oldFase1OfPrecioCollectionNewPrecio = em.merge(oldFase1OfPrecioCollectionNewPrecio);
                    }
                }
            }
            for (Partidos partidosCollectionOldPartidos : partidosCollectionOld) {
                if (!partidosCollectionNew.contains(partidosCollectionOldPartidos)) {
                    partidosCollectionOldPartidos.setFase(null);
                    partidosCollectionOldPartidos = em.merge(partidosCollectionOldPartidos);
                }
            }
            for (Partidos partidosCollectionNewPartidos : partidosCollectionNew) {
                if (!partidosCollectionOld.contains(partidosCollectionNewPartidos)) {
                    Fase oldFaseOfPartidosCollectionNewPartidos = partidosCollectionNewPartidos.getFase();
                    partidosCollectionNewPartidos.setFase(fase);
                    partidosCollectionNewPartidos = em.merge(partidosCollectionNewPartidos);
                    if (oldFaseOfPartidosCollectionNewPartidos != null && !oldFaseOfPartidosCollectionNewPartidos.equals(fase)) {
                        oldFaseOfPartidosCollectionNewPartidos.getPartidosCollection().remove(partidosCollectionNewPartidos);
                        oldFaseOfPartidosCollectionNewPartidos = em.merge(oldFaseOfPartidosCollectionNewPartidos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = fase.getFase();
                if (findFase(id) == null) {
                    throw new NonexistentEntityException("The fase with id " + id + " no longer exists.");
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
            Fase fase;
            try {
                fase = em.getReference(Fase.class, id);
                fase.getFase();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Precio> precioCollectionOrphanCheck = fase.getPrecioCollection();
            for (Precio precioCollectionOrphanCheckPrecio : precioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Fase (" + fase + ") cannot be destroyed since the Precio " + precioCollectionOrphanCheckPrecio + " in its precioCollection field has a non-nullable fase1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Partidos> partidosCollection = fase.getPartidosCollection();
            for (Partidos partidosCollectionPartidos : partidosCollection) {
                partidosCollectionPartidos.setFase(null);
                partidosCollectionPartidos = em.merge(partidosCollectionPartidos);
            }
            em.remove(fase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fase> findFaseEntities() {
        return findFaseEntities(true, -1, -1);
    }

    public List<Fase> findFaseEntities(int maxResults, int firstResult) {
        return findFaseEntities(false, maxResults, firstResult);
    }

    private List<Fase> findFaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fase.class));
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

    public Fase findFase(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fase.class, id);
        } finally {
            em.close();
        }
    }

    public int getFaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fase> rt = cq.from(Fase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
