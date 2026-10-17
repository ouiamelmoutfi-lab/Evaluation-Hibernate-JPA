package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import java.sql.Date;

public class TestStock {
    public static void main(String[] args) {
        try {
            executerDemonstration();
        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }

    public static void executerDemonstration() {
        CategorieService cs = new CategorieService();
        ProduitService ps = new ProduitService();
        CommandeService cms = new CommandeService();
        LigneCommandeService ls = new LigneCommandeService();

        // Création des catégories et des produits
        Categorie categorie = new Categorie("C01", "Informatique");
        cs.create(categorie);
        Produit p1 = new Produit("ES12", 120);
        Produit p2 = new Produit("ZR85", 100);
        Produit p3 = new Produit("EE85", 200);
        for (Produit produit : new Produit[]{p1, p2, p3}) {
            produit.setCategorie(categorie);
            ps.create(produit);
        }

        // Création d'une commande et de ses lignes
        Commande commande = new Commande(Date.valueOf("2013-03-14"));
        cms.create(commande);
        ls.create(new LigneCommandeProduit(new LigneCommandeId(commande.getId(), p1.getId()), 7, commande, p1));
        ls.create(new LigneCommandeProduit(new LigneCommandeId(commande.getId(), p2.getId()), 14, commande, p2));
        ls.create(new LigneCommandeProduit(new LigneCommandeId(commande.getId(), p3.getId()), 5, commande, p3));

        ps.afficherProduitsParCategorie(categorie);
        System.out.println("Produits commandés entre les deux dates :");
        ps.afficherProduitsEntreDates(Date.valueOf("2013-01-01"), Date.valueOf("2013-12-31"));
        ps.afficherProduitsCommande(commande.getId());
        System.out.println("Produits dont le prix est supérieur à 100 DH :");
        ps.afficherPrixSuperieurA100();
    }
}
