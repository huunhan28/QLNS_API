package com.example.springapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springapi.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
	List<Comment> findByProductIdOrderByCreateAtDesc(int productId);
    Optional<Comment> findTopByOrderIdOrderByCreateAtDesc(int orderId);
	long deleteById(int id);
	List<Comment> findById(int id);
}
