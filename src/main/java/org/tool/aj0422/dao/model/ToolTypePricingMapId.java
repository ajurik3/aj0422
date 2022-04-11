package org.tool.aj0422.dao.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ToolTypePricingMapId implements Serializable {

    @Column(name="tool_type_id")
    private long toolTypeId;

    @Column(name="tool_pricing_id")
    private long toolPricingId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolTypePricingMapId that = (ToolTypePricingMapId) o;
        return toolTypeId == that.toolTypeId && toolPricingId == that.toolPricingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toolTypeId, toolPricingId);
    }
}
