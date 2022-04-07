package com.example.springapi.config;

import javax.persistence.Basic;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		// taoj oobject va cau hinh
		ModelMapper modelmap = new ModelMapper();
		modelmap.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);// mapp muc 3, tat ca ten thuoc tinh
																					// phai gong nhau va dung thu tu
		return modelmap;
	}

}
