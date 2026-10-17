package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NamedQueries({
        @NamedQuery(name = "EmployeTache.realiseesParProjet",
                query = "SELECT et FROM EmployeTache et WHERE et.tache.projet.id = :projetId "
                        + "AND et.dateFinReelle IS NOT NULL"),
        @NamedQuery(name = "EmployeTache.supprimerParTache",
                query = "DELETE FROM EmployeTache et WHERE et.pk.tacheId = :tacheId"),
        @NamedQuery(name = "EmployeTache.supprimerParProjet",
                query = "DELETE FROM EmployeTache et WHERE et.pk.tacheId IN "
                        + "(SELECT t.id FROM Tache t WHERE t.projet.id = :projetId)")
})
public class EmployeTache {
    @EmbeddedId
    private EmployeTachePK pk;
    private LocalDate dateDebutReelle;
    private LocalDate dateFinReelle;

    @ManyToOne
    @JoinColumn(name = "employeId", nullable = false, insertable = false, updatable = false)
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "tacheId", nullable = false, insertable = false, updatable = false)
    private Tache tache;

    public EmployeTache() {
    }

    public EmployeTache(EmployeTachePK pk, LocalDate dateDebutReelle, LocalDate dateFinReelle,
                        Employe employe, Tache tache) {
        this.pk = pk;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
        this.employe = employe;
        this.tache = tache;
    }

    public EmployeTachePK getPk() { return pk; }
    public void setPk(EmployeTachePK pk) { this.pk = pk; }
    public LocalDate getDateDebutReelle() { return dateDebutReelle; }
    public void setDateDebutReelle(LocalDate dateDebutReelle) { this.dateDebutReelle = dateDebutReelle; }
    public LocalDate getDateFinReelle() { return dateFinReelle; }
    public void setDateFinReelle(LocalDate dateFinReelle) { this.dateFinReelle = dateFinReelle; }
    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
    public Tache getTache() { return tache; }
    public void setTache(Tache tache) { this.tache = tache; }
}
