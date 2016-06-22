/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Negocio.Configuraciones;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Todesser
 */
public class ConfiguracionesJpaController implements Serializable {

    public ConfiguracionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Configuraciones configuraciones) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(configuraciones);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConfiguraciones(configuraciones.getDescripcion()) != null) {
                throw new PreexistingEntityException("Configuraciones " + configuraciones + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Configuraciones configuraciones) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            configuraciones = em.merge(configuraciones);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = configuraciones.getDescripcion();
                if (findConfiguraciones(id) == null) {
                    throw new NonexistentEntityException("The configuraciones with id " + id + " no longer exists.");
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
            Configuraciones configuraciones;
            try {
                configuraciones = em.getReference(Configuraciones.class, id);
                configuraciones.getDescripcion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configuraciones with id " + id + " no longer exists.", enfe);
            }
            em.remove(configuraciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Configuraciones> findConfiguracionesEntities() {
        return findConfiguracionesEntities(true, -1, -1);
    }

    public List<Configuraciones> findConfiguracionesEntities(int maxResults, int firstResult) {
        return findConfiguracionesEntities(false, maxResults, firstResult);
    }

    private List<Configuraciones> findConfiguracionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Configuraciones.class));
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

    public Configuraciones findConfiguraciones(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Configuraciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfiguracionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Configuraciones> rt = cq.from(Configuraciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
