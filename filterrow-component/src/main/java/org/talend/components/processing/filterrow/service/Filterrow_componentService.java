package org.talend.components.processing.filterrow.service;

import org.talend.sdk.component.api.service.Service;
import org.talend.sdk.component.api.service.completion.DynamicValues;
import org.talend.sdk.component.api.service.completion.Values;

import java.util.stream.Collectors;

import static org.talend.components.processing.filterrow.ConditionsRowConstant.ALL_FUNCTIONS;
import static org.talend.components.processing.filterrow.ConditionsRowConstant.DEFAULT_OPERATORS;

@Service
public class Filterrow_componentService {

  @DynamicValues("functionProvider")
  public Values FunctionProvider() {
    return new Values(ALL_FUNCTIONS.stream().map(item -> new Values.Item(item, item)).collect(Collectors.toList()));
  }

  @DynamicValues("operatorProvider")
  public Values OperatorProvider() {
    return new Values(DEFAULT_OPERATORS.stream().map(item -> new Values.Item(item, item)).collect(Collectors.toList()));
  }
}