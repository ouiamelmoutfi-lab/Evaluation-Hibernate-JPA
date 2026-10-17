package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.LigneCommandeId;
import ma.projet.util.HibernateUtil;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LigneCommandeService implements IDao<LigneCommandeProduit> {

    public void create(LigneCommandeProduit ligneCommande) {
        verifierLigne(ligneCommande);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.save(ligneCommande);

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

    public void update(LigneCommandeProduit ligneCommande) {
        verifierLigne(ligneCommande);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.update(ligneCommande);

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

    public void delete(LigneCommandeProduit ligneCommande) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.delete(ligneCommande);

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

    public LigneCommandeProduit findById(LigneCommandeId id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.get(LigneCommandeProduit.class, id);

        } finally {
            session.close();
        }
    }

    public List<LigneCommandeProduit> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery(
                    "FROM LigneCommandeProduit",
                    LigneCommandeProduit.class
            ).list();

        } finally {
            session.close();
        }
    }

    @Override
    public LigneCommandeProduit findById(int id) {
        throw new IllegalArgumentException("Utilisez findById(LigneCommandeId) pour une ligne de commande.");
    }


    private void verifierLigne(LigneCommandeProduit ligne) {
        if (ligne == null || ligne.getPk() == null || ligne.getCommande() == null || ligne.getProduit() == null
                || ligne.getCommande().getId() == 0 || ligne.getProduit().getId() == 0
                || ligne.getPk().getCommande() != ligne.getCommande().getId()
                || ligne.getPk().getProduit() != ligne.getProduit().getId()) {
            throw new IllegalArgumentException("La clé doit correspondre à la commande et au produit enregistrés.");
        }
        if (ligne.getQuantite() <= 0) throw new IllegalArgumentException("La quantité doit être supérieure à zéro.");
    }
}
