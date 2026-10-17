package ma.projet.beans;

import javax.persistence.*;
import java.util.Objects;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class MariagePK implements Serializable {

    @Temporal(TemporalType.DATE)
    protected Date dateDebut;
    protected Integer hommeId;
    protected Integer femmeId;

    public MariagePK() {
    }

    public MariagePK(Date dateDebut, Integer hommeId, Integer femmeId) {
        this.dateDebut = dateDebut;
        this.hommeId = hommeId;
        this.femmeId = femmeId;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Integer getHommeId() {
        return hommeId;
    }

    public void setHommeId(Integer hommeId) {
        this.hommeId = hommeId;
    }

    public Integer getFemmeId() {
        return femmeId;
    }

    public void setFemmeId(Integer femmeId) {
        this.femmeId = femmeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MariagePK)) return false;
        MariagePK autre = (MariagePK) o;
        return Objects.equals(dateDebut, autre.dateDebut)
                && Objects.equals(hommeId, autre.hommeId) && Objects.equals(femmeId, autre.femmeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateDebut, hommeId, femmeId);
    }

}
