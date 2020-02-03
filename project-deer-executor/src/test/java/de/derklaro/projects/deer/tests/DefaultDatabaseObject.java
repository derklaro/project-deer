package de.derklaro.projects.deer.tests;

import de.derklaro.projects.deer.api.writer.FileWriter;
import org.jetbrains.annotations.NotNull;

public final class DefaultDatabaseObject implements FileWriter {

    public DefaultDatabaseObject(String value) {
        this.value = value;
    }

    public String value;

    @NotNull
    @Override
    public String toWriteableString() {
        return value;
    }
}
