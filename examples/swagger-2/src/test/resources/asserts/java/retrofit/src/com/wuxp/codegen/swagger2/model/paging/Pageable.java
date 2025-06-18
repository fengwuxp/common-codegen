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

        private Integer page_size;

        private Integer page_number;

        private Sort sort;

}
