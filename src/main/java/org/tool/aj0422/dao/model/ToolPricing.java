package org.tool.aj0422.dao.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="tool_pricing")
public class ToolPricing {

    @Id
    @Column(name="tool_pricing_id")
    private long getToolPricingId;

    @Column(name="tool_rate")
    private BigDecimal toolRate;

    private boolean weekday;

    private boolean weekend;

    private boolean holiday;

    @Column(name="create_time")
    private LocalDateTime createTime;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="tool_pricing_id", updatable = false, insertable = false)
    private List<ToolTypePricingMap> toolTypePricingMaps;
}
