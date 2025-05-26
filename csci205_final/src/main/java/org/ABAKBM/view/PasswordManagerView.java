/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim
 * Date: 4/29/25
 * Time: 10:39 PM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: PasswordManagerView
 *
 * Description: View class for the Password Manager application UI
 *
 * ****************************************
 */

package org.ABAKBM.view;

import org.ABAKBM.PasswordEntry;
import org.ABAKBM.PasswordGenerator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * PasswordManagerView class is responsible for creating the GUI components
 * for the password manager application.
 */
public class PasswordManagerView {
    // Main layout container
    private BorderPane root;

    // Toolbar components
    private ToolBar toolBar;
    private Label usernameLabel;
    private Button logoutButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button generateButton;

    // Password table
    private TableView<PasswordEntry> passwordTable;

    // Search and filter
    private HBox searchBox;
    private TextField searchField;
    private ComboBox<String> categoryFilter;

    // Details panel
    private VBox detailsPanel;
    private Label detailsTitle;
    private Label websiteLabel;
    private Label usernameValueLabel;
    private PasswordField passwordField;
    private TextField passwordVisible;
    private ToggleButton showPasswordButton;
    private Button copyPasswordButton;
    private Button copyUsernameButton;
    private Label categoryLabel;
    private Label urlLabel;
    private TextArea notesArea;
    private Label createdLabel;
    private Label modifiedLabel;

    // Status
    private Label statusLabel;

    // Currently selected entry
    private PasswordEntry selectedEntry;

    /**
     * Constructor for PasswordManagerView.
     * Initializes the GUI components and sets up the layout.
     */
    public PasswordManagerView() {
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
        root.setPadding(new Insets(10));
        root.getStyleClass().add("password-manager");

        setupToolbar();
        setupPasswordTable();
        setupSearchAndFilter();
        setupDetailsPanel();
        setupStatusBar();

        // Apply some styling
        applyStyles();
    }

    /**
     * Sets up the toolbar with action buttons.
     */
    private void setupToolbar() {
        toolBar = new ToolBar();

        // User label
        usernameLabel = new Label();
        usernameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        // Add spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Create action buttons
        addButton = new Button("Add Password");
        editButton = new Button("Edit");
        editButton.setDisable(true);
        deleteButton = new Button("Delete");
        deleteButton.setDisable(true);
        generateButton = new Button("Generate Password");
        logoutButton = new Button("Logout");

        // Add all components to toolbar
        toolBar.getItems().addAll(
                usernameLabel, spacer,
                addButton, new Separator(),
                editButton, deleteButton, new Separator(),
                generateButton, new Separator(),
                logoutButton);

        root.setTop(toolBar);
    }

    /**
     * Sets up the password table that displays all password entries.
     */
    private void setupPasswordTable() {
        passwordTable = new TableView<>();

        // Create columns
        TableColumn<PasswordEntry, String> websiteColumn = new TableColumn<>("Website/App");
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("websiteOrApp"));
        websiteColumn.setPrefWidth(180);

        TableColumn<PasswordEntry, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setPrefWidth(180);

        TableColumn<PasswordEntry, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setPrefWidth(120);

        TableColumn<PasswordEntry, Date> modifiedColumn = new TableColumn<>("Last Modified");
        modifiedColumn.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
        modifiedColumn.setPrefWidth(150);
        modifiedColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a").format(item));
                }
            }
        });

        // Add columns to table
        passwordTable.getColumns().addAll(websiteColumn, usernameColumn, categoryColumn, modifiedColumn);

        // Set selection listener
        passwordTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedEntry = newSelection;
            updateDetailsPanel();
            updateButtonStates();
        });

        // Set placeholder message
        passwordTable.setPlaceholder(new Label("No passwords stored. Click 'Add Password' to create one."));

        // Set resize policy
        passwordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add table to the root layout
        root.setCenter(passwordTable);
    }

    /**
     * Sets up the search and filter bar.
     */
    private void setupSearchAndFilter() {
        searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10, 0, 10, 0));
        searchBox.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Search:");
        searchField = new TextField();
        searchField.setPromptText("Search passwords...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Label categoryLabel = new Label("Category:");
        categoryFilter = new ComboBox<>();
        categoryFilter.setPromptText("All Categories");
        categoryFilter.setPrefWidth(150);

        searchBox.getChildren().addAll(searchLabel, searchField, categoryLabel, categoryFilter);

        // Add search box below toolbar
        VBox topSection = new VBox();
        topSection.getChildren().addAll(toolBar, searchBox);
        root.setTop(topSection);
    }

    /**
     * Sets up the details panel that shows information about the selected password.
     */
    private void setupDetailsPanel() {
        detailsPanel = new VBox(10);
        detailsPanel.setPadding(new Insets(10));
        detailsPanel.setPrefWidth(300);
        detailsPanel.getStyleClass().add("details-panel");

        // Title
        detailsTitle = new Label("Password Details");
        detailsTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Fields
        GridPane fields = new GridPane();
        fields.setHgap(10);
        fields.setVgap(10);
        fields.setPadding(new Insets(10, 0, 10, 0));

        int row = 0;

        // Website/App
        fields.add(new Label("Website/App:"), 0, row);
        websiteLabel = new Label();
        websiteLabel.setWrapText(true);
        fields.add(websiteLabel, 1, row++);

        // Username
        fields.add(new Label("Username:"), 0, row);
        HBox usernameBox = new HBox(5);
        usernameValueLabel = new Label();
        copyUsernameButton = new Button("Copy");
        copyUsernameButton.setOnAction(e -> {
            if (selectedEntry != null) {
                copyToClipboard(selectedEntry.getUsername());
                showStatus("Username copied to clipboard", false);
            }
        });
        usernameBox.getChildren().addAll(usernameValueLabel, copyUsernameButton);
        fields.add(usernameBox, 1, row++);

        // Password
        fields.add(new Label("Password:"), 0, row);
        VBox passwordBox = new VBox(5);

        HBox passwordFieldBox = new HBox();
        passwordField = new PasswordField();
        passwordField.setEditable(false);
        passwordVisible = new TextField();
        passwordVisible.setEditable(false);
        passwordVisible.setManaged(false);
        passwordVisible.setVisible(false);
        passwordFieldBox.getChildren().addAll(passwordField, passwordVisible);
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        HBox.setHgrow(passwordVisible, Priority.ALWAYS);

        HBox passwordButtonsBox = new HBox(5);
        showPasswordButton = new ToggleButton("Show");
        showPasswordButton.setOnAction(e -> {
            if (showPasswordButton.isSelected()) {
                passwordField.setManaged(false);
                passwordField.setVisible(false);
                passwordVisible.setManaged(true);
                passwordVisible.setVisible(true);
                showPasswordButton.setText("Hide");
            } else {
                passwordField.setManaged(true);
                passwordField.setVisible(true);
                passwordVisible.setManaged(false);
                passwordVisible.setVisible(false);
                showPasswordButton.setText("Show");
            }
        });

        copyPasswordButton = new Button("Copy");
        copyPasswordButton.setOnAction(e -> {
            if (selectedEntry != null) {
                copyToClipboard(passwordField.getText());
                showStatus("Password copied to clipboard", false);
            }
        });

        passwordButtonsBox.getChildren().addAll(showPasswordButton, copyPasswordButton);
        passwordBox.getChildren().addAll(passwordFieldBox, passwordButtonsBox);
        fields.add(passwordBox, 1, row++);

        // URL
        fields.add(new Label("URL:"), 0, row);
        urlLabel = new Label();
        urlLabel.setWrapText(true);
        fields.add(urlLabel, 1, row++);

        // Category
        fields.add(new Label("Category:"), 0, row);
        categoryLabel = new Label();
        fields.add(categoryLabel, 1, row++);

        // Notes
        fields.add(new Label("Notes:"), 0, row);
        notesArea = new TextArea();
        notesArea.setEditable(false);
        notesArea.setWrapText(true);
        notesArea.setPrefRowCount(5);
        fields.add(notesArea, 1, row++);

        // Created date
        fields.add(new Label("Created:"), 0, row);
        createdLabel = new Label();
        fields.add(createdLabel, 1, row++);

        // Modified date
        fields.add(new Label("Modified:"), 0, row);
        modifiedLabel = new Label();
        fields.add(modifiedLabel, 1, row++);

        // Add to details panel
        detailsPanel.getChildren().addAll(detailsTitle, new Separator(), fields);

        // Set column constraints for the grid
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(100);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        fields.getColumnConstraints().addAll(col1, col2);

        // Add to root
        root.setRight(detailsPanel);

        // Initially disable details panel
        clearDetailsPanel();
    }

    /**
     * Sets up the status bar.
     */
    private void setupStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5));
        statusBar.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label();
        statusBar.getChildren().add(statusLabel);

        root.setBottom(statusBar);
    }

    /**
     * Updates the button states based on selection.
     */
    private void updateButtonStates() {
        boolean hasSelection = selectedEntry != null;
        editButton.setDisable(!hasSelection);
        deleteButton.setDisable(!hasSelection);
    }

    /**
     * Updates the details panel with the currently selected entry.
     */
    private void updateDetailsPanel() {
        if (selectedEntry == null) {
            clearDetailsPanel();
            return;
        }

        // Enable details panel
        detailsPanel.setDisable(false);

        // Update fields
        websiteLabel.setText(selectedEntry.getWebsiteOrApp());
        usernameValueLabel.setText(selectedEntry.getUsername());
        urlLabel.setText(selectedEntry.getUrl() != null ? selectedEntry.getUrl() : "");
        categoryLabel.setText(selectedEntry.getCategory());
        notesArea.setText(selectedEntry.getNotes() != null ? selectedEntry.getNotes() : "");

        // Format dates
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
        createdLabel.setText(dateFormat.format(selectedEntry.getCreationDate()));
        modifiedLabel.setText(dateFormat.format(selectedEntry.getModificationDate()));
    }

    /**
     * Clears the details panel.
     */
    private void clearDetailsPanel() {
        detailsPanel.setDisable(true);

        websiteLabel.setText("");
        usernameValueLabel.setText("");
        passwordField.setText("");
        passwordVisible.setText("");
        urlLabel.setText("");
        categoryLabel.setText("");
        notesArea.setText("");
        createdLabel.setText("");
        modifiedLabel.setText("");
    }

    /**
     * Populates the table with password entries.
     *
     * @param entries List of password entries to display
     */
    public void populateTable(List<PasswordEntry> entries) {
        passwordTable.getItems().clear();
        if (entries != null && !entries.isEmpty()) {
            passwordTable.getItems().addAll(entries);
        }
    }

    /**
     * Updates the categories dropdown.
     *
     * @param categories List of categories to display
     */
    public void updateCategoryFilter(List<String> categories) {
        categoryFilter.getItems().clear();
        categoryFilter.getItems().add("All Categories");
        if (categories != null && !categories.isEmpty()) {
            categoryFilter.getItems().addAll(categories);
        }
        categoryFilter.getSelectionModel().selectFirst();
    }

    /**
     * Sets the username displayed in the toolbar.
     *
     * @param username The current user's username
     */
    public void setUsername(String username) {
        usernameLabel.setText("Logged in as: " + username);
    }

    /**
     * Sets the decrypted password in the password field.
     *
     * @param decryptedPassword The decrypted password
     */
    public void setDecryptedPassword(String decryptedPassword) {
        passwordField.setText(decryptedPassword);
        passwordVisible.setText(decryptedPassword);
    }

    /**
     * Shows a dialog to add or edit a password entry.
     *
     * @param existingEntry The entry to edit, or null for a new entry
     * @param categories Available categories
     * @return The created or edited entry, or null if cancelled
     */
    public PasswordEntry showPasswordDialog(PasswordEntry existingEntry, List<String> categories) {
        Dialog<PasswordEntry> dialog = new Dialog<>();
        dialog.setTitle(existingEntry == null ? "Add Password" : "Edit Password");
        dialog.setHeaderText(existingEntry == null ? "Add a new password" : "Edit password details");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField websiteField = new TextField();
        websiteField.setPromptText("Website or App");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username or Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField urlField = new TextField();
        urlField.setPromptText("URL (optional)");
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(categories);
        categoryCombo.setPromptText("Category");
        categoryCombo.setEditable(true);
        TextArea notesField = new TextArea();
        notesField.setPromptText("Notes (optional)");
        notesField.setPrefRowCount(3);

        // Setup password visibility toggle
        ToggleButton showPasswordButton = new ToggleButton("Show");
        TextField passwordVisible = new TextField();
        passwordVisible.setPromptText("Password");
        passwordVisible.setManaged(false);
        passwordVisible.setVisible(false);

        showPasswordButton.setOnAction(e -> {
            if (showPasswordButton.isSelected()) {
                passwordVisible.setText(passwordField.getText());
                passwordField.setManaged(false);
                passwordField.setVisible(false);
                passwordVisible.setManaged(true);
                passwordVisible.setVisible(true);
                showPasswordButton.setText("Hide");
            } else {
                passwordField.setText(passwordVisible.getText());
                passwordField.setManaged(true);
                passwordField.setVisible(true);
                passwordVisible.setManaged(false);
                passwordVisible.setVisible(false);
                showPasswordButton.setText("Show");
            }
        });

        // Password fields sync
        passwordField.textProperty().addListener((obs, old, newVal) ->
                passwordVisible.setText(newVal));
        passwordVisible.textProperty().addListener((obs, old, newVal) ->
                passwordField.setText(newVal));

        // Password generator button
        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            String generated = PasswordGenerator.generatePassword();
            passwordField.setText(generated);
            passwordVisible.setText(generated);
        });

        // Add fields to the grid
        int row = 0;
        grid.add(new Label("Website/App:*"), 0, row);
        grid.add(websiteField, 1, row, 2, 1);
        row++;

        grid.add(new Label("Username:*"), 0, row);
        grid.add(usernameField, 1, row, 2, 1);
        row++;

        grid.add(new Label("Password:*"), 0, row);
        grid.add(passwordField, 1, row);
        grid.add(passwordVisible, 1, row);

        HBox passwordButtonBox = new HBox(5);
        passwordButtonBox.getChildren().addAll(showPasswordButton, generateButton);
        grid.add(passwordButtonBox, 2, row);
        row++;

        grid.add(new Label("URL:"), 0, row);
        grid.add(urlField, 1, row, 2, 1);
        row++;

        grid.add(new Label("Category:"), 0, row);
        grid.add(categoryCombo, 1, row, 2, 1);
        row++;

        grid.add(new Label("Notes:"), 0, row);
        grid.add(notesField, 1, row, 2, 1);
        row++;

        grid.add(new Label("* Required fields"), 0, row, 3, 1);

        // Fill in the fields if editing
        if (existingEntry != null) {
            websiteField.setText(existingEntry.getWebsiteOrApp());
            usernameField.setText(existingEntry.getUsername());
            urlField.setText(existingEntry.getUrl() != null ? existingEntry.getUrl() : "");
            categoryCombo.setValue(existingEntry.getCategory());
            notesField.setText(existingEntry.getNotes() != null ? existingEntry.getNotes() : "");

            // Disable website field for editing (primary key)
            websiteField.setDisable(true);
        } else {
            // Default category
            categoryCombo.setValue("Default");
        }

        // Set focus on first field
        websiteField.requestFocus();

        dialog.getDialogPane().setContent(grid);

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Validate required fields
                if (websiteField.getText().trim().isEmpty() ||
                        usernameField.getText().trim().isEmpty() ||
                        passwordField.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setHeaderText("Required Fields Missing");
                    alert.setContentText("Website/App, Username, and Password are required fields.");
                    alert.showAndWait();
                    return null;
                }

                String website = websiteField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText();
                String url = urlField.getText().trim();
                String category = categoryCombo.getValue();
                String notes = notesField.getText();

                if (category == null || category.isEmpty()) {
                    category = "Default";
                }

                return new DialogPasswordEntry(website, username, password, url, notes, category);
            }
            return null;
        });

        Optional<PasswordEntry> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Shows a password generator dialog.
     */
    public void showPasswordGenerator() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Generator");
        alert.setHeaderText("Generate a Secure Password");

        // Generate initial password
        String initial = PasswordGenerator.generatePassword();

        // Masked field
        PasswordField passwordField = new PasswordField();
        passwordField.setText(initial);
        passwordField.setEditable(false);
        passwordField.setPrefWidth(250);

        // Visible field
        TextField passwordVisible = new TextField();
        passwordVisible.setText(initial);
        passwordVisible.setEditable(false);
        passwordVisible.setPrefWidth(250);
        passwordVisible.setVisible(false);

        // StackPane to overlap them
        StackPane passwordPane = new StackPane(passwordField, passwordVisible);
        passwordPane.setPrefWidth(250);

        // Toggle button
        ToggleButton showButton = new ToggleButton("Show Password");
        passwordField.visibleProperty().bind(showButton.selectedProperty().not());
        passwordVisible.visibleProperty().bind(showButton.selectedProperty());
        showButton.selectedProperty().addListener((obs, oldV, newV) ->
                showButton.setText(newV ? "Hide Password" : "Show Password")
        );

        Button copyButton = new Button("Copy to Clipboard");
        copyButton.setOnAction(e -> {
            String current = passwordVisible.isVisible()
                    ? passwordVisible.getText()
                    : passwordField.getText();
            copyToClipboard(current);

            copyButton.setText("Copied!");
            copyButton.setDisable(true);

            // create the transition
            javafx.animation.PauseTransition pause =
                    new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
            // configure its end‑of‑pause behavior
            pause.setOnFinished(evt -> {
                copyButton.setText("Copy to Clipboard");
                copyButton.setDisable(false);
            });
            // now play it
            pause.play();
        });

        // Regenerate button
        Button regenerate = new Button("Generate Another");
        regenerate.setOnAction(e -> {
            String next = PasswordGenerator.generatePassword();
            passwordField.setText(next);
            passwordVisible.setText(next);
            copyButton.setText("Copy to Clipboard");
            copyButton.setDisable(false);
        });

        HBox controls = new HBox(10, showButton, copyButton, regenerate);
        VBox content = new VBox(10,
                new Label("Here's a secure password you can use:"),
                passwordPane,
                controls
        );
        content.setPadding(new Insets(10));

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);

        alert.showAndWait();
    }


    /**
     * Shows a confirmation dialog for deletion.
     *
     * @param entry The entry to delete
     * @return true if confirmed, false otherwise
     */
    public boolean showDeleteConfirmation(PasswordEntry entry) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Password");
        alert.setHeaderText("Delete Password Entry");
        alert.setContentText("Are you sure you want to delete the password for " +
                entry.getWebsiteOrApp() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Shows a status message.
     *
     * @param message The message to display
     * @param isError Whether this is an error message
     */
    public void showStatus(String message, boolean isError) {
        statusLabel.setText(message);

        if (isError) {
            statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }

        // Clear status after a delay
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(3));
        pause.setOnFinished(e -> {
            statusLabel.setText("");
            statusLabel.setStyle("");
        });
        pause.play();
    }

    /**
     * Copies text to the clipboard.
     *
     * @param text The text to copy
     */
    private void copyToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }

    /**
     * Applies CSS styles to the components.
     */
    private void applyStyles() {
        // Apply styles here if needed
    }

    // Getters for the UI components
    public Button getLogoutButton() {
        return logoutButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getGenerateButton() {
        return generateButton;
    }

    public TableView<PasswordEntry> getPasswordTable() {
        return passwordTable;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public ComboBox<String> getCategoryFilter() {
        return categoryFilter;
    }

    public PasswordEntry getSelectedEntry() {
        return selectedEntry;
    }

    /**
     * Static helper class for dialog password entry to avoid inner class issues.
     */
    public static class DialogPasswordEntry extends PasswordEntry {
        private final String plainPassword;

        public DialogPasswordEntry(String websiteOrApp, String username, String plainPassword,
                                   String url, String notes, String category) {
            super(websiteOrApp, username, "", url, notes, category);
            this.plainPassword = plainPassword;
        }

        public String getPlainPassword() {
            return plainPassword;
        }
    }
}