/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim
 * Date: 4/29/25
 * Time: 11:05 PM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: PasswordManagerApp
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import org.ABAKBM.controller.LoginController;
import org.ABAKBM.view.LoginView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main application class for the Password Manager.
 * Initializes the encryption key, managers, and shows the login screen.
 */
public class PasswordManagerApp extends Application {

    private static final String STORAGE_DIR = System.getProperty("user.home") +
            File.separator + ".passwordmanager";
    private static final String KEY_FILENAME = "encryption.key";

    private EncryptionManager encryptionManager;
    private AuthenticationManager authManager;
    private PasswordManager passwordManager;

    /**
     * Main method to launch the application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize managers
            initializeManagers();

            // Create login view
            LoginView loginView = new LoginView();

            // Create login controller
            LoginController loginController = new LoginController(
                    authManager, passwordManager, loginView, primaryStage);

            // Set up the scene
            Scene scene = new Scene(loginView.getRoot(), 400, 500);

            // Apply any CSS if needed
            // scene.getStylesheets().add(getClass().getResource("/org/ABAKBM/css/styles.css").toExternalForm());

            // Set up the stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Password Manager - Login");
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            showErrorAndExit("Failed to start application", e.getMessage());
        }
    }

    @Override
    public void stop() {
        // Save data when application is closing
        if (authManager != null) {
            authManager.saveAllData();
        }
    }

    /**
     * Initializes the encryption manager, authentication manager, and password manager.
     *
     * @throws IOException If an error occurs during initialization
     */
    private void initializeManagers() throws IOException {
        // Create storage directory if it doesn't exist
        Path storagePath = Paths.get(STORAGE_DIR);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        // Initialize encryption manager with existing key or create new one
        Path keyPath = storagePath.resolve(KEY_FILENAME);
        if (Files.exists(keyPath)) {
            // Use existing key
            String encryptionKey = Files.readString(keyPath).trim();
            encryptionManager = new EncryptionManager(encryptionKey);
        } else {
            // Create new key and save it
            encryptionManager = new EncryptionManager();
            String encryptionKey = encryptionManager.getBase64Key();
            Files.writeString(keyPath, encryptionKey);
        }

        // Initialize authentication and password managers
        authManager = new AuthenticationManager(encryptionManager);
        passwordManager = new PasswordManager(authManager, encryptionManager);
    }

    /**
     * Shows an error dialog and exits the application.
     *
     * @param header The error header
     * @param content The detailed error message
     */
    private void showErrorAndExit(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        Platform.exit();
    }
}