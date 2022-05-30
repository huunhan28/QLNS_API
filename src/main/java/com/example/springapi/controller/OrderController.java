package com.example.springapi.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.validation.ConstraintViolationException;
import com.example.springapi.apputil.AppUtils;
import com.example.springapi.dto.OrderDTO;
import com.example.springapi.dto.OrderWithProducts;
import com.example.springapi.dto.ProductDTO;
import com.example.springapi.mapper.MapperService;
import com.example.springapi.models.Cart;
import com.example.springapi.models.Discount;
import com.example.springapi.models.OrderDetail;
import com.example.springapi.models.OrderDetailKey;
import com.example.springapi.models.Orders;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.CartResponsitory;
import com.example.springapi.repositories.DiscountResponsitory;
import com.example.springapi.repositories.OrderDetailResponsitory;
import com.example.springapi.repositories.OrderResponsitory;
import com.example.springapi.security.repository.UserRepository;
import com.example.springapi.service.QueryMySql;
import com.twilio.rest.media.v1.MediaProcessor.Order;

import lombok.Setter;

import com.example.springapi.security.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping(path = "/api/v1/Orders")
public class OrderController {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    OrderResponsitory orderResponsitory;

    @Autowired
    CartResponsitory cartResponsitory;

    @Autowired
    OrderDetailResponsitory orderDetailResponsitory;

    @Autowired
    UserRepository userResponsitory;

    @Autowired
    DiscountResponsitory discountResponsitory;

    @Autowired
    QueryMySql<OrderWithProducts> mysql;

    @Autowired
    MapperService mapperService;

    @GetMapping("")
    List<Orders> getAllOrders() {
        return orderResponsitory.findAll();
    }

    @GetMapping("/dto")
    List<OrderDTO> getAllOrdersDTO() {
        return mapperService.mapList(orderResponsitory.findAll(), OrderDTO.class);
    }

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("/OrderWithProducts/{id}")
    ResponseEntity<ResponseObject> getOrderWithProducts(@PathVariable("id") int id) {
        String sql = "select a.order_id id, c.name productName, quantity, price, discount "
                + "from (select order_id from orders where order_id=" + id + ") a, "
                + "(select order_order_id,product_product_id, quantity, price, discount from order_detail) b, "
                + "(select name, product_id from product) c\r\n"
                + "where a.order_id = b.order_order_id and b.product_product_id = c.product_id"
                + "order by a.order_id";
        return AppUtils.returnJS(HttpStatus.OK, "OK", "List product of order",
                mysql.select(OrderWithProducts.class.getName(), sql, null));
    }

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("user/{userId}")
    List<Orders> getAllOrdersByUserId(@PathVariable int userId) {
        return orderResponsitory.findAllByUserIdOrderByIdDesc(userId);
    }

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getProduct(@PathVariable int id) {
        Optional<Orders> findOrder = orderResponsitory.findById(id);
        if (findOrder.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Query product sucessfully", findOrder));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find order with id=" + id, ""));
        }

    }

    // insert new Order with POST method
    // Postman : Raw, JSON
    // @PostMapping("/insert")
    // ResponseEntity<ResponseObject> insertOrder(@RequestBody Orders newOrders) {

    // return ResponseEntity.status(HttpStatus.OK).body(
    // new ResponseObject("ok", "Insert Order successfully",
    // orderResponsitory.save(newOrders))
    // );
    // }

    @CrossOrigin(origins = "http://organicfood.com")
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertOrder(@RequestBody OrderDTO newOrderDTO) {
        Optional<User> user = userResponsitory.findById(newOrderDTO.getUserId());
        Optional<Discount> discount = null;
        Orders order;
        float km = 0;
        if (newOrderDTO.getDiscountId() != null) {
            discount = discountResponsitory.findById(newOrderDTO.getDiscountId());

            order = new Orders(
                    user.get(),
                    newOrderDTO.getCreateAt(),
                    discount.get(),
                    newOrderDTO.getState());
            km = discount.get().getPercent();

        } else {
            order = new Orders(
                    user.get(),
                    newOrderDTO.getCreateAt(),
                    null,
                    newOrderDTO.getState());
        }
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
    
        Optional<Orders> optionalOrder = orderResponsitory.findTopByOrderByIdDesc();
        // String productName = "";
        // if(optionalOrder.isPresent()) {
        // List<OrderDetail> list = optionalOrder.get().getOrderDetails();
        // list.forEach(orderDetail ->{
        // if(productName.length()>0) {
        // productName+="\n";
        //
        // }
        // productName += orderDetail.getProduct().getName() + " 1x" +
        // orderDetail.getQuantity();
        // });
        // }
        int orderId = 100;
        if (optionalOrder.isPresent())
            orderId = optionalOrder.get().getId();
        if (user.get().getEmail() != null && !user.get().getEmail().equals("")) {
            message.setTo(user.get().getEmail());
            message.setSubject("Organic Food");
            message.setText("Thanks for your order\n" +
                    "Order information: \n" +
                    "Order ID: " + orderId + "\n" +
                    "Your name: " + order.getUser().getName() + "\n" +
                    "Date: " + new SimpleDateFormat("dd/MM/yyyy)").format(order.getCreateAt()) + "\n" +
                    "State: Waiting" + "\n");

            // Send Message!
            // this.emailSender.send(message);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert Order successfully", orderResponsitory.save(order)));
    }


    @CrossOrigin(origins = "http://organicfood.com")
    @PostMapping("/insert/v2")
    ResponseEntity<ResponseObject> insertOrderV2(@RequestBody OrderDTO newOrderDTO) {
        
        List<Cart> listCart = cartResponsitory.findAllByUserId(newOrderDTO.getUserId());
        if(listCart.size()==0)
        return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "No product in your cart", null);
        Optional<Discount> discount = null;
        Optional<User> user = userResponsitory.findById(newOrderDTO.getUserId());
        Orders order;
        float km = 0;
        if (newOrderDTO.getDiscountId() != null) {
            discount = discountResponsitory.findById(newOrderDTO.getDiscountId());

            order = new Orders(
                    user.get(),
                    newOrderDTO.getCreateAt(),
                    discount.get(),
                    newOrderDTO.getState());
            km = discount.get().getPercent();

        } else {
            order = new Orders(
                    user.get(),
                    newOrderDTO.getCreateAt(),
                    null,
                    newOrderDTO.getState());
        }
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
        Orders ordered  = null;
        try {
           ordered = orderResponsitory.save(order) ;
        } catch (Exception e) {
            return AppUtils.returnJS(HttpStatus.NOT_IMPLEMENTED, "Failed", "Insert order failed", null);
        }
        Optional<Orders> optionalOrder = orderResponsitory.findTopByOrderByIdDesc();
        // let insert order details follow cart of user
        int orderId = 100;
        String productName ="";
        if (optionalOrder.isPresent())
            orderId = optionalOrder.get().getId();
        for (Cart cart : listCart) {
            try {
                if(productName.length()>0) {
                    productName+="\n";}
                    else{
                        productName+=cart.getProduct().getName() + "1x" + cart.getQuantity();
                    }
                orderDetailResponsitory.save(new OrderDetail(
                    new OrderDetailKey(orderId, cart.getProduct().getProductId()), 
                    optionalOrder.get(),
                    cart.getProduct(),
                    cart.getQuantity(),
                    cart.getProduct().getPrice(),
                    km));
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        // String productName = "";
        // if(optionalOrder.isPresent()) {
        // List<OrderDetail> list = optionalOrder.get().getOrderDetails();
        // list.forEach(orderDetail ->{
        // 
        //
        // }
        // productName += orderDetail.getProduct().getName() + " 1x" +
        // orderDetail.getQuantity();
        // });
        // }
       
        if (user.get().getEmail() != null && !user.get().getEmail().equals("")) {
            message.setTo(user.get().getEmail());
            message.setSubject("Organic Food");
            message.setText("Thanks for your order\n" +
                    "Order information: \n" +
                    "Order ID: " + orderId + "\n" +
                    "Your name: " + order.getUser().getName() + "\n" +
                    "Date: " + new SimpleDateFormat("dd/MM/yyyy)").format(order.getCreateAt()) + "\n" +
                    "State: Waiting" + "\n"+
                    "List of products:" + "\n" + 
                    productName);

            // Send Message!
            // this.emailSender.send(message);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert Order successfully", ordered));
    }
    @CrossOrigin(origins = "http://organicfood.com")
    @PutMapping("/updateState/{id}")
    public ResponseEntity<ResponseObject> updateStateByOrderId(@PathVariable("id") int id,
            @RequestParam("state") String state) {
        Optional<Orders> optional = orderResponsitory.findById(id);
        if (optional.isPresent()) {
            Orders temp = optional.get();
            temp.setState(state);
            return AppUtils.returnJS(HttpStatus.OK, "OK", "Update state order success", orderResponsitory.save(temp));
        } else {
            return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Not found this order", null);
        }
    }

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("/param/state")
    public ResponseEntity<ResponseObject> getOrdersByState(@RequestParam("state") String state) {
        List<Orders> list = new ArrayList<>();

        try {
            list = orderResponsitory.findAllByState(state);
            return AppUtils.returnJS(HttpStatus.OK, "OK", "Get order by state success ", list);
        } catch (ConstraintViolationException e) {
            // TODO: handle exception
            return AppUtils.returnJS(HttpStatus.OK, "OK", AppUtils.getExceptionSql(e), null);
        }

    }

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("/param/state/date")
    public ResponseEntity<ResponseObject> getOrdersByStateAndCreateAtBetween(@RequestParam("state") String state,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<Orders> list = new ArrayList<>();
        String patternDate = "dd-MM-yyyy";

        try {
            list = orderResponsitory.findAllByStateAndCreateAtBetweenOrderByIdDesc(state,
                    AppUtils.stringToDate(startDate, patternDate),
                    AppUtils.stringToDate(endDate, patternDate));
            return AppUtils.returnJS(HttpStatus.OK, "OK", "Get order by state success ", list);
        } catch (ConstraintViolationException e) {
            // TODO: handle exception
            return AppUtils.returnJS(HttpStatus.OK, "OK", AppUtils.getExceptionSql(e), null);
        }

    }

}
