import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoTest {


    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        Configuration configuration = new Configuration();

        Properties properties = new Properties();
        properties.put("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
        properties.put("hibernate.connection.username", postgreSQLContainer.getUsername());
        properties.put("hibernate.connection.password", postgreSQLContainer.getPassword());
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "true");

        configuration.setProperties(properties);
        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDao();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testSaveAndFindById() {
        User user = new User("TestName", "test@example.com", 30);
        userDao.save(user);

        Optional<User> foundUser = userDao.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("TestName", foundUser.get().getName());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testFindAll() {
        User user1 = new User("User1", "user1@example.com", 25);
        User user2 = new User("User2", "user2@example.com", 35);
        userDao.save(user1);
        userDao.save(user2);

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void testUpdate() {
        User user = new User("InitialName", "initial@example.com", 1);
        userDao.save(user);

        user.setName("UpdatedName");
        userDao.update(user);

        Optional<User> updatedUser = userDao.findById(user.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("UpdatedName", updatedUser.get().getName());
    }

    @Test
    public void testDelete() {
        User user = new User("UserToDelete", "delete@example.com", 99);
        userDao.save(user);
        Long userId = user.getId();

        userDao.delete(userId);

        Optional<User> deletedUser = userDao.findById(userId);
        assertFalse(deletedUser.isPresent());
    }
}