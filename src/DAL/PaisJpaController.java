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
import java.util.ArrayList;
import java.util.Collection;
import Mundo.Jugador;
import Mundo.Pais;
import Mundo.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) throws PreexistingEntityException, Exception {
        if (pais.getJuezCollection() == null) {
            pais.setJuezCollection(new ArrayList<Juez>());
        }
        if (pais.getJugadorCollection() == null) {
            pais.setJugadorCollection(new ArrayList<Jugador>());
        }
        if (pais.getUsuariosCollection() == null) {
            pais.setUsuariosCollection(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Juez> attachedJuezCollection = new ArrayList<Juez>();
            for (Juez juezCollectionJuezToAttach : pais.getJuezCollection()) {
                juezCollectionJuezToAttach = em.getReference(juezCollectionJuezToAttach.getClass(), juezCollectionJuezToAttach.getCodj());
                attachedJuezCollection.add(juezCollectionJuezToAttach);
            }
            pais.setJuezCollection(attachedJuezCollection);
            Collection<Jugador> attachedJugadorCollection = new ArrayList<Jugador>();
            for (Jugador jugadorCollectionJugadorToAttach : pais.getJugadorCollection()) {
                jugadorCollectionJugadorToAttach = em.getReference(jugadorCollectionJugadorToAttach.getClass(), jugadorCollectionJugadorToAttach.getCodigoju());
                attachedJugadorCollection.add(jugadorCollectionJugadorToAttach);
            }
            pais.setJugadorCollection(attachedJugadorCollection);
            Collection<Usuarios> attachedUsuariosCollection = new ArrayList<Usuarios>();
            for (Usuarios usuariosCollectionUsuariosToAttach : pais.getUsuariosCollection()) {
                usuariosCollectionUsuariosToAttach = em.getReference(usuariosCollectionUsuariosToAttach.getClass(), usuariosCollectionUsuariosToAttach.getId());
                attachedUsuariosCollection.add(usuariosCollectionUsuariosToAttach);
            }
            pais.setUsuariosCollection(attachedUsuariosCollection);
            em.persist(pais);
            for (Juez juezCollectionJuez : pais.getJuezCollection()) {
                Pais oldCodpaisOfJuezCollectionJuez = juezCollectionJuez.getCodpais();
                juezCollectionJuez.setCodpais(pais);
                juezCollectionJuez = em.merge(juezCollectionJuez);
                if (oldCodpaisOfJuezCollectionJuez != null) {
                    oldCodpaisOfJuezCollectionJuez.getJuezCollection().remove(juezCollectionJuez);
                    oldCodpaisOfJuezCollectionJuez = em.merge(oldCodpaisOfJuezCollectionJuez);
                }
            }
            for (Jugador jugadorCollectionJugador : pais.getJugadorCollection()) {
                Pais oldCodpaisOfJugadorCollectionJugador = jugadorCollectionJugador.getCodpais();
                jugadorCollectionJugador.setCodpais(pais);
                jugadorCollectionJugador = em.merge(jugadorCollectionJugador);
                if (oldCodpaisOfJugadorCollectionJugador != null) {
                    oldCodpaisOfJugadorCollectionJugador.getJugadorCollection().remove(jugadorCollectionJugador);
                    oldCodpaisOfJugadorCollectionJugador = em.merge(oldCodpaisOfJugadorCollectionJugador);
                }
            }
            for (Usuarios usuariosCollectionUsuarios : pais.getUsuariosCollection()) {
                Pais oldCodpaisOfUsuariosCollectionUsuarios = usuariosCollectionUsuarios.getCodpais();
                usuariosCollectionUsuarios.setCodpais(pais);
                usuariosCollectionUsuarios = em.merge(usuariosCollectionUsuarios);
                if (oldCodpaisOfUsuariosCollectionUsuarios != null) {
                    oldCodpaisOfUsuariosCollectionUsuarios.getUsuariosCollection().remove(usuariosCollectionUsuarios);
                    oldCodpaisOfUsuariosCollectionUsuarios = em.merge(oldCodpaisOfUsuariosCollectionUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPais(pais.getCodpais()) != null) {
                throw new PreexistingEntityException("Pais " + pais + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getCodpais());
            Collection<Juez> juezCollectionOld = persistentPais.getJuezCollection();
            Collection<Juez> juezCollectionNew = pais.getJuezCollection();
            Collection<Jugador> jugadorCollectionOld = persistentPais.getJugadorCollection();
            Collection<Jugador> jugadorCollectionNew = pais.getJugadorCollection();
            Collection<Usuarios> usuariosCollectionOld = persistentPais.getUsuariosCollection();
            Collection<Usuarios> usuariosCollectionNew = pais.getUsuariosCollection();
            Collection<Juez> attachedJuezCollectionNew = new ArrayList<Juez>();
            for (Juez juezCollectionNewJuezToAttach : juezCollectionNew) {
                juezCollectionNewJuezToAttach = em.getReference(juezCollectionNewJuezToAttach.getClass(), juezCollectionNewJuezToAttach.getCodj());
                attachedJuezCollectionNew.add(juezCollectionNewJuezToAttach);
            }
            juezCollectionNew = attachedJuezCollectionNew;
            pais.setJuezCollection(juezCollectionNew);
            Collection<Jugador> attachedJugadorCollectionNew = new ArrayList<Jugador>();
            for (Jugador jugadorCollectionNewJugadorToAttach : jugadorCollectionNew) {
                jugadorCollectionNewJugadorToAttach = em.getReference(jugadorCollectionNewJugadorToAttach.getClass(), jugadorCollectionNewJugadorToAttach.getCodigoju());
                attachedJugadorCollectionNew.add(jugadorCollectionNewJugadorToAttach);
            }
            jugadorCollectionNew = attachedJugadorCollectionNew;
            pais.setJugadorCollection(jugadorCollectionNew);
            Collection<Usuarios> attachedUsuariosCollectionNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosCollectionNewUsuariosToAttach : usuariosCollectionNew) {
                usuariosCollectionNewUsuariosToAttach = em.getReference(usuariosCollectionNewUsuariosToAttach.getClass(), usuariosCollectionNewUsuariosToAttach.getId());
                attachedUsuariosCollectionNew.add(usuariosCollectionNewUsuariosToAttach);
            }
            usuariosCollectionNew = attachedUsuariosCollectionNew;
            pais.setUsuariosCollection(usuariosCollectionNew);
            pais = em.merge(pais);
            for (Juez juezCollectionOldJuez : juezCollectionOld) {
                if (!juezCollectionNew.contains(juezCollectionOldJuez)) {
                    juezCollectionOldJuez.setCodpais(null);
                    juezCollectionOldJuez = em.merge(juezCollectionOldJuez);
                }
            }
            for (Juez juezCollectionNewJuez : juezCollectionNew) {
                if (!juezCollectionOld.contains(juezCollectionNewJuez)) {
                    Pais oldCodpaisOfJuezCollectionNewJuez = juezCollectionNewJuez.getCodpais();
                    juezCollectionNewJuez.setCodpais(pais);
                    juezCollectionNewJuez = em.merge(juezCollectionNewJuez);
                    if (oldCodpaisOfJuezCollectionNewJuez != null && !oldCodpaisOfJuezCollectionNewJuez.equals(pais)) {
                        oldCodpaisOfJuezCollectionNewJuez.getJuezCollection().remove(juezCollectionNewJuez);
                        oldCodpaisOfJuezCollectionNewJuez = em.merge(oldCodpaisOfJuezCollectionNewJuez);
                    }
                }
            }
            for (Jugador jugadorCollectionOldJugador : jugadorCollectionOld) {
                if (!jugadorCollectionNew.contains(jugadorCollectionOldJugador)) {
                    jugadorCollectionOldJugador.setCodpais(null);
                    jugadorCollectionOldJugador = em.merge(jugadorCollectionOldJugador);
                }
            }
            for (Jugador jugadorCollectionNewJugador : jugadorCollectionNew) {
                if (!jugadorCollectionOld.contains(jugadorCollectionNewJugador)) {
                    Pais oldCodpaisOfJugadorCollectionNewJugador = jugadorCollectionNewJugador.getCodpais();
                    jugadorCollectionNewJugador.setCodpais(pais);
                    jugadorCollectionNewJugador = em.merge(jugadorCollectionNewJugador);
                    if (oldCodpaisOfJugadorCollectionNewJugador != null && !oldCodpaisOfJugadorCollectionNewJugador.equals(pais)) {
                        oldCodpaisOfJugadorCollectionNewJugador.getJugadorCollection().remove(jugadorCollectionNewJugador);
                        oldCodpaisOfJugadorCollectionNewJugador = em.merge(oldCodpaisOfJugadorCollectionNewJugador);
                    }
                }
            }
            for (Usuarios usuariosCollectionOldUsuarios : usuariosCollectionOld) {
                if (!usuariosCollectionNew.contains(usuariosCollectionOldUsuarios)) {
                    usuariosCollectionOldUsuarios.setCodpais(null);
                    usuariosCollectionOldUsuarios = em.merge(usuariosCollectionOldUsuarios);
                }
            }
            for (Usuarios usuariosCollectionNewUsuarios : usuariosCollectionNew) {
                if (!usuariosCollectionOld.contains(usuariosCollectionNewUsuarios)) {
                    Pais oldCodpaisOfUsuariosCollectionNewUsuarios = usuariosCollectionNewUsuarios.getCodpais();
                    usuariosCollectionNewUsuarios.setCodpais(pais);
                    usuariosCollectionNewUsuarios = em.merge(usuariosCollectionNewUsuarios);
                    if (oldCodpaisOfUsuariosCollectionNewUsuarios != null && !oldCodpaisOfUsuariosCollectionNewUsuarios.equals(pais)) {
                        oldCodpaisOfUsuariosCollectionNewUsuarios.getUsuariosCollection().remove(usuariosCollectionNewUsuarios);
                        oldCodpaisOfUsuariosCollectionNewUsuarios = em.merge(oldCodpaisOfUsuariosCollectionNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pais.getCodpais();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getCodpais();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            Collection<Juez> juezCollection = pais.getJuezCollection();
            for (Juez juezCollectionJuez : juezCollection) {
                juezCollectionJuez.setCodpais(null);
                juezCollectionJuez = em.merge(juezCollectionJuez);
            }
            Collection<Jugador> jugadorCollection = pais.getJugadorCollection();
            for (Jugador jugadorCollectionJugador : jugadorCollection) {
                jugadorCollectionJugador.setCodpais(null);
                jugadorCollectionJugador = em.merge(jugadorCollectionJugador);
            }
            Collection<Usuarios> usuariosCollection = pais.getUsuariosCollection();
            for (Usuarios usuariosCollectionUsuarios : usuariosCollection) {
                usuariosCollectionUsuarios.setCodpais(null);
                usuariosCollectionUsuarios = em.merge(usuariosCollectionUsuarios);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
