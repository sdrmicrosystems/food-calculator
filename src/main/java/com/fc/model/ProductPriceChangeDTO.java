package com.fc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductPriceChangeDTO {

    private Long id;
    private BigDecimal price;

}
