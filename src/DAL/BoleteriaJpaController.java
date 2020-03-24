/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import Mundo.Boleteria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Pago;
import Mundo.Partidos;
import Mundo.Precio;
import Mundo.Sillas;
import Mundo.SillasPK;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class BoleteriaJpaController implements Serializable {

    public BoleteriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Boleteria boleteria) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago = boleteria.getPago();
            if (pago != null) {
                pago = em.getReference(pago.getClass(), pago.getPagoPK());
                boleteria.setPago(pago);
            }
            Partidos codigopartido = boleteria.getCodigopartido();
            if (codigopartido != null) {
                codigopartido = em.getReference(codigopartido.getClass(), codigopartido.getCodigopartido());
                boleteria.setCodigopartido(codigopartido);
            }

            Sillas sillas = boleteria.getSillas();
            if (sillas != null) {
                sillas = em.getReference(sillas.getClass(), sillas.getSillasPK());
                boleteria.setSillas(sillas);
            }
            em.persist(boleteria);
            if (pago != null) {
                pago.getBoleteriaCollection().add(boleteria);
                pago = em.merge(pago);
            }
            if (codigopartido != null) {
                codigopartido.getBoleteriaCollection().add(boleteria);
                codigopartido = em.merge(codigopartido);
            }

            if (sillas != null) {
                sillas.getBoleteriaCollection().add(boleteria);
                sillas = em.merge(sillas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBoleteria(boleteria.getCodigoboleta()) != null) {
                throw new PreexistingEntityException("Boleteria " + boleteria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Boleteria boleteria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Boleteria persistentBoleteria = em.find(Boleteria.class, boleteria.getCodigoboleta());
            Pago pagoOld = persistentBoleteria.getPago();
            Pago pagoNew = boleteria.getPago();
            Partidos codigopartidoOld = persistentBoleteria.getCodigopartido();
            Partidos codigopartidoNew = boleteria.getCodigopartido();
            Sillas sillasOld = persistentBoleteria.getSillas();
            Sillas sillasNew = boleteria.getSillas();
            if (pagoNew != null) {
                pagoNew = em.getReference(pagoNew.getClass(), pagoNew.getPagoPK());
                boleteria.setPago(pagoNew);
            }
            if (codigopartidoNew != null) {
                codigopartidoNew = em.getReference(codigopartidoNew.getClass(), codigopartidoNew.getCodigopartido());
                boleteria.setCodigopartido(codigopartidoNew);
            }
            if (sillasNew != null) {
                sillasNew = em.getReference(sillasNew.getClass(), sillasNew.getSillasPK());
                boleteria.setSillas(sillasNew);
            }
            boleteria = em.merge(boleteria);
            if (pagoOld != null && !pagoOld.equals(pagoNew)) {
                pagoOld.getBoleteriaCollection().remove(boleteria);
                pagoOld = em.merge(pagoOld);
            }
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                pagoNew.getBoleteriaCollection().add(boleteria);
                pagoNew = em.merge(pagoNew);
            }
            if (codigopartidoOld != null && !codigopartidoOld.equals(codigopartidoNew)) {
                codigopartidoOld.getBoleteriaCollection().remove(boleteria);
                codigopartidoOld = em.merge(codigopartidoOld);
            }
            if (codigopartidoNew != null && !codigopartidoNew.equals(codigopartidoOld)) {
                codigopartidoNew.getBoleteriaCollection().add(boleteria);
                codigopartidoNew = em.merge(codigopartidoNew);
            }

            if (sillasOld != null && !sillasOld.equals(sillasNew)) {
                sillasOld.getBoleteriaCollection().remove(boleteria);
                sillasOld = em.merge(sillasOld);
            }
            if (sillasNew != null && !sillasNew.equals(sillasOld)) {
                sillasNew.getBoleteriaCollection().add(boleteria);
                sillasNew = em.merge(sillasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = boleteria.getCodigoboleta();
                if (findBoleteria(id) == null) {
                    throw new NonexistentEntityException("The boleteria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Boleteria boleteria;
            try {
                boleteria = em.getReference(Boleteria.class, id);
                boleteria.getCodigoboleta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boleteria with id " + id + " no longer exists.", enfe);
            }
            Pago pago = boleteria.getPago();
            if (pago != null) {
                pago.getBoleteriaCollection().remove(boleteria);
                pago = em.merge(pago);
            }
            Partidos codigopartido = boleteria.getCodigopartido();
            if (codigopartido != null) {
                codigopartido.getBoleteriaCollection().remove(boleteria);
                codigopartido = em.merge(codigopartido);
            }
            Sillas sillas = boleteria.getSillas();
            if (sillas != null) {
                sillas.getBoleteriaCollection().remove(boleteria);
                sillas = em.merge(sillas);
            }
            em.remove(boleteria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Boleteria> findBoleteriaEntities() {
        return findBoleteriaEntities(true, -1, -1);
    }

    public List<Boleteria> findBoleteriaEntities(int maxResults, int firstResult) {
        return findBoleteriaEntities(false, maxResults, firstResult);
    }

    private List<Boleteria> findBoleteriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Boleteria.class));
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

    public Boleteria findBoleteria(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Boleteria.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoleteriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Boleteria> rt = cq.from(Boleteria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public long obtenerCanti(Partidos p)
    {
        EntityManager em = getEntityManager();
        try {
        Query q= em.createNamedQuery("Boleteria.catDisT");
        q.setParameter("part", p);
        return (long) q.getSingleResult();
         } finally {
            em.close();
        }
    }

    public long obtenerCantiPat(Partidos p, BigInteger c) {
         EntityManager em = getEntityManager();
        try {
        Query q= em.createNamedQuery("Boleteria.catpat");
        q.setParameter("part", p);
        q.setParameter("cate", c);
        Vector<Object>v=(Vector<Object>)q.getResultList();
         return v.size();
         } finally {
            em.close();
        }
    }
    
    public List<Boleteria> obtenerCantPat(Partidos p, BigInteger c) {
         EntityManager em = getEntityManager();
        try {
        Query q= em.createNamedQuery("Boleteria.catpat");
        q.setParameter("part", p);
        q.setParameter("cate", c);
       return(List<Boleteria>)q.getResultList();
         } finally {
            em.close();
        }
    }
    
     public long obtenerCantiDis(Partidos p, BigInteger c) {
            EntityManager em = getEntityManager();
        try {
        Query q= em.createNamedQuery("Boleteria.catDis");
        q.setParameter("part", p.getCodigopartido());
        q.setParameter("cate", c);
        return (long) q.getSingleResult();
         } finally {
            em.close();
        }
    }
      public List<Boleteria> obtenerCantDis1(Partidos p, BigInteger c) {
            EntityManager em = getEntityManager();
        try {
        Query q= em.createNamedQuery("Boleteria.catDisT1");
        q.setParameter("part", p.getCodigopartido());
        q.setParameter("cate", c);
        return (List<Boleteria>) q.getResultList();
         } finally {
            em.close();
        }
    }

    long getDisponibles(Partidos p) {
        EntityManager em = getEntityManager();
         try {
        Query q= em.createNamedQuery("Boleteria.canti");
        q.setParameter("codigopartido", p.getCodigopartido());
        return (long) q.getSingleResult();
         } finally {
            em.close();
        }
    }
    
    Boleteria boletaSP(SillasPK ay, Partidos p)
    {
         EntityManager em = getEntityManager();
         try {
        Query q= em.createNamedQuery("Boleteria.spk");
        q.setParameter("part", p);
        q.setParameter("spk", ay);
        return (Boleteria) q.getSingleResult();
         } finally {
            em.close();
        }
    }
    
}
