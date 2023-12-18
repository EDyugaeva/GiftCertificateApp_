package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime createdTime;
    private Float price;
    @ManyToOne
    @JoinColumn(name="gift_certificate_id", nullable=false)
    private GiftCertificate giftCertificate;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnoreProperties("orderList")
    private User user;
}
