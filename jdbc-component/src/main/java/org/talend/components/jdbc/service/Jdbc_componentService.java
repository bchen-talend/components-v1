package org.talend.components.jdbc.service;

import org.apache.avro.Schema;
import org.talend.components.jdbc.DBType;
import org.talend.components.jdbc.JDBCConnectionConfiguraiton;
import org.talend.components.jdbc.JDBCDatasetConfiguration;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.service.Service;
import org.talend.sdk.component.api.service.completion.DynamicValues;
import org.talend.sdk.component.api.service.completion.Values;
import org.talend.sdk.component.api.service.healthcheck.HealthCheck;
import org.talend.sdk.component.api.service.healthcheck.HealthCheckStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class Jdbc_componentService {

    @DynamicValues("dbTypesValuesProvider")
    public Values DbTypesValuesProvider() {
        return new Values(dbTypesInfo.keySet().stream().map(s -> new Values.Item(s, s)).collect(toList()));
    }

    @DynamicValues("jdbcUrlValuesProvider")
    public Values JdbcUrlValuesProvider() {
        // return dbTypesInfo.get(dbTypes).clazz; //TODO(bchen) how to use property from configuration class?
        return null;
    }

    @HealthCheck
    public HealthCheckStatus testConnection(@Option JDBCConnectionConfiguraiton config) {
        try {
            connect(config);
        } catch (ClassNotFoundException cnfe) {
            return new HealthCheckStatus(HealthCheckStatus.Status.KO, dbTypesInfo.get(config.getDbTypes()).clazz + " not found in classloader");
        } catch (SQLException se) {
            return new HealthCheckStatus(HealthCheckStatus.Status.KO, se.getMessage());
        }
        return new HealthCheckStatus(HealthCheckStatus.Status.OK, "JDBC connection test OK!");
    }

    public static Schema getSchema(JDBCDatasetConfiguration config) {
        try(Connection conn = connect(config.getConnection());
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(config.getSql())){
            ResultSetMetaData metaData = resultSet.getMetaData();
            return JDBCAvroRegistryString.get().inferSchemaResultSetMetaData(metaData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection connect(JDBCConnectionConfiguraiton config) throws ClassNotFoundException, SQLException {
        Class.forName(dbTypesInfo.get(config.getDbTypes()).clazz);
        return DriverManager.getConnection(config.getJdbcUrl(), config.getUserId(), config.getPassword());
    }

    public static Map<String, DBType> dbTypesInfo = new LinkedHashMap<>();

    static {
        dbTypesInfo.put("MYSQL",
                new DBType(
                        "MYSQL",
                        "org.gjt.mm.mysql.Driver",
                        "jdbc:mysql://localhost:3306/db",
                        Arrays.asList(new String[]{"mvn:org.talend.libraries/mysql-connector-java-5.1.30-bin/6.0.0/jar"})));
        dbTypesInfo.put("DERBY",
                new DBType(
                        "DERBY",
                        "org.apache.derby.jdbc.ClientDriver",
                        "jdbc:derby://localhost:3306/db",
                        Arrays.asList(new String[]{"mvn:org.apache.derby/derby/10.12.1.1"})));
        dbTypesInfo.put("ORACLE",
                new DBType(
                        "ORACLE",
                        "oracle.jdbc.OracleDriver",
                        "jdbc:oracle:thin:@localhost:1521:db",
                        Arrays.asList(new String[]{"mvn:oracle/ojdbc/7"})));

    }

}
