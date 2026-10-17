package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

import java.util.List;

public class ProduitServiceTest {

     public static void main(String[] args){

         try {
         CategorieService cs = new CategorieService();
         ProduitService ps = new ProduitService();

         Categorie c =new Categorie("C01","Info");
         cs.create(c);
         Produit p1 = new Produit("P01", 80);
         Produit p2 = new Produit("P02", 150);
         Produit p3 = new Produit("P03", 200);

         p1.setCategorie(c);
         p2.setCategorie(c);
         p3.setCategorie(c);

         ps.create(p1);
         ps.create(p2);
         ps.create(p3);

         List<Produit> produits = ps.findByCategorie(c);

         System.out.println("Produits de la catégorie : " + c.getLibelle());

         for (Produit p : produits) {
             System.out.println(
                     "Référence : " + p.getReference()
                             + " | Prix : " + p.getPrix() + " DH"
             );
         }
         } finally {
             HibernateUtil.getSessionFactory().close();
         }
     }

}
