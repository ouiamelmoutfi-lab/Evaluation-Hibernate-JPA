package ma.projet.beans;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("HOMME")
public class Homme extends Personne{



    public Homme() {
    }

    public Homme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }



}
