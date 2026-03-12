// OrderItemDTO.java
package com.app.ecom.DTO.Response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDTO {
    private Long id;
    private Long productid;
    private Integer quantity;
    private BigDecimal price;
}