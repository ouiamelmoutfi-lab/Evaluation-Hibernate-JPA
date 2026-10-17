package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractFacade<T> implements IDao <T> {
    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public boolean create(T o) {
        Session session = null;
        Transaction tx = null;
        boolean etat = false;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(o);

            tx.commit();
            etat = true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace(); // utile pour voir l'erreur
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return etat;
    }

    @Override
    public boolean delete(T o) {
        Session session = null;
        Transaction tx = null;
        boolean etat = false;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.delete(o);

            tx.commit();
            etat = true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return etat;
    }

    @Override
    public boolean update(T o) {
        Session session = null;
        Transaction tx = null;
        boolean etat = false;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.update(o);

            tx.commit();
            etat = true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return etat;
    }

    public T findById(int id) {
        Session session = null;
        Transaction tx = null;
        T obj = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            obj = session.get(entityClass, id);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return obj;
    }


    public List<T> findAll() {
        Session session = null;
        Transaction tx = null;
        List<T> list = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            list = session.createQuery("from " + entityClass.getSimpleName(), entityClass).list();

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return list;
    }

    // Une période est valide lorsque les deux dates existent et sont dans l'ordre.
    protected void verifierPeriode(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || debut.isAfter(fin)) {
            throw new IllegalArgumentException("La période doit avoir un début inférieur ou égal à la fin.");
        }
    }

}
