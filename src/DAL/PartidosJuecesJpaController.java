/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Juez;
import Mundo.Partidos;
import Mundo.PartidosJueces;
import Mundo.PartidosJuecesPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class PartidosJuecesJpaController implements Serializable {

    public PartidosJuecesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PartidosJueces partidosJueces) throws PreexistingEntityException, Exception {
        if (partidosJueces.getPartidosJuecesPK() == null) {
            partidosJueces.setPartidosJuecesPK(new PartidosJuecesPK());
        }
        partidosJueces.getPartidosJuecesPK().setCodj(partidosJueces.getJuez().getCodj());
        partidosJueces.getPartidosJuecesPK().setCodigopartido(partidosJueces.getPartidos().getCodigopartido());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juez juez = partidosJueces.getJuez();
            if (juez != null) {
                juez = em.getReference(juez.getClass(), juez.getCodj());
                partidosJueces.setJuez(juez);
            }
            Partidos partidos = partidosJueces.getPartidos();
            if (partidos != null) {
                partidos = em.getReference(partidos.getClass(), partidos.getCodigopartido());
                partidosJueces.setPartidos(partidos);
            }
            em.persist(partidosJueces);
            if (juez != null) {
                juez.getPartidosJuecesCollection().add(partidosJueces);
                juez = em.merge(juez);
            }
            if (partidos != null) {
                partidos.getPartidosJuecesCollection().add(partidosJueces);
                partidos = em.merge(partidos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPartidosJueces(partidosJueces.getPartidosJuecesPK()) != null) {
                throw new PreexistingEntityException("PartidosJueces " + partidosJueces + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PartidosJueces partidosJueces) throws NonexistentEntityException, Exception {
        partidosJueces.getPartidosJuecesPK().setCodj(partidosJueces.getJuez().getCodj());
        partidosJueces.getPartidosJuecesPK().setCodigopartido(partidosJueces.getPartidos().getCodigopartido());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PartidosJueces persistentPartidosJueces = em.find(PartidosJueces.class, partidosJueces.getPartidosJuecesPK());
            Juez juezOld = persistentPartidosJueces.getJuez();
            Juez juezNew = partidosJueces.getJuez();
            Partidos partidosOld = persistentPartidosJueces.getPartidos();
            Partidos partidosNew = partidosJueces.getPartidos();
            if (juezNew != null) {
                juezNew = em.getReference(juezNew.getClass(), juezNew.getCodj());
                partidosJueces.setJuez(juezNew);
            }
            if (partidosNew != null) {
                partidosNew = em.getReference(partidosNew.getClass(), partidosNew.getCodigopartido());
                partidosJueces.setPartidos(partidosNew);
            }
            partidosJueces = em.merge(partidosJueces);
            if (juezOld != null && !juezOld.equals(juezNew)) {
                juezOld.getPartidosJuecesCollection().remove(partidosJueces);
                juezOld = em.merge(juezOld);
            }
            if (juezNew != null && !juezNew.equals(juezOld)) {
                juezNew.getPartidosJuecesCollection().add(partidosJueces);
                juezNew = em.merge(juezNew);
            }
            if (partidosOld != null && !partidosOld.equals(partidosNew)) {
                partidosOld.getPartidosJuecesCollection().remove(partidosJueces);
                partidosOld = em.merge(partidosOld);
            }
            if (partidosNew != null && !partidosNew.equals(partidosOld)) {
                partidosNew.getPartidosJuecesCollection().add(partidosJueces);
                partidosNew = em.merge(partidosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PartidosJuecesPK id = partidosJueces.getPartidosJuecesPK();
                if (findPartidosJueces(id) == null) {
                    throw new NonexistentEntityException("The partidosJueces with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PartidosJuecesPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PartidosJueces partidosJueces;
            try {
                partidosJueces = em.getReference(PartidosJueces.class, id);
                partidosJueces.getPartidosJuecesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The partidosJueces with id " + id + " no longer exists.", enfe);
            }
            Juez juez = partidosJueces.getJuez();
            if (juez != null) {
                juez.getPartidosJuecesCollection().remove(partidosJueces);
                juez = em.merge(juez);
            }
            Partidos partidos = partidosJueces.getPartidos();
            if (partidos != null) {
                partidos.getPartidosJuecesCollection().remove(partidosJueces);
                partidos = em.merge(partidos);
            }
            em.remove(partidosJueces);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PartidosJueces> findPartidosJuecesEntities() {
        return findPartidosJuecesEntities(true, -1, -1);
    }

    public List<PartidosJueces> findPartidosJuecesEntities(int maxResults, int firstResult) {
        return findPartidosJuecesEntities(false, maxResults, firstResult);
    }

    private List<PartidosJueces> findPartidosJuecesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PartidosJueces.class));
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

    public PartidosJueces findPartidosJueces(PartidosJuecesPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PartidosJueces.class, id);
        } finally {
            em.close();
        }
    }

    public int getPartidosJuecesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PartidosJueces> rt = cq.from(PartidosJueces.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
