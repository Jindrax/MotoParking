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
 * @author Todesser
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
            Cupo cupoConsecutivo = cobroDiario.getCupoConsecutivo();
            if (cupoConsecutivo != null) {
                cupoConsecutivo = em.getReference(cupoConsecutivo.getClass(), cupoConsecutivo.getConsecutivo());
                cobroDiario.setCupoConsecutivo(cupoConsecutivo);
            }
            em.persist(cobroDiario);
            if (cupoConsecutivo != null) {
                cupoConsecutivo.getCobroDiarioList().add(cobroDiario);
                cupoConsecutivo = em.merge(cupoConsecutivo);
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
            Cupo cupoConsecutivoOld = persistentCobroDiario.getCupoConsecutivo();
            Cupo cupoConsecutivoNew = cobroDiario.getCupoConsecutivo();
            if (cupoConsecutivoNew != null) {
                cupoConsecutivoNew = em.getReference(cupoConsecutivoNew.getClass(), cupoConsecutivoNew.getConsecutivo());
                cobroDiario.setCupoConsecutivo(cupoConsecutivoNew);
            }
            cobroDiario = em.merge(cobroDiario);
            if (cupoConsecutivoOld != null && !cupoConsecutivoOld.equals(cupoConsecutivoNew)) {
                cupoConsecutivoOld.getCobroDiarioList().remove(cobroDiario);
                cupoConsecutivoOld = em.merge(cupoConsecutivoOld);
            }
            if (cupoConsecutivoNew != null && !cupoConsecutivoNew.equals(cupoConsecutivoOld)) {
                cupoConsecutivoNew.getCobroDiarioList().add(cobroDiario);
                cupoConsecutivoNew = em.merge(cupoConsecutivoNew);
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
            Cupo cupoConsecutivo = cobroDiario.getCupoConsecutivo();
            if (cupoConsecutivo != null) {
                cupoConsecutivo.getCobroDiarioList().remove(cobroDiario);
                cupoConsecutivo = em.merge(cupoConsecutivo);
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
