package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Product;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Integer>, ProductRepositoryCustom {
}
