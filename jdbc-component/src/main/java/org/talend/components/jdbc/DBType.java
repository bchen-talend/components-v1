package org.talend.components.jdbc;

import java.util.ArrayList;
import java.util.List;

public class DBType {

    public String id;

    public String clazz;

    public String url;

    public List<String> paths = new ArrayList<>();

    public DBType(String id, String clazz, String url, List<String> paths) {
        this.id = id;
        this.clazz = clazz;
        this.url = url;
        this.paths = paths;
    }

}
