package com.wuxp.codegen.swagger2.model.paging;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import com.wuxp.codegen.swagger2.model.paging.Pageable;
        import com.wuxp.codegen.swagger2.model.paging.Sort;

@Data
@Accessors(chain = true)
public class  Page<T> {

        private List<T> content;

        private String empty;

        private String first;

        private String last;

        private Integer number;

        private Integer numberOfElements;

        private Integer size;

        private Integer totalElements;

        private Integer totalPages;

        private Pageable pageable;

        private Sort sort;

}
