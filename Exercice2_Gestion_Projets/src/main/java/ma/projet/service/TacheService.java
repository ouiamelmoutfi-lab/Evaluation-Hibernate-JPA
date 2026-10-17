package ma.projet.service;

import ma.projet.classes.Tache;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class TacheService extends AbstractFacade<Tache> {
    public TacheService() {
        super(Tache.class);
    }

    @Override
    public boolean create(Tache tache) {
        verifierTache(tache);
        return super.create(tache);
    }

    @Override
    public boolean update(Tache tache) {
        verifierTache(tache);
        return super.update(tache);
    }

    private void verifierTache(Tache tache) {
        if (tache == null || tache.getProjet() == null || tache.getProjet().getId() == null) {
            throw new IllegalArgumentException("La tâche doit appartenir à un projet déjà enregistré.");
        }
        verifierPeriode(tache.getDateDebut(), tache.getDateFin());
        if (!Double.isFinite(tache.getPrix()) || tache.getPrix() < 0) {
            throw new IllegalArgumentException("Le prix doit être un nombre positif ou nul.");
        }
    }

    public List<Tache> findPrixSuperieurA1000() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Tache.prixSuperieurA1000", Tache.class).getResultList();
        }
    }

    public List<Tache> findRealiseesEntreDates(LocalDate debut, LocalDate fin) {
        verifierPeriode(debut, fin);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Tache.realiseesEntreDates", Tache.class)
                    .setParameter("debut", debut).setParameter("fin", fin).getResultList();
        }
    }

    public void afficherPrixSuperieurA1000() {
        findPrixSuperieurA1000().forEach(System.out::println);
    }

    public void afficherRealiseesEntreDates(LocalDate debut, LocalDate fin) {
        findRealiseesEntreDates(debut, fin).forEach(System.out::println);
    }

    @Override
    public boolean delete(Tache tache) {
        if (tache == null || tache.getId() == null) return false;
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Tache existante = session.get(Tache.class, tache.getId());
            if (existante == null) {
                tx.commit();
                return false;
            }
            session.createNamedQuery("EmployeTache.supprimerParTache")
                    .setParameter("tacheId", tache.getId()).executeUpdate();
            session.delete(existante);
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
