/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accesoadatos.controller;

import accesoadatos.controller.exceptions.IllegalOrphanException;
import accesoadatos.controller.exceptions.NonexistentEntityException;
import accesoadatos.entity.Cuestionario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import accesoadatos.entity.Usuario;
import accesoadatos.entity.Pregunta;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Carlos Onorio
 */
public class CuestionarioJpaController implements Serializable {

    public CuestionarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuestionario cuestionario) {
        if (cuestionario.getPreguntaCollection() == null) {
            cuestionario.setPreguntaCollection(new ArrayList<Pregunta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario autor = cuestionario.getAutor();
            if (autor != null) {
                autor = em.getReference(autor.getClass(), autor.getNombre());
                cuestionario.setAutor(autor);
            }
            Collection<Pregunta> attachedPreguntaCollection = new ArrayList<Pregunta>();
            for (Pregunta preguntaCollectionPreguntaToAttach : cuestionario.getPreguntaCollection()) {
                preguntaCollectionPreguntaToAttach = em.getReference(preguntaCollectionPreguntaToAttach.getClass(), preguntaCollectionPreguntaToAttach.getPreguntaPK());
                attachedPreguntaCollection.add(preguntaCollectionPreguntaToAttach);
            }
            cuestionario.setPreguntaCollection(attachedPreguntaCollection);
            em.persist(cuestionario);
            if (autor != null) {
                autor.getCuestionarioCollection().add(cuestionario);
                autor = em.merge(autor);
            }
            for (Pregunta preguntaCollectionPregunta : cuestionario.getPreguntaCollection()) {
                Cuestionario oldCuestionarioOfPreguntaCollectionPregunta = preguntaCollectionPregunta.getCuestionario();
                preguntaCollectionPregunta.setCuestionario(cuestionario);
                preguntaCollectionPregunta = em.merge(preguntaCollectionPregunta);
                if (oldCuestionarioOfPreguntaCollectionPregunta != null) {
                    oldCuestionarioOfPreguntaCollectionPregunta.getPreguntaCollection().remove(preguntaCollectionPregunta);
                    oldCuestionarioOfPreguntaCollectionPregunta = em.merge(oldCuestionarioOfPreguntaCollectionPregunta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuestionario cuestionario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuestionario persistentCuestionario = em.find(Cuestionario.class, cuestionario.getId());
            Usuario autorOld = persistentCuestionario.getAutor();
            Usuario autorNew = cuestionario.getAutor();
            Collection<Pregunta> preguntaCollectionOld = persistentCuestionario.getPreguntaCollection();
            Collection<Pregunta> preguntaCollectionNew = cuestionario.getPreguntaCollection();
            List<String> illegalOrphanMessages = null;
            for (Pregunta preguntaCollectionOldPregunta : preguntaCollectionOld) {
                if (!preguntaCollectionNew.contains(preguntaCollectionOldPregunta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pregunta " + preguntaCollectionOldPregunta + " since its cuestionario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (autorNew != null) {
                autorNew = em.getReference(autorNew.getClass(), autorNew.getNombre());
                cuestionario.setAutor(autorNew);
            }
            Collection<Pregunta> attachedPreguntaCollectionNew = new ArrayList<Pregunta>();
            for (Pregunta preguntaCollectionNewPreguntaToAttach : preguntaCollectionNew) {
                preguntaCollectionNewPreguntaToAttach = em.getReference(preguntaCollectionNewPreguntaToAttach.getClass(), preguntaCollectionNewPreguntaToAttach.getPreguntaPK());
                attachedPreguntaCollectionNew.add(preguntaCollectionNewPreguntaToAttach);
            }
            preguntaCollectionNew = attachedPreguntaCollectionNew;
            cuestionario.setPreguntaCollection(preguntaCollectionNew);
            cuestionario = em.merge(cuestionario);
            if (autorOld != null && !autorOld.equals(autorNew)) {
                autorOld.getCuestionarioCollection().remove(cuestionario);
                autorOld = em.merge(autorOld);
            }
            if (autorNew != null && !autorNew.equals(autorOld)) {
                autorNew.getCuestionarioCollection().add(cuestionario);
                autorNew = em.merge(autorNew);
            }
            for (Pregunta preguntaCollectionNewPregunta : preguntaCollectionNew) {
                if (!preguntaCollectionOld.contains(preguntaCollectionNewPregunta)) {
                    Cuestionario oldCuestionarioOfPreguntaCollectionNewPregunta = preguntaCollectionNewPregunta.getCuestionario();
                    preguntaCollectionNewPregunta.setCuestionario(cuestionario);
                    preguntaCollectionNewPregunta = em.merge(preguntaCollectionNewPregunta);
                    if (oldCuestionarioOfPreguntaCollectionNewPregunta != null && !oldCuestionarioOfPreguntaCollectionNewPregunta.equals(cuestionario)) {
                        oldCuestionarioOfPreguntaCollectionNewPregunta.getPreguntaCollection().remove(preguntaCollectionNewPregunta);
                        oldCuestionarioOfPreguntaCollectionNewPregunta = em.merge(oldCuestionarioOfPreguntaCollectionNewPregunta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cuestionario.getId();
                if (findCuestionario(id) == null) {
                    throw new NonexistentEntityException("The cuestionario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuestionario cuestionario;
            try {
                cuestionario = em.getReference(Cuestionario.class, id);
                cuestionario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuestionario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pregunta> preguntaCollectionOrphanCheck = cuestionario.getPreguntaCollection();
            for (Pregunta preguntaCollectionOrphanCheckPregunta : preguntaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuestionario (" + cuestionario + ") cannot be destroyed since the Pregunta " + preguntaCollectionOrphanCheckPregunta + " in its preguntaCollection field has a non-nullable cuestionario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario autor = cuestionario.getAutor();
            if (autor != null) {
                autor.getCuestionarioCollection().remove(cuestionario);
                autor = em.merge(autor);
            }
            em.remove(cuestionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cuestionario> findCuestionarioEntities() {
        return findCuestionarioEntities(true, -1, -1);
    }

    public List<Cuestionario> findCuestionarioEntities(int maxResults, int firstResult) {
        return findCuestionarioEntities(false, maxResults, firstResult);
    }

    private List<Cuestionario> findCuestionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuestionario.class));
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

    public Cuestionario findCuestionario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuestionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuestionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuestionario> rt = cq.from(Cuestionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
