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
import Mundo.Pais;
import Mundo.Pago;
import Mundo.Usuarios;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getPagoCollection() == null) {
            usuarios.setPagoCollection(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais codpais = usuarios.getCodpais();
            if (codpais != null) {
                codpais = em.getReference(codpais.getClass(), codpais.getCodpais());
                usuarios.setCodpais(codpais);
            }
            List<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : usuarios.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getPagoPK());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            usuarios.setPagoCollection(attachedPagoCollection);
            em.persist(usuarios);
            if (codpais != null) {
                codpais.getUsuariosCollection().add(usuarios);
                codpais = em.merge(codpais);
            }
            for (Pago pagoCollectionPago : usuarios.getPagoCollection()) {
                Usuarios oldUsuariosIdOfPagoCollectionPago = pagoCollectionPago.getUsuariosId();
                pagoCollectionPago.setUsuariosId(usuarios);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldUsuariosIdOfPagoCollectionPago != null) {
                    oldUsuariosIdOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldUsuariosIdOfPagoCollectionPago = em.merge(oldUsuariosIdOfPagoCollectionPago);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getId()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            Pais codpaisOld = persistentUsuarios.getCodpais();
            Pais codpaisNew = usuarios.getCodpais();
            List<Pago> pagoCollectionOld = persistentUsuarios.getPagoCollection();
            List<Pago> pagoCollectionNew = usuarios.getPagoCollection();
            if (codpaisNew != null) {
                codpaisNew = em.getReference(codpaisNew.getClass(), codpaisNew.getCodpais());
                usuarios.setCodpais(codpaisNew);
            }
            List<Pago> attachedPagoCollectionNew = new ArrayList<Pago>();
            for (Pago pagoCollectionNewPagoToAttach : pagoCollectionNew) {
                pagoCollectionNewPagoToAttach = em.getReference(pagoCollectionNewPagoToAttach.getClass(), pagoCollectionNewPagoToAttach.getPagoPK());
                attachedPagoCollectionNew.add(pagoCollectionNewPagoToAttach);
            }
            pagoCollectionNew = attachedPagoCollectionNew;
            usuarios.setPagoCollection(pagoCollectionNew);
            usuarios = em.merge(usuarios);
            if (codpaisOld != null && !codpaisOld.equals(codpaisNew)) {
                codpaisOld.getUsuariosCollection().remove(usuarios);
                codpaisOld = em.merge(codpaisOld);
            }
            if (codpaisNew != null && !codpaisNew.equals(codpaisOld)) {
                codpaisNew.getUsuariosCollection().add(usuarios);
                codpaisNew = em.merge(codpaisNew);
            }
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    pagoCollectionOldPago.setUsuariosId(null);
                    pagoCollectionOldPago = em.merge(pagoCollectionOldPago);
                }
            }
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    Usuarios oldUsuariosIdOfPagoCollectionNewPago = pagoCollectionNewPago.getUsuariosId();
                    pagoCollectionNewPago.setUsuariosId(usuarios);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldUsuariosIdOfPagoCollectionNewPago != null && !oldUsuariosIdOfPagoCollectionNewPago.equals(usuarios)) {
                        oldUsuariosIdOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldUsuariosIdOfPagoCollectionNewPago = em.merge(oldUsuariosIdOfPagoCollectionNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Pais codpais = usuarios.getCodpais();
            if (codpais != null) {
                codpais.getUsuariosCollection().remove(usuarios);
                codpais = em.merge(codpais);
            }
            Collection<Pago> pagoCollection = usuarios.getPagoCollection();
            for (Pago pagoCollectionPago : pagoCollection) {
                pagoCollectionPago.setUsuariosId(null);
                pagoCollectionPago = em.merge(pagoCollectionPago);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
