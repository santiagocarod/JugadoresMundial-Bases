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
import Mundo.Categorias;
import Mundo.Fase;
import Mundo.Precio;
import Mundo.PrecioPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author mafeg
 */
public class PrecioJpaController implements Serializable {

    public PrecioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Precio precio) throws PreexistingEntityException, Exception {
        if (precio.getPrecioPK() == null) {
            precio.setPrecioPK(new PrecioPK());
        }
        precio.getPrecioPK().setFase(precio.getFase1().getFase());
        precio.getPrecioPK().setCodcategoria(precio.getCategorias().getCodcategoria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorias categorias = precio.getCategorias();
            if (categorias != null) {
                categorias = em.getReference(categorias.getClass(), categorias.getCodcategoria());
                precio.setCategorias(categorias);
            }
            Fase fase1 = precio.getFase1();
            if (fase1 != null) {
                fase1 = em.getReference(fase1.getClass(), fase1.getFase());
                precio.setFase1(fase1);
            }
            em.persist(precio);
            if (categorias != null) {
                categorias.getPrecioCollection().add(precio);
                categorias = em.merge(categorias);
            }
            if (fase1 != null) {
                fase1.getPrecioCollection().add(precio);
                fase1 = em.merge(fase1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPrecio(precio.getPrecioPK()) != null) {
                throw new PreexistingEntityException("Precio " + precio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Precio precio) throws NonexistentEntityException, Exception {
        precio.getPrecioPK().setFase(precio.getFase1().getFase());
        precio.getPrecioPK().setCodcategoria(precio.getCategorias().getCodcategoria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Precio persistentPrecio = em.find(Precio.class, precio.getPrecioPK());
            Categorias categoriasOld = persistentPrecio.getCategorias();
            Categorias categoriasNew = precio.getCategorias();
            Fase fase1Old = persistentPrecio.getFase1();
            Fase fase1New = precio.getFase1();
            if (categoriasNew != null) {
                categoriasNew = em.getReference(categoriasNew.getClass(), categoriasNew.getCodcategoria());
                precio.setCategorias(categoriasNew);
            }
            if (fase1New != null) {
                fase1New = em.getReference(fase1New.getClass(), fase1New.getFase());
                precio.setFase1(fase1New);
            }
            precio = em.merge(precio);
            if (categoriasOld != null && !categoriasOld.equals(categoriasNew)) {
                categoriasOld.getPrecioCollection().remove(precio);
                categoriasOld = em.merge(categoriasOld);
            }
            if (categoriasNew != null && !categoriasNew.equals(categoriasOld)) {
                categoriasNew.getPrecioCollection().add(precio);
                categoriasNew = em.merge(categoriasNew);
            }
            if (fase1Old != null && !fase1Old.equals(fase1New)) {
                fase1Old.getPrecioCollection().remove(precio);
                fase1Old = em.merge(fase1Old);
            }
            if (fase1New != null && !fase1New.equals(fase1Old)) {
                fase1New.getPrecioCollection().add(precio);
                fase1New = em.merge(fase1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PrecioPK id = precio.getPrecioPK();
                if (findPrecio(id) == null) {
                    throw new NonexistentEntityException("The precio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PrecioPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Precio precio;
            try {
                precio = em.getReference(Precio.class, id);
                precio.getPrecioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The precio with id " + id + " no longer exists.", enfe);
            }
            Categorias categorias = precio.getCategorias();
            if (categorias != null) {
                categorias.getPrecioCollection().remove(precio);
                categorias = em.merge(categorias);
            }
            Fase fase1 = precio.getFase1();
            if (fase1 != null) {
                fase1.getPrecioCollection().remove(precio);
                fase1 = em.merge(fase1);
            }
            em.remove(precio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Precio> findPrecioEntities() {
        return findPrecioEntities(true, -1, -1);
    }

    public List<Precio> findPrecioEntities(int maxResults, int firstResult) {
        return findPrecioEntities(false, maxResults, firstResult);
    }

    private List<Precio> findPrecioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Precio.class));
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

    public Precio findPrecio(PrecioPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Precio.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrecioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Precio> rt = cq.from(Precio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
