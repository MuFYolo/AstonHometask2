import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user); // Use persist for new entities
            transaction.commit();
            logger.info("User saved: {}", user.getEmail());
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to save user: {}", user.getEmail(), e);
            throw e; // Проброс исключения для обработки в UI
        }
    }

    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.find(User.class, id);
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            logger.error("Failed to find user by ID: {}", id, e);
            throw e;
        }
    }

    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (HibernateException e) {
            logger.error("Failed to retrieve all users", e);
            throw e;
        }
    }

    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user); // Use merge for detached/updated entities
            transaction.commit();
            logger.info("User updated: {}", user.getId());
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to update user: {}", user.getId(), e);
            throw e;
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user != null) {
                session.delete(user);
                logger.info("User deleted: {}", id);
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to delete user: {}", id, e);
            throw e;
        }
    }
}