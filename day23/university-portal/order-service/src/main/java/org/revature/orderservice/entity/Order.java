package org.revature.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;      // Foreign key — but NO JPA relation (microservices rule)
    private Integer quantity;
    private Double totalAmount;
    private String status;       // PENDING, CONFIRMED, CANCELLED
}