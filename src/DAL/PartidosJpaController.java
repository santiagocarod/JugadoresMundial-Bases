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
import Mundo.Estadio;
import Mundo.Fase;
import Mundo.Anotacionxpartido;
import java.util.ArrayList;
import java.util.Collection;
import Mundo.PartidosJueces;
import Mundo.Boleteria;
import Mundo.Alineaciones;
import Mundo.Partidos;
import static Mundo.Partidos_.fase;
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
public class PartidosJpaController implements Serializable {

    public PartidosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Partidos partidos) throws PreexistingEntityException, Exception {
        if (partidos.getAnotacionxpartidoCollection() == null) {
            partidos.setAnotacionxpartidoCollection(new ArrayList<Anotacionxpartido>());
        }
        if (partidos.getPartidosJuecesCollection() == null) {
            partidos.setPartidosJuecesCollection(new ArrayList<PartidosJueces>());
        }
        if (partidos.getBoleteriaCollection() == null) {
            partidos.setBoleteriaCollection(new ArrayList<Boleteria>());
        }
        if (partidos.getAlineacionesCollection() == null) {
            partidos.setAlineacionesCollection(new ArrayList<Alineaciones>());
        }
        if (partidos.getTarjetaspartidoCollection() == null) {
            partidos.setTarjetaspartidoCollection(new ArrayList<Tarjetaspartido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo local = partidos.getLocal();
            if (local != null) {
                local = em.getReference(local.getClass(), local.getNombreeq());
                partidos.setLocal(local);
            }
            Equipo visitante = partidos.getVisitante();
            if (visitante != null) {
                visitante = em.getReference(visitante.getClass(), visitante.getNombreeq());
                partidos.setVisitante(visitante);
            }
            Estadio codestadio = partidos.getCodestadio();
            if (codestadio != null) {
                codestadio = em.getReference(codestadio.getClass(), codestadio.getCodestadio());
                partidos.setCodestadio(codestadio);
            }
            Fase fase = partidos.getFase();
            if (fase != null) {
                fase = em.getReference(fase.getClass(), fase.getFase());
                partidos.setFase(fase);
            }
            Collection<Anotacionxpartido> attachedAnotacionxpartidoCollection = new ArrayList<Anotacionxpartido>();
            for (Anotacionxpartido anotacionxpartidoCollectionAnotacionxpartidoToAttach : partidos.getAnotacionxpartidoCollection()) {
                anotacionxpartidoCollectionAnotacionxpartidoToAttach = em.getReference(anotacionxpartidoCollectionAnotacionxpartidoToAttach.getClass(), anotacionxpartidoCollectionAnotacionxpartidoToAttach.getAnotacionxpartidoPK());
                attachedAnotacionxpartidoCollection.add(anotacionxpartidoCollectionAnotacionxpartidoToAttach);
            }
            partidos.setAnotacionxpartidoCollection(attachedAnotacionxpartidoCollection);
            Collection<PartidosJueces> attachedPartidosJuecesCollection = new ArrayList<PartidosJueces>();
            for (PartidosJueces partidosJuecesCollectionPartidosJuecesToAttach : partidos.getPartidosJuecesCollection()) {
                partidosJuecesCollectionPartidosJuecesToAttach = em.getReference(partidosJuecesCollectionPartidosJuecesToAttach.getClass(), partidosJuecesCollectionPartidosJuecesToAttach.getPartidosJuecesPK());
                attachedPartidosJuecesCollection.add(partidosJuecesCollectionPartidosJuecesToAttach);
            }
            partidos.setPartidosJuecesCollection(attachedPartidosJuecesCollection);
            List<Boleteria> attachedBoleteriaCollection = new ArrayList<Boleteria>();
            for (Boleteria boleteriaCollectionBoleteriaToAttach : partidos.getBoleteriaCollection()) {
                boleteriaCollectionBoleteriaToAttach = em.getReference(boleteriaCollectionBoleteriaToAttach.getClass(), boleteriaCollectionBoleteriaToAttach.getCodigoboleta());
                attachedBoleteriaCollection.add(boleteriaCollectionBoleteriaToAttach);
            }
            partidos.setBoleteriaCollection(attachedBoleteriaCollection);
            Collection<Alineaciones> attachedAlineacionesCollection = new ArrayList<Alineaciones>();
            for (Alineaciones alineacionesCollectionAlineacionesToAttach : partidos.getAlineacionesCollection()) {
                alineacionesCollectionAlineacionesToAttach = em.getReference(alineacionesCollectionAlineacionesToAttach.getClass(), alineacionesCollectionAlineacionesToAttach.getAlineacionesPK());
                attachedAlineacionesCollection.add(alineacionesCollectionAlineacionesToAttach);
            }
            partidos.setAlineacionesCollection(attachedAlineacionesCollection);
            Collection<Tarjetaspartido> attachedTarjetaspartidoCollection = new ArrayList<Tarjetaspartido>();
            for (Tarjetaspartido tarjetaspartidoCollectionTarjetaspartidoToAttach : partidos.getTarjetaspartidoCollection()) {
                tarjetaspartidoCollectionTarjetaspartidoToAttach = em.getReference(tarjetaspartidoCollectionTarjetaspartidoToAttach.getClass(), tarjetaspartidoCollectionTarjetaspartidoToAttach.getTarjetaspartidoPK());
                attachedTarjetaspartidoCollection.add(tarjetaspartidoCollectionTarjetaspartidoToAttach);
            }
            partidos.setTarjetaspartidoCollection(attachedTarjetaspartidoCollection);
            em.persist(partidos);
            if (local != null) {
                local.getPartidosCollection().add(partidos);
                local = em.merge(local);
            }
            if (visitante != null) {
                visitante.getPartidosCollection().add(partidos);
                visitante = em.merge(visitante);
            }
            if (codestadio != null) {
                codestadio.getPartidosCollection().add(partidos);
                codestadio = em.merge(codestadio);
            }
            if (fase != null) {
                fase.getPartidosCollection().add(partidos);
                fase = em.merge(fase);
            }
            for (Anotacionxpartido anotacionxpartidoCollectionAnotacionxpartido : partidos.getAnotacionxpartidoCollection()) {
                Partidos oldPartidosOfAnotacionxpartidoCollectionAnotacionxpartido = anotacionxpartidoCollectionAnotacionxpartido.getPartidos();
                anotacionxpartidoCollectionAnotacionxpartido.setPartidos(partidos);
                anotacionxpartidoCollectionAnotacionxpartido = em.merge(anotacionxpartidoCollectionAnotacionxpartido);
                if (oldPartidosOfAnotacionxpartidoCollectionAnotacionxpartido != null) {
                    oldPartidosOfAnotacionxpartidoCollectionAnotacionxpartido.getAnotacionxpartidoCollection().remove(anotacionxpartidoCollectionAnotacionxpartido);
                    oldPartidosOfAnotacionxpartidoCollectionAnotacionxpartido = em.merge(oldPartidosOfAnotacionxpartidoCollectionAnotacionxpartido);
                }
            }
            for (PartidosJueces partidosJuecesCollectionPartidosJueces : partidos.getPartidosJuecesCollection()) {
                Partidos oldPartidosOfPartidosJuecesCollectionPartidosJueces = partidosJuecesCollectionPartidosJueces.getPartidos();
                partidosJuecesCollectionPartidosJueces.setPartidos(partidos);
                partidosJuecesCollectionPartidosJueces = em.merge(partidosJuecesCollectionPartidosJueces);
                if (oldPartidosOfPartidosJuecesCollectionPartidosJueces != null) {
                    oldPartidosOfPartidosJuecesCollectionPartidosJueces.getPartidosJuecesCollection().remove(partidosJuecesCollectionPartidosJueces);
                    oldPartidosOfPartidosJuecesCollectionPartidosJueces = em.merge(oldPartidosOfPartidosJuecesCollectionPartidosJueces);
                }
            }
            for (Boleteria boleteriaCollectionBoleteria : partidos.getBoleteriaCollection()) {
                Partidos oldCodigopartidoOfBoleteriaCollectionBoleteria = boleteriaCollectionBoleteria.getCodigopartido();
                boleteriaCollectionBoleteria.setCodigopartido(partidos);
                boleteriaCollectionBoleteria = em.merge(boleteriaCollectionBoleteria);
                if (oldCodigopartidoOfBoleteriaCollectionBoleteria != null) {
                    oldCodigopartidoOfBoleteriaCollectionBoleteria.getBoleteriaCollection().remove(boleteriaCollectionBoleteria);
                    oldCodigopartidoOfBoleteriaCollectionBoleteria = em.merge(oldCodigopartidoOfBoleteriaCollectionBoleteria);
                }
            }
            for (Alineaciones alineacionesCollectionAlineaciones : partidos.getAlineacionesCollection()) {
                Partidos oldPartidosOfAlineacionesCollectionAlineaciones = alineacionesCollectionAlineaciones.getPartidos();
                alineacionesCollectionAlineaciones.setPartidos(partidos);
                alineacionesCollectionAlineaciones = em.merge(alineacionesCollectionAlineaciones);
                if (oldPartidosOfAlineacionesCollectionAlineaciones != null) {
                    oldPartidosOfAlineacionesCollectionAlineaciones.getAlineacionesCollection().remove(alineacionesCollectionAlineaciones);
                    oldPartidosOfAlineacionesCollectionAlineaciones = em.merge(oldPartidosOfAlineacionesCollectionAlineaciones);
                }
            }
            for (Tarjetaspartido tarjetaspartidoCollectionTarjetaspartido : partidos.getTarjetaspartidoCollection()) {
                Partidos oldPartidosOfTarjetaspartidoCollectionTarjetaspartido = tarjetaspartidoCollectionTarjetaspartido.getPartidos();
                tarjetaspartidoCollectionTarjetaspartido.setPartidos(partidos);
                tarjetaspartidoCollectionTarjetaspartido = em.merge(tarjetaspartidoCollectionTarjetaspartido);
                if (oldPartidosOfTarjetaspartidoCollectionTarjetaspartido != null) {
                    oldPartidosOfTarjetaspartidoCollectionTarjetaspartido.getTarjetaspartidoCollection().remove(tarjetaspartidoCollectionTarjetaspartido);
                    oldPartidosOfTarjetaspartidoCollectionTarjetaspartido = em.merge(oldPartidosOfTarjetaspartidoCollectionTarjetaspartido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPartidos(partidos.getCodigopartido()) != null) {
                throw new PreexistingEntityException("Partidos " + partidos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Partidos partidos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partidos persistentPartidos = em.find(Partidos.class, partidos.getCodigopartido());
            Equipo localOld = persistentPartidos.getLocal();
            Equipo localNew = partidos.getLocal();
            Equipo visitanteOld = persistentPartidos.getVisitante();
            Equipo visitanteNew = partidos.getVisitante();
            Estadio codestadioOld = persistentPartidos.getCodestadio();
            Estadio codestadioNew = partidos.getCodestadio();
            Fase faseOld = persistentPartidos.getFase();
            Fase faseNew = partidos.getFase();
            Collection<Anotacionxpartido> anotacionxpartidoCollectionOld = persistentPartidos.getAnotacionxpartidoCollection();
            Collection<Anotacionxpartido> anotacionxpartidoCollectionNew = partidos.getAnotacionxpartidoCollection();
            Collection<PartidosJueces> partidosJuecesCollectionOld = persistentPartidos.getPartidosJuecesCollection();
            Collection<PartidosJueces> partidosJuecesCollectionNew = partidos.getPartidosJuecesCollection();
            Collection<Boleteria> boleteriaCollectionOld = persistentPartidos.getBoleteriaCollection();
            List<Boleteria> boleteriaCollectionNew = partidos.getBoleteriaCollection();
            Collection<Alineaciones> alineacionesCollectionOld = persistentPartidos.getAlineacionesCollection();
            Collection<Alineaciones> alineacionesCollectionNew = partidos.getAlineacionesCollection();
            Collection<Tarjetaspartido> tarjetaspartidoCollectionOld = persistentPartidos.getTarjetaspartidoCollection();
            Collection<Tarjetaspartido> tarjetaspartidoCollectionNew = partidos.getTarjetaspartidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Anotacionxpartido anotacionxpartidoCollectionOldAnotacionxpartido : anotacionxpartidoCollectionOld) {
                if (!anotacionxpartidoCollectionNew.contains(anotacionxpartidoCollectionOldAnotacionxpartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Anotacionxpartido " + anotacionxpartidoCollectionOldAnotacionxpartido + " since its partidos field is not nullable.");
                }
            }
            for (PartidosJueces partidosJuecesCollectionOldPartidosJueces : partidosJuecesCollectionOld) {
                if (!partidosJuecesCollectionNew.contains(partidosJuecesCollectionOldPartidosJueces)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PartidosJueces " + partidosJuecesCollectionOldPartidosJueces + " since its partidos field is not nullable.");
                }
            }
            for (Boleteria boleteriaCollectionOldBoleteria : boleteriaCollectionOld) {
                if (!boleteriaCollectionNew.contains(boleteriaCollectionOldBoleteria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boleteria " + boleteriaCollectionOldBoleteria + " since its codigopartido field is not nullable.");
                }
            }
            for (Alineaciones alineacionesCollectionOldAlineaciones : alineacionesCollectionOld) {
                if (!alineacionesCollectionNew.contains(alineacionesCollectionOldAlineaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alineaciones " + alineacionesCollectionOldAlineaciones + " since its partidos field is not nullable.");
                }
            }
            for (Tarjetaspartido tarjetaspartidoCollectionOldTarjetaspartido : tarjetaspartidoCollectionOld) {
                if (!tarjetaspartidoCollectionNew.contains(tarjetaspartidoCollectionOldTarjetaspartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjetaspartido " + tarjetaspartidoCollectionOldTarjetaspartido + " since its partidos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (localNew != null) {
                localNew = em.getReference(localNew.getClass(), localNew.getNombreeq());
                partidos.setLocal(localNew);
            }
            if (visitanteNew != null) {
                visitanteNew = em.getReference(visitanteNew.getClass(), visitanteNew.getNombreeq());
                partidos.setVisitante(visitanteNew);
            }
            if (codestadioNew != null) {
                codestadioNew = em.getReference(codestadioNew.getClass(), codestadioNew.getCodestadio());
                partidos.setCodestadio(codestadioNew);
            }
            if (faseNew != null) {
                faseNew = em.getReference(faseNew.getClass(), faseNew.getFase());
                partidos.setFase(faseNew);
            }
            Collection<Anotacionxpartido> attachedAnotacionxpartidoCollectionNew = new ArrayList<Anotacionxpartido>();
            for (Anotacionxpartido anotacionxpartidoCollectionNewAnotacionxpartidoToAttach : anotacionxpartidoCollectionNew) {
                anotacionxpartidoCollectionNewAnotacionxpartidoToAttach = em.getReference(anotacionxpartidoCollectionNewAnotacionxpartidoToAttach.getClass(), anotacionxpartidoCollectionNewAnotacionxpartidoToAttach.getAnotacionxpartidoPK());
                attachedAnotacionxpartidoCollectionNew.add(anotacionxpartidoCollectionNewAnotacionxpartidoToAttach);
            }
            anotacionxpartidoCollectionNew = attachedAnotacionxpartidoCollectionNew;
            partidos.setAnotacionxpartidoCollection(anotacionxpartidoCollectionNew);
            Collection<PartidosJueces> attachedPartidosJuecesCollectionNew = new ArrayList<PartidosJueces>();
            for (PartidosJueces partidosJuecesCollectionNewPartidosJuecesToAttach : partidosJuecesCollectionNew) {
                partidosJuecesCollectionNewPartidosJuecesToAttach = em.getReference(partidosJuecesCollectionNewPartidosJuecesToAttach.getClass(), partidosJuecesCollectionNewPartidosJuecesToAttach.getPartidosJuecesPK());
                attachedPartidosJuecesCollectionNew.add(partidosJuecesCollectionNewPartidosJuecesToAttach);
            }
            partidosJuecesCollectionNew = attachedPartidosJuecesCollectionNew;
            partidos.setPartidosJuecesCollection(partidosJuecesCollectionNew);
            List<Boleteria> attachedBoleteriaCollectionNew = new ArrayList<Boleteria>();
            for (Boleteria boleteriaCollectionNewBoleteriaToAttach : boleteriaCollectionNew) {
                boleteriaCollectionNewBoleteriaToAttach = em.getReference(boleteriaCollectionNewBoleteriaToAttach.getClass(), boleteriaCollectionNewBoleteriaToAttach.getCodigoboleta());
                attachedBoleteriaCollectionNew.add(boleteriaCollectionNewBoleteriaToAttach);
            }
            boleteriaCollectionNew = attachedBoleteriaCollectionNew;
            partidos.setBoleteriaCollection(boleteriaCollectionNew);
            Collection<Alineaciones> attachedAlineacionesCollectionNew = new ArrayList<Alineaciones>();
            for (Alineaciones alineacionesCollectionNewAlineacionesToAttach : alineacionesCollectionNew) {
                alineacionesCollectionNewAlineacionesToAttach = em.getReference(alineacionesCollectionNewAlineacionesToAttach.getClass(), alineacionesCollectionNewAlineacionesToAttach.getAlineacionesPK());
                attachedAlineacionesCollectionNew.add(alineacionesCollectionNewAlineacionesToAttach);
            }
            alineacionesCollectionNew = attachedAlineacionesCollectionNew;
            partidos.setAlineacionesCollection(alineacionesCollectionNew);
            Collection<Tarjetaspartido> attachedTarjetaspartidoCollectionNew = new ArrayList<Tarjetaspartido>();
            for (Tarjetaspartido tarjetaspartidoCollectionNewTarjetaspartidoToAttach : tarjetaspartidoCollectionNew) {
                tarjetaspartidoCollectionNewTarjetaspartidoToAttach = em.getReference(tarjetaspartidoCollectionNewTarjetaspartidoToAttach.getClass(), tarjetaspartidoCollectionNewTarjetaspartidoToAttach.getTarjetaspartidoPK());
                attachedTarjetaspartidoCollectionNew.add(tarjetaspartidoCollectionNewTarjetaspartidoToAttach);
            }
            tarjetaspartidoCollectionNew = attachedTarjetaspartidoCollectionNew;
            partidos.setTarjetaspartidoCollection(tarjetaspartidoCollectionNew);
            partidos = em.merge(partidos);
            if (localOld != null && !localOld.equals(localNew)) {
                localOld.getPartidosCollection().remove(partidos);
                localOld = em.merge(localOld);
            }
            if (localNew != null && !localNew.equals(localOld)) {
                localNew.getPartidosCollection().add(partidos);
                localNew = em.merge(localNew);
            }
            if (visitanteOld != null && !visitanteOld.equals(visitanteNew)) {
                visitanteOld.getPartidosCollection().remove(partidos);
                visitanteOld = em.merge(visitanteOld);
            }
            if (visitanteNew != null && !visitanteNew.equals(visitanteOld)) {
                visitanteNew.getPartidosCollection().add(partidos);
                visitanteNew = em.merge(visitanteNew);
            }
            if (codestadioOld != null && !codestadioOld.equals(codestadioNew)) {
                codestadioOld.getPartidosCollection().remove(partidos);
                codestadioOld = em.merge(codestadioOld);
            }
            if (codestadioNew != null && !codestadioNew.equals(codestadioOld)) {
                codestadioNew.getPartidosCollection().add(partidos);
                codestadioNew = em.merge(codestadioNew);
            }
            if (faseOld != null && !faseOld.equals(faseNew)) {
                faseOld.getPartidosCollection().remove(partidos);
                faseOld = em.merge(faseOld);
            }
            if (faseNew != null && !faseNew.equals(faseOld)) {
                faseNew.getPartidosCollection().add(partidos);
                faseNew = em.merge(faseNew);
            }
            for (Anotacionxpartido anotacionxpartidoCollectionNewAnotacionxpartido : anotacionxpartidoCollectionNew) {
                if (!anotacionxpartidoCollectionOld.contains(anotacionxpartidoCollectionNewAnotacionxpartido)) {
                    Partidos oldPartidosOfAnotacionxpartidoCollectionNewAnotacionxpartido = anotacionxpartidoCollectionNewAnotacionxpartido.getPartidos();
                    anotacionxpartidoCollectionNewAnotacionxpartido.setPartidos(partidos);
                    anotacionxpartidoCollectionNewAnotacionxpartido = em.merge(anotacionxpartidoCollectionNewAnotacionxpartido);
                    if (oldPartidosOfAnotacionxpartidoCollectionNewAnotacionxpartido != null && !oldPartidosOfAnotacionxpartidoCollectionNewAnotacionxpartido.equals(partidos)) {
                        oldPartidosOfAnotacionxpartidoCollectionNewAnotacionxpartido.getAnotacionxpartidoCollection().remove(anotacionxpartidoCollectionNewAnotacionxpartido);
                        oldPartidosOfAnotacionxpartidoCollectionNewAnotacionxpartido = em.merge(oldPartidosOfAnotacionxpartidoCollectionNewAnotacionxpartido);
                    }
                }
            }
            for (PartidosJueces partidosJuecesCollectionNewPartidosJueces : partidosJuecesCollectionNew) {
                if (!partidosJuecesCollectionOld.contains(partidosJuecesCollectionNewPartidosJueces)) {
                    Partidos oldPartidosOfPartidosJuecesCollectionNewPartidosJueces = partidosJuecesCollectionNewPartidosJueces.getPartidos();
                    partidosJuecesCollectionNewPartidosJueces.setPartidos(partidos);
                    partidosJuecesCollectionNewPartidosJueces = em.merge(partidosJuecesCollectionNewPartidosJueces);
                    if (oldPartidosOfPartidosJuecesCollectionNewPartidosJueces != null && !oldPartidosOfPartidosJuecesCollectionNewPartidosJueces.equals(partidos)) {
                        oldPartidosOfPartidosJuecesCollectionNewPartidosJueces.getPartidosJuecesCollection().remove(partidosJuecesCollectionNewPartidosJueces);
                        oldPartidosOfPartidosJuecesCollectionNewPartidosJueces = em.merge(oldPartidosOfPartidosJuecesCollectionNewPartidosJueces);
                    }
                }
            }
            for (Boleteria boleteriaCollectionNewBoleteria : boleteriaCollectionNew) {
                if (!boleteriaCollectionOld.contains(boleteriaCollectionNewBoleteria)) {
                    Partidos oldCodigopartidoOfBoleteriaCollectionNewBoleteria = boleteriaCollectionNewBoleteria.getCodigopartido();
                    boleteriaCollectionNewBoleteria.setCodigopartido(partidos);
                    boleteriaCollectionNewBoleteria = em.merge(boleteriaCollectionNewBoleteria);
                    if (oldCodigopartidoOfBoleteriaCollectionNewBoleteria != null && !oldCodigopartidoOfBoleteriaCollectionNewBoleteria.equals(partidos)) {
                        oldCodigopartidoOfBoleteriaCollectionNewBoleteria.getBoleteriaCollection().remove(boleteriaCollectionNewBoleteria);
                        oldCodigopartidoOfBoleteriaCollectionNewBoleteria = em.merge(oldCodigopartidoOfBoleteriaCollectionNewBoleteria);
                    }
                }
            }
            for (Alineaciones alineacionesCollectionNewAlineaciones : alineacionesCollectionNew) {
                if (!alineacionesCollectionOld.contains(alineacionesCollectionNewAlineaciones)) {
                    Partidos oldPartidosOfAlineacionesCollectionNewAlineaciones = alineacionesCollectionNewAlineaciones.getPartidos();
                    alineacionesCollectionNewAlineaciones.setPartidos(partidos);
                    alineacionesCollectionNewAlineaciones = em.merge(alineacionesCollectionNewAlineaciones);
                    if (oldPartidosOfAlineacionesCollectionNewAlineaciones != null && !oldPartidosOfAlineacionesCollectionNewAlineaciones.equals(partidos)) {
                        oldPartidosOfAlineacionesCollectionNewAlineaciones.getAlineacionesCollection().remove(alineacionesCollectionNewAlineaciones);
                        oldPartidosOfAlineacionesCollectionNewAlineaciones = em.merge(oldPartidosOfAlineacionesCollectionNewAlineaciones);
                    }
                }
            }
            for (Tarjetaspartido tarjetaspartidoCollectionNewTarjetaspartido : tarjetaspartidoCollectionNew) {
                if (!tarjetaspartidoCollectionOld.contains(tarjetaspartidoCollectionNewTarjetaspartido)) {
                    Partidos oldPartidosOfTarjetaspartidoCollectionNewTarjetaspartido = tarjetaspartidoCollectionNewTarjetaspartido.getPartidos();
                    tarjetaspartidoCollectionNewTarjetaspartido.setPartidos(partidos);
                    tarjetaspartidoCollectionNewTarjetaspartido = em.merge(tarjetaspartidoCollectionNewTarjetaspartido);
                    if (oldPartidosOfTarjetaspartidoCollectionNewTarjetaspartido != null && !oldPartidosOfTarjetaspartidoCollectionNewTarjetaspartido.equals(partidos)) {
                        oldPartidosOfTarjetaspartidoCollectionNewTarjetaspartido.getTarjetaspartidoCollection().remove(tarjetaspartidoCollectionNewTarjetaspartido);
                        oldPartidosOfTarjetaspartidoCollectionNewTarjetaspartido = em.merge(oldPartidosOfTarjetaspartidoCollectionNewTarjetaspartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = partidos.getCodigopartido();
                if (findPartidos(id) == null) {
                    throw new NonexistentEntityException("The partidos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigInteger id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partidos partidos;
            try {
                partidos = em.getReference(Partidos.class, id);
                partidos.getCodigopartido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The partidos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Anotacionxpartido> anotacionxpartidoCollectionOrphanCheck = partidos.getAnotacionxpartidoCollection();
            for (Anotacionxpartido anotacionxpartidoCollectionOrphanCheckAnotacionxpartido : anotacionxpartidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partidos (" + partidos + ") cannot be destroyed since the Anotacionxpartido " + anotacionxpartidoCollectionOrphanCheckAnotacionxpartido + " in its anotacionxpartidoCollection field has a non-nullable partidos field.");
            }
            Collection<PartidosJueces> partidosJuecesCollectionOrphanCheck = partidos.getPartidosJuecesCollection();
            for (PartidosJueces partidosJuecesCollectionOrphanCheckPartidosJueces : partidosJuecesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partidos (" + partidos + ") cannot be destroyed since the PartidosJueces " + partidosJuecesCollectionOrphanCheckPartidosJueces + " in its partidosJuecesCollection field has a non-nullable partidos field.");
            }
            Collection<Boleteria> boleteriaCollectionOrphanCheck = partidos.getBoleteriaCollection();
            for (Boleteria boleteriaCollectionOrphanCheckBoleteria : boleteriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partidos (" + partidos + ") cannot be destroyed since the Boleteria " + boleteriaCollectionOrphanCheckBoleteria + " in its boleteriaCollection field has a non-nullable codigopartido field.");
            }
            Collection<Alineaciones> alineacionesCollectionOrphanCheck = partidos.getAlineacionesCollection();
            for (Alineaciones alineacionesCollectionOrphanCheckAlineaciones : alineacionesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partidos (" + partidos + ") cannot be destroyed since the Alineaciones " + alineacionesCollectionOrphanCheckAlineaciones + " in its alineacionesCollection field has a non-nullable partidos field.");
            }
            Collection<Tarjetaspartido> tarjetaspartidoCollectionOrphanCheck = partidos.getTarjetaspartidoCollection();
            for (Tarjetaspartido tarjetaspartidoCollectionOrphanCheckTarjetaspartido : tarjetaspartidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partidos (" + partidos + ") cannot be destroyed since the Tarjetaspartido " + tarjetaspartidoCollectionOrphanCheckTarjetaspartido + " in its tarjetaspartidoCollection field has a non-nullable partidos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Equipo local = partidos.getLocal();
            if (local != null) {
                local.getPartidosCollection().remove(partidos);
                local = em.merge(local);
            }
            Equipo visitante = partidos.getVisitante();
            if (visitante != null) {
                visitante.getPartidosCollection().remove(partidos);
                visitante = em.merge(visitante);
            }
            Estadio codestadio = partidos.getCodestadio();
            if (codestadio != null) {
                codestadio.getPartidosCollection().remove(partidos);
                codestadio = em.merge(codestadio);
            }
            Fase fase = partidos.getFase();
            if (fase != null) {
                fase.getPartidosCollection().remove(partidos);
                fase = em.merge(fase);
            }
            em.remove(partidos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Partidos> findPartidosEntities() {
        return findPartidosEntities(true, -1, -1);
    }

    public List<Partidos> findPartidosEntities(int maxResults, int firstResult) {
        return findPartidosEntities(false, maxResults, firstResult);
    }

    private List<Partidos> findPartidosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Partidos.class));
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

    public Partidos findPartidos(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Partidos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPartidosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Partidos> rt = cq.from(Partidos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Partidos> getPartidosFase(Fase f) {
        EntityManager em = getEntityManager();
        try {
            Query q= em.createNamedQuery("Partidos.findByFase");
            q.setParameter("fase", f);
            List<Partidos> parts=q.getResultList();
            return parts;
        } finally {
            em.close();
        }
    }
    
 

}
