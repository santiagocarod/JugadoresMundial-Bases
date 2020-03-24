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
import Mundo.Efectivo;
import Mundo.Usuarios;
import Mundo.Tarjetacredito;
import Mundo.Boleteria;
import Mundo.Pago;
import Mundo.PagoPK;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class PagoJpaController implements Serializable {

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) throws PreexistingEntityException, Exception {
        if (pago.getPagoPK() == null) {
            pago.setPagoPK(new PagoPK());
        }
        if (pago.getBoleteriaCollection() == null) {
            pago.setBoleteriaCollection(new ArrayList<Boleteria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Efectivo efectivo = pago.getEfectivo();
            if (efectivo != null) {
                efectivo = em.getReference(efectivo.getClass(), efectivo.getEfectivoPK());
                pago.setEfectivo(efectivo);
            }
            Usuarios usuariosId = pago.getUsuariosId();
            if (usuariosId != null) {
                usuariosId = em.getReference(usuariosId.getClass(), usuariosId.getId());
                pago.setUsuariosId(usuariosId);
            }
            Tarjetacredito tarjetacredito = pago.getTarjetacredito();
            if (tarjetacredito != null) {
                tarjetacredito = em.getReference(tarjetacredito.getClass(), tarjetacredito.getTarjetacreditoPK());
                pago.setTarjetacredito(tarjetacredito);
            }
            Collection<Boleteria> attachedBoleteriaCollection = new ArrayList<Boleteria>();
            for (Boleteria boleteriaCollectionBoleteriaToAttach : pago.getBoleteriaCollection()) {
                boleteriaCollectionBoleteriaToAttach = em.getReference(boleteriaCollectionBoleteriaToAttach.getClass(), boleteriaCollectionBoleteriaToAttach.getCodigoboleta());
                attachedBoleteriaCollection.add(boleteriaCollectionBoleteriaToAttach);
            }
            pago.setBoleteriaCollection(attachedBoleteriaCollection);
            em.persist(pago);
            if (efectivo != null) {
                Pago oldPagoOfEfectivo = efectivo.getPago();
                if (oldPagoOfEfectivo != null) {
                    oldPagoOfEfectivo.setEfectivo(null);
                    oldPagoOfEfectivo = em.merge(oldPagoOfEfectivo);
                }
                efectivo.setPago(pago);
                efectivo = em.merge(efectivo);
            }
            if (usuariosId != null) {
                usuariosId.getPagoCollection().add(pago);
                usuariosId = em.merge(usuariosId);
            }
            if (tarjetacredito != null) {
                Pago oldPagoOfTarjetacredito = tarjetacredito.getPago();
                if (oldPagoOfTarjetacredito != null) {
                    oldPagoOfTarjetacredito.setTarjetacredito(null);
                    oldPagoOfTarjetacredito = em.merge(oldPagoOfTarjetacredito);
                }
                tarjetacredito.setPago(pago);
                tarjetacredito = em.merge(tarjetacredito);
            }
            for (Boleteria boleteriaCollectionBoleteria : pago.getBoleteriaCollection()) {
                Pago oldPagoOfBoleteriaCollectionBoleteria = boleteriaCollectionBoleteria.getPago();
                boleteriaCollectionBoleteria.setPago(pago);
                boleteriaCollectionBoleteria = em.merge(boleteriaCollectionBoleteria);
                if (oldPagoOfBoleteriaCollectionBoleteria != null) {
                    oldPagoOfBoleteriaCollectionBoleteria.getBoleteriaCollection().remove(boleteriaCollectionBoleteria);
                    oldPagoOfBoleteriaCollectionBoleteria = em.merge(oldPagoOfBoleteriaCollectionBoleteria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPago(pago.getPagoPK()) != null) {
                throw new PreexistingEntityException("Pago " + pago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getPagoPK());
            Efectivo efectivoOld = persistentPago.getEfectivo();
            Efectivo efectivoNew = pago.getEfectivo();
            Usuarios usuariosIdOld = persistentPago.getUsuariosId();
            Usuarios usuariosIdNew = pago.getUsuariosId();
            Tarjetacredito tarjetacreditoOld = persistentPago.getTarjetacredito();
            Tarjetacredito tarjetacreditoNew = pago.getTarjetacredito();
            Collection<Boleteria> boleteriaCollectionOld = persistentPago.getBoleteriaCollection();
            Collection<Boleteria> boleteriaCollectionNew = pago.getBoleteriaCollection();
            List<String> illegalOrphanMessages = null;
            if (efectivoOld != null && !efectivoOld.equals(efectivoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Efectivo " + efectivoOld + " since its pago field is not nullable.");
            }
            if (tarjetacreditoOld != null && !tarjetacreditoOld.equals(tarjetacreditoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Tarjetacredito " + tarjetacreditoOld + " since its pago field is not nullable.");
            }
            for (Boleteria boleteriaCollectionOldBoleteria : boleteriaCollectionOld) {
                if (!boleteriaCollectionNew.contains(boleteriaCollectionOldBoleteria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boleteria " + boleteriaCollectionOldBoleteria + " since its pago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (efectivoNew != null) {
                efectivoNew = em.getReference(efectivoNew.getClass(), efectivoNew.getEfectivoPK());
                pago.setEfectivo(efectivoNew);
            }
            if (usuariosIdNew != null) {
                usuariosIdNew = em.getReference(usuariosIdNew.getClass(), usuariosIdNew.getId());
                pago.setUsuariosId(usuariosIdNew);
            }
            if (tarjetacreditoNew != null) {
                tarjetacreditoNew = em.getReference(tarjetacreditoNew.getClass(), tarjetacreditoNew.getTarjetacreditoPK());
                pago.setTarjetacredito(tarjetacreditoNew);
            }
            Collection<Boleteria> attachedBoleteriaCollectionNew = new ArrayList<Boleteria>();
            for (Boleteria boleteriaCollectionNewBoleteriaToAttach : boleteriaCollectionNew) {
                boleteriaCollectionNewBoleteriaToAttach = em.getReference(boleteriaCollectionNewBoleteriaToAttach.getClass(), boleteriaCollectionNewBoleteriaToAttach.getCodigoboleta());
                attachedBoleteriaCollectionNew.add(boleteriaCollectionNewBoleteriaToAttach);
            }
            boleteriaCollectionNew = attachedBoleteriaCollectionNew;
            pago.setBoleteriaCollection(boleteriaCollectionNew);
            pago = em.merge(pago);
            if (efectivoNew != null && !efectivoNew.equals(efectivoOld)) {
                Pago oldPagoOfEfectivo = efectivoNew.getPago();
                if (oldPagoOfEfectivo != null) {
                    oldPagoOfEfectivo.setEfectivo(null);
                    oldPagoOfEfectivo = em.merge(oldPagoOfEfectivo);
                }
                efectivoNew.setPago(pago);
                efectivoNew = em.merge(efectivoNew);
            }
            if (usuariosIdOld != null && !usuariosIdOld.equals(usuariosIdNew)) {
                usuariosIdOld.getPagoCollection().remove(pago);
                usuariosIdOld = em.merge(usuariosIdOld);
            }
            if (usuariosIdNew != null && !usuariosIdNew.equals(usuariosIdOld)) {
                usuariosIdNew.getPagoCollection().add(pago);
                usuariosIdNew = em.merge(usuariosIdNew);
            }
            if (tarjetacreditoNew != null && !tarjetacreditoNew.equals(tarjetacreditoOld)) {
                Pago oldPagoOfTarjetacredito = tarjetacreditoNew.getPago();
                if (oldPagoOfTarjetacredito != null) {
                    oldPagoOfTarjetacredito.setTarjetacredito(null);
                    oldPagoOfTarjetacredito = em.merge(oldPagoOfTarjetacredito);
                }
                tarjetacreditoNew.setPago(pago);
                tarjetacreditoNew = em.merge(tarjetacreditoNew);
            }
            for (Boleteria boleteriaCollectionNewBoleteria : boleteriaCollectionNew) {
                if (!boleteriaCollectionOld.contains(boleteriaCollectionNewBoleteria)) {
                    Pago oldPagoOfBoleteriaCollectionNewBoleteria = boleteriaCollectionNewBoleteria.getPago();
                    boleteriaCollectionNewBoleteria.setPago(pago);
                    boleteriaCollectionNewBoleteria = em.merge(boleteriaCollectionNewBoleteria);
                    if (oldPagoOfBoleteriaCollectionNewBoleteria != null && !oldPagoOfBoleteriaCollectionNewBoleteria.equals(pago)) {
                        oldPagoOfBoleteriaCollectionNewBoleteria.getBoleteriaCollection().remove(boleteriaCollectionNewBoleteria);
                        oldPagoOfBoleteriaCollectionNewBoleteria = em.merge(oldPagoOfBoleteriaCollectionNewBoleteria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PagoPK id = pago.getPagoPK();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PagoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getPagoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Efectivo efectivoOrphanCheck = pago.getEfectivo();
            if (efectivoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Efectivo " + efectivoOrphanCheck + " in its efectivo field has a non-nullable pago field.");
            }
            Tarjetacredito tarjetacreditoOrphanCheck = pago.getTarjetacredito();
            if (tarjetacreditoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Tarjetacredito " + tarjetacreditoOrphanCheck + " in its tarjetacredito field has a non-nullable pago field.");
            }
            Collection<Boleteria> boleteriaCollectionOrphanCheck = pago.getBoleteriaCollection();
            for (Boleteria boleteriaCollectionOrphanCheckBoleteria : boleteriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Boleteria " + boleteriaCollectionOrphanCheckBoleteria + " in its boleteriaCollection field has a non-nullable pago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios usuariosId = pago.getUsuariosId();
            if (usuariosId != null) {
                usuariosId.getPagoCollection().remove(pago);
                usuariosId = em.merge(usuariosId);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(PagoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }
    public long actualizarPago() {
        EntityManager em = getEntityManager();
        try {
            Query q=em.createNamedQuery("Pago.act");
            return  (long)q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
