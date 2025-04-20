package kr.co.lotteon.entity;


import jakarta.persistence.*;
import kr.co.lotteon.entity.Seller;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller")
    private Seller seller;


    @Column(nullable = false)
    private String cate;
    private String name;
    private String description;
    private int price;
    private int stock;
    private int salesCount;
    private double rating;
    private int reviewCount;

    private String rdate;

    @CreationTimestamp
    private LocalDateTime wdate;

    private String productcol;


}
