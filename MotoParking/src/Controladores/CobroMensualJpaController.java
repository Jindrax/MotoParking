/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Negocio.CobroMensual;
import Negocio.CobroMensualPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Negocio.UsuarioMensual;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Todesser
 */
public class CobroMensualJpaController implements Serializable {

    public CobroMensualJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CobroMensual cobroMensual) throws PreexistingEntityException, Exception {
        if (cobroMensual.getCobroMensualPK() == null) {
            cobroMensual.setCobroMensualPK(new CobroMensualPK());
        }
        cobroMensual.getCobroMensualPK().setPlaca(cobroMensual.getUsuarioMensual().getPlaca());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioMensual usuarioMensual = cobroMensual.getUsuarioMensual();
            if (usuarioMensual != null) {
                usuarioMensual = em.getReference(usuarioMensual.getClass(), usuarioMensual.getPlaca());
                cobroMensual.setUsuarioMensual(usuarioMensual);
            }
            em.persist(cobroMensual);
            if (usuarioMensual != null) {
                usuarioMensual.getCobroMensualList().add(cobroMensual);
                usuarioMensual = em.merge(usuarioMensual);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCobroMensual(cobroMensual.getCobroMensualPK()) != null) {
                throw new PreexistingEntityException("CobroMensual " + cobroMensual + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CobroMensual cobroMensual) throws NonexistentEntityException, Exception {
        cobroMensual.getCobroMensualPK().setPlaca(cobroMensual.getUsuarioMensual().getPlaca());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CobroMensual persistentCobroMensual = em.find(CobroMensual.class, cobroMensual.getCobroMensualPK());
            UsuarioMensual usuarioMensualOld = persistentCobroMensual.getUsuarioMensual();
            UsuarioMensual usuarioMensualNew = cobroMensual.getUsuarioMensual();
            if (usuarioMensualNew != null) {
                usuarioMensualNew = em.getReference(usuarioMensualNew.getClass(), usuarioMensualNew.getPlaca());
                cobroMensual.setUsuarioMensual(usuarioMensualNew);
            }
            cobroMensual = em.merge(cobroMensual);
            if (usuarioMensualOld != null && !usuarioMensualOld.equals(usuarioMensualNew)) {
                usuarioMensualOld.getCobroMensualList().remove(cobroMensual);
                usuarioMensualOld = em.merge(usuarioMensualOld);
            }
            if (usuarioMensualNew != null && !usuarioMensualNew.equals(usuarioMensualOld)) {
                usuarioMensualNew.getCobroMensualList().add(cobroMensual);
                usuarioMensualNew = em.merge(usuarioMensualNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CobroMensualPK id = cobroMensual.getCobroMensualPK();
                if (findCobroMensual(id) == null) {
                    throw new NonexistentEntityException("The cobroMensual with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CobroMensualPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CobroMensual cobroMensual;
            try {
                cobroMensual = em.getReference(CobroMensual.class, id);
                cobroMensual.getCobroMensualPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cobroMensual with id " + id + " no longer exists.", enfe);
            }
            UsuarioMensual usuarioMensual = cobroMensual.getUsuarioMensual();
            if (usuarioMensual != null) {
                usuarioMensual.getCobroMensualList().remove(cobroMensual);
                usuarioMensual = em.merge(usuarioMensual);
            }
            em.remove(cobroMensual);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CobroMensual> findCobroMensualEntities() {
        return findCobroMensualEntities(true, -1, -1);
    }

    public List<CobroMensual> findCobroMensualEntities(int maxResults, int firstResult) {
        return findCobroMensualEntities(false, maxResults, firstResult);
    }

    private List<CobroMensual> findCobroMensualEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CobroMensual.class));
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

    public CobroMensual findCobroMensual(CobroMensualPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CobroMensual.class, id);
        } finally {
            em.close();
        }
    }

    public int getCobroMensualCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CobroMensual> rt = cq.from(CobroMensual.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
