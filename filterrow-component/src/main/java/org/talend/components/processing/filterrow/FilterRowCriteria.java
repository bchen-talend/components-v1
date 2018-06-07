package org.talend.components.processing.filterrow;

import lombok.Data;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.action.Proposable;
import org.talend.sdk.component.api.meta.Documentation;

import java.io.Serializable;

@Data
public class FilterRowCriteria implements Serializable {
  @Option
  @Documentation("This enum will be filled with the name of the input columns")
  private String columnName;

  @Option
  @Proposable("functionProvider")
  @Documentation("")
  private String function;

  @Option
  @Proposable("operatorProvider")
  @Documentation("")
  private String operator;

  @Option
  @Documentation("")
  private String value;
}
