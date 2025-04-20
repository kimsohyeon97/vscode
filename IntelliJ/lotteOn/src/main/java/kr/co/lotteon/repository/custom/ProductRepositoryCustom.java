package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {


    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);

    public Page<Tuple> selectBestAllForList(String cate);

    public Page<Tuple> sortedProducts(PageRequestDTO pageRequestDTO, Pageable pageable);
}
