import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Admin Username=Admin and Password=Admin1234

public class FitnessApp {
    private static int loggedInUserId = -1;

    // Method to validate user credentials
    private static boolean validateUser(String name, String password) {
        boolean isValid = false;

        // Validate inputs
        if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return false;
        }

        try {
            // Establish database connection
            Connection connection = DatabaseConnection.getConnection();

            // Prepare the SQL query to check the user's credentials
            String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, password);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if any record was returned
            if (resultSet.next()) {
                isValid = true;
                loggedInUserId = resultSet.getInt("id");
            } else {
                System.out.println("Invalid username or password.");
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }

        return isValid;
    }

    // Method to validate email format
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DatabaseConnection.getConnection()) {
            AdminDAO adminService = new AdminDAOImpl(connection);

            while (true) {
                System.out.println("Welcome to the Fitness Tracker App!");
                System.out.println("Press 1 for Admin functions");
                System.out.println("Press 2 for User functions");
                System.out.println("Press 3 for Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character

                if (choice == 1) {
                    // Admin login functionality
                    String inputUsername = "";
                    String inputPassword = "";

                    // Loop for valid admin credentials
                    while (true) {
                        System.out.print("Enter your username: ");
                        inputUsername = scanner.nextLine();
                        System.out.print("Enter your password: ");
                        inputPassword = scanner.nextLine();

                        // Validate inputs
                        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                            System.out.println("Username and password cannot be empty.");
                        } else if (inputUsername.equals("Admin") && inputPassword.equals("Admin1234")) {
                            System.out.println("Login successful! Welcome!");

                            // Admin Dashboard loop
                            while (true) {
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
                                int adminChoice = scanner.nextInt();
                                scanner.nextLine();  // Consume the newline character

                                if (adminChoice == 8) {
                                    System.out.println("Exiting...");
                                    break; // Exit the admin dashboard loop
                                }

                                // Handle other options here
                                // For now, just print the selected option
                                System.out.println("You selected option " + adminChoice);
                            }

                            break; // Exit the admin login loop
                        } else {
                            System.out.println("Invalid username or password. Please try again.");
                        }
                    }
                } else if (choice == 2) {
                    // User functionality (same as before)
                    System.out.println("Welcome to the Fitness Tracker App!");
                    System.out.println("Press 1 for Sign up");
                    System.out.println("Press 2 for Login");

                    int userChoice = scanner.nextInt();
                    scanner.nextLine();  // Consume the newline character

                    switch (userChoice) {
                        case 1:
                            // Sign up process with validation
                            String name = "";
                            String email = "";
                            String pass = "";

                            while (true) {
                                System.out.print("Enter name: ");
                                name = scanner.nextLine().trim();
                                if (name.isEmpty()) {
                                    System.out.println("Name cannot be empty.");
                                    continue;
                                }

                                System.out.print("Enter email: ");
                                email = scanner.nextLine().trim();
                                if (!isValidEmail(email)) {
                                    System.out.println("Invalid email format. Please enter a valid email.");
                                    continue;
                                }

                                System.out.print("Enter password: ");
                                pass = scanner.nextLine().trim();
                                if (pass.isEmpty()) {
                                    System.out.println("Password cannot be empty.");
                                    continue;
                                }

                                // Proceed to create user
                                loggedInUserId = adminService.createUser(name, email, pass);
                                if (loggedInUserId != -1) {
                                    System.out.println("Sign up successful! Welcome, " + name);
                                    User user = new User();
                                    user.main(new int[]{loggedInUserId});
                                    break;
                                } else {
                                    System.out.println("User registration failed. Please try again.");
                                }
                            }
                            break;

                        case 2:
                            // Login process with validation
                            String Username = "";
                            String Password = "";

                            while (true) {
                                System.out.print("Enter your username: ");
                                Username = scanner.nextLine().trim();
                                System.out.print("Enter your password: ");
                                Password = scanner.nextLine().trim();

                                if (Username.isEmpty() || Password.isEmpty()) {
                                    System.out.println("Username and password cannot be empty.");
                                    continue;
                                }

                                // Check if the entered credentials match
                                if (validateUser(Username, Password)) {
                                    System.out.println("Login successful! Welcome!");
                                    User users = new User();
                                    users.main(new int[]{loggedInUserId});
                                    break;
                                } else {
                                    System.out.println("Invalid username or password. Please try again.");
                                }
                            }
                            break;

                        default:
                            System.out.println("Invalid choice. Please press 1 or 2.");
                    }
                }else if(choice == 3) {
                	System.out.println("Exiting....");
                	break;
                } else {
                    System.out.println("Invalid choice. Please press 1 or 2.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();  // Close scanner after all processes are complete
        }
    }
}
