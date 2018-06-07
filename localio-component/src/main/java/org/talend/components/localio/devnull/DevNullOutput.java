package org.talend.components.localio.devnull;

import org.apache.avro.generic.IndexedRecord;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.processor.Processor;


@Version(1) // default version is 1, if some configuration changes happen between 2 versions you can add a migrationHandler
@Icon(Icon.IconType.STAR) // you can use a custom one using @Icon(value=CUSTOM, custom="filename") and adding icons/filename_icon32.png in resources
@Processor(name = "DevNull")
@Documentation("TODO fill the documentation for this processor")
public class DevNullOutput extends PTransform<PCollection<IndexedRecord>, PCollection<IndexedRecord>> {
    private final DevNullOutputConfiguration configuration;

    public DevNullOutput(@Option("configuration") final DevNullOutputConfiguration configuration){
        this.configuration = configuration;
    }

    @Override
    public PCollection<IndexedRecord> expand(PCollection in) {
        return in;
    }
}