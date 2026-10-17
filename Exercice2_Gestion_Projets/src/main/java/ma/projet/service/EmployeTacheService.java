package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.EmployeTachePK;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Objects;

public class EmployeTacheService extends AbstractFacade<EmployeTache> {
    public EmployeTacheService() {
        super(EmployeTache.class);
    }

    public EmployeTache findById(EmployeTachePK pk) {
        if (pk == null) throw new IllegalArgumentException("La clé de l'affectation est obligatoire.");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(EmployeTache.class, pk);
        }
    }

    @Override
    public EmployeTache findById(int id) {
        throw new IllegalArgumentException("Utilisez findById(EmployeTachePK) pour une affectation.");
    }

    @Override
    public boolean create(EmployeTache affectation) {
        verifierAffectation(affectation);
        return super.create(affectation);
    }

    @Override
    public boolean update(EmployeTache affectation) {
        verifierAffectation(affectation);
        return super.update(affectation);
    }

    private void verifierAffectation(EmployeTache affectation) {
        if (affectation == null || affectation.getPk() == null || affectation.getEmploye() == null
                || affectation.getTache() == null || affectation.getEmploye().getId() == null
                || affectation.getTache().getId() == null) {
            throw new IllegalArgumentException("L'affectation doit relier un employé et une tâche déjà enregistrés.");
        }
        if (!Objects.equals(affectation.getPk().getEmployeId(), affectation.getEmploye().getId())
                || !Objects.equals(affectation.getPk().getTacheId(), affectation.getTache().getId())) {
            throw new IllegalArgumentException("La clé doit correspondre à l'employé et à la tâche.");
        }
        if (affectation.getDateFinReelle() != null) {
            verifierPeriode(affectation.getDateDebutReelle(), affectation.getDateFinReelle());
        }
    }
}
