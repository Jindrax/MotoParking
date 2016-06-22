/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Negocio.Usuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Negocio.UsuarioMensual;
import Negocio.UsuarioDiario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Todesser
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioMensual usuarioMensual = usuario.getUsuarioMensual();
            if (usuarioMensual != null) {
                usuarioMensual = em.getReference(usuarioMensual.getClass(), usuarioMensual.getPlaca());
                usuario.setUsuarioMensual(usuarioMensual);
            }
            UsuarioDiario usuarioDiario = usuario.getUsuarioDiario();
            if (usuarioDiario != null) {
                usuarioDiario = em.getReference(usuarioDiario.getClass(), usuarioDiario.getPlaca());
                usuario.setUsuarioDiario(usuarioDiario);
            }
            em.persist(usuario);
            if (usuarioMensual != null) {
                Usuario oldUsuarioOfUsuarioMensual = usuarioMensual.getUsuario();
                if (oldUsuarioOfUsuarioMensual != null) {
                    oldUsuarioOfUsuarioMensual.setUsuarioMensual(null);
                    oldUsuarioOfUsuarioMensual = em.merge(oldUsuarioOfUsuarioMensual);
                }
                usuarioMensual.setUsuario(usuario);
                usuarioMensual = em.merge(usuarioMensual);
            }
            if (usuarioDiario != null) {
                Usuario oldUsuarioOfUsuarioDiario = usuarioDiario.getUsuario();
                if (oldUsuarioOfUsuarioDiario != null) {
                    oldUsuarioOfUsuarioDiario.setUsuarioDiario(null);
                    oldUsuarioOfUsuarioDiario = em.merge(oldUsuarioOfUsuarioDiario);
                }
                usuarioDiario.setUsuario(usuario);
                usuarioDiario = em.merge(usuarioDiario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getPlaca()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getPlaca());
            UsuarioMensual usuarioMensualOld = persistentUsuario.getUsuarioMensual();
            UsuarioMensual usuarioMensualNew = usuario.getUsuarioMensual();
            UsuarioDiario usuarioDiarioOld = persistentUsuario.getUsuarioDiario();
            UsuarioDiario usuarioDiarioNew = usuario.getUsuarioDiario();
            List<String> illegalOrphanMessages = null;
            if (usuarioMensualOld != null && !usuarioMensualOld.equals(usuarioMensualNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain UsuarioMensual " + usuarioMensualOld + " since its usuario field is not nullable.");
            }
            if (usuarioDiarioOld != null && !usuarioDiarioOld.equals(usuarioDiarioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain UsuarioDiario " + usuarioDiarioOld + " since its usuario field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioMensualNew != null) {
                usuarioMensualNew = em.getReference(usuarioMensualNew.getClass(), usuarioMensualNew.getPlaca());
                usuario.setUsuarioMensual(usuarioMensualNew);
            }
            if (usuarioDiarioNew != null) {
                usuarioDiarioNew = em.getReference(usuarioDiarioNew.getClass(), usuarioDiarioNew.getPlaca());
                usuario.setUsuarioDiario(usuarioDiarioNew);
            }
            usuario = em.merge(usuario);
            if (usuarioMensualNew != null && !usuarioMensualNew.equals(usuarioMensualOld)) {
                Usuario oldUsuarioOfUsuarioMensual = usuarioMensualNew.getUsuario();
                if (oldUsuarioOfUsuarioMensual != null) {
                    oldUsuarioOfUsuarioMensual.setUsuarioMensual(null);
                    oldUsuarioOfUsuarioMensual = em.merge(oldUsuarioOfUsuarioMensual);
                }
                usuarioMensualNew.setUsuario(usuario);
                usuarioMensualNew = em.merge(usuarioMensualNew);
            }
            if (usuarioDiarioNew != null && !usuarioDiarioNew.equals(usuarioDiarioOld)) {
                Usuario oldUsuarioOfUsuarioDiario = usuarioDiarioNew.getUsuario();
                if (oldUsuarioOfUsuarioDiario != null) {
                    oldUsuarioOfUsuarioDiario.setUsuarioDiario(null);
                    oldUsuarioOfUsuarioDiario = em.merge(oldUsuarioOfUsuarioDiario);
                }
                usuarioDiarioNew.setUsuario(usuario);
                usuarioDiarioNew = em.merge(usuarioDiarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getPlaca();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getPlaca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            UsuarioMensual usuarioMensualOrphanCheck = usuario.getUsuarioMensual();
            if (usuarioMensualOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioMensual " + usuarioMensualOrphanCheck + " in its usuarioMensual field has a non-nullable usuario field.");
            }
            UsuarioDiario usuarioDiarioOrphanCheck = usuario.getUsuarioDiario();
            if (usuarioDiarioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioDiario " + usuarioDiarioOrphanCheck + " in its usuarioDiario field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
