package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;

public class HommeService extends AbstractFacade<Homme>{
    public HommeService() {
        super(Homme.class);


    }

    public List<Femme> findEpousesEntreDates(int hommeId, Date debut, Date fin) {
        verifierPeriode(debut, fin);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT m.femme FROM Mariage m WHERE m.homme.id = :hommeId "
                            + "AND m.pk.dateDebut <= :fin AND (m.dateFin IS NULL OR m.dateFin >= :debut)", Femme.class)
                    .setParameter("hommeId", hommeId).setParameter("debut", debut).setParameter("fin", fin).list();
        }
    }

    public void afficherEpousesEntreDates(int hommeId, Date debut, Date fin) {
        for (Femme femme : findEpousesEntreDates(hommeId, debut, fin)) {
            System.out.println(femme);
        }
    }

    // Vérifier les changements de situation pour compter quatre épouses simultanées.
    public List<Homme> findHommesMariesAQuatreFemmes(Date debut, Date fin) {
        verifierPeriode(debut, fin);
        Map<Integer, Homme> hommes = new TreeMap<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Mariage> changements = cb.createQuery(Mariage.class);
            Root<Mariage> mariage = changements.from(Mariage.class);
            changements.select(mariage).where(cb.lessThanOrEqualTo(mariage.get("pk").<Date>get("dateDebut"), fin),
                    cb.or(cb.isNull(mariage.get("dateFin")), cb.greaterThanOrEqualTo(mariage.<Date>get("dateFin"), debut)));
            Set<LocalDate> dates = new TreeSet<>();
            LocalDate premier = new java.sql.Date(debut.getTime()).toLocalDate();
            LocalDate dernier = new java.sql.Date(fin.getTime()).toLocalDate();
            dates.add(premier);
            for (Mariage m : session.createQuery(changements).list()) {
                LocalDate dateDebut = new java.sql.Date(m.getDateDebut().getTime()).toLocalDate();
                if (!dateDebut.isBefore(premier)) dates.add(dateDebut);
                if (m.getDateFin() != null) {
                    LocalDate lendemain = new java.sql.Date(m.getDateFin().getTime()).toLocalDate().plusDays(1);
                    if (!lendemain.isAfter(dernier)) dates.add(lendemain);
                }
            }
            for (LocalDate date : dates) {
                CriteriaQuery<Homme> query = cb.createQuery(Homme.class);
                Root<Homme> homme = query.from(Homme.class);
                Subquery<Long> nombre = query.subquery(Long.class);
                Root<Mariage> m = nombre.from(Mariage.class);
                Date jour = java.sql.Date.valueOf(date);
                nombre.select(cb.countDistinct(m.get("femme").get("id"))).where(
                        cb.equal(m.get("homme").get("id"), homme.get("id")),
                        cb.lessThanOrEqualTo(m.get("pk").<Date>get("dateDebut"), jour),
                        cb.or(cb.isNull(m.get("dateFin")), cb.greaterThanOrEqualTo(m.<Date>get("dateFin"), jour)));
                query.select(homme).where(cb.equal(nombre, 4L));
                for (Homme h : session.createQuery(query).list()) hommes.put(h.getId(), h);
            }
        }
        return new ArrayList<>(hommes.values());
    }

    public long countHommesMariesAQuatreFemmes(Date debut, Date fin) {
        return findHommesMariesAQuatreFemmes(debut, fin).size();
    }

    public void afficherHommesMariesAQuatreFemmes(Date debut, Date fin) {
        List<Homme> hommes = findHommesMariesAQuatreFemmes(debut, fin);
        System.out.println("Nombre d'hommes mariés à quatre femmes : " + hommes.size());
        for (Homme homme : hommes) {
            System.out.println(homme);
        }
    }

}
