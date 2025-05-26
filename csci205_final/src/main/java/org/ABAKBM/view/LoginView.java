/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim
 * Date: 4/29/25
 * Time: 11:04 PM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: LoginView
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * LoginView class for the Password Manager application.
 * Provides the user interface for login and registration.
 */
public class LoginView {
    // Main container
    private BorderPane root;

    // Login form components
    private VBox loginForm;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private Label statusLabel;
    private ProgressIndicator progressIndicator;

    // Registration form components
    private VBox registerForm;
    private TextField regUsernameField;
    private PasswordField regPasswordField;
    private PasswordField confirmPasswordField;
    private Button createAccountButton;
    private Button backButton;
    private Label regStatusLabel;

    // Current state: LOGIN or REGISTER
    private enum ViewState { LOGIN, REGISTER }
    private ViewState currentState;

    /**
     * Constructor for LoginView.
     * Initializes the UI components and sets up the layout.
     */
    public LoginView() {
        initializeView();
    }

    /**
     * Gets the root pane of the view.
     *
     * @return The root BorderPane
     */
    public BorderPane getRoot() {
        return root;
    }

    /**
     * Initializes the view components.
     */
    private void initializeView() {
        root = new BorderPane();
        root.setPadding(new Insets(20));

        setupHeaderFooter();
        setupLoginForm();
        setupRegisterForm();

        // Start with login form
        showLoginForm();
    }

    /**
     * Sets up the header and footer of the view.
     */
    private void setupHeaderFooter() {
        // Header
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        Text title = new Text("Password Manager");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setFill(Color.web("#2196f3"));

        Text subtitle = new Text("Secure. Simple. Safe.");
        subtitle.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitle.setFill(Color.web("#757575"));

        header.getChildren().addAll(title, subtitle);

        // Footer
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 0, 0, 0));

        Label footerLabel = new Label("CSCI 205 - Spring 2025");
        footerLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));
        footerLabel.setTextFill(Color.web("#757575"));

        footer.getChildren().add(footerLabel);

        // Add to root
        root.setTop(header);
        root.setBottom(footer);
    }

    /**
     * Sets up the login form.
     */
    private void setupLoginForm() {
        loginForm = new VBox(15);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(20));
        loginForm.setMaxWidth(300);

        // Title
        Label formTitle = new Label("Login");
        formTitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Username field
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password field
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Master Password");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your master password");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setDefaultButton(true);

        registerButton = new Button("Register");
        registerButton.setPrefWidth(100);

        buttonBox.getChildren().addAll(loginButton, registerButton);

        // Status label
        statusLabel = new Label();
        statusLabel.setTextFill(Color.RED);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setMinHeight(20);

        // Progress indicator
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxHeight(30);
        progressIndicator.setMaxWidth(30);

        // Add all to form
        loginForm.getChildren().addAll(
                formTitle,
                usernameBox,
                passwordBox,
                buttonBox,
                progressIndicator,
                statusLabel);
    }

    /**
     * Sets up the registration form.
     */
    private void setupRegisterForm() {
        registerForm = new VBox(15);
        registerForm.setAlignment(Pos.CENTER);
        registerForm.setPadding(new Insets(20));
        registerForm.setMaxWidth(300);

        // Title
        Label formTitle = new Label("Create Account");
        formTitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Username field
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username");
        regUsernameField = new TextField();
        regUsernameField.setPromptText("Choose a username");
        usernameBox.getChildren().addAll(usernameLabel, regUsernameField);

        // Password field
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Master Password");
        regPasswordField = new PasswordField();
        regPasswordField.setPromptText("Create a strong master password");
        passwordBox.getChildren().addAll(passwordLabel, regPasswordField);

        // Confirm password field
        VBox confirmBox = new VBox(5);
        Label confirmLabel = new Label("Confirm Password");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your master password");
        confirmBox.getChildren().addAll(confirmLabel, confirmPasswordField);


        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        backButton = new Button("Back");
        backButton.setPrefWidth(100);

        createAccountButton = new Button("Register");
        createAccountButton.setPrefWidth(100);
        createAccountButton.setDefaultButton(true);

        buttonBox.getChildren().addAll(backButton, createAccountButton);

        // Status label
        regStatusLabel = new Label();
        regStatusLabel.setTextFill(Color.RED);
        regStatusLabel.setMaxWidth(Double.MAX_VALUE);
        regStatusLabel.setAlignment(Pos.CENTER);
        regStatusLabel.setMinHeight(20);

        // Add all to form
        registerForm.getChildren().addAll(
                formTitle,
                usernameBox,
                passwordBox,
                confirmBox,
                buttonBox,
                regStatusLabel);
    }

    /**
     * Shows the login form.
     */
    public void showLoginForm() {
        root.setCenter(loginForm);
        currentState = ViewState.LOGIN;

        // Clear fields
        usernameField.clear();
        passwordField.clear();
        statusLabel.setText("");
        progressIndicator.setVisible(false);

        // Reset focus
        usernameField.requestFocus();
    }

    /**
     * Shows the registration form.
     */
    public void showRegisterForm() {
        root.setCenter(registerForm);
        currentState = ViewState.REGISTER;

        // Clear fields
        regUsernameField.clear();
        regPasswordField.clear();
        confirmPasswordField.clear();
        regStatusLabel.setText("");

        // Reset focus
        regUsernameField.requestFocus();
    }

    /**
     * Shows the progress indicator during login.
     *
     * @param show Whether to show the indicator
     */
    public void showProgress(boolean show) {
        progressIndicator.setVisible(show);
        loginForm.setDisable(show);
    }

    /**
     * Sets the status message in the login form.
     *
     * @param message The message to display
     * @param isError Whether this is an error message
     */
    public void setLoginStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }

    /**
     * Sets the status message in the registration form.
     *
     * @param message The message to display
     * @param isError Whether this is an error message
     */
    public void setRegisterStatus(String message, boolean isError) {
        regStatusLabel.setText(message);
        regStatusLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }

    /**
     * Validates the login form.
     *
     * @return true if valid, false otherwise
     */
    public boolean validateLoginForm() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            setLoginStatus("Username cannot be empty", true);
            return false;
        }

        if (password.isEmpty()) {
            setLoginStatus("Password cannot be empty", true);
            return false;
        }

        return true;
    }

    /**
     * Validates the registration form.
     *
     * @return true if valid, false otherwise
     */
    public boolean validateRegisterForm() {
        String username = regUsernameField.getText().trim();
        String password = regPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty()) {
            setRegisterStatus("Username cannot be empty", true);
            return false;
        }

        if (password.isEmpty()) {
            setRegisterStatus("Password cannot be empty", true);
            return false;
        }

        if (password.length() < 8) {
            setRegisterStatus("Password must be at least 8 characters", true);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            setRegisterStatus("Passwords do not match", true);
            return false;
        }

        return true;
    }

    // Getters for UI components
    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public TextField getRegUsernameField() {
        return regUsernameField;
    }

    public PasswordField getRegPasswordField() {
        return regPasswordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public ViewState getCurrentState() {
        return currentState;
    }
}