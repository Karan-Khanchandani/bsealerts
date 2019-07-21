package com.ronin47.bse.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BseController {
    @PostMapping("/add-stock")
    public void addStock(String stockId){

    }
}
