package com.example.springapi.repositories;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springapi.models.ProductReport;

@Transactional
@Repository
public interface ReportRepository {

//	@Modifying
//	@Query("select product_product_id id,product.name, sum(b.doanhthu) revenue from (select  product_product_id, a.quantity, price,percent, price*a.quantity*(1-COALESCE(percent,0)) 							doanhthu\r\n"
//			+ "								from (select orders.order_id, product_product_id, order_detail.quantity, price, discount_id \r\n"
//			+ "								from orders,order_detail \r\n"
//			+ "								where orders.order_id= order_order_id and (create_at between :start_date and :end_date)) a\r\n"
//			+ "left join discount on a.discount_id = discount.discount_id) b,\r\n"
//			+ "product where product_id=product_product_id\r\n"
//			+ "group by product_product_id, product.name\r\n"
//			+ "order by doanhthu desc\r\n"
//			+ "limit :limit offset :offset")
//	public List<ProductReport> getProductRevenue(@Param("start_date") Date startDate,
//												@Param("end_date") Date endDate,
//												@Param("limit") int limit,
//												@Param("offset") int offset
//			);
	
}
