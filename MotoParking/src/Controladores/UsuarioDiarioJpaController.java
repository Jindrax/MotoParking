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
import Negocio.Cupo;
import Negocio.UsuarioDiario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jairo
 */
public class UsuarioDiarioJpaController implements Serializable {

    public UsuarioDiarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioDiario usuarioDiario) throws PreexistingEntityException, Exception {
        if (usuarioDiario.getCupoList() == null) {
            usuarioDiario.setCupoList(new ArrayList<Cupo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cupo> attachedCupoList = new ArrayList<Cupo>();
            for (Cupo cupoListCupoToAttach : usuarioDiario.getCupoList()) {
                cupoListCupoToAttach = em.getReference(cupoListCupoToAttach.getClass(), cupoListCupoToAttach.getCupoPK());
                attachedCupoList.add(cupoListCupoToAttach);
            }
            usuarioDiario.setCupoList(attachedCupoList);
            em.persist(usuarioDiario);
            for (Cupo cupoListCupo : usuarioDiario.getCupoList()) {
                UsuarioDiario oldPlacaOfCupoListCupo = cupoListCupo.getPlaca();
                cupoListCupo.setPlaca(usuarioDiario);
                cupoListCupo = em.merge(cupoListCupo);
                if (oldPlacaOfCupoListCupo != null) {
                    oldPlacaOfCupoListCupo.getCupoList().remove(cupoListCupo);
                    oldPlacaOfCupoListCupo = em.merge(oldPlacaOfCupoListCupo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarioDiario(usuarioDiario.getPlaca()) != null) {
                throw new PreexistingEntityException("UsuarioDiario " + usuarioDiario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioDiario usuarioDiario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioDiario persistentUsuarioDiario = em.find(UsuarioDiario.class, usuarioDiario.getPlaca());
            List<Cupo> cupoListOld = persistentUsuarioDiario.getCupoList();
            List<Cupo> cupoListNew = usuarioDiario.getCupoList();
            List<String> illegalOrphanMessages = null;
            for (Cupo cupoListOldCupo : cupoListOld) {
                if (!cupoListNew.contains(cupoListOldCupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cupo " + cupoListOldCupo + " since its placa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cupo> attachedCupoListNew = new ArrayList<Cupo>();
            for (Cupo cupoListNewCupoToAttach : cupoListNew) {
                cupoListNewCupoToAttach = em.getReference(cupoListNewCupoToAttach.getClass(), cupoListNewCupoToAttach.getCupoPK());
                attachedCupoListNew.add(cupoListNewCupoToAttach);
            }
            cupoListNew = attachedCupoListNew;
            usuarioDiario.setCupoList(cupoListNew);
            usuarioDiario = em.merge(usuarioDiario);
            for (Cupo cupoListNewCupo : cupoListNew) {
                if (!cupoListOld.contains(cupoListNewCupo)) {
                    UsuarioDiario oldPlacaOfCupoListNewCupo = cupoListNewCupo.getPlaca();
                    cupoListNewCupo.setPlaca(usuarioDiario);
                    cupoListNewCupo = em.merge(cupoListNewCupo);
                    if (oldPlacaOfCupoListNewCupo != null && !oldPlacaOfCupoListNewCupo.equals(usuarioDiario)) {
                        oldPlacaOfCupoListNewCupo.getCupoList().remove(cupoListNewCupo);
                        oldPlacaOfCupoListNewCupo = em.merge(oldPlacaOfCupoListNewCupo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuarioDiario.getPlaca();
                if (findUsuarioDiario(id) == null) {
                    throw new NonexistentEntityException("The usuarioDiario with id " + id + " no longer exists.");
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
            UsuarioDiario usuarioDiario;
            try {
                usuarioDiario = em.getReference(UsuarioDiario.class, id);
                usuarioDiario.getPlaca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioDiario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cupo> cupoListOrphanCheck = usuarioDiario.getCupoList();
            for (Cupo cupoListOrphanCheckCupo : cupoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsuarioDiario (" + usuarioDiario + ") cannot be destroyed since the Cupo " + cupoListOrphanCheckCupo + " in its cupoList field has a non-nullable placa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarioDiario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioDiario> findUsuarioDiarioEntities() {
        return findUsuarioDiarioEntities(true, -1, -1);
    }

    public List<UsuarioDiario> findUsuarioDiarioEntities(int maxResults, int firstResult) {
        return findUsuarioDiarioEntities(false, maxResults, firstResult);
    }

    private List<UsuarioDiario> findUsuarioDiarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioDiario.class));
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

    public UsuarioDiario findUsuarioDiario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioDiario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioDiarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioDiario> rt = cq.from(UsuarioDiario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
