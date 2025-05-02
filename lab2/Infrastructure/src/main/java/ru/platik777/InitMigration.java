package ru.platik777;

import org.flywaydb.core.Flyway;

public class InitMigration {
    public static void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/postgres",
                        "postgres", "postgres")
                .locations("classpath:db.migration")
                .load();
        flyway.migrate();
    }
}
