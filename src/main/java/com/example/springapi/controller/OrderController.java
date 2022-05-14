package com.example.springapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


import javax.validation.ConstraintViolationException;
import com.example.springapi.apputil.AppUtils;
import com.example.springapi.dto.OrderDTO;
import com.example.springapi.dto.OrderWithProducts;
import com.example.springapi.dto.ProductDTO;
import com.example.springapi.models.Discount;
import com.example.springapi.models.Orders;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.DiscountResponsitory;
import com.example.springapi.repositories.OrderResponsitory;
import com.example.springapi.security.repository.UserRepository;
import com.example.springapi.service.QueryMySql;
import com.example.springapi.security.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/api/v1/Orders")
public class OrderController {
    
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
	OrderResponsitory orderResponsitory;

    @Autowired
	UserRepository userResponsitory;

    @Autowired
	DiscountResponsitory discountResponsitory;
    
    @Autowired
    QueryMySql<OrderWithProducts> mysql;
	
	@GetMapping("")
	List<Orders> getAllOrders(){
		return orderResponsitory.findAll();
	}
	
	@GetMapping("/OrderWithProducts/{id}")
	ResponseEntity<ResponseObject>  getOrderWithProducts(@PathVariable("id") int id){
		String sql ="select a.order_id id, c.name productName, quantity, price, discount "
				+ "from (select order_id from orders where order_id="+id+") a, "
				+ "(select order_order_id,product_product_id, quantity, price, discount from order_detail) b, "
				+ "(select name, product_id from product) c\r\n"
				+ "where a.order_id = b.order_order_id and b.product_product_id = c.product_id";
		return AppUtils.returnJS(HttpStatus.OK, "OK", "List product of order", 
				mysql.select(OrderWithProducts.class.getName(), sql, null));
	}


    @GetMapping("user/{userId}")
	List<Orders> getAllOrdersByUserId(@PathVariable int userId){
		return orderResponsitory.findAllByUserId(userId);
	}

    @GetMapping("/{id}")
	ResponseEntity<ResponseObject> getProduct(@PathVariable int id){
		Optional<Orders> findOrder = orderResponsitory.findById(id);
		if(findOrder.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("ok", "Query product sucessfully", findOrder));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject("failed", "Can not find order with id=" + id, ""));
		}
        
	}
    
    //insert new Order with POST method
    //Postman : Raw, JSON
    // @PostMapping("/insert")
    // ResponseEntity<ResponseObject> insertOrder(@RequestBody Orders newOrders) {
        
    //     return ResponseEntity.status(HttpStatus.OK).body(
    //        new ResponseObject("ok", "Insert Order successfully", orderResponsitory.save(newOrders))
    //     );
    // }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertOrder(@RequestBody OrderDTO newOrderDTO) {
        Optional<User> user=userResponsitory.findById(newOrderDTO.getUserId());
        Optional<Discount> discount=null;
        Orders order;
        float km=0;
        if(newOrderDTO.getDiscountId()!=null){
            discount=discountResponsitory.findById(newOrderDTO.getDiscountId());

            order = new Orders(
                                user.get(),
                                newOrderDTO.getCreateAt(),
                                discount.get(),
                                newOrderDTO.getState());
            km=discount.get().getPercent();
                            
        }else{
            order = new Orders(
                                user.get(),
                                newOrderDTO.getCreateAt(),
                                null,
                                newOrderDTO.getState());
        }
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
                
        message.setTo(user.get().getEmail());
        message.setSubject("Organic Food");

        message.setText("Chung toi cam on ban vi da mua hang tai Organic Food \n"+
                        "Chi tiet don hang cua ban: \n"+
                        order.getUser().getName()+"\n"+
                        order.getCreateAt()+"\n"+
                        km+"\n"+
                        order.getState());

        // Send Message!
        this.emailSender.send(message);
        
         
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert Order successfully", orderResponsitory.save(order))
        );
    }
    
    @PutMapping("/updateState/{id}")
    public ResponseEntity<ResponseObject> updateStateByOrderId(@PathVariable("id") int id,
    															@RequestParam("state") String state){
    	Optional<Orders> optional = orderResponsitory.findById(id);
    	if(optional.isPresent()) {
    		Orders temp = optional.get();
    		temp.setState(state);
    		return AppUtils.returnJS(HttpStatus.OK, "OK","Update state order success", orderResponsitory.save(temp)); 
    	}else {
    		return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed","Not found this order", null); 
    	}
    }
    
    @GetMapping("/param/state")
    public ResponseEntity<ResponseObject> getOrdersByState(@RequestParam("state") String state){
    	List<Orders> list = new ArrayList<>();
    	
    	try {
			list = orderResponsitory.findAllByState(state);
			return AppUtils.returnJS(HttpStatus.OK, "OK","Get order by state success ", list);
		} catch (ConstraintViolationException e) {
			// TODO: handle exception
			return AppUtils.returnJS(HttpStatus.OK, "OK",AppUtils.getExceptionSql(e), null);
		}
    	
    }
    
    @GetMapping("/param/state/date")
    public ResponseEntity<ResponseObject> getOrdersByStateAndCreateAtBetween(@RequestParam("state") String state,
    																		@RequestParam("startDate") String startDate,
    																		@RequestParam("endDate") String endDate){
    	List<Orders> list = new ArrayList<>();
    	String patternDate = "dd-MM-yyyy";
    	
    	try {
			list = orderResponsitory.findAllByStateAndCreateAtBetween(state, AppUtils.stringToDate(startDate, patternDate),
					AppUtils.stringToDate(endDate, patternDate));
			return AppUtils.returnJS(HttpStatus.OK, "OK","Get order by state success ", list);
		} catch (ConstraintViolationException e) {
			// TODO: handle exception
			return AppUtils.returnJS(HttpStatus.OK, "OK",AppUtils.getExceptionSql(e), null);
		}
    	
    }
    
    
}
