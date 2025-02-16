import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

//Admin Username=Admin and Password=Admin1234

public class AdminApp {

    public static void main(String[] args) {
        // Establishing a database connection and handling any related exceptions
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Initializing Admin Service and Scanner for user input
            AdminDAO adminService = new AdminDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                // Displaying Admin Dashboard menu
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

                // Validating menu input to ensure it is an integer
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 8.");
                    scanner.next();  // Discard invalid input
                    System.out.print("Choose an option: ");
                }
                choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline character

                // Handling each menu option
                switch (choice) {
                    case 1: // Create a new user
                        try {
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

                            // Attempting to create a user
                            int userId = adminService.createUser(name, email, pass);
                            if (userId != -1) {
                                System.out.println("User registered successfully. User ID: " + userId);
                            } else {
                                System.out.println("Failed to register user.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error while creating user: " + e.getMessage());
                        }
                        break;

                    case 2: // Update an existing user
                        try {
                            System.out.print("Enter user ID to update: ");
                            while (!scanner.hasNextInt()) {
                                System.out.println("Invalid input. Please enter a valid user ID.");
                                scanner.next();
                            }
                            int userId = scanner.nextInt();
                            scanner.nextLine();  // Consume newline

                            System.out.print("Enter new name: ");
                            String name = scanner.nextLine().trim();
                            System.out.print("Enter new email: ");
                            String email = scanner.nextLine().trim();
                            System.out.print("Enter new password: ");
                            String pass = scanner.nextLine().trim();

                            // Attempting to update user details
                            String updated = adminService.updateUser(userId, name, email, pass);
                            System.out.println(updated);
                        } catch (Exception e) {
                            System.out.println("Error while updating user: " + e.getMessage());
                        }
                        break;

                    case 3: // Delete a user
                        try {
                            System.out.print("Enter user ID to delete: ");
                            while (!scanner.hasNextInt()) {
                                System.out.println("Invalid input. Please enter a valid user ID.");
                                scanner.next();
                            }
                            int userId = scanner.nextInt();

                            // Attempting to delete the user
                            String deleted = adminService.deleteUser(userId);
                            System.out.println(deleted);
                        } catch (Exception e) {
                            System.out.println("Error while deleting user: " + e.getMessage());
                        }
                        break;

                    case 4: // Add a new challenge
                        try {
                            System.out.print("Enter Challenge Name: ");
                            String cName = scanner.nextLine().trim();
                            System.out.print("Enter challenge Description: ");
                            String description = scanner.nextLine().trim();

                            if (cName.isEmpty() || description.isEmpty()) {
                                System.out.println("Challenge name and description cannot be empty.");
                                break;
                            }

                            // Attempting to add a challenge
                            Challenge challenge = new Challenge(cName, description);
                            adminService.addChallenge(challenge);
                            System.out.println("Challenge added successfully.");
                        } catch (Exception e) {
                            System.out.println("Error while adding challenge: " + e.getMessage());
                        }
                        break;

                    case 5: // View all fitness content
                        try {
                            List<Workout> workouts = adminService.getAllWorkouts();
                            if (workouts.isEmpty()) {
                                System.out.println("No workouts found.");
                            } else {
                                workouts.forEach(w -> System.out.println(w.toString()));
                            }
                        } catch (Exception e) {
                            System.out.println("Error while viewing fitness content: " + e.getMessage());
                        }
                        break;

                    case 6: // Add reviews for a user
                        try {
                            System.out.print("Enter user ID: ");
                            while (!scanner.hasNextInt()) {
                                System.out.println("Invalid input. Please enter a valid user ID.");
                                scanner.next();
                            }
                            int userId = scanner.nextInt();
                            scanner.nextLine();  // Consume newline

                            System.out.print("Enter Feedback: ");
                            String feedback = scanner.nextLine().trim();

                            // Attempting to add feedback
                            String feedbackAdded = adminService.userInteraction(userId, feedback);
                            System.out.println(feedbackAdded);
                        } catch (Exception e) {
                            System.out.println("Error while adding reviews: " + e.getMessage());
                        }
                        break;

                    case 7: // Update system settings
                        try {
                            System.out.print("Enter setting name: ");
                            String settingName = scanner.nextLine().trim();
                            System.out.print("Enter setting value: ");
                            String settingValue = scanner.nextLine().trim();

                            // Attempting to update settings
                            String settingUpdated = adminService.updateSetting(settingName, settingValue);
                            System.out.println(settingUpdated);
                        } catch (Exception e) {
                            System.out.println("Error while updating system setting: " + e.getMessage());
                        }
                        break;

                    case 8: // Exit the application
                        System.out.println("Exiting...");
                        break;

                    default: // Handle invalid choices
                        System.out.println("Invalid choice. Please select a number between 1 and 8.");
                }
            } while (choice != 8); // Loop until the user chooses to exit

            scanner.close(); // Close the scanner
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}

