package com.wuxp.codegen.swagger2.model.paging;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Accessors(chain = true)
public class  Sort {

        private String unsorted;

        private String sorted;

        private String empty;

}
