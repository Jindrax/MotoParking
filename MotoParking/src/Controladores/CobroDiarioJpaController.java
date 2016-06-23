/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Negocio.CobroDiario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Negocio.Cupo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author santiago pc
 */
public class CobroDiarioJpaController implements Serializable {

    public CobroDiarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CobroDiario cobroDiario) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cupo cupo = cobroDiario.getCupo();
            if (cupo != null) {
                cupo = em.getReference(cupo.getClass(), cupo.getCupoPK());
                cobroDiario.setCupo(cupo);
            }
            em.persist(cobroDiario);
            if (cupo != null) {
                cupo.getCobroDiarioList().add(cobroDiario);
                cupo = em.merge(cupo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCobroDiario(cobroDiario.getConsecutivo()) != null) {
                throw new PreexistingEntityException("CobroDiario " + cobroDiario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CobroDiario cobroDiario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CobroDiario persistentCobroDiario = em.find(CobroDiario.class, cobroDiario.getConsecutivo());
            Cupo cupoOld = persistentCobroDiario.getCupo();
            Cupo cupoNew = cobroDiario.getCupo();
            if (cupoNew != null) {
                cupoNew = em.getReference(cupoNew.getClass(), cupoNew.getCupoPK());
                cobroDiario.setCupo(cupoNew);
            }
            cobroDiario = em.merge(cobroDiario);
            if (cupoOld != null && !cupoOld.equals(cupoNew)) {
                cupoOld.getCobroDiarioList().remove(cobroDiario);
                cupoOld = em.merge(cupoOld);
            }
            if (cupoNew != null && !cupoNew.equals(cupoOld)) {
                cupoNew.getCobroDiarioList().add(cobroDiario);
                cupoNew = em.merge(cupoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cobroDiario.getConsecutivo();
                if (findCobroDiario(id) == null) {
                    throw new NonexistentEntityException("The cobroDiario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CobroDiario cobroDiario;
            try {
                cobroDiario = em.getReference(CobroDiario.class, id);
                cobroDiario.getConsecutivo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cobroDiario with id " + id + " no longer exists.", enfe);
            }
            Cupo cupo = cobroDiario.getCupo();
            if (cupo != null) {
                cupo.getCobroDiarioList().remove(cobroDiario);
                cupo = em.merge(cupo);
            }
            em.remove(cobroDiario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CobroDiario> findCobroDiarioEntities() {
        return findCobroDiarioEntities(true, -1, -1);
    }

    public List<CobroDiario> findCobroDiarioEntities(int maxResults, int firstResult) {
        return findCobroDiarioEntities(false, maxResults, firstResult);
    }

    private List<CobroDiario> findCobroDiarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CobroDiario.class));
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

    public CobroDiario findCobroDiario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CobroDiario.class, id);
        } finally {
            em.close();
        }
    }

    public int getCobroDiarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CobroDiario> rt = cq.from(CobroDiario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
