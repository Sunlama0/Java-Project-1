package com.example.demo.repository;

import com.example.demo.model.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    Page<Produit> findByNomContainingIgnoreCaseAndTypeTheContainingIgnoreCase(
            String nom, String typeThe, Pageable pageable
    );
}
