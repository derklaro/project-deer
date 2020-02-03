package de.derklaro.projects.deer.tests;

import de.derklaro.projects.deer.api.Database;
import de.derklaro.projects.deer.api.basic.Filters;
import de.derklaro.projects.deer.api.provider.DatabaseProvider;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static org.junit.Assert.*;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public final class DatabaseTest {

    @Test
    public void stage0TestInitDatabase() {
        try {
            Class.forName("de.derklaro.projects.deer.executor.BasicDatabaseDriver");
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        DatabaseProvider.getDatabaseDriver().getDatabase(
                new File("test"),
                file -> {
                    try {
                        String line = String.join("\n", Files.readAllLines(file.toPath()));
                        return new DefaultDatabaseObject(line);
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }

                    return new DefaultDatabaseObject("NULL");
                }, 1
        );

        assertTrue("Database isn't created", new File("test").isDirectory());
    }

    @Test
    public void stage1TestInsert() {
        Database<DefaultDatabaseObject> database = DatabaseProvider.getDatabaseDriver().getDatabaseIfExists(
                new File("test"),
                file -> {
                    try {
                        String line = String.join("\n", Files.readAllLines(file.toPath()));
                        return new DefaultDatabaseObject(line);
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }

                    return new DefaultDatabaseObject("NULL");
                }
        );

        assertNotNull("Could not load database, it does not exists", database);

        database.insert("test", new String[]{"hello"}, new DefaultDatabaseObject("test"));

        Optional<DefaultDatabaseObject> object = database.getEntry(Filters.keyEq("test"));

        assertTrue("The database object is not inserted", object.isPresent());
        assertNotEquals("The database object is not correctly read from the database", "NULL", object.get().value);
    }

    @Test
    public void stage2TestUpdate() {
        Database<DefaultDatabaseObject> database = DatabaseProvider.getDatabaseDriver().getDatabaseIfExists(
                new File("test"),
                file -> {
                    try {
                        String line = String.join("\n", Files.readAllLines(file.toPath()));
                        return new DefaultDatabaseObject(line);
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }

                    return new DefaultDatabaseObject("NULL");
                }
        );

        assertNotNull("Could not load database, it does not exists", database);

        Optional<DefaultDatabaseObject> object = database.getEntry(Filters.keyEq("test"));

        assertTrue("The database object is not inserted", object.isPresent());
        assertEquals("The database object is not correctly inserted", "test", object.get().value);

        database.updateKey(Filters.keyEq("test"), new DefaultDatabaseObject("hello"));

        object = database.getEntry(Filters.keyEq("test"));

        assertTrue("The database object is not inserted", object.isPresent());
        assertEquals("The database object is not correctly inserted", "hello", object.get().value);

        object = database.getEntry(Filters.findValue("hello", 1));

        assertTrue("The database object is not inserted", object.isPresent());
        assertEquals("The database object is not correctly inserted", "hello", object.get().value);
    }

    @Test
    public void stage3TestDelete() {
        Database<DefaultDatabaseObject> database = DatabaseProvider.getDatabaseDriver().getDatabaseIfExists(
                new File("test"),
                file -> {
                    try {
                        String line = String.join("\n", Files.readAllLines(file.toPath()));
                        return new DefaultDatabaseObject(line);
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }

                    return new DefaultDatabaseObject("NULL");
                }
        );

        assertNotNull("Could not load database, it does not exists", database);

        Optional<DefaultDatabaseObject> object = database.getEntry(Filters.findValue("hello", 1));

        assertTrue("The database object is not inserted", object.isPresent());
        assertEquals("The database object is not correctly inserted", "hello", object.get().value);

        database.delete(Filters.keyEq("test"));

        object = database.getEntry(Filters.findValue("hello", 1));

        assertFalse("The database object is not inserted", object.isPresent());
    }

    @Test
    public void stage4ClearDatabase() {
        DatabaseProvider.getDatabaseDriver().clearDatabase(new File("test"));

        File[] files = new File("test").listFiles();

        assertNotNull("The database is not created", files);
        assertEquals("The database is not empty", 1, files.length);
    }

    @Test
    public void stage5DeleteDatabase() {
        File file = new File("test");

        assertTrue("Database is not a directory", file.isDirectory());

        DatabaseProvider.getDatabaseDriver().deleteDatabase(new File("test"));

        assertFalse("Could not delete database", file.exists());
    }
}
