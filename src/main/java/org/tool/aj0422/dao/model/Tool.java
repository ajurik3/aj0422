package org.tool.aj0422.dao.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="tool")
public class Tool {

    @Id
    @Column(name="tool_code")
    private String toolCode;

    @Column(name="tool_type_id", updatable = false, insertable = false)
    private long toolTypeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tool_type_id")
    private ToolType toolType;

    private String brand;
    private boolean active;
}
