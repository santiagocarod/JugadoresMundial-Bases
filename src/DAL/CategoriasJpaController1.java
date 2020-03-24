/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.IllegalOrphanException;
import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import Mundo.Categorias;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Estadio;
import java.util.ArrayList;
import java.util.Collection;
import Mundo.Precio;
import Mundo.Sillas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author mafeg
 */
public class CategoriasJpaController1 implements Serializable {

    public CategoriasJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categorias categorias) throws PreexistingEntityException, Exception {
        if (categorias.getEstadioCollection() == null) {
            categorias.setEstadioCollection(new ArrayList<Estadio>());
        }
        if (categorias.getPrecioCollection() == null) {
            categorias.setPrecioCollection(new ArrayList<Precio>());
        }
        if (categorias.getSillasCollection() == null) {
            categorias.setSillasCollection(new ArrayList<Sillas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Estadio> attachedEstadioCollection = new ArrayList<Estadio>();
            for (Estadio estadioCollectionEstadioToAttach : categorias.getEstadioCollection()) {
                estadioCollectionEstadioToAttach = em.getReference(estadioCollectionEstadioToAttach.getClass(), estadioCollectionEstadioToAttach.getCodestadio());
                attachedEstadioCollection.add(estadioCollectionEstadioToAttach);
            }
            categorias.setEstadioCollection(attachedEstadioCollection);
            Collection<Precio> attachedPrecioCollection = new ArrayList<Precio>();
            for (Precio precioCollectionPrecioToAttach : categorias.getPrecioCollection()) {
                precioCollectionPrecioToAttach = em.getReference(precioCollectionPrecioToAttach.getClass(), precioCollectionPrecioToAttach.getPrecioPK());
                attachedPrecioCollection.add(precioCollectionPrecioToAttach);
            }
            categorias.setPrecioCollection(attachedPrecioCollection);
            Collection<Sillas> attachedSillasCollection = new ArrayList<Sillas>();
            for (Sillas sillasCollectionSillasToAttach : categorias.getSillasCollection()) {
                sillasCollectionSillasToAttach = em.getReference(sillasCollectionSillasToAttach.getClass(), sillasCollectionSillasToAttach.getSillasPK());
                attachedSillasCollection.add(sillasCollectionSillasToAttach);
            }
            categorias.setSillasCollection(attachedSillasCollection);
            em.persist(categorias);
            for (Estadio estadioCollectionEstadio : categorias.getEstadioCollection()) {
                estadioCollectionEstadio.getCategoriasCollection().add(categorias);
                estadioCollectionEstadio = em.merge(estadioCollectionEstadio);
            }
            for (Precio precioCollectionPrecio : categorias.getPrecioCollection()) {
                Categorias oldCategoriasOfPrecioCollectionPrecio = precioCollectionPrecio.getCategorias();
                precioCollectionPrecio.setCategorias(categorias);
                precioCollectionPrecio = em.merge(precioCollectionPrecio);
                if (oldCategoriasOfPrecioCollectionPrecio != null) {
                    oldCategoriasOfPrecioCollectionPrecio.getPrecioCollection().remove(precioCollectionPrecio);
                    oldCategoriasOfPrecioCollectionPrecio = em.merge(oldCategoriasOfPrecioCollectionPrecio);
                }
            }
            for (Sillas sillasCollectionSillas : categorias.getSillasCollection()) {
                Categorias oldCodcategoriaOfSillasCollectionSillas = sillasCollectionSillas.getCodcategoria();
                sillasCollectionSillas.setCodcategoria(categorias);
                sillasCollectionSillas = em.merge(sillasCollectionSillas);
                if (oldCodcategoriaOfSillasCollectionSillas != null) {
                    oldCodcategoriaOfSillasCollectionSillas.getSillasCollection().remove(sillasCollectionSillas);
                    oldCodcategoriaOfSillasCollectionSillas = em.merge(oldCodcategoriaOfSillasCollectionSillas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategorias(categorias.getCodcategoria()) != null) {
                throw new PreexistingEntityException("Categorias " + categorias + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categorias categorias) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorias persistentCategorias = em.find(Categorias.class, categorias.getCodcategoria());
            Collection<Estadio> estadioCollectionOld = persistentCategorias.getEstadioCollection();
            Collection<Estadio> estadioCollectionNew = categorias.getEstadioCollection();
            Collection<Precio> precioCollectionOld = persistentCategorias.getPrecioCollection();
            Collection<Precio> precioCollectionNew = categorias.getPrecioCollection();
            Collection<Sillas> sillasCollectionOld = persistentCategorias.getSillasCollection();
            Collection<Sillas> sillasCollectionNew = categorias.getSillasCollection();
            List<String> illegalOrphanMessages = null;
            for (Precio precioCollectionOldPrecio : precioCollectionOld) {
                if (!precioCollectionNew.contains(precioCollectionOldPrecio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Precio " + precioCollectionOldPrecio + " since its categorias field is not nullable.");
                }
            }
            for (Sillas sillasCollectionOldSillas : sillasCollectionOld) {
                if (!sillasCollectionNew.contains(sillasCollectionOldSillas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sillas " + sillasCollectionOldSillas + " since its codcategoria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Estadio> attachedEstadioCollectionNew = new ArrayList<Estadio>();
            for (Estadio estadioCollectionNewEstadioToAttach : estadioCollectionNew) {
                estadioCollectionNewEstadioToAttach = em.getReference(estadioCollectionNewEstadioToAttach.getClass(), estadioCollectionNewEstadioToAttach.getCodestadio());
                attachedEstadioCollectionNew.add(estadioCollectionNewEstadioToAttach);
            }
            estadioCollectionNew = attachedEstadioCollectionNew;
            categorias.setEstadioCollection(estadioCollectionNew);
            Collection<Precio> attachedPrecioCollectionNew = new ArrayList<Precio>();
            for (Precio precioCollectionNewPrecioToAttach : precioCollectionNew) {
                precioCollectionNewPrecioToAttach = em.getReference(precioCollectionNewPrecioToAttach.getClass(), precioCollectionNewPrecioToAttach.getPrecioPK());
                attachedPrecioCollectionNew.add(precioCollectionNewPrecioToAttach);
            }
            precioCollectionNew = attachedPrecioCollectionNew;
            categorias.setPrecioCollection(precioCollectionNew);
            Collection<Sillas> attachedSillasCollectionNew = new ArrayList<Sillas>();
            for (Sillas sillasCollectionNewSillasToAttach : sillasCollectionNew) {
                sillasCollectionNewSillasToAttach = em.getReference(sillasCollectionNewSillasToAttach.getClass(), sillasCollectionNewSillasToAttach.getSillasPK());
                attachedSillasCollectionNew.add(sillasCollectionNewSillasToAttach);
            }
            sillasCollectionNew = attachedSillasCollectionNew;
            categorias.setSillasCollection(sillasCollectionNew);
            categorias = em.merge(categorias);
            for (Estadio estadioCollectionOldEstadio : estadioCollectionOld) {
                if (!estadioCollectionNew.contains(estadioCollectionOldEstadio)) {
                    estadioCollectionOldEstadio.getCategoriasCollection().remove(categorias);
                    estadioCollectionOldEstadio = em.merge(estadioCollectionOldEstadio);
                }
            }
            for (Estadio estadioCollectionNewEstadio : estadioCollectionNew) {
                if (!estadioCollectionOld.contains(estadioCollectionNewEstadio)) {
                    estadioCollectionNewEstadio.getCategoriasCollection().add(categorias);
                    estadioCollectionNewEstadio = em.merge(estadioCollectionNewEstadio);
                }
            }
            for (Precio precioCollectionNewPrecio : precioCollectionNew) {
                if (!precioCollectionOld.contains(precioCollectionNewPrecio)) {
                    Categorias oldCategoriasOfPrecioCollectionNewPrecio = precioCollectionNewPrecio.getCategorias();
                    precioCollectionNewPrecio.setCategorias(categorias);
                    precioCollectionNewPrecio = em.merge(precioCollectionNewPrecio);
                    if (oldCategoriasOfPrecioCollectionNewPrecio != null && !oldCategoriasOfPrecioCollectionNewPrecio.equals(categorias)) {
                        oldCategoriasOfPrecioCollectionNewPrecio.getPrecioCollection().remove(precioCollectionNewPrecio);
                        oldCategoriasOfPrecioCollectionNewPrecio = em.merge(oldCategoriasOfPrecioCollectionNewPrecio);
                    }
                }
            }
            for (Sillas sillasCollectionNewSillas : sillasCollectionNew) {
                if (!sillasCollectionOld.contains(sillasCollectionNewSillas)) {
                    Categorias oldCodcategoriaOfSillasCollectionNewSillas = sillasCollectionNewSillas.getCodcategoria();
                    sillasCollectionNewSillas.setCodcategoria(categorias);
                    sillasCollectionNewSillas = em.merge(sillasCollectionNewSillas);
                    if (oldCodcategoriaOfSillasCollectionNewSillas != null && !oldCodcategoriaOfSillasCollectionNewSillas.equals(categorias)) {
                        oldCodcategoriaOfSillasCollectionNewSillas.getSillasCollection().remove(sillasCollectionNewSillas);
                        oldCodcategoriaOfSillasCollectionNewSillas = em.merge(oldCodcategoriaOfSillasCollectionNewSillas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = categorias.getCodcategoria();
                if (findCategorias(id) == null) {
                    throw new NonexistentEntityException("The categorias with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigInteger id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorias categorias;
            try {
                categorias = em.getReference(Categorias.class, id);
                categorias.getCodcategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categorias with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Precio> precioCollectionOrphanCheck = categorias.getPrecioCollection();
            for (Precio precioCollectionOrphanCheckPrecio : precioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categorias (" + categorias + ") cannot be destroyed since the Precio " + precioCollectionOrphanCheckPrecio + " in its precioCollection field has a non-nullable categorias field.");
            }
            Collection<Sillas> sillasCollectionOrphanCheck = categorias.getSillasCollection();
            for (Sillas sillasCollectionOrphanCheckSillas : sillasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categorias (" + categorias + ") cannot be destroyed since the Sillas " + sillasCollectionOrphanCheckSillas + " in its sillasCollection field has a non-nullable codcategoria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Estadio> estadioCollection = categorias.getEstadioCollection();
            for (Estadio estadioCollectionEstadio : estadioCollection) {
                estadioCollectionEstadio.getCategoriasCollection().remove(categorias);
                estadioCollectionEstadio = em.merge(estadioCollectionEstadio);
            }
            em.remove(categorias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categorias> findCategoriasEntities() {
        return findCategoriasEntities(true, -1, -1);
    }

    public List<Categorias> findCategoriasEntities(int maxResults, int firstResult) {
        return findCategoriasEntities(false, maxResults, firstResult);
    }

    private List<Categorias> findCategoriasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categorias.class));
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

    public Categorias findCategorias(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categorias.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categorias> rt = cq.from(Categorias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
