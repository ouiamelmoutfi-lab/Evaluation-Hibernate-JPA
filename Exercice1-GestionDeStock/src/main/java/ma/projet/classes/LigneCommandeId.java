package ma.projet.classes;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class LigneCommandeId implements Serializable {

    private int commande;
    private int produit;

    public LigneCommandeId() {
    }

    public LigneCommandeId(int commande, int produit) {
        this.commande = commande;
        this.produit = produit;
    }

    public int getCommande() {
        return commande;
    }

    public void setCommande(int commande) {
        this.commande = commande;
    }

    public int getProduit() {
        return produit;
    }

    public void setProduit(int produit) {
        this.produit = produit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneCommandeId)) return false;
        LigneCommandeId autre = (LigneCommandeId) o;
        return commande == autre.commande && produit == autre.produit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commande, produit);
    }

}
