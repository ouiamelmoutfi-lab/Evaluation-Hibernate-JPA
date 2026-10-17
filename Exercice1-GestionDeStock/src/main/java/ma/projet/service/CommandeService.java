package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CommandeService implements IDao<Commande> {

    @Override
    public void create(Commande commande) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.save(commande);

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
    public void update(Commande commande) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.update(commande);

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
    public void delete(Commande commande) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.delete(commande);

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
    public Commande findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.get(Commande.class, id);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }

    @Override
    public List<Commande> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery(
                    "FROM Commande",
                    Commande.class
            ).list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }
}
