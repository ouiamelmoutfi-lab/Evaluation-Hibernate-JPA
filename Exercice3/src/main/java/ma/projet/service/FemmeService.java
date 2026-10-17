package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class FemmeService extends AbstractFacade <Femme>{

    public FemmeService(){
        super(Femme.class);
    }

    public Femme findPlusAgee() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Femme> femmes = session.createQuery(
                    "FROM Femme f WHERE f.dateNaissance IS NOT NULL ORDER BY f.dateNaissance, f.id", Femme.class)
                    .setMaxResults(1).list();
            return femmes.isEmpty() ? null : femmes.get(0);
        }
    }

    public long nombreEnfantsEntreDates(int femmeId, Date debut, Date fin) {
        verifierPeriode(debut, fin);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Number nombre = (Number) session.getNamedNativeQuery("Femme.nombreEnfantsEntreDates")
                    .setParameter("femmeId", femmeId).setParameter("debut", debut)
                    .setParameter("fin", fin).getSingleResult();
            return nombre.longValue();
        }
    }

    public List<Femme> findMarieesAuMoinsDeuxFois() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Femme.marieesAuMoinsDeuxFois", Femme.class).list();
        }
    }

    public void afficherNombreEnfantsEntreDates(int femmeId, Date debut, Date fin) {
        System.out.println("Nombre d'enfants : " + nombreEnfantsEntreDates(femmeId, debut, fin));
    }

    public void afficherMarieesAuMoinsDeuxFois() {
        for (Femme femme : findMarieesAuMoinsDeuxFois()) {
            System.out.println(femme);
        }
    }

}
