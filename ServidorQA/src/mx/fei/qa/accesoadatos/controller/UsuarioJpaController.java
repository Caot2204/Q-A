/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.accesoadatos.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mx.fei.qa.accesoadatos.controller.exceptions.IllegalOrphanException;
import mx.fei.qa.accesoadatos.controller.exceptions.NonexistentEntityException;
import mx.fei.qa.accesoadatos.controller.exceptions.PreexistingEntityException;
import mx.fei.qa.accesoadatos.entity.Cuestionario;
import mx.fei.qa.accesoadatos.entity.Usuario;

/**
 *
 * @author Carlos Onorio
 */
public class UsuarioJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getCuestionarioCollection() == null) {
            usuario.setCuestionarioCollection(new ArrayList<Cuestionario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Cuestionario> attachedCuestionarioCollection = new ArrayList<Cuestionario>();
            for (Cuestionario cuestionarioCollectionCuestionarioToAttach : usuario.getCuestionarioCollection()) {
                cuestionarioCollectionCuestionarioToAttach = em.getReference(cuestionarioCollectionCuestionarioToAttach.getClass(), cuestionarioCollectionCuestionarioToAttach.getId());
                attachedCuestionarioCollection.add(cuestionarioCollectionCuestionarioToAttach);
            }
            usuario.setCuestionarioCollection(attachedCuestionarioCollection);
            em.persist(usuario);
            for (Cuestionario cuestionarioCollectionCuestionario : usuario.getCuestionarioCollection()) {
                Usuario oldAutorOfCuestionarioCollectionCuestionario = cuestionarioCollectionCuestionario.getAutor();
                cuestionarioCollectionCuestionario.setAutor(usuario);
                cuestionarioCollectionCuestionario = em.merge(cuestionarioCollectionCuestionario);
                if (oldAutorOfCuestionarioCollectionCuestionario != null) {
                    oldAutorOfCuestionarioCollectionCuestionario.getCuestionarioCollection().remove(cuestionarioCollectionCuestionario);
                    oldAutorOfCuestionarioCollectionCuestionario = em.merge(oldAutorOfCuestionarioCollectionCuestionario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getNombre()) != null) {
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
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getNombre());
            Collection<Cuestionario> cuestionarioCollectionOld = persistentUsuario.getCuestionarioCollection();
            Collection<Cuestionario> cuestionarioCollectionNew = usuario.getCuestionarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Cuestionario cuestionarioCollectionOldCuestionario : cuestionarioCollectionOld) {
                if (!cuestionarioCollectionNew.contains(cuestionarioCollectionOldCuestionario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cuestionario " + cuestionarioCollectionOldCuestionario + " since its autor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Cuestionario> attachedCuestionarioCollectionNew = new ArrayList<Cuestionario>();
            for (Cuestionario cuestionarioCollectionNewCuestionarioToAttach : cuestionarioCollectionNew) {
                cuestionarioCollectionNewCuestionarioToAttach = em.getReference(cuestionarioCollectionNewCuestionarioToAttach.getClass(), cuestionarioCollectionNewCuestionarioToAttach.getId());
                attachedCuestionarioCollectionNew.add(cuestionarioCollectionNewCuestionarioToAttach);
            }
            cuestionarioCollectionNew = attachedCuestionarioCollectionNew;
            usuario.setCuestionarioCollection(cuestionarioCollectionNew);
            usuario = em.merge(usuario);
            for (Cuestionario cuestionarioCollectionNewCuestionario : cuestionarioCollectionNew) {
                if (!cuestionarioCollectionOld.contains(cuestionarioCollectionNewCuestionario)) {
                    Usuario oldAutorOfCuestionarioCollectionNewCuestionario = cuestionarioCollectionNewCuestionario.getAutor();
                    cuestionarioCollectionNewCuestionario.setAutor(usuario);
                    cuestionarioCollectionNewCuestionario = em.merge(cuestionarioCollectionNewCuestionario);
                    if (oldAutorOfCuestionarioCollectionNewCuestionario != null && !oldAutorOfCuestionarioCollectionNewCuestionario.equals(usuario)) {
                        oldAutorOfCuestionarioCollectionNewCuestionario.getCuestionarioCollection().remove(cuestionarioCollectionNewCuestionario);
                        oldAutorOfCuestionarioCollectionNewCuestionario = em.merge(oldAutorOfCuestionarioCollectionNewCuestionario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getNombre();
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
                usuario.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Cuestionario> cuestionarioCollectionOrphanCheck = usuario.getCuestionarioCollection();
            for (Cuestionario cuestionarioCollectionOrphanCheckCuestionario : cuestionarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Cuestionario " + cuestionarioCollectionOrphanCheckCuestionario + " in its cuestionarioCollection field has a non-nullable autor field.");
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
