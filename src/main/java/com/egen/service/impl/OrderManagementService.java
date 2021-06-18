package com.egen.service.impl;

import com.egen.model.GroceryOrder;
import com.egen.repo.IOrderManagementRepo;
import com.egen.repo.impl.OrderManagementRepo;
import com.egen.service.IOrderManagementService;
import org.springframework.stereotype.Service;
import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uk.co.jemos.podam.api.PodamUtils;
import uk.co.jemos.podam.typeManufacturers.IntTypeManufacturerImpl;
import uk.co.jemos.podam.typeManufacturers.TypeManufacturer;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderManagementService implements IOrderManagementService {

    private final IOrderManagementRepo orderManagementRepo;

    public OrderManagementService(OrderManagementRepo orderManagementRepo){
        this.orderManagementRepo = orderManagementRepo;
    }

    @Override
    public List<GroceryOrder> getAllOrders() {
        List<GroceryOrder> groceryOrders = orderManagementRepo.getAllOrders();
        return groceryOrders;
    }

    @Override
    public GroceryOrder getOrderById(String orderId) {
        return orderManagementRepo.getOrderById(orderId);
    }

    @Override
    public List<GroceryOrder> getAllOrdersWithInInterval(Timestamp startTime, Timestamp endTime) {
        return orderManagementRepo.getAllOrdersWithInInterval(startTime,endTime);
    }

    @Override
    public List<GroceryOrder> top10OrdersWithHighestDollarAmountInZip(String zip) {
        List<GroceryOrder> orders = orderManagementRepo.getAllOrders();
        List<GroceryOrder> groceryOrdersInZip = new LinkedList<>();
        for(GroceryOrder order: orders){
            if(order.getShippingAddress().getZip().equalsIgnoreCase(zip)) groceryOrdersInZip.add(order);
        }
        Collections.sort(groceryOrdersInZip, new Comparator<GroceryOrder>() {
            @Override
            public int compare(GroceryOrder o1, GroceryOrder o2) {
                return Double.compare(o2.getTotalAmount(), o1.getTotalAmount());
            }
        });
        List<GroceryOrder> newList = new ArrayList<>(10);
        for(int i=0 ; i < 10; i++){
            newList.add(groceryOrdersInZip.get(i));
        }
        return newList;
    }

    @Override
    public GroceryOrder saveOrder(GroceryOrder groceryOrder) {
        return orderManagementRepo.saveOrder(groceryOrder);
    }

    @Override
    public GroceryOrder cancelOrder(GroceryOrder groceryOrder) {
        //logic to check if cancel is possible
        return orderManagementRepo.cancelOrder(groceryOrder);
    }

    @Override
    public GroceryOrder updateOrder(GroceryOrder groceryOrder) {
        //logic to check if update is possible
        return orderManagementRepo.updateOrder(groceryOrder);
    }

    @Override
    public String createRandomOrders(int num){
        PodamFactory factory = new PodamFactoryImpl();
        TypeManufacturer<Integer> manufacturer = new IntTypeManufacturerImpl() {

            @Override
            public Integer getInteger(AttributeMetadata attributeMetadata) {

                if (attributeMetadata.getPojoClass().getName().equalsIgnoreCase("java.sql.Timestamp")) {
                    return PodamUtils.getIntegerInRange(0, 999);
                } else {
                    return super.getInteger(attributeMetadata);
                }
            }
        };
        factory.getStrategy().addOrReplaceTypeManufacturer(int.class, manufacturer);
        for(int i =0 ; i< num ; i++){
            GroceryOrder order = factory.manufacturePojoWithFullData(GroceryOrder.class);
            this.saveOrder(order);
        }
        return "success";
    }
}
