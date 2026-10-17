package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("FEMME")
@NamedQuery(name = "Femme.marieesAuMoinsDeuxFois",
        query = "FROM Femme f WHERE (SELECT COUNT(m) FROM Mariage m WHERE m.femme = f) >= 2 ORDER BY f.id")
@NamedNativeQuery(name = "Femme.nombreEnfantsEntreDates",
        query = "SELECT COALESCE(SUM(nbrEnfant), 0) FROM mariages "
                + "WHERE femmeId = :femmeId AND dateDebut <= :fin "
                + "AND (dateFin IS NULL OR dateFin >= :debut)")
public class Femme extends Personne{

    public Femme() {
    }

    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }
}
