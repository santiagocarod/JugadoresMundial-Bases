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
import Mundo.Anotaciones;
import Mundo.Anotacionxpartido;
import Mundo.AnotacionxpartidoPK;
import Mundo.Equipo;
import Mundo.Jugador;
import Mundo.Partidos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class AnotacionxpartidoJpaController implements Serializable {

    public AnotacionxpartidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Anotacionxpartido anotacionxpartido) throws PreexistingEntityException, Exception {
        if (anotacionxpartido.getAnotacionxpartidoPK() == null) {
            anotacionxpartido.setAnotacionxpartidoPK(new AnotacionxpartidoPK());
        }
        anotacionxpartido.getAnotacionxpartidoPK().setCodigopartido(anotacionxpartido.getPartidos().getCodigopartido());
        anotacionxpartido.getAnotacionxpartidoPK().setCodigoju(anotacionxpartido.getJugador().getCodigoju());
        anotacionxpartido.getAnotacionxpartidoPK().setTipoan(anotacionxpartido.getAnotaciones().getTipoan());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Anotaciones anotaciones = anotacionxpartido.getAnotaciones();
            if (anotaciones != null) {
                anotaciones = em.getReference(anotaciones.getClass(), anotaciones.getTipoan());
                anotacionxpartido.setAnotaciones(anotaciones);
            }
            Jugador jugador = anotacionxpartido.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getCodigoju());
                anotacionxpartido.setJugador(jugador);
            }
            Partidos partidos = anotacionxpartido.getPartidos();
            if (partidos != null) {
                partidos = em.getReference(partidos.getClass(), partidos.getCodigopartido());
                anotacionxpartido.setPartidos(partidos);
            }
            
            if (anotaciones != null) {
                anotaciones.getAnotacionxpartidoCollection().add(anotacionxpartido);
                anotaciones = em.merge(anotaciones);
            }
            if (jugador != null) {
                jugador.getAnotacionxpartidoCollection().add(anotacionxpartido);
                jugador = em.merge(jugador);
            }
            if (partidos != null) {
                partidos.getAnotacionxpartidoCollection().add(anotacionxpartido);
                partidos = em.merge(partidos);
            }
            em.persist(anotacionxpartido);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnotacionxpartido(anotacionxpartido.getAnotacionxpartidoPK()) != null) {
                throw new PreexistingEntityException("Anotacionxpartido " + anotacionxpartido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Anotacionxpartido anotacionxpartido) throws NonexistentEntityException, Exception {
        anotacionxpartido.getAnotacionxpartidoPK().setCodigopartido(anotacionxpartido.getPartidos().getCodigopartido());
        anotacionxpartido.getAnotacionxpartidoPK().setCodigoju(anotacionxpartido.getJugador().getCodigoju());
        anotacionxpartido.getAnotacionxpartidoPK().setTipoan(anotacionxpartido.getAnotaciones().getTipoan());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Anotacionxpartido persistentAnotacionxpartido = em.find(Anotacionxpartido.class, anotacionxpartido.getAnotacionxpartidoPK());
            Anotaciones anotacionesOld = persistentAnotacionxpartido.getAnotaciones();
            Anotaciones anotacionesNew = anotacionxpartido.getAnotaciones();
            Jugador jugadorOld = persistentAnotacionxpartido.getJugador();
            Jugador jugadorNew = anotacionxpartido.getJugador();
            Partidos partidosOld = persistentAnotacionxpartido.getPartidos();
            Partidos partidosNew = anotacionxpartido.getPartidos();
            if (anotacionesNew != null) {
                anotacionesNew = em.getReference(anotacionesNew.getClass(), anotacionesNew.getTipoan());
                anotacionxpartido.setAnotaciones(anotacionesNew);
            }
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getCodigoju());
                anotacionxpartido.setJugador(jugadorNew);
            }
            if (partidosNew != null) {
                partidosNew = em.getReference(partidosNew.getClass(), partidosNew.getCodigopartido());
                anotacionxpartido.setPartidos(partidosNew);
            }
            anotacionxpartido = em.merge(anotacionxpartido);
            if (anotacionesOld != null && !anotacionesOld.equals(anotacionesNew)) {
                anotacionesOld.getAnotacionxpartidoCollection().remove(anotacionxpartido);
                anotacionesOld = em.merge(anotacionesOld);
            }
            if (anotacionesNew != null && !anotacionesNew.equals(anotacionesOld)) {
                anotacionesNew.getAnotacionxpartidoCollection().add(anotacionxpartido);
                anotacionesNew = em.merge(anotacionesNew);
            }
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.getAnotacionxpartidoCollection().remove(anotacionxpartido);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.getAnotacionxpartidoCollection().add(anotacionxpartido);
                jugadorNew = em.merge(jugadorNew);
            }
            if (partidosOld != null && !partidosOld.equals(partidosNew)) {
                partidosOld.getAnotacionxpartidoCollection().remove(anotacionxpartido);
                partidosOld = em.merge(partidosOld);
            }
            if (partidosNew != null && !partidosNew.equals(partidosOld)) {
                partidosNew.getAnotacionxpartidoCollection().add(anotacionxpartido);
                partidosNew = em.merge(partidosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AnotacionxpartidoPK id = anotacionxpartido.getAnotacionxpartidoPK();
                if (findAnotacionxpartido(id) == null) {
                    throw new NonexistentEntityException("The anotacionxpartido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AnotacionxpartidoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Anotacionxpartido anotacionxpartido;
            try {
                anotacionxpartido = em.getReference(Anotacionxpartido.class, id);
                anotacionxpartido.getAnotacionxpartidoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The anotacionxpartido with id " + id + " no longer exists.", enfe);
            }
            Anotaciones anotaciones = anotacionxpartido.getAnotaciones();
            if (anotaciones != null) {
                anotaciones.getAnotacionxpartidoCollection().remove(anotacionxpartido);
                anotaciones = em.merge(anotaciones);
            }
            Jugador jugador = anotacionxpartido.getJugador();
            if (jugador != null) {
                jugador.getAnotacionxpartidoCollection().remove(anotacionxpartido);
                jugador = em.merge(jugador);
            }
            Partidos partidos = anotacionxpartido.getPartidos();
            if (partidos != null) {
                partidos.getAnotacionxpartidoCollection().remove(anotacionxpartido);
                partidos = em.merge(partidos);
            }
            em.remove(anotacionxpartido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Anotacionxpartido> findAnotacionxpartidoEntities() {
        return findAnotacionxpartidoEntities(true, -1, -1);
    }

    public List<Anotacionxpartido> findAnotacionxpartidoEntities(int maxResults, int firstResult) {
        return findAnotacionxpartidoEntities(false, maxResults, firstResult);
    }

    private List<Anotacionxpartido> findAnotacionxpartidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Anotacionxpartido.class));
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

    public Anotacionxpartido findAnotacionxpartido(AnotacionxpartidoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Anotacionxpartido.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnotacionxpartidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Anotacionxpartido> rt = cq.from(Anotacionxpartido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }


    public long obtenerMarcador(BigInteger codigopartido, String nombree) {
        Query q;
        q = getEntityManager().createNamedQuery("Anotacionxpartido.marcador");
        q.setParameter("cod", codigopartido);
        Query a;
        a = getEntityManager().createNamedQuery("Equipo.findByNombreeq");
        a.setParameter("nombreeq", nombree);
        q.setParameter("equi", (Equipo)a.getSingleResult());
        long r = (long) q.getSingleResult();
        return r;
    }

    public List<Anotacionxpartido> obtenerDetalle(BigInteger codigopartido) {
        Query q;
        q = getEntityManager().createNamedQuery("Anotacionxpartido.findByCodigopartido");
        q.setParameter("codigopartido", codigopartido);
        List<Anotacionxpartido> r = q.getResultList();
        return r;
    }
}
