/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.rumos.rumos.the.movie.database.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import pt.rumos.rumos.the.movie.BestFilmsForYearResponse;
import pt.rumos.rumos.the.movie.database.MoviesDB;
import pt.rumos.rumos.the.movie.exceptions.NonexistentEntityException;
import pt.rumos.rumos.the.movie.exceptions.PreexistingEntityException;
import pt.rumos.rumos.the.movie.exceptions.RollbackFailureException;

/**
 *
 * @author mariosilva
 */
public class MoviesDBJpaController implements Serializable {

    public MoviesDBJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public MoviesDBJpaController(EntityManagerFactory emf) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MoviesDB moviesDB) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(moviesDB);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMoviesDB(moviesDB.getName()) != null) {
                throw new PreexistingEntityException("MoviesDB " + moviesDB + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MoviesDB moviesDB) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            moviesDB = em.merge(moviesDB);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Object id = moviesDB.getName();
                if (findMoviesDB(id) == null) {
                    throw new NonexistentEntityException("The moviesDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Object id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MoviesDB moviesDB;
            try {
                moviesDB = em.getReference(MoviesDB.class, id);
                moviesDB.getName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The moviesDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(moviesDB);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MoviesDB> findMoviesDBEntities() {
        return findMoviesDBEntities(true, -1, -1);
    }

    public List<MoviesDB> findMoviesDBEntities(int maxResults, int firstResult) {
        return findMoviesDBEntities(false, maxResults, firstResult);
    }

    private List<MoviesDB> findMoviesDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MoviesDB.class));
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

    public MoviesDB findMoviesDB(Object id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MoviesDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getMoviesDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MoviesDB> rt = cq.from(MoviesDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void create(BestFilmsForYearResponse.Movie movie) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
