package com.example.WebShopTP.controller;

import com.example.WebShopTP.entities.Product;
import com.example.WebShopTP.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> all() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProduct(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            Integer quantity = ((Number) request.get("quantity")).intValue();

            Product product = productService.addProduct(name, quantity);
            return ResponseEntity.ok(product);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            Integer quantity = ((Number) request.get("quantity")).intValue();

            if (!productService.updateProduct(id, name, quantity)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok("Product updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (!productService.deleteProduct(id))
            return ResponseEntity.badRequest().body("Not found");
        return ResponseEntity.ok("Product deleted successfully");
    }
}