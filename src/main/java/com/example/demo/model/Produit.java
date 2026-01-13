package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "produits")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false)
    private String typeThe; // Vert, Noir, Oolong, Blanc, Pu-erh

    @Column(nullable = false)
    private String origine; // Chine, Japon, Inde, Sri Lanka, Taiwan

    private float prix;           // entre 5 et 100 (on gère côté controller)
    private int quantiteStock;    // min 0 (on gère côté controller)

    @Column(length = 500)
    private String description;

    private LocalDate dateReception;

    public Produit() {}

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTypeThe() { return typeThe; }
    public void setTypeThe(String typeThe) { this.typeThe = typeThe; }

    public String getOrigine() { return origine; }
    public void setOrigine(String origine) { this.origine = origine; }

    public float getPrix() { return prix; }
    public void setPrix(float prix) { this.prix = prix; }

    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateReception() { return dateReception; }
    public void setDateReception(LocalDate dateReception) { this.dateReception = dateReception; }
}
