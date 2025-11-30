package com.example.WebShopTP.controller;

import com.example.WebShopTP.entities.Order;
import com.example.WebShopTP.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> all() {
        return orderService.getAllOrders();
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody Map<String, Object> request) {
        try {
            Long productId = ((Number) request.get("product_id")).longValue();
            Integer quantity = ((Number) request.get("quantity")).intValue();
            String customer = (String) request.get("customer");

            Order order = orderService.addOrder(productId, quantity, customer, "В обработке");
            return ResponseEntity.ok(order);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        if (!orderService.deleteOrder(id)) {
            response.put("status", "error");
            response.put("message", "Order not found");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("status", "success");
        response.put("message", "Order deleted");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String status = (String) request.get("status");
            String courier = (String) request.get("courier");

            if (!orderService.updateOrder(id, status, courier))
                return ResponseEntity.badRequest().body("Order not found");
            return ResponseEntity.ok("Order updated");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/by-customer/{customer}")
    public List<Order> getByCustomer(@PathVariable String customer) {
        return orderService.getOrdersByCustomer(customer);
    }

    @GetMapping("/by-courier/{courier}")
    public List<Order> getByCourier(@PathVariable String courier) {
        return orderService.getOrdersByCourier(courier);
    }
}