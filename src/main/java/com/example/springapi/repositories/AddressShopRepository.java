package com.example.springapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springapi.models.AddressShop;


@Repository
public interface AddressShopRepository extends JpaRepository<AddressShop, Integer>{
	Optional<AddressShop> findTopByOrderByIdDesc();
	long deleteById(int id);
	Optional<AddressShop> findByStt(int stt);
}
