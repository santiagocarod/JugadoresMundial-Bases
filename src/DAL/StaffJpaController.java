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
import Mundo.Staff;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Santiago Caro
 */
public class StaffJpaController implements Serializable {

    public StaffJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Staff staff) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipo = staff.getEquipo();
            if (equipo != null) {
                equipo = em.getReference(equipo.getClass(), equipo.getNombreeq());
                staff.setEquipo(equipo);
            }
            Equipo equipo1 = staff.getEquipo1();
            if (equipo1 != null) {
                equipo1 = em.getReference(equipo1.getClass(), equipo1.getNombreeq());
                staff.setEquipo1(equipo1);
            }
            em.persist(staff);
            if (equipo != null) {
                Staff oldEntrenadorOfEquipo = equipo.getEntrenador();
                if (oldEntrenadorOfEquipo != null) {
                    oldEntrenadorOfEquipo.setEquipo(null);
                    oldEntrenadorOfEquipo = em.merge(oldEntrenadorOfEquipo);
                }
                equipo.setEntrenador(staff);
                equipo = em.merge(equipo);
            }
            if (equipo1 != null) {
                Staff oldAuxiliarOfEquipo1 = equipo1.getAuxiliar();
                if (oldAuxiliarOfEquipo1 != null) {
                    oldAuxiliarOfEquipo1.setEquipo1(null);
                    oldAuxiliarOfEquipo1 = em.merge(oldAuxiliarOfEquipo1);
                }
                equipo1.setAuxiliar(staff);
                equipo1 = em.merge(equipo1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStaff(staff.getCodigo()) != null) {
                throw new PreexistingEntityException("Staff " + staff + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Staff staff) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Staff persistentStaff = em.find(Staff.class, staff.getCodigo());
            Equipo equipoOld = persistentStaff.getEquipo();
            Equipo equipoNew = staff.getEquipo();
            Equipo equipo1Old = persistentStaff.getEquipo1();
            Equipo equipo1New = staff.getEquipo1();
            List<String> illegalOrphanMessages = null;
            if (equipoOld != null && !equipoOld.equals(equipoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Equipo " + equipoOld + " since its entrenador field is not nullable.");
            }
            if (equipo1Old != null && !equipo1Old.equals(equipo1New)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Equipo " + equipo1Old + " since its auxiliar field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (equipoNew != null) {
                equipoNew = em.getReference(equipoNew.getClass(), equipoNew.getNombreeq());
                staff.setEquipo(equipoNew);
            }
            if (equipo1New != null) {
                equipo1New = em.getReference(equipo1New.getClass(), equipo1New.getNombreeq());
                staff.setEquipo1(equipo1New);
            }
            staff = em.merge(staff);
            if (equipoNew != null && !equipoNew.equals(equipoOld)) {
                Staff oldEntrenadorOfEquipo = equipoNew.getEntrenador();
                if (oldEntrenadorOfEquipo != null) {
                    oldEntrenadorOfEquipo.setEquipo(null);
                    oldEntrenadorOfEquipo = em.merge(oldEntrenadorOfEquipo);
                }
                equipoNew.setEntrenador(staff);
                equipoNew = em.merge(equipoNew);
            }
            if (equipo1New != null && !equipo1New.equals(equipo1Old)) {
                Staff oldAuxiliarOfEquipo1 = equipo1New.getAuxiliar();
                if (oldAuxiliarOfEquipo1 != null) {
                    oldAuxiliarOfEquipo1.setEquipo1(null);
                    oldAuxiliarOfEquipo1 = em.merge(oldAuxiliarOfEquipo1);
                }
                equipo1New.setAuxiliar(staff);
                equipo1New = em.merge(equipo1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = staff.getCodigo();
                if (findStaff(id) == null) {
                    throw new NonexistentEntityException("The staff with id " + id + " no longer exists.");
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
            Staff staff;
            try {
                staff = em.getReference(Staff.class, id);
                staff.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The staff with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Equipo equipoOrphanCheck = staff.getEquipo();
            if (equipoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Staff (" + staff + ") cannot be destroyed since the Equipo " + equipoOrphanCheck + " in its equipo field has a non-nullable entrenador field.");
            }
            Equipo equipo1OrphanCheck = staff.getEquipo1();
            if (equipo1OrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Staff (" + staff + ") cannot be destroyed since the Equipo " + equipo1OrphanCheck + " in its equipo1 field has a non-nullable auxiliar field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(staff);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Staff> findStaffEntities() {
        return findStaffEntities(true, -1, -1);
    }

    public List<Staff> findStaffEntities(int maxResults, int firstResult) {
        return findStaffEntities(false, maxResults, firstResult);
    }

    private List<Staff> findStaffEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Staff.class));
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

    public Staff findStaff(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Staff.class, id);
        } finally {
            em.close();
        }
    }

    public int getStaffCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Staff> rt = cq.from(Staff.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
