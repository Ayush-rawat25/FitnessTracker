import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

//Admin Username=Admin and Password=Admin1234

public class AdminApp {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AdminDAO adminService = new AdminDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\nAdmin Dashboard:");
                System.out.println("1. Create User");
                System.out.println("2. Update User");
                System.out.println("3. Delete User");
                System.out.println("4. Add Challenge");
                System.out.println("5. View Fitness Content");
                System.out.println("6. Add Reviews For User");
                System.out.println("7. Update System Setting");
                System.out.println("8. Exit");
                System.out.print("Choose an option: ");

                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 8.");
                    scanner.next();  // Discard invalid input
                    System.out.print("Choose an option: ");
                }
                choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine().trim();
                        if (name.isEmpty()) {
                            System.out.println("Name cannot be empty.");
                            break;
                        }

                        System.out.print("Enter email: ");
                        String email = scanner.nextLine().trim();
                        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                            System.out.println("Invalid email format.");
                            break;
                        }

                        System.out.print("Enter password: ");
                        String pass = scanner.nextLine().trim();
                        if (pass.length() < 6) {
                            System.out.println("Password must be at least 6 characters long.");
                            break;
                        }

                        int userId = adminService.createUser(name, email, pass);
                        if (userId != -1) {
                            System.out.println("User registered successfully. User ID: " + userId);
                        } else {
                            System.out.println("Failed to register user.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter user ID to update: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid user ID.");
                            scanner.next();
                        }
                        userId = scanner.nextInt();
                        scanner.nextLine();  // Consume newline

                        System.out.print("Enter new name: ");
                        name = scanner.nextLine().trim();
                        System.out.print("Enter new email: ");
                        email = scanner.nextLine().trim();
                        System.out.print("Enter new password: ");
                        pass = scanner.nextLine().trim();

                        String updated = adminService.updateUser(userId, name, email, pass);
                        System.out.println(updated);
                        break;

                    case 3:
                        System.out.print("Enter user ID to delete: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid user ID.");
                            scanner.next();
                        }
                        userId = scanner.nextInt();

                        String deleted = adminService.deleteUser(userId);
                        System.out.println(deleted);
                        break;

                    case 4:
                        System.out.print("Enter Challenge Name: ");
                        String cName = scanner.nextLine().trim();
                        System.out.print("Enter challenge Description: ");
                        String description = scanner.nextLine().trim();

                        if (cName.isEmpty() || description.isEmpty()) {
                            System.out.println("Challenge name and description cannot be empty.");
                            break;
                        }

                        Challenge challenge = new Challenge(cName, description);
                        adminService.addChallenge(challenge);
                        System.out.println("Challenge added successfully.");
                        break;

                    case 5:
                        List<Workout> workouts = adminService.getAllWorkouts();
                        if (workouts.isEmpty()) {
                            System.out.println("No workouts found.");
                        } else {
                            workouts.forEach(w -> System.out.println(w.toString()));
                        }
                        break;

                    case 6:
                        System.out.print("Enter user ID: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid user ID.");
                            scanner.next();
                        }
                        userId = scanner.nextInt();
                        scanner.nextLine();  // Consume newline

                        System.out.print("Enter Feedback: ");
                        String feedback = scanner.nextLine().trim();
                        String feedbackAdded = adminService.userInteraction(userId, feedback);
                        System.out.println(feedbackAdded);
                        break;

                    case 7:
                        System.out.print("Enter setting name: ");
                        String settingName = scanner.nextLine().trim();
                        System.out.print("Enter setting value: ");
                        String settingValue = scanner.nextLine().trim();

                        String settingUpdated = adminService.updateSetting(settingName, settingValue);
                        System.out.println(settingUpdated);
                        break;

                    case 8:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 8.");
                }
            } while (choice != 8);

            scanner.close();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
