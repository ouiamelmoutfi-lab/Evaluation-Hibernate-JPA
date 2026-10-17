package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;
import java.util.Locale;

public class TestProjet {
    public static void main(String[] args) {
        try {
            executerDemonstration();
        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }

    public static void executerDemonstration() {
        EmployeService es = new EmployeService();
        ProjetService ps = new ProjetService();
        TacheService ts = new TacheService();
        EmployeTacheService ets = new EmployeTacheService();

        Employe sara = new Employe("BENAni", "SARA", "0600000001");
        Employe youssef = new Employe("AMRANI", "YOUSSEF", "0600000002");
        Employe karim = new Employe("ALAMI", "KARIM", "0600000003");
        Employe nadia = new Employe("SAADI", "NADIA", "0600000004");
        for (Employe employe : new Employe[]{sara, youssef, karim, nadia}) {
            verifierCreation(es.create(employe));
        }

        Projet site = new Projet("Site web", LocalDate.of(2012, 1, 1), LocalDate.of(2012, 12, 31), sara);
        Projet mobile = new Projet("Application mobile", LocalDate.of(2012, 1, 1), LocalDate.of(2012, 12, 31), youssef);
        Projet audit = new Projet("Audit", LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31), karim);
        Projet stock = new Projet("Gestion de stock", LocalDate.of(2013, 1, 14), LocalDate.of(2013, 5, 1), sara);
        for (Projet projet : new Projet[]{site, mobile, audit, stock}) {
            projet.getChefProjet().addProjet(projet);
            verifierCreation(ps.create(projet));
        }

        Tache specifications = creerTache(ts, site, "Spécifications", 500);
        Tache maquette = creerTache(ts, site, "Maquette", 1000);
        creerTache(ts, site, "Développement web", 1800);
        creerTache(ts, mobile, "Cadrage", 900);
        Tache design = creerTache(ts, mobile, "Design", 1200);
        creerTache(ts, mobile, "Développement mobile", 2500);
        creerTache(ts, mobile, "Tests", 1000);
        creerTache(ts, audit, "Entretiens", 300);
        creerTache(ts, audit, "Analyse audit", 900);
        Tache rapport = creerTache(ts, audit, "Rapport", 1100);
        creerTache(ts, audit, "Suivi", 800);

        // Sur une base neuve, le projet stock a l'id 4 et ces tâches ont les ids 12, 13 et 14.
        // Toutes les relations utilisent les identifiants générés, sans les coder en dur.
        Tache analyse = new Tache("Analyse", LocalDate.of(2013, 2, 1), LocalDate.of(2013, 2, 28), 1200, stock);
        Tache conception = new Tache("Conception", LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 31), 1500, stock);
        Tache developpement = new Tache("Développement", LocalDate.of(2013, 4, 1), LocalDate.of(2013, 4, 30), 5000, stock);
        for (Tache tache : new Tache[]{analyse, conception, developpement}) {
            stock.addTache(tache);
            verifierCreation(ts.create(tache));
        }

        affecter(ets, sara, specifications, LocalDate.of(2012, 5, 1), LocalDate.of(2012, 5, 3));
        affecter(ets, youssef, maquette, LocalDate.of(2012, 5, 4), null);
        affecter(ets, nadia, design, null, null);
        affecter(ets, karim, rapport, LocalDate.of(2013, 1, 15), LocalDate.of(2013, 1, 20));
        affecter(ets, sara, analyse, LocalDate.of(2013, 2, 10), LocalDate.of(2013, 2, 20));
        affecter(ets, youssef, analyse, LocalDate.of(2013, 2, 12), LocalDate.of(2013, 2, 18));
        affecter(ets, youssef, conception, LocalDate.of(2013, 3, 10), LocalDate.of(2013, 3, 15));
        affecter(ets, karim, developpement, LocalDate.of(2013, 4, 10), LocalDate.of(2013, 4, 25));

        System.out.println("\n4 employés, 4 projets, 14 tâches et 8 affectations créés.");
        System.out.println("\nTâches réalisées par " + sara.getNom() + " " + sara.getPrenom() + " :");
        es.afficherTachesRealisees(sara.getId());
        System.out.println("\nProjets gérés par " + sara.getNom() + " " + sara.getPrenom() + " :");
        es.afficherProjetsGeres(sara.getId());

        System.out.println("\nTâches planifiées pour Gestion de stock :");
        ps.afficherTachesPlanifiees(stock.getId());
        System.out.println("\nTâches réalisées avec leurs dates réelles :");
        ps.afficherTachesRealisees(stock.getId());

        System.out.println("\nTâches dont le prix est strictement supérieur à 1000 DH :");
        ts.afficherPrixSuperieurA1000();
        System.out.println("\nTâches réalisées entre le 01/02/2013 et le 30/04/2013 :");
        ts.afficherRealiseesEntreDates(LocalDate.of(2013, 2, 1), LocalDate.of(2013, 4, 30));

        System.out.printf(Locale.FRANCE, "%nCoût global de Gestion de stock : %.2f DH%n", ps.calculerCoutGlobal(stock.getId()));
        System.out.println("Temps cumulé des affectations terminées : "
                + ps.calculerTempsCumuleJours(stock.getId()) + " jours calendaires.");
    }

    private static Tache creerTache(TacheService service, Projet projet, String nom, double prix) {
        Tache tache = new Tache(nom, projet.getDateDebut(), projet.getDateFin(), prix, projet);
        projet.addTache(tache);
        verifierCreation(service.create(tache));
        return tache;
    }

    private static void affecter(EmployeTacheService service, Employe employe, Tache tache,
                                LocalDate debut, LocalDate fin) {
        EmployeTachePK pk = new EmployeTachePK(employe.getId(), tache.getId());
        EmployeTache affectation = new EmployeTache(pk, debut, fin, employe, tache);
        employe.addAffectation(affectation);
        tache.addAffectation(affectation);
        verifierCreation(service.create(affectation));
    }

    private static void verifierCreation(boolean reussie) {
        if (!reussie) {
            throw new IllegalStateException("L'enregistrement a échoué. Consultez l'erreur Hibernate.");
        }
    }
}
