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
import Mundo.Staff;
import Mundo.Jugador;
import java.util.ArrayList;
import java.util.Collection;
import Mundo.Alineaciones;
import Mundo.Equipo;
import Mundo.Partidos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class EquipoJpaController implements Serializable {

    public EquipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Equipo equipo) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (equipo.getJugadorCollection() == null) {
            equipo.setJugadorCollection(new ArrayList<Jugador>());
        }
        if (equipo.getAlineacionesCollection() == null) {
            equipo.setAlineacionesCollection(new ArrayList<Alineaciones>());
        }
        if (equipo.getPartidosCollection() == null) {
            equipo.setPartidosCollection(new ArrayList<Partidos>());
        }
        if (equipo.getPartidosCollection1() == null) {
            equipo.setPartidosCollection1(new ArrayList<Partidos>());
        }
        List<String> illegalOrphanMessages = null;
        Staff entrenadorOrphanCheck = equipo.getEntrenador();
        if (entrenadorOrphanCheck != null) {
            Equipo oldEquipoOfEntrenador = entrenadorOrphanCheck.getEquipo();
            if (oldEquipoOfEntrenador != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Staff " + entrenadorOrphanCheck + " already has an item of type Equipo whose entrenador column cannot be null. Please make another selection for the entrenador field.");
            }
        }
        Staff auxiliarOrphanCheck = equipo.getAuxiliar();
        if (auxiliarOrphanCheck != null) {
            Equipo oldEquipoOfAuxiliar = auxiliarOrphanCheck.getEquipo();
            if (oldEquipoOfAuxiliar != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Staff " + auxiliarOrphanCheck + " already has an item of type Equipo whose auxiliar column cannot be null. Please make another selection for the auxiliar field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Staff entrenador = equipo.getEntrenador();
            if (entrenador != null) {
                entrenador = em.getReference(entrenador.getClass(), entrenador.getCodigo());
                equipo.setEntrenador(entrenador);
            }
            Staff auxiliar = equipo.getAuxiliar();
            if (auxiliar != null) {
                auxiliar = em.getReference(auxiliar.getClass(), auxiliar.getCodigo());
                equipo.setAuxiliar(auxiliar);
            }
            Collection<Jugador> attachedJugadorCollection = new ArrayList<Jugador>();
            for (Jugador jugadorCollectionJugadorToAttach : equipo.getJugadorCollection()) {
                jugadorCollectionJugadorToAttach = em.getReference(jugadorCollectionJugadorToAttach.getClass(), jugadorCollectionJugadorToAttach.getCodigoju());
                attachedJugadorCollection.add(jugadorCollectionJugadorToAttach);
            }
            equipo.setJugadorCollection(attachedJugadorCollection);
            Collection<Alineaciones> attachedAlineacionesCollection = new ArrayList<Alineaciones>();
            for (Alineaciones alineacionesCollectionAlineacionesToAttach : equipo.getAlineacionesCollection()) {
                alineacionesCollectionAlineacionesToAttach = em.getReference(alineacionesCollectionAlineacionesToAttach.getClass(), alineacionesCollectionAlineacionesToAttach.getAlineacionesPK());
                attachedAlineacionesCollection.add(alineacionesCollectionAlineacionesToAttach);
            }
            equipo.setAlineacionesCollection(attachedAlineacionesCollection);
            Collection<Partidos> attachedPartidosCollection = new ArrayList<Partidos>();
            for (Partidos partidosCollectionPartidosToAttach : equipo.getPartidosCollection()) {
                partidosCollectionPartidosToAttach = em.getReference(partidosCollectionPartidosToAttach.getClass(), partidosCollectionPartidosToAttach.getCodigopartido());
                attachedPartidosCollection.add(partidosCollectionPartidosToAttach);
            }
            equipo.setPartidosCollection(attachedPartidosCollection);
            Collection<Partidos> attachedPartidosCollection1 = new ArrayList<Partidos>();
            for (Partidos partidosCollection1PartidosToAttach : equipo.getPartidosCollection1()) {
                partidosCollection1PartidosToAttach = em.getReference(partidosCollection1PartidosToAttach.getClass(), partidosCollection1PartidosToAttach.getCodigopartido());
                attachedPartidosCollection1.add(partidosCollection1PartidosToAttach);
            }
            equipo.setPartidosCollection1(attachedPartidosCollection1);
            em.persist(equipo);
            if (entrenador != null) {
                entrenador.setEquipo(equipo);
                entrenador = em.merge(entrenador);
            }
            if (auxiliar != null) {
                auxiliar.setEquipo(equipo);
                auxiliar = em.merge(auxiliar);
            }
            for (Jugador jugadorCollectionJugador : equipo.getJugadorCollection()) {
                Equipo oldNombreeqOfJugadorCollectionJugador = jugadorCollectionJugador.getNombreeq();
                jugadorCollectionJugador.setNombreeq(equipo);
                jugadorCollectionJugador = em.merge(jugadorCollectionJugador);
                if (oldNombreeqOfJugadorCollectionJugador != null) {
                    oldNombreeqOfJugadorCollectionJugador.getJugadorCollection().remove(jugadorCollectionJugador);
                    oldNombreeqOfJugadorCollectionJugador = em.merge(oldNombreeqOfJugadorCollectionJugador);
                }
            }
            for (Alineaciones alineacionesCollectionAlineaciones : equipo.getAlineacionesCollection()) {
                Equipo oldEquipoOfAlineacionesCollectionAlineaciones = alineacionesCollectionAlineaciones.getEquipo();
                alineacionesCollectionAlineaciones.setEquipo(equipo);
                alineacionesCollectionAlineaciones = em.merge(alineacionesCollectionAlineaciones);
                if (oldEquipoOfAlineacionesCollectionAlineaciones != null) {
                    oldEquipoOfAlineacionesCollectionAlineaciones.getAlineacionesCollection().remove(alineacionesCollectionAlineaciones);
                    oldEquipoOfAlineacionesCollectionAlineaciones = em.merge(oldEquipoOfAlineacionesCollectionAlineaciones);
                }
            }
            for (Partidos partidosCollectionPartidos : equipo.getPartidosCollection()) {
                Equipo oldLocalOfPartidosCollectionPartidos = partidosCollectionPartidos.getLocal();
                partidosCollectionPartidos.setLocal(equipo);
                partidosCollectionPartidos = em.merge(partidosCollectionPartidos);
                if (oldLocalOfPartidosCollectionPartidos != null) {
                    oldLocalOfPartidosCollectionPartidos.getPartidosCollection().remove(partidosCollectionPartidos);
                    oldLocalOfPartidosCollectionPartidos = em.merge(oldLocalOfPartidosCollectionPartidos);
                }
            }
            for (Partidos partidosCollection1Partidos : equipo.getPartidosCollection1()) {
                Equipo oldVisitanteOfPartidosCollection1Partidos = partidosCollection1Partidos.getVisitante();
                partidosCollection1Partidos.setVisitante(equipo);
                partidosCollection1Partidos = em.merge(partidosCollection1Partidos);
                if (oldVisitanteOfPartidosCollection1Partidos != null) {
                    oldVisitanteOfPartidosCollection1Partidos.getPartidosCollection1().remove(partidosCollection1Partidos);
                    oldVisitanteOfPartidosCollection1Partidos = em.merge(oldVisitanteOfPartidosCollection1Partidos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEquipo(equipo.getNombreeq()) != null) {
                throw new PreexistingEntityException("Equipo " + equipo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Equipo equipo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo persistentEquipo = em.find(Equipo.class, equipo.getNombreeq());
            Staff entrenadorOld = persistentEquipo.getEntrenador();
            Staff entrenadorNew = equipo.getEntrenador();
            Staff auxiliarOld = persistentEquipo.getAuxiliar();
            Staff auxiliarNew = equipo.getAuxiliar();
            Collection<Jugador> jugadorCollectionOld = persistentEquipo.getJugadorCollection();
            Collection<Jugador> jugadorCollectionNew = equipo.getJugadorCollection();
            Collection<Alineaciones> alineacionesCollectionOld = persistentEquipo.getAlineacionesCollection();
            Collection<Alineaciones> alineacionesCollectionNew = equipo.getAlineacionesCollection();
            Collection<Partidos> partidosCollectionOld = persistentEquipo.getPartidosCollection();
            Collection<Partidos> partidosCollectionNew = equipo.getPartidosCollection();
            Collection<Partidos> partidosCollection1Old = persistentEquipo.getPartidosCollection1();
            Collection<Partidos> partidosCollection1New = equipo.getPartidosCollection1();
            List<String> illegalOrphanMessages = null;
            if (entrenadorNew != null && !entrenadorNew.equals(entrenadorOld)) {
                Equipo oldEquipoOfEntrenador = entrenadorNew.getEquipo();
                if (oldEquipoOfEntrenador != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Staff " + entrenadorNew + " already has an item of type Equipo whose entrenador column cannot be null. Please make another selection for the entrenador field.");
                }
            }
            if (auxiliarNew != null && !auxiliarNew.equals(auxiliarOld)) {
                Equipo oldEquipoOfAuxiliar = auxiliarNew.getEquipo();
                if (oldEquipoOfAuxiliar != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Staff " + auxiliarNew + " already has an item of type Equipo whose auxiliar column cannot be null. Please make another selection for the auxiliar field.");
                }
            }
            for (Jugador jugadorCollectionOldJugador : jugadorCollectionOld) {
                if (!jugadorCollectionNew.contains(jugadorCollectionOldJugador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jugador " + jugadorCollectionOldJugador + " since its nombreeq field is not nullable.");
                }
            }
            for (Alineaciones alineacionesCollectionOldAlineaciones : alineacionesCollectionOld) {
                if (!alineacionesCollectionNew.contains(alineacionesCollectionOldAlineaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alineaciones " + alineacionesCollectionOldAlineaciones + " since its equipo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (entrenadorNew != null) {
                entrenadorNew = em.getReference(entrenadorNew.getClass(), entrenadorNew.getCodigo());
                equipo.setEntrenador(entrenadorNew);
            }
            if (auxiliarNew != null) {
                auxiliarNew = em.getReference(auxiliarNew.getClass(), auxiliarNew.getCodigo());
                equipo.setAuxiliar(auxiliarNew);
            }
            Collection<Jugador> attachedJugadorCollectionNew = new ArrayList<Jugador>();
            for (Jugador jugadorCollectionNewJugadorToAttach : jugadorCollectionNew) {
                jugadorCollectionNewJugadorToAttach = em.getReference(jugadorCollectionNewJugadorToAttach.getClass(), jugadorCollectionNewJugadorToAttach.getCodigoju());
                attachedJugadorCollectionNew.add(jugadorCollectionNewJugadorToAttach);
            }
            jugadorCollectionNew = attachedJugadorCollectionNew;
            equipo.setJugadorCollection(jugadorCollectionNew);
            Collection<Alineaciones> attachedAlineacionesCollectionNew = new ArrayList<Alineaciones>();
            for (Alineaciones alineacionesCollectionNewAlineacionesToAttach : alineacionesCollectionNew) {
                alineacionesCollectionNewAlineacionesToAttach = em.getReference(alineacionesCollectionNewAlineacionesToAttach.getClass(), alineacionesCollectionNewAlineacionesToAttach.getAlineacionesPK());
                attachedAlineacionesCollectionNew.add(alineacionesCollectionNewAlineacionesToAttach);
            }
            alineacionesCollectionNew = attachedAlineacionesCollectionNew;
            equipo.setAlineacionesCollection(alineacionesCollectionNew);
            Collection<Partidos> attachedPartidosCollectionNew = new ArrayList<Partidos>();
            for (Partidos partidosCollectionNewPartidosToAttach : partidosCollectionNew) {
                partidosCollectionNewPartidosToAttach = em.getReference(partidosCollectionNewPartidosToAttach.getClass(), partidosCollectionNewPartidosToAttach.getCodigopartido());
                attachedPartidosCollectionNew.add(partidosCollectionNewPartidosToAttach);
            }
            partidosCollectionNew = attachedPartidosCollectionNew;
            equipo.setPartidosCollection(partidosCollectionNew);
            Collection<Partidos> attachedPartidosCollection1New = new ArrayList<Partidos>();
            for (Partidos partidosCollection1NewPartidosToAttach : partidosCollection1New) {
                partidosCollection1NewPartidosToAttach = em.getReference(partidosCollection1NewPartidosToAttach.getClass(), partidosCollection1NewPartidosToAttach.getCodigopartido());
                attachedPartidosCollection1New.add(partidosCollection1NewPartidosToAttach);
            }
            partidosCollection1New = attachedPartidosCollection1New;
            equipo.setPartidosCollection1(partidosCollection1New);
            equipo = em.merge(equipo);
            if (entrenadorOld != null && !entrenadorOld.equals(entrenadorNew)) {
                entrenadorOld.setEquipo(null);
                entrenadorOld = em.merge(entrenadorOld);
            }
            if (entrenadorNew != null && !entrenadorNew.equals(entrenadorOld)) {
                entrenadorNew.setEquipo(equipo);
                entrenadorNew = em.merge(entrenadorNew);
            }
            if (auxiliarOld != null && !auxiliarOld.equals(auxiliarNew)) {
                auxiliarOld.setEquipo(null);
                auxiliarOld = em.merge(auxiliarOld);
            }
            if (auxiliarNew != null && !auxiliarNew.equals(auxiliarOld)) {
                auxiliarNew.setEquipo(equipo);
                auxiliarNew = em.merge(auxiliarNew);
            }
            for (Jugador jugadorCollectionNewJugador : jugadorCollectionNew) {
                if (!jugadorCollectionOld.contains(jugadorCollectionNewJugador)) {
                    Equipo oldNombreeqOfJugadorCollectionNewJugador = jugadorCollectionNewJugador.getNombreeq();
                    jugadorCollectionNewJugador.setNombreeq(equipo);
                    jugadorCollectionNewJugador = em.merge(jugadorCollectionNewJugador);
                    if (oldNombreeqOfJugadorCollectionNewJugador != null && !oldNombreeqOfJugadorCollectionNewJugador.equals(equipo)) {
                        oldNombreeqOfJugadorCollectionNewJugador.getJugadorCollection().remove(jugadorCollectionNewJugador);
                        oldNombreeqOfJugadorCollectionNewJugador = em.merge(oldNombreeqOfJugadorCollectionNewJugador);
                    }
                }
            }
            for (Alineaciones alineacionesCollectionNewAlineaciones : alineacionesCollectionNew) {
                if (!alineacionesCollectionOld.contains(alineacionesCollectionNewAlineaciones)) {
                    Equipo oldEquipoOfAlineacionesCollectionNewAlineaciones = alineacionesCollectionNewAlineaciones.getEquipo();
                    alineacionesCollectionNewAlineaciones.setEquipo(equipo);
                    alineacionesCollectionNewAlineaciones = em.merge(alineacionesCollectionNewAlineaciones);
                    if (oldEquipoOfAlineacionesCollectionNewAlineaciones != null && !oldEquipoOfAlineacionesCollectionNewAlineaciones.equals(equipo)) {
                        oldEquipoOfAlineacionesCollectionNewAlineaciones.getAlineacionesCollection().remove(alineacionesCollectionNewAlineaciones);
                        oldEquipoOfAlineacionesCollectionNewAlineaciones = em.merge(oldEquipoOfAlineacionesCollectionNewAlineaciones);
                    }
                }
            }
            for (Partidos partidosCollectionOldPartidos : partidosCollectionOld) {
                if (!partidosCollectionNew.contains(partidosCollectionOldPartidos)) {
                    partidosCollectionOldPartidos.setLocal(null);
                    partidosCollectionOldPartidos = em.merge(partidosCollectionOldPartidos);
                }
            }
            for (Partidos partidosCollectionNewPartidos : partidosCollectionNew) {
                if (!partidosCollectionOld.contains(partidosCollectionNewPartidos)) {
                    Equipo oldLocalOfPartidosCollectionNewPartidos = partidosCollectionNewPartidos.getLocal();
                    partidosCollectionNewPartidos.setLocal(equipo);
                    partidosCollectionNewPartidos = em.merge(partidosCollectionNewPartidos);
                    if (oldLocalOfPartidosCollectionNewPartidos != null && !oldLocalOfPartidosCollectionNewPartidos.equals(equipo)) {
                        oldLocalOfPartidosCollectionNewPartidos.getPartidosCollection().remove(partidosCollectionNewPartidos);
                        oldLocalOfPartidosCollectionNewPartidos = em.merge(oldLocalOfPartidosCollectionNewPartidos);
                    }
                }
            }
            for (Partidos partidosCollection1OldPartidos : partidosCollection1Old) {
                if (!partidosCollection1New.contains(partidosCollection1OldPartidos)) {
                    partidosCollection1OldPartidos.setVisitante(null);
                    partidosCollection1OldPartidos = em.merge(partidosCollection1OldPartidos);
                }
            }
            for (Partidos partidosCollection1NewPartidos : partidosCollection1New) {
                if (!partidosCollection1Old.contains(partidosCollection1NewPartidos)) {
                    Equipo oldVisitanteOfPartidosCollection1NewPartidos = partidosCollection1NewPartidos.getVisitante();
                    partidosCollection1NewPartidos.setVisitante(equipo);
                    partidosCollection1NewPartidos = em.merge(partidosCollection1NewPartidos);
                    if (oldVisitanteOfPartidosCollection1NewPartidos != null && !oldVisitanteOfPartidosCollection1NewPartidos.equals(equipo)) {
                        oldVisitanteOfPartidosCollection1NewPartidos.getPartidosCollection1().remove(partidosCollection1NewPartidos);
                        oldVisitanteOfPartidosCollection1NewPartidos = em.merge(oldVisitanteOfPartidosCollection1NewPartidos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = equipo.getNombreeq();
                if (findEquipo(id) == null) {
                    throw new NonexistentEntityException("The equipo with id " + id + " no longer exists.");
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
            Equipo equipo;
            try {
                equipo = em.getReference(Equipo.class, id);
                equipo.getNombreeq();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Jugador> jugadorCollectionOrphanCheck = equipo.getJugadorCollection();
            for (Jugador jugadorCollectionOrphanCheckJugador : jugadorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the Jugador " + jugadorCollectionOrphanCheckJugador + " in its jugadorCollection field has a non-nullable nombreeq field.");
            }
            Collection<Alineaciones> alineacionesCollectionOrphanCheck = equipo.getAlineacionesCollection();
            for (Alineaciones alineacionesCollectionOrphanCheckAlineaciones : alineacionesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the Alineaciones " + alineacionesCollectionOrphanCheckAlineaciones + " in its alineacionesCollection field has a non-nullable equipo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Staff entrenador = equipo.getEntrenador();
            if (entrenador != null) {
                entrenador.setEquipo(null);
                entrenador = em.merge(entrenador);
            }
            Staff auxiliar = equipo.getAuxiliar();
            if (auxiliar != null) {
                auxiliar.setEquipo(null);
                auxiliar = em.merge(auxiliar);
            }
            Collection<Partidos> partidosCollection = equipo.getPartidosCollection();
            for (Partidos partidosCollectionPartidos : partidosCollection) {
                partidosCollectionPartidos.setLocal(null);
                partidosCollectionPartidos = em.merge(partidosCollectionPartidos);
            }
            Collection<Partidos> partidosCollection1 = equipo.getPartidosCollection1();
            for (Partidos partidosCollection1Partidos : partidosCollection1) {
                partidosCollection1Partidos.setVisitante(null);
                partidosCollection1Partidos = em.merge(partidosCollection1Partidos);
            }
            em.remove(equipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Equipo> findEquipoEntities() {
        return findEquipoEntities(true, -1, -1);
    }

    public List<Equipo> findEquipoEntities(int maxResults, int firstResult) {
        return findEquipoEntities(false, maxResults, firstResult);
    }

    private List<Equipo> findEquipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Equipo.class));
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

    public Equipo findEquipo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Equipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Equipo> rt = cq.from(Equipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
