package com.example.springapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.springapi.dto.DiscountDTO;

@Repository
public class DiscountDTORepository {
	@Autowired
	QueryMySql<DiscountDTO> db;
	
	public List<DiscountDTO> getDiscounts(){
		String sql="select a.discount_id, link from "
				+ "(select discount_id, image_id from discount where start_date>=DATE(NOW())) a,"
				+ " file_db b where a.image_id = b.id";
		return db.select(DiscountDTO.class.getName(),
				sql, null);
		
	}
}
