package com.language.ez.syntax;

import java.util.List;

public class Model {
    private final String name;
    private final List<String> items;

    public Model(String name, List<String> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public List<String> getItems() {
        return items;
    }
}
