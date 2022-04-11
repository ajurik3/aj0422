package org.tool.aj0422.dao.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name="tool_type_pricing_map")
public class ToolTypePricingMap implements Serializable {

    @EmbeddedId
    private ToolTypePricingMapId toolTypePricingMapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tool_pricing_id", updatable = false, insertable = false)
    private ToolPricing toolPricing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tool_type_id", updatable = false, insertable = false)
    private ToolType toolType;
}
