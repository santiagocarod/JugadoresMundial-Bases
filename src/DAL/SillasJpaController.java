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
import Mundo.Boleteria;
import Mundo.Sillas;
import Mundo.SillasPK;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class SillasJpaController implements Serializable {

    public SillasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sillas sillas) throws PreexistingEntityException, Exception {
        if (sillas.getSillasPK() == null) {
            sillas.setSillasPK(new SillasPK());
        }
        if (sillas.getBoleteriaCollection() == null) {
            sillas.setBoleteriaCollection(new ArrayList<Boleteria>());
        }
        sillas.getSillasPK().setCodestadio(sillas.getEstadio().getCodestadio());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorias codcategoria = sillas.getCodcategoria();
            if (codcategoria != null) {
                codcategoria = em.getReference(codcategoria.getClass(), codcategoria.getCodcategoria());
                sillas.setCodcategoria(codcategoria);
            }
            Estadio estadio = sillas.getEstadio();
            if (estadio != null) {
                estadio = em.getReference(estadio.getClass(), estadio.getCodestadio());
                sillas.setEstadio(estadio);
            }
            Collection<Boleteria> attachedBoleteriaCollection = new ArrayList<Boleteria>();
            for (Boleteria boleteriaCollectionBoleteriaToAttach : sillas.getBoleteriaCollection()) {
                boleteriaCollectionBoleteriaToAttach = em.getReference(boleteriaCollectionBoleteriaToAttach.getClass(), boleteriaCollectionBoleteriaToAttach.getCodigoboleta());
                attachedBoleteriaCollection.add(boleteriaCollectionBoleteriaToAttach);
            }
            sillas.setBoleteriaCollection(attachedBoleteriaCollection);
            em.persist(sillas);
            if (codcategoria != null) {
                codcategoria.getSillasCollection().add(sillas);
                codcategoria = em.merge(codcategoria);
            }
            if (estadio != null) {
                estadio.getSillasCollection().add(sillas);
                estadio = em.merge(estadio);
            }
            for (Boleteria boleteriaCollectionBoleteria : sillas.getBoleteriaCollection()) {
                Sillas oldSillasOfBoleteriaCollectionBoleteria = boleteriaCollectionBoleteria.getSillas();
                boleteriaCollectionBoleteria.setSillas(sillas);
                boleteriaCollectionBoleteria = em.merge(boleteriaCollectionBoleteria);
                if (oldSillasOfBoleteriaCollectionBoleteria != null) {
                    oldSillasOfBoleteriaCollectionBoleteria.getBoleteriaCollection().remove(boleteriaCollectionBoleteria);
                    oldSillasOfBoleteriaCollectionBoleteria = em.merge(oldSillasOfBoleteriaCollectionBoleteria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSillas(sillas.getSillasPK()) != null) {
                throw new PreexistingEntityException("Sillas " + sillas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sillas sillas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        sillas.getSillasPK().setCodestadio(sillas.getEstadio().getCodestadio());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sillas persistentSillas = em.find(Sillas.class, sillas.getSillasPK());
            Categorias codcategoriaOld = persistentSillas.getCodcategoria();
            Categorias codcategoriaNew = sillas.getCodcategoria();
            Estadio estadioOld = persistentSillas.getEstadio();
            Estadio estadioNew = sillas.getEstadio();
            Collection<Boleteria> boleteriaCollectionOld = persistentSillas.getBoleteriaCollection();
            Collection<Boleteria> boleteriaCollectionNew = sillas.getBoleteriaCollection();
            List<String> illegalOrphanMessages = null;
            for (Boleteria boleteriaCollectionOldBoleteria : boleteriaCollectionOld) {
                if (!boleteriaCollectionNew.contains(boleteriaCollectionOldBoleteria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boleteria " + boleteriaCollectionOldBoleteria + " since its sillas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codcategoriaNew != null) {
                codcategoriaNew = em.getReference(codcategoriaNew.getClass(), codcategoriaNew.getCodcategoria());
                sillas.setCodcategoria(codcategoriaNew);
            }
            if (estadioNew != null) {
                estadioNew = em.getReference(estadioNew.getClass(), estadioNew.getCodestadio());
                sillas.setEstadio(estadioNew);
            }
            Collection<Boleteria> attachedBoleteriaCollectionNew = new ArrayList<Boleteria>();
            for (Boleteria boleteriaCollectionNewBoleteriaToAttach : boleteriaCollectionNew) {
                boleteriaCollectionNewBoleteriaToAttach = em.getReference(boleteriaCollectionNewBoleteriaToAttach.getClass(), boleteriaCollectionNewBoleteriaToAttach.getCodigoboleta());
                attachedBoleteriaCollectionNew.add(boleteriaCollectionNewBoleteriaToAttach);
            }
            boleteriaCollectionNew = attachedBoleteriaCollectionNew;
            sillas.setBoleteriaCollection(boleteriaCollectionNew);
            sillas = em.merge(sillas);
            if (codcategoriaOld != null && !codcategoriaOld.equals(codcategoriaNew)) {
                codcategoriaOld.getSillasCollection().remove(sillas);
                codcategoriaOld = em.merge(codcategoriaOld);
            }
            if (codcategoriaNew != null && !codcategoriaNew.equals(codcategoriaOld)) {
                codcategoriaNew.getSillasCollection().add(sillas);
                codcategoriaNew = em.merge(codcategoriaNew);
            }
            if (estadioOld != null && !estadioOld.equals(estadioNew)) {
                estadioOld.getSillasCollection().remove(sillas);
                estadioOld = em.merge(estadioOld);
            }
            if (estadioNew != null && !estadioNew.equals(estadioOld)) {
                estadioNew.getSillasCollection().add(sillas);
                estadioNew = em.merge(estadioNew);
            }
            for (Boleteria boleteriaCollectionNewBoleteria : boleteriaCollectionNew) {
                if (!boleteriaCollectionOld.contains(boleteriaCollectionNewBoleteria)) {
                    Sillas oldSillasOfBoleteriaCollectionNewBoleteria = boleteriaCollectionNewBoleteria.getSillas();
                    boleteriaCollectionNewBoleteria.setSillas(sillas);
                    boleteriaCollectionNewBoleteria = em.merge(boleteriaCollectionNewBoleteria);
                    if (oldSillasOfBoleteriaCollectionNewBoleteria != null && !oldSillasOfBoleteriaCollectionNewBoleteria.equals(sillas)) {
                        oldSillasOfBoleteriaCollectionNewBoleteria.getBoleteriaCollection().remove(boleteriaCollectionNewBoleteria);
                        oldSillasOfBoleteriaCollectionNewBoleteria = em.merge(oldSillasOfBoleteriaCollectionNewBoleteria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SillasPK id = sillas.getSillasPK();
                if (findSillas(id) == null) {
                    throw new NonexistentEntityException("The sillas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SillasPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sillas sillas;
            try {
                sillas = em.getReference(Sillas.class, id);
                sillas.getSillasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sillas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Boleteria> boleteriaCollectionOrphanCheck = sillas.getBoleteriaCollection();
            for (Boleteria boleteriaCollectionOrphanCheckBoleteria : boleteriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sillas (" + sillas + ") cannot be destroyed since the Boleteria " + boleteriaCollectionOrphanCheckBoleteria + " in its boleteriaCollection field has a non-nullable sillas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categorias codcategoria = sillas.getCodcategoria();
            if (codcategoria != null) {
                codcategoria.getSillasCollection().remove(sillas);
                codcategoria = em.merge(codcategoria);
            }
            Estadio estadio = sillas.getEstadio();
            if (estadio != null) {
                estadio.getSillasCollection().remove(sillas);
                estadio = em.merge(estadio);
            }
            em.remove(sillas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sillas> findSillasEntities() {
        return findSillasEntities(true, -1, -1);
    }

    public List<Sillas> findSillasEntities(int maxResults, int firstResult) {
        return findSillasEntities(false, maxResults, firstResult);
    }

    private List<Sillas> findSillasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sillas.class));
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

    public Sillas findSillas(SillasPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sillas.class, id);
        } finally {
            em.close();
        }
    }

    public int getSillasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sillas> rt = cq.from(Sillas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
