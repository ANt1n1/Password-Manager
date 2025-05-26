/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: ABAKBM Team
 * Date: 4/30/25
 * Time: 10:15 PM
 *
 * Project: csci205_final
 * Package: org.ABAKBM.controller
 * Class: PasswordManagerController
 *
 * Description: Controller class for the Password Manager application
 * that connects the model and view classes.
 *
 * ****************************************
 */

package org.ABAKBM.controller;

import org.ABAKBM.AuthenticationManager;
import org.ABAKBM.EncryptionManager;
import org.ABAKBM.PasswordEntry;
import org.ABAKBM.PasswordManager;
import org.ABAKBM.User;
import org.ABAKBM.view.LoginView;
import org.ABAKBM.view.PasswordManagerView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class for the Password Manager application.
 * Connects the model (AuthenticationManager, PasswordManager) with the view (PasswordManagerView).
 */
public class PasswordManagerController {
    // Model components
    private final AuthenticationManager authManager;
    private final PasswordManager passwordManager;

    // View component
    private final PasswordManagerView view;

    // Stage reference for scene switching
    private final Stage primaryStage;

    // Data
    private ObservableList<PasswordEntry> passwordEntries;
    private FilteredList<PasswordEntry> filteredPasswords;

    /**
     * Constructor for PasswordManagerController.
     *
     * @param authManager The authentication manager
     * @param passwordManager The password manager
     * @param primaryStage The primary stage
     */
    public PasswordManagerController(AuthenticationManager authManager, PasswordManager passwordManager, Stage primaryStage) {
        this.authManager = authManager;
        this.passwordManager = passwordManager;
        this.primaryStage = primaryStage;

        // Create the view
        this.view = new PasswordManagerView();

        // Initialize the controller
        initialize();
    }

    /**
     * Initializes the controller by setting up event handlers and loading data.
     */
    private void initialize() {
        // Set up event handlers
        setupEventHandlers();

        // Load data
        loadData();

        // Update the view
        updateView();
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        // Add password button
        view.getAddButton().setOnAction(e -> handleAddPassword());

        // Edit password button
        view.getEditButton().setOnAction(e -> handleEditPassword());

        // Delete password button
        view.getDeleteButton().setOnAction(e -> handleDeletePassword());

        // Generate password button
        view.getGenerateButton().setOnAction(e -> view.showPasswordGenerator());

        // Logout button
        view.getLogoutButton().setOnAction(e -> handleLogout());

        // Search field
        view.getSearchField().textProperty().addListener((obs, oldVal, newVal) -> {
            filterPasswords();
        });

        // Category filter
        view.getCategoryFilter().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filterPasswords();
        });

        // Table selection changed - update password
        view.getPasswordTable().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String decryptedPassword = passwordManager.getPassword(newVal.getWebsiteOrApp());
                view.setDecryptedPassword(decryptedPassword);
            }
        });
    }

    /**
     * Loads password data from the currently logged-in user.
     */
    private void loadData() {
        // Check if a user is logged in
        if (!authManager.isLoggedIn()) {
            showError("No user is logged in.");
            handleLogout();
            return;
        }

        // Get the current user
        User user = authManager.getLoggedInUser();

        // Get all passwords
        List<PasswordEntry> entries = user.getAllPasswords();

        // Create observable lists
        passwordEntries = FXCollections.observableArrayList(entries);
        filteredPasswords = new FilteredList<>(passwordEntries);

        // Update categories
        updateCategories();
    }

    /**
     * Updates the categories list in the filter dropdown.
     */
    private void updateCategories() {
        if (authManager.isLoggedIn()) {
            User user = authManager.getLoggedInUser();
            List<String> categories = user.getAllCategories();
            view.updateCategoryFilter(categories);
        }
    }

    /**
     * Updates the view with current data.
     */
    private void updateView() {
        // Set username in view
        if (authManager.isLoggedIn()) {
            view.setUsername(authManager.getLoggedInUser().getUsername());
        }

        // Set table data
        view.getPasswordTable().setItems(filteredPasswords);
    }

    /**
     * Filters passwords based on search text and category.
     */
    private void filterPasswords() {
        String searchText = view.getSearchField().getText().toLowerCase();
        String selectedCategory = view.getCategoryFilter().getValue();

        filteredPasswords.setPredicate(entry -> {
            // Always apply search filter
            boolean matchesSearch = searchText.isEmpty() ||
                    entry.getWebsiteOrApp().toLowerCase().contains(searchText) ||
                    entry.getUsername().toLowerCase().contains(searchText) ||
                    (entry.getNotes() != null && entry.getNotes().toLowerCase().contains(searchText));

            // Apply category filter if not "All Categories"
            boolean matchesCategory = "All Categories".equals(selectedCategory) ||
                    entry.getCategory().equals(selectedCategory);

            return matchesSearch && matchesCategory;
        });
    }

    /**
     * Handles the add password button click.
     */
    private void handleAddPassword() {
        // Get all categories for the dialog
        User user = authManager.getLoggedInUser();
        List<String> categories = user.getAllCategories();

        // Show the dialog
        PasswordEntry entryForm = view.showPasswordDialog(null, categories);

        if (entryForm != null && entryForm instanceof PasswordManagerView.DialogPasswordEntry) {
            try {
                // Get the plain password
                String plainPassword = ((PasswordManagerView.DialogPasswordEntry) entryForm).getPlainPassword();

                // Encrypt the password
                String encryptedPassword = passwordManager.encryptPassword(plainPassword);
                PasswordEntry entry = new PasswordEntry(
                        entryForm.getWebsiteOrApp(),
                        entryForm.getUsername(),
                        encryptedPassword,
                        entryForm.getUrl(),
                        entryForm.getNotes(),
                        entryForm.getCategory()
                );

                // Check if entry already exists
                if (passwordManager.hasPassword(entry.getWebsiteOrApp())) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Password Already Exists");
                    alert.setHeaderText("A password for " + entry.getWebsiteOrApp() + " already exists");
                    alert.setContentText("Do you want to overwrite it?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() != ButtonType.OK) {
                        return;
                    }
                }

                // Add the password
                user.storePasswordEntry(entry);
                authManager.saveUserPasswordEntries();

                // Add to observable list
                passwordEntries.add(entry);

                // Update categories if needed
                if (!categories.contains(entry.getCategory())) {
                    updateCategories();
                }

                // Show success message
                view.showStatus("Password added successfully", false);

            } catch (Exception e) {
                showError("Error adding password: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the edit password button click.
     */
    private void handleEditPassword() {
        PasswordEntry selectedEntry = view.getSelectedEntry();
        if (selectedEntry == null) {
            return;
        }

        // Get all categories for the dialog
        User user = authManager.getLoggedInUser();
        List<String> categories = user.getAllCategories();

        // Get the current decrypted password
        String currentPassword = passwordManager.getPassword(selectedEntry.getWebsiteOrApp());

        // Show the dialog with a temporary entry that has the decrypted password
        PasswordEntry entryForm = view.showPasswordDialog(selectedEntry, categories);

        if (entryForm != null && entryForm instanceof PasswordManagerView.DialogPasswordEntry) {
            try {
                // Get the plain password
                String plainPassword = ((PasswordManagerView.DialogPasswordEntry) entryForm).getPlainPassword();

                // Update details
                selectedEntry.updateDetails(
                        entryForm.getWebsiteOrApp(),
                        entryForm.getUsername(),
                        entryForm.getUrl(),
                        entryForm.getNotes()
                );

                // Update category
                selectedEntry.setCategory(entryForm.getCategory());

                // Update password if changed
                if (!plainPassword.equals(currentPassword)) {
                    String encryptedPassword = passwordManager.encryptPassword(plainPassword);
                    selectedEntry.updatePassword(encryptedPassword);
                }

                // Save changes
                authManager.saveUserPasswordEntries();

                // Refresh the table
                view.getPasswordTable().refresh();

                // Update categories if needed
                if (!categories.contains(entryForm.getCategory())) {
                    updateCategories();
                }

                // Show success message
                view.showStatus("Password updated successfully", false);

            } catch (Exception e) {
                showError("Error updating password: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the delete password button click.
     */
    private void handleDeletePassword() {
        PasswordEntry selectedEntry = view.getSelectedEntry();
        if (selectedEntry == null) {
            return;
        }

        // Confirm deletion
        boolean confirmed = view.showDeleteConfirmation(selectedEntry);

        if (confirmed) {
            try {
                // Get the current user
                User user = authManager.getLoggedInUser();

                // Remove the entry
                user.removePasswordEntry(selectedEntry);

                // Save changes
                authManager.saveUserPasswordEntries();

                // Remove from observable list
                passwordEntries.remove(selectedEntry);

                // Show success message
                view.showStatus("Password deleted successfully", false);

            } catch (Exception e) {
                showError("Error deleting password: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the logout button click.
     */
    private void handleLogout() {
        // Logout the user
        authManager.logout();

        // Go back to login screen
        showLoginScreen();
    }

    /**
     * Shows the login screen.
     */
    private void showLoginScreen() {
        try {
            // Create login view
            LoginView loginView = new LoginView();

            // Create login controller
            LoginController loginController = new LoginController(
                    authManager, passwordManager, loginView, primaryStage);

            // Set the scene
            Scene scene = new Scene(loginView.getRoot(), 400, 500);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Password Manager - Login");
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            showError("Error showing login screen: " + e.getMessage());
        }
    }

    /**
     * Shows an error message.
     *
     * @param message The error message
     */
    private void showError(String message) {
        view.showStatus(message, true);
    }

    /**
     * Gets the view component.
     *
     * @return The password manager view
     */
    public PasswordManagerView getView() {
        return view;
    }
}