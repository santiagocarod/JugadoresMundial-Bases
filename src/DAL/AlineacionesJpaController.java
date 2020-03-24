/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DAL.exceptions.NonexistentEntityException;
import DAL.exceptions.PreexistingEntityException;
import Mundo.Alineaciones;
import Mundo.AlineacionesPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Mundo.Equipo;
import Mundo.Jugador;
import Mundo.Partidos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class AlineacionesJpaController implements Serializable {

    public AlineacionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alineaciones alineaciones) throws PreexistingEntityException, Exception {
        if (alineaciones.getAlineacionesPK() == null) {
            alineaciones.setAlineacionesPK(new AlineacionesPK());
        }
        alineaciones.getAlineacionesPK().setCodigoju(alineaciones.getJugador().getCodigoju());
        alineaciones.getAlineacionesPK().setCodigopartido(alineaciones.getPartidos().getCodigopartido());
        alineaciones.getAlineacionesPK().setNombreeq(alineaciones.getEquipo().getNombreeq());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipo = alineaciones.getEquipo();
            if (equipo != null) {
                equipo = em.getReference(equipo.getClass(), equipo.getNombreeq());
                alineaciones.setEquipo(equipo);
            }
            Jugador jugador = alineaciones.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getCodigoju());
                alineaciones.setJugador(jugador);
            }
            Partidos partidos = alineaciones.getPartidos();
            if (partidos != null) {
                partidos = em.getReference(partidos.getClass(), partidos.getCodigopartido());
                alineaciones.setPartidos(partidos);
            }
            em.persist(alineaciones);
            if (equipo != null) {
                equipo.getAlineacionesCollection().add(alineaciones);
                equipo = em.merge(equipo);
            }
            if (jugador != null) {
                jugador.getAlineacionesCollection().add(alineaciones);
                jugador = em.merge(jugador);
            }
            if (partidos != null) {
                partidos.getAlineacionesCollection().add(alineaciones);
                partidos = em.merge(partidos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlineaciones(alineaciones.getAlineacionesPK()) != null) {
                throw new PreexistingEntityException("Alineaciones " + alineaciones + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alineaciones alineaciones) throws NonexistentEntityException, Exception {
        alineaciones.getAlineacionesPK().setCodigoju(alineaciones.getJugador().getCodigoju());
        alineaciones.getAlineacionesPK().setCodigopartido(alineaciones.getPartidos().getCodigopartido());
        alineaciones.getAlineacionesPK().setNombreeq(alineaciones.getEquipo().getNombreeq());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alineaciones persistentAlineaciones = em.find(Alineaciones.class, alineaciones.getAlineacionesPK());
            Equipo equipoOld = persistentAlineaciones.getEquipo();
            Equipo equipoNew = alineaciones.getEquipo();
            Jugador jugadorOld = persistentAlineaciones.getJugador();
            Jugador jugadorNew = alineaciones.getJugador();
            Partidos partidosOld = persistentAlineaciones.getPartidos();
            Partidos partidosNew = alineaciones.getPartidos();
            if (equipoNew != null) {
                equipoNew = em.getReference(equipoNew.getClass(), equipoNew.getNombreeq());
                alineaciones.setEquipo(equipoNew);
            }
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getCodigoju());
                alineaciones.setJugador(jugadorNew);
            }
            if (partidosNew != null) {
                partidosNew = em.getReference(partidosNew.getClass(), partidosNew.getCodigopartido());
                alineaciones.setPartidos(partidosNew);
            }
            alineaciones = em.merge(alineaciones);
            if (equipoOld != null && !equipoOld.equals(equipoNew)) {
                equipoOld.getAlineacionesCollection().remove(alineaciones);
                equipoOld = em.merge(equipoOld);
            }
            if (equipoNew != null && !equipoNew.equals(equipoOld)) {
                equipoNew.getAlineacionesCollection().add(alineaciones);
                equipoNew = em.merge(equipoNew);
            }
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.getAlineacionesCollection().remove(alineaciones);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.getAlineacionesCollection().add(alineaciones);
                jugadorNew = em.merge(jugadorNew);
            }
            if (partidosOld != null && !partidosOld.equals(partidosNew)) {
                partidosOld.getAlineacionesCollection().remove(alineaciones);
                partidosOld = em.merge(partidosOld);
            }
            if (partidosNew != null && !partidosNew.equals(partidosOld)) {
                partidosNew.getAlineacionesCollection().add(alineaciones);
                partidosNew = em.merge(partidosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AlineacionesPK id = alineaciones.getAlineacionesPK();
                if (findAlineaciones(id) == null) {
                    throw new NonexistentEntityException("The alineaciones with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AlineacionesPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alineaciones alineaciones;
            try {
                alineaciones = em.getReference(Alineaciones.class, id);
                alineaciones.getAlineacionesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alineaciones with id " + id + " no longer exists.", enfe);
            }
            Equipo equipo = alineaciones.getEquipo();
            if (equipo != null) {
                equipo.getAlineacionesCollection().remove(alineaciones);
                equipo = em.merge(equipo);
            }
            Jugador jugador = alineaciones.getJugador();
            if (jugador != null) {
                jugador.getAlineacionesCollection().remove(alineaciones);
                jugador = em.merge(jugador);
            }
            Partidos partidos = alineaciones.getPartidos();
            if (partidos != null) {
                partidos.getAlineacionesCollection().remove(alineaciones);
                partidos = em.merge(partidos);
            }
            em.remove(alineaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alineaciones> findAlineacionesEntities() {
        return findAlineacionesEntities(true, -1, -1);
    }

    public List<Alineaciones> findAlineacionesEntities(int maxResults, int firstResult) {
        return findAlineacionesEntities(false, maxResults, firstResult);
    }

    private List<Alineaciones> findAlineacionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alineaciones.class));
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

    public Alineaciones findAlineaciones(AlineacionesPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alineaciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlineacionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alineaciones> rt = cq.from(Alineaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
        public List<Alineaciones> findAlineacionesEq(String eq, int par) {
            EntityManager em = getEntityManager();
        try {
            Query q= em.createNamedQuery("Alineaciones.findByPE");
            q.setParameter("nombreeq", eq);
            q.setParameter("codigopartido", par);
            List<Alineaciones> a= (List<Alineaciones>)q.getResultList();
            return a;
        } finally {
            em.close();
        }
    }
    
}
