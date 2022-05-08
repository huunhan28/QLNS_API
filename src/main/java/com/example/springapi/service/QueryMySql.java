package com.example.springapi.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springapi.dto.CategoryDTO;
import com.example.springapi.dto.DiscountDTO;
import com.example.springapi.models.ProductReport;
import com.example.springapi.uploadfile.model.FileDB;

@Service
public class QueryMySql<T> {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public boolean update(String sql, Object... args) {
		int result = jdbcTemplate.update(sql, args);
		return result > 0;
	}
	
	public void updateLinkImage(String address) {
		int result = jdbcTemplate.update("update file_db set link=CONCAT('http://"+ address + "/images/uploads/',name)");
	}
	
	public List<T> select(String type,String sql,  Object[]args) {
		if (type.equals(CategoryDTO.class.getName())) {
			List<CategoryDTO> categoryDTOs = jdbcTemplate.query(sql, args, new RowMapper<CategoryDTO>() {

				@Override
				public CategoryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					// TODO Auto-generated method stub
					CategoryDTO categoryDTO = new CategoryDTO();
					categoryDTO.setId(rs.getInt("category_id"));
					categoryDTO.setDescription(rs.getString("description"));
					categoryDTO.setImageId(rs.getInt("id"));
					categoryDTO.setName(rs.getString("name"));
					categoryDTO.setLink(rs.getString("link"));
//					categoryDTO.setDescription(rs.getString("description"));
//					categoryDTO.setImageCategory((FileDB)rs.getObject("image_id"));
					return categoryDTO;
				}

			});
			return (List<T>) categoryDTOs;
		}else if(type.equals(DiscountDTO.class.getName())) {
			List<DiscountDTO> discountDTOs = jdbcTemplate.query(sql, args, new RowMapper<DiscountDTO>() {

				@Override
				public DiscountDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					// TODO Auto-generated method stub
					DiscountDTO discountDTO = new DiscountDTO();
					discountDTO.setId(rs.getString("discount_id"));
					discountDTO.setLink(rs.getString("link"));
					return discountDTO;
				}

			});
			return (List<T>) discountDTOs;
		}
		else if(type.equals(ProductReport.class.getName())) {
			List<ProductReport> list = jdbcTemplate.query(sql, args, new RowMapper<ProductReport>() {

				@Override
				public ProductReport mapRow(ResultSet rs, int rowNum) throws SQLException {
					// TODO Auto-generated method stub
					ProductReport item = new ProductReport();
					item.setId(rs.getInt("id"));
					item.setName(rs.getString("name"));
					item.setRevenue(rs.getFloat("revenue"));
					return item;
				}

			});
			return (List<T>) list;
		}
		
		return null;
		
		

	}
}
