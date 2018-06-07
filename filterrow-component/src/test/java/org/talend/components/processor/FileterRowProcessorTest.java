package org.talend.components.processor;


import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.sdk.component.junit.JoinInputFactory;
import org.talend.sdk.component.junit.SimpleComponentRule;
import org.talend.sdk.component.runtime.output.Processor;

public class FileterRowProcessorTest {

    @ClassRule
    public static final SimpleComponentRule COMPONENT_FACTORY = new SimpleComponentRule("org.talend.components");

    @Test
    @Ignore("You need to complete this test")
    public void map() throws IOException {

        // Processor configuration
        // Setup your component configuration for the test here
        final FileterRowProcessorConfiguration configuration =  new FileterRowProcessorConfiguration();

        // We create the component processor instance using the configuration filled above
        final Processor processor = COMPONENT_FACTORY.createProcessor(FileterRowProcessor.class, configuration);

        // The join input factory construct inputs test data for every input branch you have defined for this component
        // Make sure to fil in some test data for the branches you want to test
        // You can also remove the branches that you don't need from the factory below
        final JoinInputFactory joinInputFactory =  new JoinInputFactory()
                                                            .withInput("__default__", asList(/* TODO - list of your input data for this branch. Instances of FileterRowDefaultInput.class */));


        // Run the flow and get the outputs
        final SimpleComponentRule.Outputs outputs = COMPONENT_FACTORY.collect(processor, joinInputFactory);

        // TODO - Test Asserts
        assertEquals(2, outputs.size()); // test of the output branches count of the component

        // Here you have all your processor output branches
        // You can fill in the expected data for every branch to test them
        final List<FileterRowREJECTOutput> value_REJECT = outputs.get(FileterRowREJECTOutput.class, "REJECT");
        assertEquals(asList(/* TODO - give a list of your expected values here. Instances of FileterRowREJECTOutput.class */), value_REJECT);

        final List<FileterRowDefaultOutput> value___default__ = outputs.get(FileterRowDefaultOutput.class, "__default__");
        assertEquals(asList(/* TODO - give a list of your expected values here. Instances of FileterRowDefaultOutput.class */), value___default__);

    }

}