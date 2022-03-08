package ru.job4j.auth.testbeans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Repository
public class MetadataRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataRepository.class);

    private final EntityManager em;
    private final String initSql;
    private final String updateSql;

    public MetadataRepository(EntityManagerFactory emf) throws IOException {
        em = emf.createEntityManager();
        initSql = new String(getClass().getClassLoader().getResourceAsStream("database/init.sql").readAllBytes(), StandardCharsets.UTF_8);
        updateSql = new String(getClass().getClassLoader().getResourceAsStream("database/update.sql").readAllBytes(), StandardCharsets.UTF_8);
    }

    @Transactional
    public void refreshTable() {
        try {
            em.joinTransaction();
            em.createNativeQuery(initSql).executeUpdate();
            em.createNativeQuery(updateSql).executeUpdate();
        } catch (Throwable ex) {
            LOG.error("Ошибка обновления данных: ", ex);
        }
    }
}
