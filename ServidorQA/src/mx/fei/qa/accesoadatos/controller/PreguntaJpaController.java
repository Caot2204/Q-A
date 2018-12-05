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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mx.fei.qa.accesoadatos.controller.exceptions.NonexistentEntityException;
import mx.fei.qa.accesoadatos.controller.exceptions.PreexistingEntityException;
import mx.fei.qa.accesoadatos.entity.Cuestionario;
import mx.fei.qa.accesoadatos.entity.Pregunta;
import mx.fei.qa.accesoadatos.entity.PreguntaPK;

/**
 *
 * @author Carlos Onorio
 */
public class PreguntaJpaController implements Serializable {

    public PreguntaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(long idCuestionario, Pregunta pregunta) throws PreexistingEntityException, Exception {
        if (pregunta.getPreguntaPK() == null) {
            pregunta.setPreguntaPK(new PreguntaPK());
        }
        pregunta.getPreguntaPK().setIdCuestionario(idCuestionario);
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuestionario cuestionario = pregunta.getCuestionario();
            if (cuestionario != null) {
                cuestionario = em.getReference(cuestionario.getClass(), cuestionario.getId());
                pregunta.setCuestionario(cuestionario);
            }
            em.persist(pregunta);
            if (cuestionario != null) {
                cuestionario.getPreguntaCollection().add(pregunta);
                cuestionario = em.merge(cuestionario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPregunta(pregunta.getPreguntaPK()) != null) {
                throw new PreexistingEntityException("Pregunta " + pregunta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pregunta pregunta) throws NonexistentEntityException, Exception {
        pregunta.getPreguntaPK().setIdCuestionario(pregunta.getCuestionario().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta persistentPregunta = em.find(Pregunta.class, pregunta.getPreguntaPK());
            Cuestionario cuestionarioOld = persistentPregunta.getCuestionario();
            Cuestionario cuestionarioNew = pregunta.getCuestionario();
            if (cuestionarioNew != null) {
                cuestionarioNew = em.getReference(cuestionarioNew.getClass(), cuestionarioNew.getId());
                pregunta.setCuestionario(cuestionarioNew);
            }
            pregunta = em.merge(pregunta);
            if (cuestionarioOld != null && !cuestionarioOld.equals(cuestionarioNew)) {
                cuestionarioOld.getPreguntaCollection().remove(pregunta);
                cuestionarioOld = em.merge(cuestionarioOld);
            }
            if (cuestionarioNew != null && !cuestionarioNew.equals(cuestionarioOld)) {
                cuestionarioNew.getPreguntaCollection().add(pregunta);
                cuestionarioNew = em.merge(cuestionarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PreguntaPK id = pregunta.getPreguntaPK();
                if (findPregunta(id) == null) {
                    throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PreguntaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta pregunta;
            try {
                pregunta = em.getReference(Pregunta.class, id);
                pregunta.getPreguntaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.", enfe);
            }
            Cuestionario cuestionario = pregunta.getCuestionario();
            if (cuestionario != null) {
                cuestionario.getPreguntaCollection().remove(pregunta);
                cuestionario = em.merge(cuestionario);
            }
            em.remove(pregunta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pregunta> findPreguntaEntities() {
        return findPreguntaEntities(true, -1, -1);
    }

    public List<Pregunta> findPreguntaEntities(int maxResults, int firstResult) {
        return findPreguntaEntities(false, maxResults, firstResult);
    }

    private List<Pregunta> findPreguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pregunta.class));
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

    public Pregunta findPregunta(PreguntaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pregunta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPreguntaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pregunta> rt = cq.from(Pregunta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Pregunta> getPreguntasDeCuestionario(long idCuestionario) {
        EntityManager entityManager = getEntityManager();
        Query consulta = entityManager.createNamedQuery("Pregunta.findByIdCuestionario");
        consulta.setParameter("idCuestionario", idCuestionario);
        return consulta.getResultList();
    }

}
