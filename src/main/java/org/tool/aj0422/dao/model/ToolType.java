package org.tool.aj0422.dao.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="tool_type")
public class ToolType {
    @Id
    @Column(name="tool_type_id")
    private long toolTypeId;
    @Column(name="tool_type")
    private String toolType;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="tool_type_id", insertable = false, updatable = false)
    private List<ToolTypePricingMap> toolTypePricingMaps;
}
