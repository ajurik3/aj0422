package org.tool.aj0422.dao.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="sale")
public class Sale {

    @Id
    @Column(name="sale_id")
    private long saleId;

    @Column(name="sale_time")
    private LocalDateTime saleTime;

    private int days;
    private BigDecimal discount;

    @Column(name="tool_code", insertable = false, updatable = false)
    private String toolCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tool_code")
    private Tool tool;

    @Column(name="pre_discount_total")
    private BigDecimal preDiscountTotal;
}
