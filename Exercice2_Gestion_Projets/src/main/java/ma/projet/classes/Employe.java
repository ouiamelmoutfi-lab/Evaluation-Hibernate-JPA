package ma.projet.classes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Employe.tachesRealisees",
                query = "SELECT DISTINCT t FROM EmployeTache et JOIN et.tache t "
                        + "WHERE et.employe.id = :employeId AND et.dateFinReelle IS NOT NULL "
                        + "ORDER BY t.id"),
        @NamedQuery(name = "Employe.projetsGeres",
                query = "SELECT p FROM Projet p WHERE p.chefProjet.id = :employeId ORDER BY p.id")
})
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    private String telephone;

    @OneToMany(mappedBy = "chefProjet")
    private List<Projet> projets = new ArrayList<>();

    @OneToMany(mappedBy = "employe")
    private List<EmployeTache> affectations = new ArrayList<>();

    public Employe() {
    }

    public Employe(String nom, String prenom, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public List<Projet> getProjets() { return projets; }
    public List<EmployeTache> getAffectations() { return affectations; }

    public void addProjet(Projet projet) {
        Objects.requireNonNull(projet, "Le projet est obligatoire.");
        Employe ancienChef = projet.getChefProjet();
        if (ancienChef != null && ancienChef != this) {
            ancienChef.getProjets().remove(projet);
        }
        if (!projets.contains(projet)) projets.add(projet);
        projet.setChefProjet(this);
    }

    public void removeProjet(Projet projet) {
        if (projets.remove(projet) && projet.getChefProjet() == this) {
            projet.setChefProjet(null);
        }
    }

    public void addAffectation(EmployeTache affectation) {
        Objects.requireNonNull(affectation, "L'affectation est obligatoire.");
        if (affectation.getPk() != null && !Objects.equals(affectation.getPk().getEmployeId(), id)) {
            throw new IllegalArgumentException("L'employé doit correspondre à la clé de l'affectation.");
        }
        Employe ancienEmploye = affectation.getEmploye();
        if (ancienEmploye != null && ancienEmploye != this) {
            ancienEmploye.getAffectations().remove(affectation);
        }
        if (!affectations.contains(affectation)) affectations.add(affectation);
        affectation.setEmploye(this);
    }

    public void removeAffectation(EmployeTache affectation) {
        if (affectations.remove(affectation) && affectation.getEmploye() == this) {
            affectation.setEmploye(null);
        }
    }

    @Override
    public String toString() {
        return id + " - " + nom + " " + prenom;
    }
}
