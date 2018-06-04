package org.talend.components.jdbc;

import lombok.Data;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.action.Checkable;
import org.talend.sdk.component.api.configuration.action.Proposable;
import org.talend.sdk.component.api.configuration.constraint.Required;
import org.talend.sdk.component.api.configuration.type.DataStore;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.configuration.ui.widget.Credential;
import org.talend.sdk.component.api.meta.Documentation;

import java.io.Serializable;

@Version(1)
@Data
@DataStore("JDBCConnection")
@Checkable
@GridLayout({
        @GridLayout.Row({"dbTypes"}),
        @GridLayout.Row({"jdbcUrl"}),
        @GridLayout.Row({"userId"}),
        @GridLayout.Row({"password"})
})
public class JDBCConnectionConfiguraiton implements Serializable {
    @Option
    @Proposable("dbTypesValuesProvider") //TODO(bchen) how to set default value when using Proposable
    @Required
    @Documentation("TODO fill the documentation for this parameter")
    private String dbTypes;

    @Option
    @Required
    @Documentation("TODO fill the documentation for this parameter")
    private String jdbcUrl; //TODO(bchen) how to set placeholder by different DB type

    @Option
    @Required
    @Documentation("TODO fill the documentation for this parameter")
    private String userId;

    @Credential
    @Option
    @Required
    @Documentation("TODO fill the documentation for this parameter")
    private String password;


}
