package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class ProduitService implements IDao<Produit> {

    @Override
    public void create(Produit produit) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(produit);
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
    public void update(Produit produit) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(produit);
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
    public void delete(Produit produit) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(produit);
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
    public Produit findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.get(Produit.class, id);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }

    @Override
    public List<Produit> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery(
                    "FROM Produit",
                    Produit.class
            ).list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }

    // 1. Produits par catégorie
    public List<Produit> findByCategorie(Categorie categorie) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery(
                            "FROM Produit p WHERE p.categorie = :categorie",
                            Produit.class
                    )
                    .setParameter("categorie", categorie)
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }

    // 2. Produits commandés entre deux dates
    public List<Produit> findByDateBetween(Date date1, Date date2) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery(
                            "SELECT DISTINCT l.produit " +
                                    "FROM LigneCommandeProduit l " +
                                    "WHERE l.commande.date BETWEEN :date1 AND :date2",
                            Produit.class
                    )
                    .setParameter("date1", date1)
                    .setParameter("date2", date2)
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }

    // 3. Produits commandés dans une commande donnée
    public List<Produit> findByCommande(int commandeId) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return session.createQuery(
                            "SELECT l.produit " +
                                    "FROM LigneCommandeProduit l " +
                                    "WHERE l.commande.id = :commandeId",
                            Produit.class
                    )
                    .setParameter("commandeId", commandeId)
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }



    // 4. Produits dont le prix est supérieur à 100 DH
    public List<Produit> findPrixSuperieurA100() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createNamedQuery("Produit.findByPrixSuperieur100", Produit.class).list();
        } finally {
            session.close();
        }
    }

    public void afficherProduitsParCategorie(Categorie categorie) {
        System.out.println("Produits de la catégorie : " + categorie.getLibelle());
        for (Produit produit : findByCategorie(categorie)) {
            System.out.println("Référence : " + produit.getReference() + " | Prix : " + produit.getPrix() + " DH");
        }
    }

    public void afficherProduitsEntreDates(Date debut, Date fin) {
        for (Produit produit : findByDateBetween(debut, fin)) {
            System.out.println(produit);
        }
    }

    public void afficherPrixSuperieurA100() {
        for (Produit produit : findPrixSuperieurA100()) {
            System.out.println(produit);
        }
    }

    public void afficherProduitsCommande(int commandeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Commande commande = session.get(Commande.class, commandeId);
            if (commande == null) {
                System.out.println("Commande introuvable : " + commandeId);
                return;
            }
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            System.out.println("Commande : " + commande.getId() + "     Date : " + format.format(commande.getDate()));
            System.out.println("Liste des produits :");
            System.out.printf("%-12s %-12s %s%n", "Référence", "Prix", "Quantité");
            List<LigneCommandeProduit> lignes = session.createQuery(
                    "FROM LigneCommandeProduit l WHERE l.commande.id = :commandeId ORDER BY l.produit.id",
                    LigneCommandeProduit.class).setParameter("commandeId", commandeId).list();
            DecimalFormat prix = new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.FRENCH));
            for (LigneCommandeProduit ligne : lignes) {
                System.out.printf(Locale.FRENCH, "%-12s %-12s %d%n", ligne.getProduit().getReference(),
                        prix.format(ligne.getProduit().getPrix()) + " DH", ligne.getQuantite());
            }
        } finally {
            session.close();
        }
    }

}
