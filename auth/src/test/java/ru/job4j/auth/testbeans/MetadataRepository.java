package ru.job4j.auth.testbeans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Repository
public class MetadataRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataRepository.class);

    private final EntityManager em;

    public MetadataRepository(EntityManagerFactory emf) {
        em = emf.createEntityManager();
    }

    @Transactional
    public void refreshTable() {
        String sql =
                "DROP TABLE IF EXISTS tz_persons; "
                + "CREATE TABLE tz_persons ("
                + "    id SERIAL PRIMARY KEY,"
                + "    login VARCHAR(60) NOT NULL UNIQUE,"
                + "    password VARCHAR(40) NOT NULL"
                + "); "
                + "INSERT INTO tz_persons (login, password) VALUES "
                + "('parsentev', '123'), ('ban', '123'), ('ivan', '123');";
        try {
            em.joinTransaction();
            em.createNativeQuery(sql).executeUpdate();
        } catch (Throwable ex) {
            LOG.error("Ошибка обновления данных: ", ex);
        }
    }
}
