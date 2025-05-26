/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Password Manager Team
 * Date: 4/30/2025
 * Time: 9:00 PM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: LoginController
 *
 * Description: Controller for the login view
 *
 * ****************************************
 */

package org.ABAKBM.controller;

import org.ABAKBM.AuthenticationManager;
import org.ABAKBM.PasswordManager;
import org.ABAKBM.view.LoginView;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the login view.
 * Handles authentication and user registration.
 */
public class LoginController {
    // Model components
    private final AuthenticationManager authManager;
    private final PasswordManager passwordManager;

    // View component
    private final LoginView view;

    // Stage reference
    private final Stage primaryStage;

    /**
     * Constructor for LoginController.
     *
     * @param authManager The authentication manager
     * @param passwordManager The password manager
     * @param view The login view
     * @param primaryStage The primary stage
     */
    public LoginController(AuthenticationManager authManager, PasswordManager passwordManager,
                           LoginView view, Stage primaryStage) {
        this.authManager = authManager;
        this.passwordManager = passwordManager;
        this.view = view;
        this.primaryStage = primaryStage;

        // Initialize the controller
        initialize();
    }

    /**
     * Initializes the controller by setting up event handlers.
     */
    private void initialize() {
        // Set up event handlers
        setupEventHandlers();
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        // Login button
        view.getLoginButton().setOnAction(e -> handleLogin());

        // Register button (to show registration form)
        view.getRegisterButton().setOnAction(e -> view.showRegisterForm());

        // Back button (to return to login form)
        view.getBackButton().setOnAction(e -> view.showLoginForm());

        // Create account button
        view.getCreateAccountButton().setOnAction(e -> handleRegister());

        // Enter key in password field triggers login
        view.getPasswordField().setOnAction(e -> handleLogin());

        // Enter key in confirm password field triggers registration
        view.getConfirmPasswordField().setOnAction(e -> handleRegister());
    }

    /**
     * Handles the login button click.
     */
    private void handleLogin() {
        // Validate form
        if (!view.validateLoginForm()) {
            return;
        }

        // Get username and password
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();

        // Show progress indicator
        view.showProgress(true);
        view.setLoginStatus("Logging in...", false);

        // Use a small delay to show the progress indicator
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            try {
                // Attempt login
                boolean success = authManager.login(username, password);

                if (success) {
                    // Login successful
                    view.setLoginStatus("Login successful!", false);

                    // Show main screen after a short delay
                    PauseTransition successPause = new PauseTransition(Duration.seconds(0.5));
                    successPause.setOnFinished(e -> showMainScreen());
                    successPause.play();
                } else {
                    // Login failed
                    view.showProgress(false);
                    view.setLoginStatus("Invalid username or password", true);
                }
            } catch (Exception e) {
                // Error occurred
                view.showProgress(false);
                view.setLoginStatus("Error: " + e.getMessage(), true);
            }
        });
        pause.play();
    }

    /**
     * Handles the register button click.
     */
    private void handleRegister() {
        // Validate form
        if (!view.validateRegisterForm()) {
            return;
        }

        // Get username and password
        String username = view.getRegUsernameField().getText().trim();
        String password = view.getRegPasswordField().getText();

        try {
            // Check if user already exists
            if (authManager.isUserRegistered(username)) {
                view.setRegisterStatus("Username already exists", true);
                return;
            }

            // Register new user
            boolean success = authManager.registerUser(username, password);

            if (success) {
                // Registration successful
                view.setRegisterStatus("Account created successfully!", false);

                // Show login screen after a short delay
                PauseTransition successPause = new PauseTransition(Duration.seconds(1.5));
                successPause.setOnFinished(e -> view.showLoginForm());
                successPause.play();
            } else {
                // Registration failed
                view.setRegisterStatus("Failed to create account", true);
            }
        } catch (Exception e) {
            // Error occurred
            view.setRegisterStatus("Error: " + e.getMessage(), true);
        }
    }

    /**
     * Shows the main password manager screen.
     */
    private void showMainScreen() {
        try {
            // Create the controller (which will create the view)
            PasswordManagerController controller = new PasswordManagerController(
                    authManager, passwordManager, primaryStage);

            // Create and set the scene
            Scene scene = new Scene(controller.getView().getRoot(), 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Password Manager - " +
                    authManager.getLoggedInUser().getUsername());
            primaryStage.setResizable(true);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace(); // Print full stack trace
            view.showProgress(false);
            view.setLoginStatus("Error showing main screen: " + e.getMessage(), true);
        }
    }
}