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
import Mundo.Equipo;
import Mundo.Pais;
import Mundo.Anotacionxpartido;
import java.util.ArrayList;
import java.util.Collection;
import Mundo.Alineaciones;
import Mundo.Jugador;
import Mundo.Tarjetaspartido;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class JugadorJpaController implements Serializable {

    public JugadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jugador jugador) throws PreexistingEntityException, Exception {
        if (jugador.getAnotacionxpartidoCollection() == null) {
            jugador.setAnotacionxpartidoCollection(new ArrayList<Anotacionxpartido>());
        }
        if (jugador.getAlineacionesCollection() == null) {
            jugador.setAlineacionesCollection(new ArrayList<Alineaciones>());
        }
        if (jugador.getTarjetaspartidoCollection() == null) {
            jugador.setTarjetaspartidoCollection(new ArrayList<Tarjetaspartido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo nombreeq = jugador.getNombreeq();
            if (nombreeq != null) {
                nombreeq = em.getReference(nombreeq.getClass(), nombreeq.getNombreeq());
                jugador.setNombreeq(nombreeq);
            }
            Pais codpais = jugador.getCodpais();
            if (codpais != null) {
                codpais = em.getReference(codpais.getClass(), codpais.getCodpais());
                jugador.setCodpais(codpais);
            }
            Collection<Anotacionxpartido> attachedAnotacionxpartidoCollection = new ArrayList<Anotacionxpartido>();
            for (Anotacionxpartido anotacionxpartidoCollectionAnotacionxpartidoToAttach : jugador.getAnotacionxpartidoCollection()) {
                anotacionxpartidoCollectionAnotacionxpartidoToAttach = em.getReference(anotacionxpartidoCollectionAnotacionxpartidoToAttach.getClass(), anotacionxpartidoCollectionAnotacionxpartidoToAttach.getAnotacionxpartidoPK());
                attachedAnotacionxpartidoCollection.add(anotacionxpartidoCollectionAnotacionxpartidoToAttach);
            }
            jugador.setAnotacionxpartidoCollection(attachedAnotacionxpartidoCollection);
            Collection<Alineaciones> attachedAlineacionesCollection = new ArrayList<Alineaciones>();
            for (Alineaciones alineacionesCollectionAlineacionesToAttach : jugador.getAlineacionesCollection()) {
                alineacionesCollectionAlineacionesToAttach = em.getReference(alineacionesCollectionAlineacionesToAttach.getClass(), alineacionesCollectionAlineacionesToAttach.getAlineacionesPK());
                attachedAlineacionesCollection.add(alineacionesCollectionAlineacionesToAttach);
            }
            jugador.setAlineacionesCollection(attachedAlineacionesCollection);
            Collection<Tarjetaspartido> attachedTarjetaspartidoCollection = new ArrayList<Tarjetaspartido>();
            for (Tarjetaspartido tarjetaspartidoCollectionTarjetaspartidoToAttach : jugador.getTarjetaspartidoCollection()) {
                tarjetaspartidoCollectionTarjetaspartidoToAttach = em.getReference(tarjetaspartidoCollectionTarjetaspartidoToAttach.getClass(), tarjetaspartidoCollectionTarjetaspartidoToAttach.getTarjetaspartidoPK());
                attachedTarjetaspartidoCollection.add(tarjetaspartidoCollectionTarjetaspartidoToAttach);
            }
            jugador.setTarjetaspartidoCollection(attachedTarjetaspartidoCollection);
            em.persist(jugador);
            if (nombreeq != null) {
                nombreeq.getJugadorCollection().add(jugador);
                nombreeq = em.merge(nombreeq);
            }
            if (codpais != null) {
                codpais.getJugadorCollection().add(jugador);
                codpais = em.merge(codpais);
            }
            for (Anotacionxpartido anotacionxpartidoCollectionAnotacionxpartido : jugador.getAnotacionxpartidoCollection()) {
                Jugador oldJugadorOfAnotacionxpartidoCollectionAnotacionxpartido = anotacionxpartidoCollectionAnotacionxpartido.getJugador();
                anotacionxpartidoCollectionAnotacionxpartido.setJugador(jugador);
                anotacionxpartidoCollectionAnotacionxpartido = em.merge(anotacionxpartidoCollectionAnotacionxpartido);
                if (oldJugadorOfAnotacionxpartidoCollectionAnotacionxpartido != null) {
                    oldJugadorOfAnotacionxpartidoCollectionAnotacionxpartido.getAnotacionxpartidoCollection().remove(anotacionxpartidoCollectionAnotacionxpartido);
                    oldJugadorOfAnotacionxpartidoCollectionAnotacionxpartido = em.merge(oldJugadorOfAnotacionxpartidoCollectionAnotacionxpartido);
                }
            }
            for (Alineaciones alineacionesCollectionAlineaciones : jugador.getAlineacionesCollection()) {
                Jugador oldJugadorOfAlineacionesCollectionAlineaciones = alineacionesCollectionAlineaciones.getJugador();
                alineacionesCollectionAlineaciones.setJugador(jugador);
                alineacionesCollectionAlineaciones = em.merge(alineacionesCollectionAlineaciones);
                if (oldJugadorOfAlineacionesCollectionAlineaciones != null) {
                    oldJugadorOfAlineacionesCollectionAlineaciones.getAlineacionesCollection().remove(alineacionesCollectionAlineaciones);
                    oldJugadorOfAlineacionesCollectionAlineaciones = em.merge(oldJugadorOfAlineacionesCollectionAlineaciones);
                }
            }
            for (Tarjetaspartido tarjetaspartidoCollectionTarjetaspartido : jugador.getTarjetaspartidoCollection()) {
                Jugador oldJugadorOfTarjetaspartidoCollectionTarjetaspartido = tarjetaspartidoCollectionTarjetaspartido.getJugador();
                tarjetaspartidoCollectionTarjetaspartido.setJugador(jugador);
                tarjetaspartidoCollectionTarjetaspartido = em.merge(tarjetaspartidoCollectionTarjetaspartido);
                if (oldJugadorOfTarjetaspartidoCollectionTarjetaspartido != null) {
                    oldJugadorOfTarjetaspartidoCollectionTarjetaspartido.getTarjetaspartidoCollection().remove(tarjetaspartidoCollectionTarjetaspartido);
                    oldJugadorOfTarjetaspartidoCollectionTarjetaspartido = em.merge(oldJugadorOfTarjetaspartidoCollectionTarjetaspartido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJugador(jugador.getCodigoju()) != null) {
                throw new PreexistingEntityException("Jugador " + jugador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jugador jugador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador persistentJugador = em.find(Jugador.class, jugador.getCodigoju());
            Equipo nombreeqOld = persistentJugador.getNombreeq();
            Equipo nombreeqNew = jugador.getNombreeq();
            Pais codpaisOld = persistentJugador.getCodpais();
            Pais codpaisNew = jugador.getCodpais();
            Collection<Anotacionxpartido> anotacionxpartidoCollectionOld = persistentJugador.getAnotacionxpartidoCollection();
            Collection<Anotacionxpartido> anotacionxpartidoCollectionNew = jugador.getAnotacionxpartidoCollection();
            Collection<Alineaciones> alineacionesCollectionOld = persistentJugador.getAlineacionesCollection();
            Collection<Alineaciones> alineacionesCollectionNew = jugador.getAlineacionesCollection();
            Collection<Tarjetaspartido> tarjetaspartidoCollectionOld = persistentJugador.getTarjetaspartidoCollection();
            Collection<Tarjetaspartido> tarjetaspartidoCollectionNew = jugador.getTarjetaspartidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Anotacionxpartido anotacionxpartidoCollectionOldAnotacionxpartido : anotacionxpartidoCollectionOld) {
                if (!anotacionxpartidoCollectionNew.contains(anotacionxpartidoCollectionOldAnotacionxpartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Anotacionxpartido " + anotacionxpartidoCollectionOldAnotacionxpartido + " since its jugador field is not nullable.");
                }
            }
            for (Alineaciones alineacionesCollectionOldAlineaciones : alineacionesCollectionOld) {
                if (!alineacionesCollectionNew.contains(alineacionesCollectionOldAlineaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alineaciones " + alineacionesCollectionOldAlineaciones + " since its jugador field is not nullable.");
                }
            }
            for (Tarjetaspartido tarjetaspartidoCollectionOldTarjetaspartido : tarjetaspartidoCollectionOld) {
                if (!tarjetaspartidoCollectionNew.contains(tarjetaspartidoCollectionOldTarjetaspartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjetaspartido " + tarjetaspartidoCollectionOldTarjetaspartido + " since its jugador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nombreeqNew != null) {
                nombreeqNew = em.getReference(nombreeqNew.getClass(), nombreeqNew.getNombreeq());
                jugador.setNombreeq(nombreeqNew);
            }
            if (codpaisNew != null) {
                codpaisNew = em.getReference(codpaisNew.getClass(), codpaisNew.getCodpais());
                jugador.setCodpais(codpaisNew);
            }
            Collection<Anotacionxpartido> attachedAnotacionxpartidoCollectionNew = new ArrayList<Anotacionxpartido>();
            for (Anotacionxpartido anotacionxpartidoCollectionNewAnotacionxpartidoToAttach : anotacionxpartidoCollectionNew) {
                anotacionxpartidoCollectionNewAnotacionxpartidoToAttach = em.getReference(anotacionxpartidoCollectionNewAnotacionxpartidoToAttach.getClass(), anotacionxpartidoCollectionNewAnotacionxpartidoToAttach.getAnotacionxpartidoPK());
                attachedAnotacionxpartidoCollectionNew.add(anotacionxpartidoCollectionNewAnotacionxpartidoToAttach);
            }
            anotacionxpartidoCollectionNew = attachedAnotacionxpartidoCollectionNew;
            jugador.setAnotacionxpartidoCollection(anotacionxpartidoCollectionNew);
            Collection<Alineaciones> attachedAlineacionesCollectionNew = new ArrayList<Alineaciones>();
            for (Alineaciones alineacionesCollectionNewAlineacionesToAttach : alineacionesCollectionNew) {
                alineacionesCollectionNewAlineacionesToAttach = em.getReference(alineacionesCollectionNewAlineacionesToAttach.getClass(), alineacionesCollectionNewAlineacionesToAttach.getAlineacionesPK());
                attachedAlineacionesCollectionNew.add(alineacionesCollectionNewAlineacionesToAttach);
            }
            alineacionesCollectionNew = attachedAlineacionesCollectionNew;
            jugador.setAlineacionesCollection(alineacionesCollectionNew);
            Collection<Tarjetaspartido> attachedTarjetaspartidoCollectionNew = new ArrayList<Tarjetaspartido>();
            for (Tarjetaspartido tarjetaspartidoCollectionNewTarjetaspartidoToAttach : tarjetaspartidoCollectionNew) {
                tarjetaspartidoCollectionNewTarjetaspartidoToAttach = em.getReference(tarjetaspartidoCollectionNewTarjetaspartidoToAttach.getClass(), tarjetaspartidoCollectionNewTarjetaspartidoToAttach.getTarjetaspartidoPK());
                attachedTarjetaspartidoCollectionNew.add(tarjetaspartidoCollectionNewTarjetaspartidoToAttach);
            }
            tarjetaspartidoCollectionNew = attachedTarjetaspartidoCollectionNew;
            jugador.setTarjetaspartidoCollection(tarjetaspartidoCollectionNew);
            jugador = em.merge(jugador);
            if (nombreeqOld != null && !nombreeqOld.equals(nombreeqNew)) {
                nombreeqOld.getJugadorCollection().remove(jugador);
                nombreeqOld = em.merge(nombreeqOld);
            }
            if (nombreeqNew != null && !nombreeqNew.equals(nombreeqOld)) {
                nombreeqNew.getJugadorCollection().add(jugador);
                nombreeqNew = em.merge(nombreeqNew);
            }
            if (codpaisOld != null && !codpaisOld.equals(codpaisNew)) {
                codpaisOld.getJugadorCollection().remove(jugador);
                codpaisOld = em.merge(codpaisOld);
            }
            if (codpaisNew != null && !codpaisNew.equals(codpaisOld)) {
                codpaisNew.getJugadorCollection().add(jugador);
                codpaisNew = em.merge(codpaisNew);
            }
            for (Anotacionxpartido anotacionxpartidoCollectionNewAnotacionxpartido : anotacionxpartidoCollectionNew) {
                if (!anotacionxpartidoCollectionOld.contains(anotacionxpartidoCollectionNewAnotacionxpartido)) {
                    Jugador oldJugadorOfAnotacionxpartidoCollectionNewAnotacionxpartido = anotacionxpartidoCollectionNewAnotacionxpartido.getJugador();
                    anotacionxpartidoCollectionNewAnotacionxpartido.setJugador(jugador);
                    anotacionxpartidoCollectionNewAnotacionxpartido = em.merge(anotacionxpartidoCollectionNewAnotacionxpartido);
                    if (oldJugadorOfAnotacionxpartidoCollectionNewAnotacionxpartido != null && !oldJugadorOfAnotacionxpartidoCollectionNewAnotacionxpartido.equals(jugador)) {
                        oldJugadorOfAnotacionxpartidoCollectionNewAnotacionxpartido.getAnotacionxpartidoCollection().remove(anotacionxpartidoCollectionNewAnotacionxpartido);
                        oldJugadorOfAnotacionxpartidoCollectionNewAnotacionxpartido = em.merge(oldJugadorOfAnotacionxpartidoCollectionNewAnotacionxpartido);
                    }
                }
            }
            for (Alineaciones alineacionesCollectionNewAlineaciones : alineacionesCollectionNew) {
                if (!alineacionesCollectionOld.contains(alineacionesCollectionNewAlineaciones)) {
                    Jugador oldJugadorOfAlineacionesCollectionNewAlineaciones = alineacionesCollectionNewAlineaciones.getJugador();
                    alineacionesCollectionNewAlineaciones.setJugador(jugador);
                    alineacionesCollectionNewAlineaciones = em.merge(alineacionesCollectionNewAlineaciones);
                    if (oldJugadorOfAlineacionesCollectionNewAlineaciones != null && !oldJugadorOfAlineacionesCollectionNewAlineaciones.equals(jugador)) {
                        oldJugadorOfAlineacionesCollectionNewAlineaciones.getAlineacionesCollection().remove(alineacionesCollectionNewAlineaciones);
                        oldJugadorOfAlineacionesCollectionNewAlineaciones = em.merge(oldJugadorOfAlineacionesCollectionNewAlineaciones);
                    }
                }
            }
            for (Tarjetaspartido tarjetaspartidoCollectionNewTarjetaspartido : tarjetaspartidoCollectionNew) {
                if (!tarjetaspartidoCollectionOld.contains(tarjetaspartidoCollectionNewTarjetaspartido)) {
                    Jugador oldJugadorOfTarjetaspartidoCollectionNewTarjetaspartido = tarjetaspartidoCollectionNewTarjetaspartido.getJugador();
                    tarjetaspartidoCollectionNewTarjetaspartido.setJugador(jugador);
                    tarjetaspartidoCollectionNewTarjetaspartido = em.merge(tarjetaspartidoCollectionNewTarjetaspartido);
                    if (oldJugadorOfTarjetaspartidoCollectionNewTarjetaspartido != null && !oldJugadorOfTarjetaspartidoCollectionNewTarjetaspartido.equals(jugador)) {
                        oldJugadorOfTarjetaspartidoCollectionNewTarjetaspartido.getTarjetaspartidoCollection().remove(tarjetaspartidoCollectionNewTarjetaspartido);
                        oldJugadorOfTarjetaspartidoCollectionNewTarjetaspartido = em.merge(oldJugadorOfTarjetaspartidoCollectionNewTarjetaspartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = jugador.getCodigoju();
                if (findJugador(id) == null) {
                    throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador;
            try {
                jugador = em.getReference(Jugador.class, id);
                jugador.getCodigoju();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Anotacionxpartido> anotacionxpartidoCollectionOrphanCheck = jugador.getAnotacionxpartidoCollection();
            for (Anotacionxpartido anotacionxpartidoCollectionOrphanCheckAnotacionxpartido : anotacionxpartidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Anotacionxpartido " + anotacionxpartidoCollectionOrphanCheckAnotacionxpartido + " in its anotacionxpartidoCollection field has a non-nullable jugador field.");
            }
            Collection<Alineaciones> alineacionesCollectionOrphanCheck = jugador.getAlineacionesCollection();
            for (Alineaciones alineacionesCollectionOrphanCheckAlineaciones : alineacionesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Alineaciones " + alineacionesCollectionOrphanCheckAlineaciones + " in its alineacionesCollection field has a non-nullable jugador field.");
            }
            Collection<Tarjetaspartido> tarjetaspartidoCollectionOrphanCheck = jugador.getTarjetaspartidoCollection();
            for (Tarjetaspartido tarjetaspartidoCollectionOrphanCheckTarjetaspartido : tarjetaspartidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Tarjetaspartido " + tarjetaspartidoCollectionOrphanCheckTarjetaspartido + " in its tarjetaspartidoCollection field has a non-nullable jugador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Equipo nombreeq = jugador.getNombreeq();
            if (nombreeq != null) {
                nombreeq.getJugadorCollection().remove(jugador);
                nombreeq = em.merge(nombreeq);
            }
            Pais codpais = jugador.getCodpais();
            if (codpais != null) {
                codpais.getJugadorCollection().remove(jugador);
                codpais = em.merge(codpais);
            }
            em.remove(jugador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jugador> findJugadorEntities() {
        return findJugadorEntities(true, -1, -1);
    }

    public List<Jugador> findJugadorEntities(int maxResults, int firstResult) {
        return findJugadorEntities(false, maxResults, firstResult);
    }

    private List<Jugador> findJugadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jugador.class));
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

    public Jugador encontrarJugadorN(String j)
    {
        Query q;
        q = getEntityManager().createNamedQuery("Jugador.findByNombreju");
        q.setParameter("nombreju", j);
        Jugador r= (Jugador) q.getSingleResult();
        return r;
    }
    
    public Jugador findJugador(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jugador.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jugador> rt = cq.from(Jugador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    List<Jugador> obtenerJugadorE(String nombreeq) {
        Query q;
        q = getEntityManager().createNamedQuery("Jugador.findByE");
        q.setParameter("no", nombreeq);
        List<Jugador> r = q.getResultList();
        return r;
    }
    
     Jugador jugadorxCodig(int cod){
        Query q;
        q= getEntityManager().createNamedQuery("Jugador.findByCodigoju");
        q.setParameter("codigoju", BigInteger.valueOf(cod));
        Jugador j= (Jugador) q.getSingleResult();
        return j;
    }

}
