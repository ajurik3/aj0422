package org.tool.aj0422.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.tool.aj0422.dao.model.Tool;

import javax.persistence.QueryHint;
import java.time.LocalDateTime;

public interface ToolRepository extends CrudRepository<Tool, String> {

    @QueryHints(value = {
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH, value = "false")
    })
    @Query("SELECT DISTINCT t FROM Tool t " +
            "INNER JOIN FETCH t.toolType tt " +
            "INNER JOIN FETCH tt.toolTypePricingMaps tpm " +
            "INNER JOIN FETCH tpm.toolPricing tp " +
            "WHERE t.toolCode=?1 AND tp.createTime < ?2 " +
            "ORDER BY tp.createTime DESC")
    Tool findToolPricingByToolCodeAndCheckOutDate(String code, LocalDateTime time);
}
