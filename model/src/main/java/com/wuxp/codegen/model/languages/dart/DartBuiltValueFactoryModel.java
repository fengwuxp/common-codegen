package com.wuxp.codegen.model.languages.dart;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * dart built value factory model
 *
 * @author wxup
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DartBuiltValueFactoryModel implements Serializable {


  /**
   * fullType code
   */
  private String fullTypeCode;


  /**
   * function code
   */
  private String functionCode;
}
