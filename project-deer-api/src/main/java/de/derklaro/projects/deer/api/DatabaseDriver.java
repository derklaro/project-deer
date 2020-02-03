package de.derklaro.projects.deer.api;

import de.derklaro.projects.deer.api.writer.FileWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.Function;

public interface DatabaseDriver {

    @NotNull
    <T extends FileWriter> Database<T> getDatabase(
            @NotNull File folder,
            @NotNull Function<File, T> applier,
            int valueLength
    );

    @Nullable
    <T extends FileWriter> Database<T> getDatabaseIfExists(
            @NotNull File folder,
            @NotNull Function<File, T> applier
    );

    void clearDatabase(@NotNull File database);

    void deleteDatabase(@NotNull File database);
}
