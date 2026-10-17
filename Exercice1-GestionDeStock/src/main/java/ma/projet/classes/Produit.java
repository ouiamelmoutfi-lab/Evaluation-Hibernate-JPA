package ma.projet.classes;

import javax.persistence.*;

@Entity
@Table(name = "produits")

@NamedQuery(
        name = "Produit.findByPrixSuperieur100",
        query = "FROM Produit p WHERE p.prix > 100"
)

public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String reference;

    private float prix;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    public Produit() {
    }

    public Produit(String reference, float prix) {
        this.reference = reference;
        this.prix = prix;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", prix=" + prix +

                '}';
    }
}
