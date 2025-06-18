package com.wuxp.codegen.swagger2.model.paging;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
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

        private Integer number_of_elements;

        private Integer size;

        private Integer total_elements;

        private Integer total_pages;

        private Pageable pageable;

        private Sort sort;

}
