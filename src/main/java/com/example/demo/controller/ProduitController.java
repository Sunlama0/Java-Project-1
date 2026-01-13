package com.example.demo.controller;

import com.example.demo.model.Produit;
import com.example.demo.service.ProduitService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProduitController {

    private final ProduitService service;

    private static final List<String> TYPES = List.of("Vert", "Noir", "Oolong", "Blanc", "Pu-erh");
    private static final List<String> ORIGINES = List.of("Chine", "Japon", "Inde", "Sri Lanka", "Taiwan");

    public ProduitController(ProduitService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model
    ) {
        Page<Produit> produits = service.search(q, type, page, size, sort, dir);

        model.addAttribute("produits", produits.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", produits.getTotalPages());
        model.addAttribute("totalItems", produits.getTotalElements());

        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("type", type == null ? "" : type);

        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("nextDir", "asc".equalsIgnoreCase(dir) ? "desc" : "asc");

        model.addAttribute("types", TYPES);
        return "index";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        Produit p = new Produit();
        p.setDateReception(LocalDate.now());

        model.addAttribute("produit", p);
        model.addAttribute("types", TYPES);
        model.addAttribute("origines", ORIGINES);
        model.addAttribute("mode", "create");
        model.addAttribute("errors", new ArrayList<String>());
        return "formulaire-produit";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@ModelAttribute Produit produit, Model model) {
        List<String> errors = validateProduit(produit);

        if (!errors.isEmpty()) {
            model.addAttribute("produit", produit);
            model.addAttribute("types", TYPES);
            model.addAttribute("origines", ORIGINES);
            model.addAttribute("mode", "create");
            model.addAttribute("errors", errors);
            return "formulaire-produit";
        }

        service.save(produit);
        return "redirect:/";
    }

    @GetMapping("/modifier/{id}")
    public String modifierForm(@PathVariable Long id, Model model) {
        Produit p = service.findById(id);
        if (p == null) return "redirect:/";

        model.addAttribute("produit", p);
        model.addAttribute("types", TYPES);
        model.addAttribute("origines", ORIGINES);
        model.addAttribute("mode", "edit");
        model.addAttribute("errors", new ArrayList<String>());
        return "formulaire-produit";
    }

    @PostMapping("/modifier/{id}")
    public String modifier(@PathVariable Long id, @ModelAttribute Produit produit, Model model) {
        Produit existing = service.findById(id);
        if (existing == null) return "redirect:/";

        produit.setId(id);

        List<String> errors = validateProduit(produit);
        if (!errors.isEmpty()) {
            model.addAttribute("produit", produit);
            model.addAttribute("types", TYPES);
            model.addAttribute("origines", ORIGINES);
            model.addAttribute("mode", "edit");
            model.addAttribute("errors", errors);
            return "formulaire-produit";
        }

        service.save(produit);
        return "redirect:/";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/";
    }

    // BONUS Export CSV
    @GetMapping("/export/csv")
    public void exportCsv(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "nom") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            HttpServletResponse response
    ) throws Exception {

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=produits_thes.csv");

        List<Produit> produits = service.searchAllForExport(q, type, sort, dir);

        try (PrintWriter writer = response.getWriter()) {
            writer.write('\uFEFF'); // BOM pour Excel
            writer.println("id;nom;typeThe;origine;prix;quantiteStock;description;dateReception");

            for (Produit p : produits) {
                writer.printf("%d;%s;%s;%s;%.2f;%d;%s;%s%n",
                        p.getId(),
                        esc(p.getNom()),
                        esc(p.getTypeThe()),
                        esc(p.getOrigine()),
                        p.getPrix(),
                        p.getQuantiteStock(),
                        esc(p.getDescription()),
                        p.getDateReception() == null ? "" : p.getDateReception().toString()
                );
            }
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }

    // Validation manuelle (sans starter-validation)
    private List<String> validateProduit(Produit p) {
        List<String> errors = new ArrayList<>();

        if (p.getNom() == null || p.getNom().trim().isEmpty()) {
            errors.add("Le nom est obligatoire.");
        } else if (p.getNom().length() > 100) {
            errors.add("Le nom ne doit pas dépasser 100 caractères.");
        }

        if (p.getTypeThe() == null || p.getTypeThe().trim().isEmpty()) {
            errors.add("Le type de thé est obligatoire.");
        }

        if (p.getOrigine() == null || p.getOrigine().trim().isEmpty()) {
            errors.add("L'origine est obligatoire.");
        }

        if (p.getPrix() < 5 || p.getPrix() > 100) {
            errors.add("Le prix doit être compris entre 5 et 100.");
        }

        if (p.getQuantiteStock() < 0) {
            errors.add("La quantité en stock doit être ≥ 0.");
        }

        if (p.getDescription() != null && p.getDescription().length() > 500) {
            errors.add("La description ne doit pas dépasser 500 caractères.");
        }

        return errors;
    }
}
