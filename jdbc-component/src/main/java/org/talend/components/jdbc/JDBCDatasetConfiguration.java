package org.talend.components.jdbc;

import lombok.Data;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.condition.ActiveIf;
import org.talend.sdk.component.api.configuration.type.DataSet;
import org.talend.sdk.component.api.configuration.ui.DefaultValue;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import java.io.Serializable;

import static org.talend.components.jdbc.JDBCDatasetConfiguration.SourceType.QUERY;
import static org.talend.components.jdbc.JDBCDatasetConfiguration.SourceType.TABLE;

@Version(1)
@Data
@DataSet("JDBCDataset")
@GridLayout({
        @GridLayout.Row({"connection"}),
        @GridLayout.Row({"sourceType"}),
        @GridLayout.Row({"tableName"}),
        @GridLayout.Row({"sql"})
})
public class JDBCDatasetConfiguration implements Serializable {

    @Option
    @Documentation("jdbc connection")
    private JDBCConnectionConfiguraiton connection;

    @Option
    @DefaultValue("QUERY")
    @Documentation("TODO fill the documentation for this parameter")
    private SourceType sourceType;

    @Option
    @ActiveIf(target = "sourceType", value = "TABLE")
    @Documentation("TODO fill the documentation for this parameter")
    private String tableName;

    @Option
    @ActiveIf(target = "sourceType", value = "QUERY")
    @Documentation("TODO fill the documentation for this parameter")
    private String sql;

    public String getSql() {
        switch (getSourceType()) {
            case TABLE:
                return "select * from " + getTableName();
            case QUERY:
            default:
                return sql;
        }
    }


    public enum SourceType {
        TABLE,
        QUERY
    }


}
