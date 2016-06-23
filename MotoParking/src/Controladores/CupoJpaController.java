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
import Negocio.Locker;
import Negocio.UsuarioDiario;
import Negocio.CobroDiario;
import Negocio.Cupo;
import Negocio.CupoPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author santiago pc
 */
public class CupoJpaController implements Serializable {

    public CupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cupo cupo) throws PreexistingEntityException, Exception {
        if (cupo.getCupoPK() == null) {
            cupo.setCupoPK(new CupoPK());
        }
        if (cupo.getCobroDiarioList() == null) {
            cupo.setCobroDiarioList(new ArrayList<CobroDiario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Locker locker = cupo.getLocker();
            if (locker != null) {
                locker = em.getReference(locker.getClass(), locker.getIdentificador());
                cupo.setLocker(locker);
            }
            UsuarioDiario placa = cupo.getPlaca();
            if (placa != null) {
                placa = em.getReference(placa.getClass(), placa.getPlaca());
                cupo.setPlaca(placa);
            }
            List<CobroDiario> attachedCobroDiarioList = new ArrayList<CobroDiario>();
            for (CobroDiario cobroDiarioListCobroDiarioToAttach : cupo.getCobroDiarioList()) {
                cobroDiarioListCobroDiarioToAttach = em.getReference(cobroDiarioListCobroDiarioToAttach.getClass(), cobroDiarioListCobroDiarioToAttach.getConsecutivo());
                attachedCobroDiarioList.add(cobroDiarioListCobroDiarioToAttach);
            }
            cupo.setCobroDiarioList(attachedCobroDiarioList);
            em.persist(cupo);
            if (locker != null) {
                locker.getCupoList().add(cupo);
                locker = em.merge(locker);
            }
            if (placa != null) {
                placa.getCupoList().add(cupo);
                placa = em.merge(placa);
            }
            for (CobroDiario cobroDiarioListCobroDiario : cupo.getCobroDiarioList()) {
                Cupo oldCupoOfCobroDiarioListCobroDiario = cobroDiarioListCobroDiario.getCupo();
                cobroDiarioListCobroDiario.setCupo(cupo);
                cobroDiarioListCobroDiario = em.merge(cobroDiarioListCobroDiario);
                if (oldCupoOfCobroDiarioListCobroDiario != null) {
                    oldCupoOfCobroDiarioListCobroDiario.getCobroDiarioList().remove(cobroDiarioListCobroDiario);
                    oldCupoOfCobroDiarioListCobroDiario = em.merge(oldCupoOfCobroDiarioListCobroDiario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCupo(cupo.getCupoPK()) != null) {
                throw new PreexistingEntityException("Cupo " + cupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cupo cupo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cupo persistentCupo = em.find(Cupo.class, cupo.getCupoPK());
            Locker lockerOld = persistentCupo.getLocker();
            Locker lockerNew = cupo.getLocker();
            UsuarioDiario placaOld = persistentCupo.getPlaca();
            UsuarioDiario placaNew = cupo.getPlaca();
            List<CobroDiario> cobroDiarioListOld = persistentCupo.getCobroDiarioList();
            List<CobroDiario> cobroDiarioListNew = cupo.getCobroDiarioList();
            List<String> illegalOrphanMessages = null;
            for (CobroDiario cobroDiarioListOldCobroDiario : cobroDiarioListOld) {
                if (!cobroDiarioListNew.contains(cobroDiarioListOldCobroDiario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CobroDiario " + cobroDiarioListOldCobroDiario + " since its cupo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (lockerNew != null) {
                lockerNew = em.getReference(lockerNew.getClass(), lockerNew.getIdentificador());
                cupo.setLocker(lockerNew);
            }
            if (placaNew != null) {
                placaNew = em.getReference(placaNew.getClass(), placaNew.getPlaca());
                cupo.setPlaca(placaNew);
            }
            List<CobroDiario> attachedCobroDiarioListNew = new ArrayList<CobroDiario>();
            for (CobroDiario cobroDiarioListNewCobroDiarioToAttach : cobroDiarioListNew) {
                cobroDiarioListNewCobroDiarioToAttach = em.getReference(cobroDiarioListNewCobroDiarioToAttach.getClass(), cobroDiarioListNewCobroDiarioToAttach.getConsecutivo());
                attachedCobroDiarioListNew.add(cobroDiarioListNewCobroDiarioToAttach);
            }
            cobroDiarioListNew = attachedCobroDiarioListNew;
            cupo.setCobroDiarioList(cobroDiarioListNew);
            cupo = em.merge(cupo);
            if (lockerOld != null && !lockerOld.equals(lockerNew)) {
                lockerOld.getCupoList().remove(cupo);
                lockerOld = em.merge(lockerOld);
            }
            if (lockerNew != null && !lockerNew.equals(lockerOld)) {
                lockerNew.getCupoList().add(cupo);
                lockerNew = em.merge(lockerNew);
            }
            if (placaOld != null && !placaOld.equals(placaNew)) {
                placaOld.getCupoList().remove(cupo);
                placaOld = em.merge(placaOld);
            }
            if (placaNew != null && !placaNew.equals(placaOld)) {
                placaNew.getCupoList().add(cupo);
                placaNew = em.merge(placaNew);
            }
            for (CobroDiario cobroDiarioListNewCobroDiario : cobroDiarioListNew) {
                if (!cobroDiarioListOld.contains(cobroDiarioListNewCobroDiario)) {
                    Cupo oldCupoOfCobroDiarioListNewCobroDiario = cobroDiarioListNewCobroDiario.getCupo();
                    cobroDiarioListNewCobroDiario.setCupo(cupo);
                    cobroDiarioListNewCobroDiario = em.merge(cobroDiarioListNewCobroDiario);
                    if (oldCupoOfCobroDiarioListNewCobroDiario != null && !oldCupoOfCobroDiarioListNewCobroDiario.equals(cupo)) {
                        oldCupoOfCobroDiarioListNewCobroDiario.getCobroDiarioList().remove(cobroDiarioListNewCobroDiario);
                        oldCupoOfCobroDiarioListNewCobroDiario = em.merge(oldCupoOfCobroDiarioListNewCobroDiario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CupoPK id = cupo.getCupoPK();
                if (findCupo(id) == null) {
                    throw new NonexistentEntityException("The cupo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CupoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cupo cupo;
            try {
                cupo = em.getReference(Cupo.class, id);
                cupo.getCupoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cupo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CobroDiario> cobroDiarioListOrphanCheck = cupo.getCobroDiarioList();
            for (CobroDiario cobroDiarioListOrphanCheckCobroDiario : cobroDiarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cupo (" + cupo + ") cannot be destroyed since the CobroDiario " + cobroDiarioListOrphanCheckCobroDiario + " in its cobroDiarioList field has a non-nullable cupo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Locker locker = cupo.getLocker();
            if (locker != null) {
                locker.getCupoList().remove(cupo);
                locker = em.merge(locker);
            }
            UsuarioDiario placa = cupo.getPlaca();
            if (placa != null) {
                placa.getCupoList().remove(cupo);
                placa = em.merge(placa);
            }
            em.remove(cupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cupo> findCupoEntities() {
        return findCupoEntities(true, -1, -1);
    }

    public List<Cupo> findCupoEntities(int maxResults, int firstResult) {
        return findCupoEntities(false, maxResults, firstResult);
    }

    private List<Cupo> findCupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cupo.class));
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

    public Cupo findCupo(CupoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cupo> rt = cq.from(Cupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
