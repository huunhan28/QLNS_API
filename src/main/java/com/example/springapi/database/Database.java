package com.example.springapi.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.StaticApplicationContext;

import com.example.springapi.models.Product;
import com.example.springapi.repositories.ProductResponsitory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// if data base is empty use this to make virtual db
//@Configuration
public class Database {

//	private static final Logger logger = LoggerFactory.getLogger(Database.class);
//	
//	@Bean
//	CommandLineRunner initDatabase(ProductResponsitory productResponsitory) {
//	
//		return new CommandLineRunner() {
//			
//			@Override
//			public void run(String... args) throws Exception {
//				// TODO Auto-generated method stub
////				Product productA = new Product("IPHONE13 PRO MAX", 2022, 3000,"" );
////				Product productB = new Product("IPHONE12 PRO MAX", 2022, 1500,"" );
////				logger.info("insert product:" + productResponsitory.save(productA));
////				logger.info("insert product:" + productResponsitory.save(productB));
//			}
//		};
//	}
}
