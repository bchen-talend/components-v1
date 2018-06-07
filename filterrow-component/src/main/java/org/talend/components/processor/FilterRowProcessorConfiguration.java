package org.talend.components.processor;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@Data
@GridLayout({
    @GridLayout.Row("logicalOpType"),
    @GridLayout.Row("criterias")
})
@Documentation("TODO fill the documentation for this configuration")
public class FilterRowProcessorConfiguration implements Serializable {
  @Option
  @Documentation("")
  private LogicalOpType logicalOpType;

  @Option
  @Documentation("")
  private List<FilterRowCriteria> criterias;
}