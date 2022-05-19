package com.example.springapi.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.example.springapi.models.Cart;
import com.example.springapi.models.Product;
import com.example.springapi.security.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface CartResponsitory extends JpaRepository<Cart, Integer>{

    Optional<Cart> findByProduct(Product product);

    boolean existsByUserId(Integer id);

    List<Cart> findAllByUserId(Integer id);
    Optional<Cart> findByIdProductIdAndIdUserId(int productId, int userId);

    void deleteByUser(User user);

    boolean existsByUser(User user);

    void deleteAllByUser(User user);

    @Modifying
    @Query("delete from Cart c where c.user.id = :userId")
    void removeUserCart(@Param("userId") int userId);

    @Modifying
    @Query("delete from Cart c where c.user.id = :userId and c.product.productId = :productId")
    void removeUserProductCart(@Param("userId") int userId,@Param("productId") int productId);

    @Modifying
    @Query("update Cart c set quantity=:quantity where c.user.id=:userId and c.product.productId=:productId")
    void updateCart(@Param("userId") int userId,@Param("productId") int productId,@Param("quantity") int quantity);


}
