package com.wuxp.codegen.swagger2.model.paging;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import com.wuxp.codegen.swagger2.model.paging.Sort;

@Data
@Accessors(chain = true)
public class  Pageable {

        private String paged;

        private String unpaged;

        private Integer offset;

        private Integer pageSize;

        private Integer pageNumber;

        private Sort sort;

}
