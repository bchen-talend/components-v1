package org.talend.component.integration;

import org.apache.beam.sdk.transforms.PTransform;
import org.talend.sdk.component.runtime.base.Lifecycle;
import org.talend.sdk.component.runtime.input.Mapper;
import org.talend.sdk.component.runtime.manager.ComponentManager;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Test {
    public static void main(String[] args) {
        Jsonb jsonb = JsonbBuilder.create();
        Map map = jsonb.fromJson("{\"configuration\":{\"sourceType\":\"QUERY\",\"connection\":{\"dbTypes\":\"MYSQL\",\"jdbcUrl\":\"jdbc:localhost:3306\",\"userId\":\"abc\",\"password\":\"abc\"},\"sql\":\"select * from abc\"}}", Map.class);
        System.out.println(map);
        map = jsonb.fromJson("{\"configuration.sourceType\": \"QUERY\"}", Map.class);
        System.out.println(map);
        ComponentManager componentManager = ComponentManager.instance();
        Map<String, String> config = new HashMap();
        config.put("configuration.sourceType","QUERY");
        config.put("configuration.connection.dbTypes","MYSQL");
        config.put("configuration.connection.jdbcUrl","jdbc:localhost:3306");
        config.put("configuration.connection.userId","abc");
        config.put("configuration.connection.password","abc");
        config.put("configuration.sql","select * from abc");
        Optional<Mapper> mapper = componentManager.findMapper("JDBC", "JDBCInput", 1, config);
        System.out.println(mapper.get() instanceof PTransform);
        System.out.print(mapper);

        Optional<Object> component = componentManager.createComponent("JDBC", "JDBCInput", ComponentManager.ComponentType.MAPPER, 1, config);
        System.out.println(component.get() instanceof PTransform);
        System.out.print(component);
        ((Lifecycle)component.get()).start();


    }
}
