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
import Mundo.Categorias;
import Mundo.Estadio;
import java.util.ArrayList;
import java.util.Collection;
import Mundo.Sillas;
import Mundo.Partidos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class EstadioJpaController implements Serializable {

    public EstadioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estadio estadio) throws PreexistingEntityException, Exception {
        if (estadio.getCategoriasCollection() == null) {
            estadio.setCategoriasCollection(new ArrayList<Categorias>());
        }
        if (estadio.getSillasCollection() == null) {
            estadio.setSillasCollection(new ArrayList<Sillas>());
        }
        if (estadio.getPartidosCollection() == null) {
            estadio.setPartidosCollection(new ArrayList<Partidos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Categorias> attachedCategoriasCollection = new ArrayList<Categorias>();
            for (Categorias categoriasCollectionCategoriasToAttach : estadio.getCategoriasCollection()) {
                categoriasCollectionCategoriasToAttach = em.getReference(categoriasCollectionCategoriasToAttach.getClass(), categoriasCollectionCategoriasToAttach.getCodcategoria());
                attachedCategoriasCollection.add(categoriasCollectionCategoriasToAttach);
            }
            estadio.setCategoriasCollection(attachedCategoriasCollection);
            Collection<Sillas> attachedSillasCollection = new ArrayList<Sillas>();
            for (Sillas sillasCollectionSillasToAttach : estadio.getSillasCollection()) {
                sillasCollectionSillasToAttach = em.getReference(sillasCollectionSillasToAttach.getClass(), sillasCollectionSillasToAttach.getSillasPK());
                attachedSillasCollection.add(sillasCollectionSillasToAttach);
            }
            estadio.setSillasCollection(attachedSillasCollection);
            List<Partidos> attachedPartidosCollection = new ArrayList<Partidos>();
            for (Partidos partidosCollectionPartidosToAttach : estadio.getPartidosCollection()) {
                partidosCollectionPartidosToAttach = em.getReference(partidosCollectionPartidosToAttach.getClass(), partidosCollectionPartidosToAttach.getCodigopartido());
                attachedPartidosCollection.add(partidosCollectionPartidosToAttach);
            }
            estadio.setPartidosCollection(attachedPartidosCollection);
            em.persist(estadio);
            for (Categorias categoriasCollectionCategorias : estadio.getCategoriasCollection()) {
                categoriasCollectionCategorias.getEstadioCollection().add(estadio);
                categoriasCollectionCategorias = em.merge(categoriasCollectionCategorias);
            }
            for (Sillas sillasCollectionSillas : estadio.getSillasCollection()) {
                Estadio oldEstadioOfSillasCollectionSillas = sillasCollectionSillas.getEstadio();
                sillasCollectionSillas.setEstadio(estadio);
                sillasCollectionSillas = em.merge(sillasCollectionSillas);
                if (oldEstadioOfSillasCollectionSillas != null) {
                    oldEstadioOfSillasCollectionSillas.getSillasCollection().remove(sillasCollectionSillas);
                    oldEstadioOfSillasCollectionSillas = em.merge(oldEstadioOfSillasCollectionSillas);
                }
            }
            for (Partidos partidosCollectionPartidos : estadio.getPartidosCollection()) {
                Estadio oldCodestadioOfPartidosCollectionPartidos = partidosCollectionPartidos.getCodestadio();
                partidosCollectionPartidos.setCodestadio(estadio);
                partidosCollectionPartidos = em.merge(partidosCollectionPartidos);
                if (oldCodestadioOfPartidosCollectionPartidos != null) {
                    oldCodestadioOfPartidosCollectionPartidos.getPartidosCollection().remove(partidosCollectionPartidos);
                    oldCodestadioOfPartidosCollectionPartidos = em.merge(oldCodestadioOfPartidosCollectionPartidos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadio(estadio.getCodestadio()) != null) {
                throw new PreexistingEntityException("Estadio " + estadio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estadio estadio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estadio persistentEstadio = em.find(Estadio.class, estadio.getCodestadio());
            Collection<Categorias> categoriasCollectionOld = persistentEstadio.getCategoriasCollection();
            List<Categorias> categoriasCollectionNew = estadio.getCategoriasCollection();
            Collection<Sillas> sillasCollectionOld = persistentEstadio.getSillasCollection();
            Collection<Sillas> sillasCollectionNew = estadio.getSillasCollection();
            Collection<Partidos> partidosCollectionOld = persistentEstadio.getPartidosCollection();
            Collection<Partidos> partidosCollectionNew = estadio.getPartidosCollection();
            List<String> illegalOrphanMessages = null;
            for (Sillas sillasCollectionOldSillas : sillasCollectionOld) {
                if (!sillasCollectionNew.contains(sillasCollectionOldSillas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sillas " + sillasCollectionOldSillas + " since its estadio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Categorias> attachedCategoriasCollectionNew = new ArrayList<Categorias>();
            for (Categorias categoriasCollectionNewCategoriasToAttach : categoriasCollectionNew) {
                categoriasCollectionNewCategoriasToAttach = em.getReference(categoriasCollectionNewCategoriasToAttach.getClass(), categoriasCollectionNewCategoriasToAttach.getCodcategoria());
                attachedCategoriasCollectionNew.add(categoriasCollectionNewCategoriasToAttach);
            }
            categoriasCollectionNew = attachedCategoriasCollectionNew;
            estadio.setCategoriasCollection(categoriasCollectionNew);
            Collection<Sillas> attachedSillasCollectionNew = new ArrayList<Sillas>();
            for (Sillas sillasCollectionNewSillasToAttach : sillasCollectionNew) {
                sillasCollectionNewSillasToAttach = em.getReference(sillasCollectionNewSillasToAttach.getClass(), sillasCollectionNewSillasToAttach.getSillasPK());
                attachedSillasCollectionNew.add(sillasCollectionNewSillasToAttach);
            }
            sillasCollectionNew = attachedSillasCollectionNew;
            estadio.setSillasCollection(sillasCollectionNew);
            Collection<Partidos> attachedPartidosCollectionNew = new ArrayList<Partidos>();
            for (Partidos partidosCollectionNewPartidosToAttach : partidosCollectionNew) {
                partidosCollectionNewPartidosToAttach = em.getReference(partidosCollectionNewPartidosToAttach.getClass(), partidosCollectionNewPartidosToAttach.getCodigopartido());
                attachedPartidosCollectionNew.add(partidosCollectionNewPartidosToAttach);
            }
            partidosCollectionNew = attachedPartidosCollectionNew;
            estadio.setPartidosCollection(partidosCollectionNew);
            estadio = em.merge(estadio);
            for (Categorias categoriasCollectionOldCategorias : categoriasCollectionOld) {
                if (!categoriasCollectionNew.contains(categoriasCollectionOldCategorias)) {
                    categoriasCollectionOldCategorias.getEstadioCollection().remove(estadio);
                    categoriasCollectionOldCategorias = em.merge(categoriasCollectionOldCategorias);
                }
            }
            for (Categorias categoriasCollectionNewCategorias : categoriasCollectionNew) {
                if (!categoriasCollectionOld.contains(categoriasCollectionNewCategorias)) {
                    categoriasCollectionNewCategorias.getEstadioCollection().add(estadio);
                    categoriasCollectionNewCategorias = em.merge(categoriasCollectionNewCategorias);
                }
            }
            for (Sillas sillasCollectionNewSillas : sillasCollectionNew) {
                if (!sillasCollectionOld.contains(sillasCollectionNewSillas)) {
                    Estadio oldEstadioOfSillasCollectionNewSillas = sillasCollectionNewSillas.getEstadio();
                    sillasCollectionNewSillas.setEstadio(estadio);
                    sillasCollectionNewSillas = em.merge(sillasCollectionNewSillas);
                    if (oldEstadioOfSillasCollectionNewSillas != null && !oldEstadioOfSillasCollectionNewSillas.equals(estadio)) {
                        oldEstadioOfSillasCollectionNewSillas.getSillasCollection().remove(sillasCollectionNewSillas);
                        oldEstadioOfSillasCollectionNewSillas = em.merge(oldEstadioOfSillasCollectionNewSillas);
                    }
                }
            }
            for (Partidos partidosCollectionOldPartidos : partidosCollectionOld) {
                if (!partidosCollectionNew.contains(partidosCollectionOldPartidos)) {
                    partidosCollectionOldPartidos.setCodestadio(null);
                    partidosCollectionOldPartidos = em.merge(partidosCollectionOldPartidos);
                }
            }
            for (Partidos partidosCollectionNewPartidos : partidosCollectionNew) {
                if (!partidosCollectionOld.contains(partidosCollectionNewPartidos)) {
                    Estadio oldCodestadioOfPartidosCollectionNewPartidos = partidosCollectionNewPartidos.getCodestadio();
                    partidosCollectionNewPartidos.setCodestadio(estadio);
                    partidosCollectionNewPartidos = em.merge(partidosCollectionNewPartidos);
                    if (oldCodestadioOfPartidosCollectionNewPartidos != null && !oldCodestadioOfPartidosCollectionNewPartidos.equals(estadio)) {
                        oldCodestadioOfPartidosCollectionNewPartidos.getPartidosCollection().remove(partidosCollectionNewPartidos);
                        oldCodestadioOfPartidosCollectionNewPartidos = em.merge(oldCodestadioOfPartidosCollectionNewPartidos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = estadio.getCodestadio();
                if (findEstadio(id) == null) {
                    throw new NonexistentEntityException("The estadio with id " + id + " no longer exists.");
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
            Estadio estadio;
            try {
                estadio = em.getReference(Estadio.class, id);
                estadio.getCodestadio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Sillas> sillasCollectionOrphanCheck = estadio.getSillasCollection();
            for (Sillas sillasCollectionOrphanCheckSillas : sillasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estadio (" + estadio + ") cannot be destroyed since the Sillas " + sillasCollectionOrphanCheckSillas + " in its sillasCollection field has a non-nullable estadio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Categorias> categoriasCollection = estadio.getCategoriasCollection();
            for (Categorias categoriasCollectionCategorias : categoriasCollection) {
                categoriasCollectionCategorias.getEstadioCollection().remove(estadio);
                categoriasCollectionCategorias = em.merge(categoriasCollectionCategorias);
            }
            Collection<Partidos> partidosCollection = estadio.getPartidosCollection();
            for (Partidos partidosCollectionPartidos : partidosCollection) {
                partidosCollectionPartidos.setCodestadio(null);
                partidosCollectionPartidos = em.merge(partidosCollectionPartidos);
            }
            em.remove(estadio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estadio> findEstadioEntities() {
        return findEstadioEntities(true, -1, -1);
    }

    public List<Estadio> findEstadioEntities(int maxResults, int firstResult) {
        return findEstadioEntities(false, maxResults, firstResult);
    }

    private List<Estadio> findEstadioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estadio.class));
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

    public Estadio findEstadio(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estadio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estadio> rt = cq.from(Estadio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
