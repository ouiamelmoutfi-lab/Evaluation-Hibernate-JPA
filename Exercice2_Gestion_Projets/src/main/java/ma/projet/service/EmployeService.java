package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class EmployeService extends AbstractFacade<Employe> {
    public EmployeService() {
        super(Employe.class);
    }

    public List<Tache> findTachesRealisees(int employeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Employe.tachesRealisees", Tache.class)
                    .setParameter("employeId", employeId).getResultList();
        }
    }

    public List<Projet> findProjetsGeres(int employeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Employe.projetsGeres", Projet.class)
                    .setParameter("employeId", employeId).getResultList();
        }
    }

    public void afficherTachesRealisees(int employeId) {
        findTachesRealisees(employeId).forEach(System.out::println);
    }

    public void afficherProjetsGeres(int employeId) {
        findProjetsGeres(employeId).forEach(System.out::println);
    }
}
