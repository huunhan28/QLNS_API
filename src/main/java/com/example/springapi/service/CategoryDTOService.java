package com.example.springapi.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springapi.dto.CategoryDTO;
import com.example.springapi.models.Category;
import com.example.springapi.repositories.CategoryResponsitory;

@Service
public class CategoryDTOService {
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private QueryMySql<CategoryDTO> categoryMySQL;
	
	@Autowired
	private CategoryResponsitory categoryResponsitory;
	
	public CategoryDTO getCategory(int id){
		Optional<Category> categoriy = categoryResponsitory.findById(id);
		 
		CategoryDTO categoryDTO = mapper.map(categoriy.get(), CategoryDTO.class);
		return categoryDTO;
	}
	
	public List<CategoryDTO> getCategoryDTOs(){
		String sql ="select category_id, a.`name`, description,b.id, b.link from category a,"
				+ " file_db b where a.image_id = b.id";
		return categoryMySQL.select(CategoryDTO.class.getName(),sql ,null );
	}
}
