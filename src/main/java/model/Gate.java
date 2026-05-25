package model;

import enums.GateType;

public abstract class  Gate {
    private final String id;

    public Gate(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract GateType getType();
}
