package ma.projet.beans;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Table(name = "mariages")
public class Mariage {

    @EmbeddedId
    private  MariagePK pk;

    @ManyToOne
    @JoinColumn(name="hommeId" , insertable = false , updatable = false)
    private Homme homme;

    @ManyToOne
    @JoinColumn(name="femmeId" , insertable = false , updatable = false)
    private Femme femme;

    private int nbrEnfant;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    public Mariage() {
    }

    public Mariage(MariagePK pk, Homme homme, Femme femme, int nbrEnfant, Date dateFin) {
        this.pk = pk;
        this.homme = homme;
        this.femme = femme;
        this.nbrEnfant = nbrEnfant;
        this.dateFin = dateFin;
    }

    public MariagePK getPk() {
        return pk;
    }

    public void setPk(MariagePK pk) {
        this.pk = pk;
    }


    public int getNbrEnfant() {
        return nbrEnfant;
    }

    public void setNbrEnfant(int nbrEnfant) {
        this.nbrEnfant = nbrEnfant;
    }

    public Homme getHomme() {
        return homme;
    }

    public void setHomme(Homme homme) {
        this.homme = homme;
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "Mariage{" +
                "pk=" + pk +
                ", homme=" + homme +
                ", femme=" + femme +
                ", nbrEnfant=" + nbrEnfant +
                '}';
    }

    public Date getDateDebut() {
        return pk == null ? null : pk.getDateDebut();
    }

}
