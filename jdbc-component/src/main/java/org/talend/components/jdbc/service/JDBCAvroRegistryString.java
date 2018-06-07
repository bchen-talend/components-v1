// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.jdbc.service;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.SchemaBuilder;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.NameUtil;
import org.talend.daikon.avro.SchemaConstants;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JDBCAvroRegistryString {

    private static final JDBCAvroRegistryString sInstance = new JDBCAvroRegistryString();

    public static JDBCAvroRegistryString get() {
        return sInstance;
    }

    protected Schema inferSchemaResultSetMetaData(ResultSetMetaData metadata) throws SQLException {
        List<Field> fields = new ArrayList<>();

        Set<String> existNames = new HashSet<String>();
        int index = 0;

        int count = metadata.getColumnCount();
        for (int i = 1; i <= count; i++) {
            int size = metadata.getPrecision(i);
            int scale = metadata.getScale(i);
            boolean nullable = ResultSetMetaData.columnNullable == metadata.isNullable(i);

            int dbtype = metadata.getColumnType(i);
            String fieldName = metadata.getColumnLabel(i);
            String dbColumnName = metadata.getColumnName(i);

            // not necessary for the result schema from the query statement
            boolean isKey = false;

            String validName = NameUtil.correct(fieldName, index++, existNames);
            existNames.add(validName);

            Field field = sqlType2Avro(size, scale, dbtype, nullable, validName, dbColumnName, null, isKey);

            fields.add(field);
        }
        return Schema.createRecord("DYNAMIC", null, null, false, fields);
    }


    private Set<String> getPrimaryKeys(DatabaseMetaData databaseMetdata, String catalogName, String schemaName, String tableName)
            throws SQLException {
        Set<String> result = new HashSet<>();

        try (ResultSet resultSet = databaseMetdata.getPrimaryKeys(catalogName, schemaName, tableName)) {
            while (resultSet.next()) {
                result.add(resultSet.getString("COLUMN_NAME"));
            }
        }

        return result;
    }

    protected Field sqlType2Avro(int size, int scale, int dbtype, boolean isNullable, String name, String dbColumnName,
                                 Object defaultValue, boolean isKey) {
        Field field = null;
        Schema schema = null;

        switch (dbtype) {
            case java.sql.Types.VARCHAR:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, size);
                break;
            case java.sql.Types.INTEGER:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PRECISION, size);
                break;
            case java.sql.Types.DECIMAL:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PRECISION, size);
                field.addProp(SchemaConstants.TALEND_COLUMN_SCALE, scale);
                break;
            case java.sql.Types.BIGINT:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PRECISION, size);
                break;
            case java.sql.Types.NUMERIC:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PRECISION, size);
                field.addProp(SchemaConstants.TALEND_COLUMN_SCALE, scale);
                break;
            case java.sql.Types.TINYINT:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PRECISION, size);
                break;
            case java.sql.Types.DOUBLE:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                break;
            case java.sql.Types.FLOAT:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                break;
            case java.sql.Types.DATE:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PATTERN, "yyyy-MM-dd");
                break;
            case java.sql.Types.TIME:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PATTERN, "HH:mm:ss");
                break;
            case java.sql.Types.TIMESTAMP:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PATTERN, "yyyy-MM-dd HH:mm:ss.SSS");
                break;
            case java.sql.Types.BOOLEAN:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                break;
            case java.sql.Types.REAL:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                break;
            case java.sql.Types.SMALLINT:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PRECISION, size);
                break;
            case java.sql.Types.LONGVARCHAR:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, size);
                break;
            case java.sql.Types.CHAR:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, size);
                break;
            default:
                schema = AvroUtils._string();
                field = wrap(isNullable, schema, name);
                break;
        }

        field.addProp(SchemaConstants.TALEND_COLUMN_DB_TYPE, dbtype);
        field.addProp(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, dbColumnName);

        if (defaultValue != null) {
            field.addProp(SchemaConstants.TALEND_COLUMN_DEFAULT, defaultValue);
        }

        if (isKey) {
            field.addProp(SchemaConstants.TALEND_COLUMN_IS_KEY, "true");
        }

        return field;
    }

    protected Field wrap(boolean nullable, Schema base, String name) {
        Schema schema = nullable ? SchemaBuilder.builder().nullable().type(base) : base;
        return new Field(name, schema, null, (Object) null);
    }

}
