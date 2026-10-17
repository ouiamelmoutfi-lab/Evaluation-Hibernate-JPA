package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Projet.tachesPlanifiees",
                query = "SELECT t FROM Tache t WHERE t.projet.id = :projetId ORDER BY t.dateDebut, t.id"),
        @NamedQuery(name = "Projet.tachesRealisees",
                query = "SELECT t.id, t.nom, MIN(et.dateDebutReelle), MAX(et.dateFinReelle) "
                        + "FROM EmployeTache et JOIN et.tache t WHERE t.projet.id = :projetId "
                        + "GROUP BY t.id, t.nom HAVING COUNT(et.dateFinReelle) = COUNT(et) ORDER BY t.id"),
        @NamedQuery(name = "Projet.coutGlobal",
                query = "SELECT COALESCE(SUM(t.prix), 0.0) FROM Tache t WHERE t.projet.id = :projetId")
})
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    @Column(nullable = false)
    private LocalDate dateDebut;
    @Column(nullable = false)
    private LocalDate dateFin;

    // Plusieurs projets peuvent avoir le même chef, mais chaque projet a un seul chef.
    @ManyToOne
    @JoinColumn(name = "chef_id", nullable = false)
    private Employe chefProjet;

    @OneToMany(mappedBy = "projet")
    private List<Tache> taches = new ArrayList<>();

    public Projet() {
    }

    public Projet(String nom, LocalDate dateDebut, LocalDate dateFin, Employe chefProjet) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.chefProjet = chefProjet;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public Employe getChefProjet() { return chefProjet; }
    public void setChefProjet(Employe chefProjet) { this.chefProjet = chefProjet; }

    public List<Tache> getTaches() { return taches; }

    public void addTache(Tache tache) {
        Objects.requireNonNull(tache, "La tâche est obligatoire.");
        Projet ancienProjet = tache.getProjet();
        if (ancienProjet != null && ancienProjet != this) {
            ancienProjet.getTaches().remove(tache);
        }
        if (!taches.contains(tache)) taches.add(tache);
        tache.setProjet(this);
    }

    public void removeTache(Tache tache) {
        if (taches.remove(tache) && tache.getProjet() == this) {
            tache.setProjet(null);
        }
    }

    @Override
    public String toString() {
        return id + " - " + nom;
    }
}
