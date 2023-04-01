package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    Map<String,Order> orderDb = new HashMap<>();
    Map<String,DeliveryPartner> partnerDb = new HashMap<>();
    Map<String, HashSet<String>> partnerOrderDb = new HashMap<>();

    public String addOrder(Order order){
        String key = order.getId();
        orderDb.put(key,order);

        return "New order added successfully";
    }
    public String addPartner(String partnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        String key = deliveryPartner.getId();
        partnerDb.put(key,deliveryPartner);

        return "New delivery partner added successfully";
    }

    public String addOrderPartnerPair(String orderId,String partnerId){

        HashSet<String> hs = partnerOrderDb.get(partnerId);

        if(hs.isEmpty()){
            hs = new HashSet<>();
        }

        hs.add(orderId);
        partnerOrderDb.put(partnerId,hs);
        return "New order-partner pair added successfully";
    }
    public Order getOrderById(String orderId){
        return orderDb.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        return partnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){

        HashSet<String> ans = partnerOrderDb.get(partnerId);
        return ans.size();

    }
    public List<String> getOrdersByPartnerId(String partnerId){
        HashSet<String> hs = partnerOrderDb.get(partnerId);
        List<String> ans = new ArrayList<>();
        for(String s : hs){
            ans.add(s);
        }
        return ans;
    }
    public List<String> getAllOrders(){
        List<String> ans = new ArrayList<>();
        for(String s:orderDb.keySet()){
            ans.add(s);
        }
        return ans;
    }
    public Integer getCountOfUnassignedOrders(){
        Integer countOfOrders = 0;
        HashSet<String> ref = new HashSet<>();
        for(HashSet<String> hs : partnerOrderDb.values()){
            for(String s : hs){
                ref.add(s);
            }
        }
        for(String s : orderDb.keySet()){
            if(!ref.contains(s)){
                countOfOrders += 1;
            }
        }
        return countOfOrders;
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        int count = 0;
        int hours = Integer.valueOf(time.substring(0,2));
        int minutes = Integer.valueOf(time.substring(3));
        int total = hours*60 + minutes;

        if(partnerOrderDb.containsKey(partnerId))
        {
            HashSet<String> set = partnerOrderDb.get(partnerId);

            for(String st : set)
            {
                if(orderDb.containsKey(st))
                {
                    Order order = orderDb.get(st);

                    if(total < order.getDeliveryTime())
                        count++;
                }
            }
        }

        return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String time = null;
        int delivery_time = 0;

        if(partnerDb.containsKey(partnerId))
        {
            HashSet<String> list = partnerOrderDb.get(partnerId);

            for(String st : list)
            {
                if(orderDb.containsKey(st))
                {
                    Order order = orderDb.get(st);

                    if(delivery_time < order.getDeliveryTime())
                        delivery_time = order.getDeliveryTime();
                }
            }
        }
        StringBuilder str = new StringBuilder();

        int hr = delivery_time / 60;                 // calculate hour
        if(hr < 10)
            str.append(0).append(hr);
        else
            str.append(hr);

        str.append(":");

        int min = delivery_time - (hr*60);          // calculate minutes
        if(min < 10)
            str.append(0).append(min);
        else
            str.append(min);

//        str.append(min);

        return str.toString();
    }

    public void deletePartnerById(String partnerId){
        HashSet<String> list = new HashSet<>();

        if(partnerOrderDb.containsKey(partnerId)) {
            list = partnerOrderDb.get(partnerId);
        }
//        for(String s : list){
//            if(!orderDb.containsKey(s)){
//                orderDb.put()
//            }
//        }
        partnerOrderDb.remove(partnerId);

        if(partnerDb.containsKey(partnerId)) {
        partnerOrderDb.remove(partnerId);
        }
    }
    public void deleteOrderById(String orderId){
        String partnerId = null;
        for (Map.Entry<String, HashSet<String>> entry : partnerOrderDb.entrySet()) {
            HashSet<String> orderSet = entry.getValue();

            if (orderSet.contains(orderId)) {
                partnerId = entry.getKey();
                break;
            }
        }
        HashSet<String> list = partnerOrderDb.get(partnerId);
        list.remove(orderId);
        partnerOrderDb.put(partnerId,list);
        DeliveryPartner deliveryPartner = partnerDb.get(partnerId);
        deliveryPartner.setNumberOfOrders(list.size());

        if(orderDb.containsKey(orderId)) {
            orderDb.remove(orderId);
        }
    }
}
