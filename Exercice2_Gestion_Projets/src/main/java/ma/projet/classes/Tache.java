package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Tache.prixSuperieurA1000",
                query = "SELECT t FROM Tache t WHERE t.prix > 1000 ORDER BY t.id"),
        @NamedQuery(name = "Tache.realiseesEntreDates",
                query = "SELECT t FROM Tache t WHERE t.id IN "
                        + "(SELECT et.tache.id FROM EmployeTache et GROUP BY et.tache.id "
                        + "HAVING COUNT(et.dateFinReelle) = COUNT(et) "
                        + "AND MIN(et.dateDebutReelle) >= :debut AND MAX(et.dateFinReelle) <= :fin) "
                        + "ORDER BY t.id"),
        @NamedQuery(name = "Tache.supprimerParProjet",
                query = "DELETE FROM Tache t WHERE t.projet.id = :projetId")
})
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    @Column(nullable = false)
    private LocalDate dateDebut;
    @Column(nullable = false)
    private LocalDate dateFin;
    private double prix;

    // Une tâche appartient obligatoirement à un seul projet (composition du diagramme).
    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    @OneToMany(mappedBy = "tache")
    private List<EmployeTache> affectations = new ArrayList<>();

    public Tache() {
    }

    public Tache(String nom, LocalDate dateDebut, LocalDate dateFin, double prix, Projet projet) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
        this.projet = projet;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }

    public List<EmployeTache> getAffectations() { return affectations; }

    public void addAffectation(EmployeTache affectation) {
        Objects.requireNonNull(affectation, "L'affectation est obligatoire.");
        if (affectation.getPk() != null && !Objects.equals(affectation.getPk().getTacheId(), id)) {
            throw new IllegalArgumentException("La tâche doit correspondre à la clé de l'affectation.");
        }
        Tache ancienneTache = affectation.getTache();
        if (ancienneTache != null && ancienneTache != this) {
            ancienneTache.getAffectations().remove(affectation);
        }
        if (!affectations.contains(affectation)) affectations.add(affectation);
        affectation.setTache(this);
    }

    public void removeAffectation(EmployeTache affectation) {
        if (affectations.remove(affectation) && affectation.getTache() == this) {
            affectation.setTache(null);
        }
    }

    @Override
    public String toString() {
        return id + " - " + nom + " (" + prix + " DH)";
    }
}
