package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.MariagePK;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.text.SimpleDateFormat;
import java.util.List;
import ma.projet.beans.Mariage;

public class MariageService extends AbstractFacade<Mariage>{
    public MariageService(){
        super(Mariage.class);

    }


    public Mariage findById(MariagePK pk) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Mariage.class, pk);
        }
    }

    @Override
    public Mariage findById(int id) {
        throw new IllegalArgumentException("Utilisez findById(MariagePK) pour un mariage.");
    }

    private void verifierMariage(Mariage mariage) {
        if (mariage == null || mariage.getPk() == null || mariage.getHomme() == null
                || mariage.getFemme() == null || mariage.getDateDebut() == null
                || mariage.getHomme().getId() == 0 || mariage.getFemme().getId() == 0
                || !Integer.valueOf(mariage.getHomme().getId()).equals(mariage.getPk().getHommeId())
                || !Integer.valueOf(mariage.getFemme().getId()).equals(mariage.getPk().getFemmeId())) {
            throw new IllegalArgumentException("La clé doit correspondre à l'homme et à la femme enregistrés.");
        }
        if (mariage.getDateFin() != null) verifierPeriode(mariage.getDateDebut(), mariage.getDateFin());
        if (mariage.getNbrEnfant() < 0) throw new IllegalArgumentException("Le nombre d'enfants doit être positif ou nul.");
    }

    @Override
    public boolean create(Mariage mariage) {
        verifierMariage(mariage);
        return super.create(mariage);
    }

    @Override
    public boolean update(Mariage mariage) {
        verifierMariage(mariage);
        return super.update(mariage);
    }

    public List<Mariage> findByHomme(int hommeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Mariage m WHERE m.homme.id = :hommeId ORDER BY m.pk.dateDebut", Mariage.class)
                    .setParameter("hommeId", hommeId).list();
        }
    }

    public void afficherMariages(int hommeId) {
        Homme homme = new HommeService().findById(hommeId);
        if (homme == null) {
            System.out.println("Homme introuvable : " + hommeId);
            return;
        }
        List<Mariage> mariages = findByHomme(hommeId);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Nom : " + homme.getNom() + " " + homme.getPrenom());
        System.out.println("Mariages En Cours :");
        int numero = 1;
        for (Mariage mariage : mariages) {
            if (mariage.getDateFin() == null) {
                System.out.println(numero++ + ". Femme : " + mariage.getFemme().getNom() + " " + mariage.getFemme().getPrenom()
                        + "   Date Début : " + format.format(mariage.getDateDebut())
                        + "   Nbr Enfants : " + mariage.getNbrEnfant());
            }
        }
        System.out.println("\nMariages échoués :");
        numero = 1;
        for (Mariage mariage : mariages) {
            if (mariage.getDateFin() != null) {
                System.out.println(numero++ + ". Femme : " + mariage.getFemme().getNom() + " " + mariage.getFemme().getPrenom()
                        + "   Date Début : " + format.format(mariage.getDateDebut())
                        + "   Date Fin : " + format.format(mariage.getDateFin())
                        + "   Nbr Enfants : " + mariage.getNbrEnfant());
            }
        }
    }

}
