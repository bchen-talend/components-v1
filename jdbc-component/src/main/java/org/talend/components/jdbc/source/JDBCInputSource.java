package org.talend.components.jdbc.source;

import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.talend.components.jdbc.JDBCDatasetConfiguration;
import org.talend.components.jdbc.service.Jdbc_componentService;
import org.talend.components.jdbc.service.ResultSetStringRecordConverter;
import org.talend.daikon.avro.converter.IndexedRecordConverter;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.input.Assessor;
import org.talend.sdk.component.api.input.Emitter;
import org.talend.sdk.component.api.input.PartitionMapper;
import org.talend.sdk.component.api.input.PartitionSize;
import org.talend.sdk.component.api.input.Split;
import org.talend.sdk.component.api.meta.Documentation;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.util.List;

@Version(1)
@Icon(Icon.IconType.STAR)
@PartitionMapper(name = "JDBCInput")
@Documentation("TODO fill the documentation for this source")
public class JDBCInputSource extends PTransform<PBegin, PCollection<IndexedRecord>> {
    private final JDBCDatasetConfiguration config;

    private AvroCoder<?> defaultOutputCoder;

    private transient IndexedRecordConverter<ResultSet, IndexedRecord> factory;

    public JDBCInputSource(@Option("configuration") final JDBCDatasetConfiguration config) {
        this.config = config;
        Schema schema = Jdbc_componentService.getSchema(config);
        defaultOutputCoder = AvroCoder.of(schema);
    }

    public Coder getDefaultOutputCoder() {
        return defaultOutputCoder;
    }

    @Override
    public PCollection<IndexedRecord> expand(PBegin input) {
        return (PCollection<IndexedRecord>) input.apply(JdbcIO
                .<IndexedRecord>read()
                .withDataSourceConfiguration(
                        JdbcIO.DataSourceConfiguration
                                .create(Jdbc_componentService.dbTypesInfo.get(config.getConnection().getDbTypes()).clazz,
                                        config.getConnection().getJdbcUrl())
                                .withUsername(config.getConnection().getUserId())
                                .withPassword(config.getConnection().getPassword()))
                .withQuery(config.getSql()).withRowMapper(new JdbcIO.RowMapper<IndexedRecord>() {

                    @Override
                    public IndexedRecord mapRow(ResultSet resultSet) throws Exception {
                        if (factory == null) {
                            factory = new ResultSetStringRecordConverter();
                            factory.setSchema(defaultOutputCoder.getSchema());
                        }
                        return factory.convertToAvro(resultSet);
                    }
                }).withCoder(getDefaultOutputCoder()));
    }

    @Assessor
    public long estimateDataSetByteSize() {
        return 0l;
    }

    @Split
    public List<JDBCInputSource> split(@PartitionSize final long desiredSize) {
        return null;
    }
    @Emitter
    public Object create() {
        return null;
    }
}
