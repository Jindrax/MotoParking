/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Negocio.CobroMensual;
import Negocio.UsuarioMensual;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jairo
 */
public class UsuarioMensualJpaController implements Serializable {

    public UsuarioMensualJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioMensual usuarioMensual) throws PreexistingEntityException, Exception {
        if (usuarioMensual.getCobroMensualList() == null) {
            usuarioMensual.setCobroMensualList(new ArrayList<CobroMensual>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CobroMensual> attachedCobroMensualList = new ArrayList<CobroMensual>();
            for (CobroMensual cobroMensualListCobroMensualToAttach : usuarioMensual.getCobroMensualList()) {
                cobroMensualListCobroMensualToAttach = em.getReference(cobroMensualListCobroMensualToAttach.getClass(), cobroMensualListCobroMensualToAttach.getCobroMensualPK());
                attachedCobroMensualList.add(cobroMensualListCobroMensualToAttach);
            }
            usuarioMensual.setCobroMensualList(attachedCobroMensualList);
            em.persist(usuarioMensual);
            for (CobroMensual cobroMensualListCobroMensual : usuarioMensual.getCobroMensualList()) {
                UsuarioMensual oldUsuarioMensualOfCobroMensualListCobroMensual = cobroMensualListCobroMensual.getUsuarioMensual();
                cobroMensualListCobroMensual.setUsuarioMensual(usuarioMensual);
                cobroMensualListCobroMensual = em.merge(cobroMensualListCobroMensual);
                if (oldUsuarioMensualOfCobroMensualListCobroMensual != null) {
                    oldUsuarioMensualOfCobroMensualListCobroMensual.getCobroMensualList().remove(cobroMensualListCobroMensual);
                    oldUsuarioMensualOfCobroMensualListCobroMensual = em.merge(oldUsuarioMensualOfCobroMensualListCobroMensual);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarioMensual(usuarioMensual.getPlaca()) != null) {
                throw new PreexistingEntityException("UsuarioMensual " + usuarioMensual + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioMensual usuarioMensual) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioMensual persistentUsuarioMensual = em.find(UsuarioMensual.class, usuarioMensual.getPlaca());
            List<CobroMensual> cobroMensualListOld = persistentUsuarioMensual.getCobroMensualList();
            List<CobroMensual> cobroMensualListNew = usuarioMensual.getCobroMensualList();
            List<String> illegalOrphanMessages = null;
            for (CobroMensual cobroMensualListOldCobroMensual : cobroMensualListOld) {
                if (!cobroMensualListNew.contains(cobroMensualListOldCobroMensual)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CobroMensual " + cobroMensualListOldCobroMensual + " since its usuarioMensual field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<CobroMensual> attachedCobroMensualListNew = new ArrayList<CobroMensual>();
            for (CobroMensual cobroMensualListNewCobroMensualToAttach : cobroMensualListNew) {
                cobroMensualListNewCobroMensualToAttach = em.getReference(cobroMensualListNewCobroMensualToAttach.getClass(), cobroMensualListNewCobroMensualToAttach.getCobroMensualPK());
                attachedCobroMensualListNew.add(cobroMensualListNewCobroMensualToAttach);
            }
            cobroMensualListNew = attachedCobroMensualListNew;
            usuarioMensual.setCobroMensualList(cobroMensualListNew);
            usuarioMensual = em.merge(usuarioMensual);
            for (CobroMensual cobroMensualListNewCobroMensual : cobroMensualListNew) {
                if (!cobroMensualListOld.contains(cobroMensualListNewCobroMensual)) {
                    UsuarioMensual oldUsuarioMensualOfCobroMensualListNewCobroMensual = cobroMensualListNewCobroMensual.getUsuarioMensual();
                    cobroMensualListNewCobroMensual.setUsuarioMensual(usuarioMensual);
                    cobroMensualListNewCobroMensual = em.merge(cobroMensualListNewCobroMensual);
                    if (oldUsuarioMensualOfCobroMensualListNewCobroMensual != null && !oldUsuarioMensualOfCobroMensualListNewCobroMensual.equals(usuarioMensual)) {
                        oldUsuarioMensualOfCobroMensualListNewCobroMensual.getCobroMensualList().remove(cobroMensualListNewCobroMensual);
                        oldUsuarioMensualOfCobroMensualListNewCobroMensual = em.merge(oldUsuarioMensualOfCobroMensualListNewCobroMensual);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuarioMensual.getPlaca();
                if (findUsuarioMensual(id) == null) {
                    throw new NonexistentEntityException("The usuarioMensual with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioMensual usuarioMensual;
            try {
                usuarioMensual = em.getReference(UsuarioMensual.class, id);
                usuarioMensual.getPlaca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioMensual with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CobroMensual> cobroMensualListOrphanCheck = usuarioMensual.getCobroMensualList();
            for (CobroMensual cobroMensualListOrphanCheckCobroMensual : cobroMensualListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsuarioMensual (" + usuarioMensual + ") cannot be destroyed since the CobroMensual " + cobroMensualListOrphanCheckCobroMensual + " in its cobroMensualList field has a non-nullable usuarioMensual field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarioMensual);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioMensual> findUsuarioMensualEntities() {
        return findUsuarioMensualEntities(true, -1, -1);
    }

    public List<UsuarioMensual> findUsuarioMensualEntities(int maxResults, int firstResult) {
        return findUsuarioMensualEntities(false, maxResults, firstResult);
    }

    private List<UsuarioMensual> findUsuarioMensualEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioMensual.class));
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

    public UsuarioMensual findUsuarioMensual(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioMensual.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioMensualCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioMensual> rt = cq.from(UsuarioMensual.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
