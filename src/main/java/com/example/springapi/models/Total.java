package com.example.springapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Total {
    private long totalProduct;
    private long totalCategory;
    private long totalOrder;
    private float totalRevenue;
    
}
