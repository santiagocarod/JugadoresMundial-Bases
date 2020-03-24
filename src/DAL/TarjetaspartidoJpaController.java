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
import Mundo.Jugador;
import Mundo.Partidos;
import Mundo.Tarjeta;
import Mundo.Tarjetaspartido;
import Mundo.TarjetaspartidoPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class TarjetaspartidoJpaController implements Serializable {

    public TarjetaspartidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjetaspartido tarjetaspartido) throws PreexistingEntityException, Exception {
        if (tarjetaspartido.getTarjetaspartidoPK() == null) {
            tarjetaspartido.setTarjetaspartidoPK(new TarjetaspartidoPK());
        }
        tarjetaspartido.getTarjetaspartidoPK().setCodigoju(tarjetaspartido.getJugador().getCodigoju());
        tarjetaspartido.getTarjetaspartidoPK().setCodigopartido(tarjetaspartido.getPartidos().getCodigopartido());
        tarjetaspartido.getTarjetaspartidoPK().setTipot(tarjetaspartido.getTarjeta().getTipot());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador = tarjetaspartido.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getCodigoju());
                tarjetaspartido.setJugador(jugador);
            }
            Partidos partidos = tarjetaspartido.getPartidos();
            if (partidos != null) {
                partidos = em.getReference(partidos.getClass(), partidos.getCodigopartido());
                tarjetaspartido.setPartidos(partidos);
            }
            Tarjeta tarjeta = tarjetaspartido.getTarjeta();
            if (tarjeta != null) {
                tarjeta = em.getReference(tarjeta.getClass(), tarjeta.getTipot());
                tarjetaspartido.setTarjeta(tarjeta);
            }
            em.persist(tarjetaspartido);
            if (jugador != null) {
                jugador.getTarjetaspartidoCollection().add(tarjetaspartido);
                jugador = em.merge(jugador);
            }
            if (partidos != null) {
                partidos.getTarjetaspartidoCollection().add(tarjetaspartido);
                partidos = em.merge(partidos);
            }
            if (tarjeta != null) {
                tarjeta.getTarjetaspartidoCollection().add(tarjetaspartido);
                tarjeta = em.merge(tarjeta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTarjetaspartido(tarjetaspartido.getTarjetaspartidoPK()) != null) {
                throw new PreexistingEntityException("Tarjetaspartido " + tarjetaspartido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarjetaspartido tarjetaspartido) throws NonexistentEntityException, Exception {
        tarjetaspartido.getTarjetaspartidoPK().setCodigoju(tarjetaspartido.getJugador().getCodigoju());
        tarjetaspartido.getTarjetaspartidoPK().setCodigopartido(tarjetaspartido.getPartidos().getCodigopartido());
        tarjetaspartido.getTarjetaspartidoPK().setTipot(tarjetaspartido.getTarjeta().getTipot());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjetaspartido persistentTarjetaspartido = em.find(Tarjetaspartido.class, tarjetaspartido.getTarjetaspartidoPK());
            Jugador jugadorOld = persistentTarjetaspartido.getJugador();
            Jugador jugadorNew = tarjetaspartido.getJugador();
            Partidos partidosOld = persistentTarjetaspartido.getPartidos();
            Partidos partidosNew = tarjetaspartido.getPartidos();
            Tarjeta tarjetaOld = persistentTarjetaspartido.getTarjeta();
            Tarjeta tarjetaNew = tarjetaspartido.getTarjeta();
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getCodigoju());
                tarjetaspartido.setJugador(jugadorNew);
            }
            if (partidosNew != null) {
                partidosNew = em.getReference(partidosNew.getClass(), partidosNew.getCodigopartido());
                tarjetaspartido.setPartidos(partidosNew);
            }
            if (tarjetaNew != null) {
                tarjetaNew = em.getReference(tarjetaNew.getClass(), tarjetaNew.getTipot());
                tarjetaspartido.setTarjeta(tarjetaNew);
            }
            tarjetaspartido = em.merge(tarjetaspartido);
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.getTarjetaspartidoCollection().remove(tarjetaspartido);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.getTarjetaspartidoCollection().add(tarjetaspartido);
                jugadorNew = em.merge(jugadorNew);
            }
            if (partidosOld != null && !partidosOld.equals(partidosNew)) {
                partidosOld.getTarjetaspartidoCollection().remove(tarjetaspartido);
                partidosOld = em.merge(partidosOld);
            }
            if (partidosNew != null && !partidosNew.equals(partidosOld)) {
                partidosNew.getTarjetaspartidoCollection().add(tarjetaspartido);
                partidosNew = em.merge(partidosNew);
            }
            if (tarjetaOld != null && !tarjetaOld.equals(tarjetaNew)) {
                tarjetaOld.getTarjetaspartidoCollection().remove(tarjetaspartido);
                tarjetaOld = em.merge(tarjetaOld);
            }
            if (tarjetaNew != null && !tarjetaNew.equals(tarjetaOld)) {
                tarjetaNew.getTarjetaspartidoCollection().add(tarjetaspartido);
                tarjetaNew = em.merge(tarjetaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TarjetaspartidoPK id = tarjetaspartido.getTarjetaspartidoPK();
                if (findTarjetaspartido(id) == null) {
                    throw new NonexistentEntityException("The tarjetaspartido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TarjetaspartidoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjetaspartido tarjetaspartido;
            try {
                tarjetaspartido = em.getReference(Tarjetaspartido.class, id);
                tarjetaspartido.getTarjetaspartidoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjetaspartido with id " + id + " no longer exists.", enfe);
            }
            Jugador jugador = tarjetaspartido.getJugador();
            if (jugador != null) {
                jugador.getTarjetaspartidoCollection().remove(tarjetaspartido);
                jugador = em.merge(jugador);
            }
            Partidos partidos = tarjetaspartido.getPartidos();
            if (partidos != null) {
                partidos.getTarjetaspartidoCollection().remove(tarjetaspartido);
                partidos = em.merge(partidos);
            }
            Tarjeta tarjeta = tarjetaspartido.getTarjeta();
            if (tarjeta != null) {
                tarjeta.getTarjetaspartidoCollection().remove(tarjetaspartido);
                tarjeta = em.merge(tarjeta);
            }
            em.remove(tarjetaspartido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarjetaspartido> findTarjetaspartidoEntities() {
        return findTarjetaspartidoEntities(true, -1, -1);
    }

    public List<Tarjetaspartido> findTarjetaspartidoEntities(int maxResults, int firstResult) {
        return findTarjetaspartidoEntities(false, maxResults, firstResult);
    }

    private List<Tarjetaspartido> findTarjetaspartidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjetaspartido.class));
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

    public Tarjetaspartido findTarjetaspartido(TarjetaspartidoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjetaspartido.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetaspartidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjetaspartido> rt = cq.from(Tarjetaspartido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
