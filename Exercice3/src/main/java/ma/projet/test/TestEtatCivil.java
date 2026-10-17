package ma.projet.test;

import ma.projet.beans.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import java.sql.Date;

public class TestEtatCivil {
    public static void main(String[] args) {
        try {
            executerDemonstration();
        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }

    public static void executerDemonstration() {
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();

        // Création de 10 femmes et 5 hommes
        Femme[] femmes = {
                new Femme("KARIMA", "ALAMI", "0600000001", "Rabat", Date.valueOf("1965-01-05")),
                new Femme("SALIMA", "RAMI", "0600000002", "Marrakech", Date.valueOf("1970-05-12")),
                new Femme("AMAL", "ALI", "0600000003", "Rabat", Date.valueOf("1975-06-15")),
                new Femme("WAFA", "ALAOUI", "0600000004", "Fès", Date.valueOf("1978-08-20")),
                new Femme("Meryem", "jirar", "068223398", "Rabat", Date.valueOf("1992-01-05")),
                new Femme("Nazha", "lamine", "068273298", "Casablanca", Date.valueOf("1990-08-09")),
                new Femme("Nadia", "Saadi", "0600000007", "Oujda", Date.valueOf("1991-04-18")),
                new Femme("Sara", "Benali", "0600000008", "Marrakech", Date.valueOf("1993-02-10")),
                new Femme("Hajar", "Safi", "0600000009", "Rabat", Date.valueOf("1995-07-11")),
                new Femme("Imane", "Alaoui", "0600000010", "Fès", Date.valueOf("1994-11-22"))
        };
        Homme[] hommes = {
                new Homme("SAFI", "SAID", "0610000001", "Marrakech", Date.valueOf("1960-02-10")),
                new Homme("Marwan", "chafiqi", "063777288", "Marrakech", Date.valueOf("1980-06-02")),
                new Homme("Yasser", "Boudad", "06373348", "Oujda", Date.valueOf("1979-02-10")),
                new Homme("Ali", "Rami", "0610000004", "Rabat", Date.valueOf("1985-09-12")),
                new Homme("Karim", "Benali", "0610000005", "Fès", Date.valueOf("1988-03-25"))
        };
        for (Femme femme : femmes) verifierCreation(fs.create(femme));
        for (Homme homme : hommes) verifierCreation(hs.create(homme));
        marier(ms, hommes[0], femmes[0], "1989-09-03", "1990-09-03", 0);
        marier(ms, hommes[0], femmes[1], "1990-09-03", null, 4);
        marier(ms, hommes[0], femmes[2], "1995-09-03", null, 2);
        marier(ms, hommes[0], femmes[3], "2000-11-04", null, 3);
        marier(ms, hommes[1], femmes[0], "2001-01-01", null, 1);
        marier(ms, hommes[1], femmes[4], "2015-03-02", null, 2);
        marier(ms, hommes[1], femmes[5], "2016-04-10", null, 1);
        marier(ms, hommes[1], femmes[6], "2017-05-15", null, 0);
        marier(ms, hommes[2], femmes[7], "2013-02-01", "2014-03-01", 1);
        marier(ms, hommes[3], femmes[7], "2015-04-01", null, 2);
        marier(ms, hommes[4], femmes[8], "2016-06-01", null, 0);

        System.out.println("Liste des femmes :");
        for (Femme femme : fs.findAll()) System.out.println(femme);
        System.out.println("Femme la plus âgée : " + fs.findPlusAgee());
        System.out.println("Épouses de SAFI SAID entre les deux dates :");
        hs.afficherEpousesEntreDates(hommes[0].getId(), Date.valueOf("1989-01-01"), Date.valueOf("2020-12-31"));
        fs.afficherNombreEnfantsEntreDates(femmes[7].getId(), Date.valueOf("2010-01-01"), Date.valueOf("2020-12-31"));
        System.out.println("Femmes mariées deux fois ou plus :");
        fs.afficherMarieesAuMoinsDeuxFois();
        hs.afficherHommesMariesAQuatreFemmes(Date.valueOf("2017-01-01"), Date.valueOf("2020-12-31"));
        ms.afficherMariages(hommes[0].getId());
    }

    private static void marier(MariageService ms, Homme homme, Femme femme, String debut, String fin, int enfants) {
        MariagePK pk = new MariagePK(Date.valueOf(debut), homme.getId(), femme.getId());
        verifierCreation(ms.create(new Mariage(pk, homme, femme, enfants, fin == null ? null : Date.valueOf(fin))));
    }

    private static void verifierCreation(boolean reussie) {
        if (!reussie) throw new IllegalStateException("L'enregistrement a échoué. Consultez l'erreur Hibernate.");
    }
}
