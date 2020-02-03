package de.derklaro.projects.deer.executor;

import de.derklaro.projects.deer.api.Database;
import de.derklaro.projects.deer.api.DatabaseDriver;
import de.derklaro.projects.deer.api.provider.DatabaseProvider;
import de.derklaro.projects.deer.api.writer.FileWriter;
import de.derklaro.projects.deer.executor.utils.DatabaseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;

public class BasicDatabaseDriver implements DatabaseDriver {

    static {
        DatabaseProvider.setDatabaseDriver(new BasicDatabaseDriver());
    }

    private BasicDatabaseDriver() {
    }

    @NotNull
    @Override
    public <T extends FileWriter> Database<T> getDatabase(@NotNull File folder, @NotNull Function<File, T> applier, int valueLength) {
        return DatabaseUtil.loadOrCreateDatabase(folder, applier, valueLength);
    }

    @Nullable
    @Override
    public <T extends FileWriter> Database<T> getDatabaseIfExists(@NotNull File folder, @NotNull Function<File, T> applier) {
        return DatabaseUtil.loadDatabase(folder, applier);
    }

    @Override
    public void deleteDatabase(@NotNull File database) {
        if (!database.exists() || !database.isDirectory()) {
            return;
        }

        try {
            Files.walkFileTree(database.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        Files.deleteIfExists(file);
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
