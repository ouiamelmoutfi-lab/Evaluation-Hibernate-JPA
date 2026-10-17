package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class ProjetService extends AbstractFacade<Projet> {
    public ProjetService() {
        super(Projet.class);
    }

    @Override
    public boolean create(Projet projet) {
        verifierProjet(projet);
        return super.create(projet);
    }

    @Override
    public boolean update(Projet projet) {
        verifierProjet(projet);
        return super.update(projet);
    }

    private void verifierProjet(Projet projet) {
        if (projet == null || projet.getChefProjet() == null || projet.getChefProjet().getId() == null) {
            throw new IllegalArgumentException("Le projet doit avoir un chef déjà enregistré.");
        }
        verifierPeriode(projet.getDateDebut(), projet.getDateFin());
    }

    public List<Tache> findTachesPlanifiees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Projet.tachesPlanifiees", Tache.class)
                    .setParameter("projetId", projetId).getResultList();
        }
    }

    // Chaque ligne contient : id tâche, nom, début réel minimum, fin réelle maximum.
    public List<Object[]> findTachesRealisees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Projet.tachesRealisees", Object[].class)
                    .setParameter("projetId", projetId).getResultList();
        }
    }

    public void afficherTachesPlanifiees(int projetId) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Tache tache : findTachesPlanifiees(projetId)) {
            System.out.println(tache + " — du " + tache.getDateDebut().format(format)
                    + " au " + tache.getDateFin().format(format));
        }
    }

    public void afficherTachesRealisees(int projetId) {
        Projet projet = findById(projetId);
        if (projet == null) {
            System.out.println("Projet introuvable : " + projetId);
            return;
        }
        DateTimeFormatter entete = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom()
                + "     Date début : " + projet.getDateDebut().format(entete));
        System.out.println("Liste des tâches:");
        System.out.printf("%-4s %-20s %-20s %s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        List<Object[]> lignes = findTachesRealisees(projetId);
        for (Object[] ligne : lignes) {
            System.out.printf("%-4s %-20s %-20s %s%n", ligne[0], ligne[1],
                    ((LocalDate) ligne[2]).format(format), ((LocalDate) ligne[3]).format(format));
        }
        if (lignes.isEmpty()) System.out.println("Aucune tâche terminée.");
    }

    public double calculerCoutGlobal(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Le prix d'une tâche est compté une seule fois, même si elle a plusieurs employés.
            return session.createNamedQuery("Projet.coutGlobal", Double.class)
                    .setParameter("projetId", projetId).getSingleResult();
        }
    }

    public long calculerTempsCumuleJours(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<EmployeTache> affectations = session.createNamedQuery(
                            "EmployeTache.realiseesParProjet", EmployeTache.class)
                    .setParameter("projetId", projetId).getResultList();
            long total = 0;
            for (EmployeTache affectation : affectations) {
                total += ChronoUnit.DAYS.between(affectation.getDateDebutReelle(), affectation.getDateFinReelle());
            }
            return total;
        }
    }

    @Override
    public boolean delete(Projet projet) {
        if (projet == null || projet.getId() == null) return false;
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Projet existant = session.get(Projet.class, projet.getId());
            if (existant == null) {
                tx.commit();
                return false;
            }
            // Composition : supprimer d'abord les affectations, puis les tâches du projet.
            session.createNamedQuery("EmployeTache.supprimerParProjet")
                    .setParameter("projetId", projet.getId()).executeUpdate();
            session.createNamedQuery("Tache.supprimerParProjet")
                    .setParameter("projetId", projet.getId()).executeUpdate();
            session.delete(existant);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
}
