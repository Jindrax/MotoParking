/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Negocio.Baneado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Negocio.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jairo
 */
public class BaneadoJpaController implements Serializable {

    public BaneadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Baneado baneado) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Usuario usuarioOrphanCheck = baneado.getUsuario();
        if (usuarioOrphanCheck != null) {
            Baneado oldBaneadoOfUsuario = usuarioOrphanCheck.getBaneado();
            if (oldBaneadoOfUsuario != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Usuario " + usuarioOrphanCheck + " already has an item of type Baneado whose usuario column cannot be null. Please make another selection for the usuario field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = baneado.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getPlaca());
                baneado.setUsuario(usuario);
            }
            em.persist(baneado);
            if (usuario != null) {
                usuario.setBaneado(baneado);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBaneado(baneado.getPlaca()) != null) {
                throw new PreexistingEntityException("Baneado " + baneado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Baneado baneado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Baneado persistentBaneado = em.find(Baneado.class, baneado.getPlaca());
            Usuario usuarioOld = persistentBaneado.getUsuario();
            Usuario usuarioNew = baneado.getUsuario();
            List<String> illegalOrphanMessages = null;
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                Baneado oldBaneadoOfUsuario = usuarioNew.getBaneado();
                if (oldBaneadoOfUsuario != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuario " + usuarioNew + " already has an item of type Baneado whose usuario column cannot be null. Please make another selection for the usuario field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getPlaca());
                baneado.setUsuario(usuarioNew);
            }
            baneado = em.merge(baneado);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.setBaneado(null);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.setBaneado(baneado);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = baneado.getPlaca();
                if (findBaneado(id) == null) {
                    throw new NonexistentEntityException("The baneado with id " + id + " no longer exists.");
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
            Baneado baneado;
            try {
                baneado = em.getReference(Baneado.class, id);
                baneado.getPlaca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baneado with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = baneado.getUsuario();
            if (usuario != null) {
                usuario.setBaneado(null);
                usuario = em.merge(usuario);
            }
            em.remove(baneado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Baneado> findBaneadoEntities() {
        return findBaneadoEntities(true, -1, -1);
    }

    public List<Baneado> findBaneadoEntities(int maxResults, int firstResult) {
        return findBaneadoEntities(false, maxResults, firstResult);
    }

    private List<Baneado> findBaneadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Baneado.class));
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

    public Baneado findBaneado(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Baneado.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaneadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Baneado> rt = cq.from(Baneado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
