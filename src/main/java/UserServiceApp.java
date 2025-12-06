import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class UserServiceApp {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceApp.class);
    private static final UserDao userDao = new UserDao();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info("Application starting...");
        boolean running = true;

        while (running) {
            printMenu();
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                try {
                    switch (choice) {
                        case 1:
                            createUser();
                            break;
                        case 2:
                            viewUserById();
                            break;
                        case 3:
                            viewAllUsers();
                            break;
                        case 4:
                            updateUser();
                            break;
                        case 5:
                            deleteUser();
                            break;
                        case 6:
                            running = false;
                            HibernateUtil.shutdown();
                            logger.info("Application exiting.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                    }
                } catch (Exception e) {
                    System.err.println("An error occurred during DB operation: " + e.getMessage());
                    logger.error("Error in operation", e);
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- User Service Menu ---");
        System.out.println("1. Create User");
        System.out.println("2. View User by ID");
        System.out.println("3. View All Users");
        System.out.println("4. Update User");
        System.out.println("5. Delete User");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createUser() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        User newUser = new User(name, email, age);
        userDao.save(newUser);
        System.out.println("User created successfully!");
    }

    private static void viewUserById() {
        System.out.print("Enter User ID: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        userDao.findById(id).ifPresentOrElse(
                user -> System.out.println("Found: " + user),
                () -> System.out.println("User not found with ID: " + id)
        );
    }

    private static void viewAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.print("Enter User ID to update: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        userDao.findById(id).ifPresentOrElse(user -> {
            System.out.print("Enter new Name (current: " + user.getName() + "): ");
            String name = scanner.nextLine();
            System.out.print("Enter new Email (current: " + user.getEmail() + "): ");
            String email = scanner.nextLine();
            System.out.print("Enter new Age (current: " + user.getAge() + "): ");
            int age = scanner.nextInt();
            scanner.nextLine();

            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            userDao.update(user);
            System.out.println("User updated successfully!");
        }, () -> System.out.println("User not found with ID: " + id));
    }

    private static void deleteUser() {
        System.out.print("Enter User ID to delete: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        userDao.findById(id).ifPresentOrElse(user -> {
            userDao.delete(id);
            System.out.println("User deleted successfully!");
        }, () -> System.out.println("User not found with ID: " + id));
    }
}