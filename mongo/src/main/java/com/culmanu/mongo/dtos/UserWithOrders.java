package com.culmanu.mongo.dtos;

import com.culmanu.mongo.enitities.Order;
import com.culmanu.mongo.enitities.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserWithOrders extends User {
    private List<Order> orders;
}

