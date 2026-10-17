package ma.projet.classes;

import javax.persistence.*;

@Entity
@Table(name = "lignes_commande")
public class LigneCommandeProduit {

    @EmbeddedId
    private LigneCommandeId pk;

    private int quantite;

    @ManyToOne
    @JoinColumn(name = "commande", insertable = false, updatable = false)
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit", insertable = false, updatable = false)
    private Produit produit;

    public LigneCommandeProduit() {
    }

    public LigneCommandeProduit(LigneCommandeId pk, int quantite, Commande commande, Produit produit) {
        this.pk = pk;
        this.quantite = quantite;
        this.commande = commande;
        this.produit = produit;
    }

    public LigneCommandeId getPk() {
        return pk;
    }

    public void setPk(LigneCommandeId pk) {
        this.pk = pk;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
}
