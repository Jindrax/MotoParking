/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Negocio.Cupo;
import Negocio.Locker;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author santiago pc
 */
public class LockerJpaController implements Serializable {

    public LockerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Locker locker) throws PreexistingEntityException, Exception {
        if (locker.getCupoList() == null) {
            locker.setCupoList(new ArrayList<Cupo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cupo> attachedCupoList = new ArrayList<Cupo>();
            for (Cupo cupoListCupoToAttach : locker.getCupoList()) {
                cupoListCupoToAttach = em.getReference(cupoListCupoToAttach.getClass(), cupoListCupoToAttach.getCupoPK());
                attachedCupoList.add(cupoListCupoToAttach);
            }
            locker.setCupoList(attachedCupoList);
            em.persist(locker);
            for (Cupo cupoListCupo : locker.getCupoList()) {
                Locker oldLockerOfCupoListCupo = cupoListCupo.getLocker();
                cupoListCupo.setLocker(locker);
                cupoListCupo = em.merge(cupoListCupo);
                if (oldLockerOfCupoListCupo != null) {
                    oldLockerOfCupoListCupo.getCupoList().remove(cupoListCupo);
                    oldLockerOfCupoListCupo = em.merge(oldLockerOfCupoListCupo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLocker(locker.getIdentificador()) != null) {
                throw new PreexistingEntityException("Locker " + locker + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Locker locker) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Locker persistentLocker = em.find(Locker.class, locker.getIdentificador());
            List<Cupo> cupoListOld = persistentLocker.getCupoList();
            List<Cupo> cupoListNew = locker.getCupoList();
            List<Cupo> attachedCupoListNew = new ArrayList<Cupo>();
            for (Cupo cupoListNewCupoToAttach : cupoListNew) {
                cupoListNewCupoToAttach = em.getReference(cupoListNewCupoToAttach.getClass(), cupoListNewCupoToAttach.getCupoPK());
                attachedCupoListNew.add(cupoListNewCupoToAttach);
            }
            cupoListNew = attachedCupoListNew;
            locker.setCupoList(cupoListNew);
            locker = em.merge(locker);
            for (Cupo cupoListOldCupo : cupoListOld) {
                if (!cupoListNew.contains(cupoListOldCupo)) {
                    cupoListOldCupo.setLocker(null);
                    cupoListOldCupo = em.merge(cupoListOldCupo);
                }
            }
            for (Cupo cupoListNewCupo : cupoListNew) {
                if (!cupoListOld.contains(cupoListNewCupo)) {
                    Locker oldLockerOfCupoListNewCupo = cupoListNewCupo.getLocker();
                    cupoListNewCupo.setLocker(locker);
                    cupoListNewCupo = em.merge(cupoListNewCupo);
                    if (oldLockerOfCupoListNewCupo != null && !oldLockerOfCupoListNewCupo.equals(locker)) {
                        oldLockerOfCupoListNewCupo.getCupoList().remove(cupoListNewCupo);
                        oldLockerOfCupoListNewCupo = em.merge(oldLockerOfCupoListNewCupo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = locker.getIdentificador();
                if (findLocker(id) == null) {
                    throw new NonexistentEntityException("The locker with id " + id + " no longer exists.");
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
            Locker locker;
            try {
                locker = em.getReference(Locker.class, id);
                locker.getIdentificador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The locker with id " + id + " no longer exists.", enfe);
            }
            List<Cupo> cupoList = locker.getCupoList();
            for (Cupo cupoListCupo : cupoList) {
                cupoListCupo.setLocker(null);
                cupoListCupo = em.merge(cupoListCupo);
            }
            em.remove(locker);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Locker> findLockerEntities() {
        return findLockerEntities(true, -1, -1);
    }

    public List<Locker> findLockerEntities(int maxResults, int firstResult) {
        return findLockerEntities(false, maxResults, firstResult);
    }

    private List<Locker> findLockerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Locker.class));
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

    public Locker findLocker(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Locker.class, id);
        } finally {
            em.close();
        }
    }

    public int getLockerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Locker> rt = cq.from(Locker.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
