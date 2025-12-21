import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
public class UserServiceApp implements CommandLineRunner {

    private final UserService userService; // Spring сам внедрит сервис
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApp.class, args);
    }

    @Override
    public void run(String... args) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Spring Boot User Console ---");
            System.out.println("1. Create  2. View By ID  3. View All  4. Update  5. Delete  6. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1 -> {
                        UserDto dto = new UserDto();
                        System.out.print("Name: ");
                        dto.setName(scanner.nextLine());
                        System.out.print("Email: ");
                        dto.setEmail(scanner.nextLine());
                        System.out.print("Age: ");
                        dto.setAge(scanner.nextInt());
                        userService.save(dto);
                        System.out.println("Saved!");
                    }
                    case 3 -> userService.findAll().forEach(System.out::println);
                    case 6 -> running = false;
                    // Другие кейсы по аналогии...
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}