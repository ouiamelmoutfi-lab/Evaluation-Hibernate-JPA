package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CategorieService implements IDao<Categorie> {

    @Override
    public void create(Categorie categorie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.save(categorie);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();

        } finally {
            session.close();
        }
    }

    @Override
    public void update(Categorie categorie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.update(categorie);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();

        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Categorie categorie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.delete(categorie);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();

        } finally {
            session.close();
        }
    }

    @Override
    public Categorie findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.get(Categorie.class, id);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }

    @Override
    public List<Categorie> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery(
                    "FROM Categorie",
                    Categorie.class
            ).list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }
}
