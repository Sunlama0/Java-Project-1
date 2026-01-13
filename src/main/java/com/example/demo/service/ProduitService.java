package com.example.demo.service;

import com.example.demo.model.Produit;
import com.example.demo.repository.ProduitRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository repo;

    public ProduitService(ProduitRepository repo) {
        this.repo = repo;
    }

    public Produit save(Produit produit) {
        return repo.save(produit);
    }

    public Produit findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Page<Produit> search(String q, String type, int page, int size, String sort, String dir) {
        String nom = (q == null) ? "" : q.trim();
        String typeThe = (type == null) ? "" : type.trim();

        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        String sortField = switch (sort == null ? "" : sort) {
            case "nom" -> "nom";
            case "prix" -> "prix";
            case "quantiteStock" -> "quantiteStock";
            default -> "nom";
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return repo.findByNomContainingIgnoreCaseAndTypeTheContainingIgnoreCase(nom, typeThe, pageable);
    }

    public List<Produit> searchAllForExport(String q, String type, String sort, String dir) {
        return search(q, type, 0, Integer.MAX_VALUE, sort, dir).getContent();
    }
}
