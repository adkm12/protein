package gachon.protein.repository;

import gachon.protein.entity.Cart;
import gachon.protein.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMemberId(Long memberId);
}
