package ma.projet.test;

import ma.projet.classes.Produit;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;
import java.sql.Date;

public class TestMachine {
    public static void main(String[] args) {
        try {
            ProduitService ps = new ProduitService();
            for (Produit p : ps.findByDateBetween(Date.valueOf("2013-01-01"), Date.valueOf("2013-12-31"))) {
                System.out.println(p);
            }
        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }
}
