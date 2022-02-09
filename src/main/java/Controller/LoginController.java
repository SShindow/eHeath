package Controller;

import Connection.DBControl;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controller class connected to login.fxml to log in for an existing account
 * @author  Hai Cao Xuan, Hoang Dinh Minh
 */
public class LoginController implements Initializable {

    @FXML
    private Button button_adminLogin;
    @FXML
    private Button button_login;
    @FXML
    private Button button_register;
    @FXML
    private Button button_cancel;
    @FXML
    private Button button_forgotPassword;

    @FXML
    private Label label_loginmessage;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField field_password;

    @FXML
    private ImageView image_background;

    /**
     * Default set for logged in username
     */
    //Used for After-Login (e.g: EditProfile) page(s)
    public static String loggedInUsername = "";

    /**
     * Method which makes the background image displayable
     * @author Hai Cao Xuan
     * @param url indicates url image
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
    }

    /**
     * Method that assigns to the login button in fxml file
     * @author Hai Cao Xuan
     * @param event when clicking login button
     * @throws IOException when encounter an I/O exception to some sort has occurred
     * @throws SQLException that provides information on a database access error or other errors
     * @throws NoSuchAlgorithmException detect other underlying exceptions
     * @throws InvalidKeySpecException when an invalid key specification is encountered
     */
    public void loginButtonOnAction(ActionEvent event) throws IOException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
//       if the username is not blank, go to the log in function to confirm account exist
        if (tf_username.getText().isBlank() == false && field_password.getText().isBlank() == false && validateLogin()) {
;
            //Switch to log in scene
            Parent root = FXMLLoader.load(getClass().getResource("after_login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            label_loginmessage.setText("Invalid credentials! Please try again.");
        }
    }

    /**
     * Method that close the application on click
     * @author Hai Cao Xuan
     * @param event when clicking the close button
     */
    public void cancelButtonOnAction(ActionEvent event) {
//        Close the application
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Method that switch to register scene on click
     * @author Hai Cao Xuan
     * @param event when clicking register text
     * @throws Exception when encounter some unexpected error
     */
    public void registerButtonOnAction(ActionEvent event) throws Exception {
//        Switch to register stage
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method that switch to recover password scene on click
     * @author Hoang Dinh Minh
     * @param event after clicking forgot password
     * @throws IOException when encouter an I/O exception to some sort has occurred
     */
    @FXML
    public void forgotPasswordButtonOnAction(ActionEvent event) throws IOException {
        //Switch to log in scene
        Parent root = FXMLLoader.load(getClass().getResource("forgot_password.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to validate for login to the application
     * @author Hoang Dinh Minh
     * @return boolean value
     * @throws SQLException that provides information on a database access error or other errors
     * @throws NoSuchAlgorithmException detect other underlying exceptions
     * @throws InvalidKeySpecException when an invalid key specification is encountered
     * @throws IOException when encounter an I/O exception to some sort has occurred
     */
    public boolean validateLogin() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        // Get Input from user
        String username = tf_username.getText();
        String password = field_password.getText();

        // Get salt and calculate hashedPassword to compare in Database. If match Login is successful.
        byte[] salt = getSaltFromDBUsername(username);
        byte[] hashedPassword;
        if(salt.length == 0)
        {
            label_loginmessage.setText("Invalid credentials! Please try again.");
            return false;
        }
        else
        hashedPassword = generateHashedPassword(password, salt);


        if (isHashedPasswordCorrect(username, hashedPassword) == true) {
            label_loginmessage.setText("Login Successfully!");
            loggedInUsername = username;
        return true;
        } else {
            System.out.println("Wrong password");
            label_loginmessage.setText("Invalid credentials! Please try again.");
            return false;
        }
    }


    /**
     * Method to switch to admin login scene on click
     * @author Hoang Dinh Minh
      * @param event when clicking admin login text
     * @throws IOException when encounter an I/O exception to some sort has occurred
     */
    @FXML
    public void adminLoginButtonOnAction(ActionEvent event) throws IOException
    {
        //Switch to admin log in scene
        Parent root = FXMLLoader.load(getClass().getResource("admin_login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    byte[] getSaltFromDBUsername(String DBUsername) throws SQLException {
        //Connection connection = getConnection();
        String query = "SELECT salt FROM user WHERE username= ?";
        PreparedStatement pst = DBControl.dbConnection.prepareStatement(query);
        pst.setString(1, DBUsername);
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
            System.out.println("No salt data of username:" + DBUsername);
            byte[] emptyByte = {};
            return emptyByte;
        }
        byte[] importedSalt = rs.getBytes(1);
        return importedSalt;
    }

    byte[] generateHashedPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashedPassword = factory.generateSecret(spec).getEncoded();
        return hashedPassword;
    }

    boolean isHashedPasswordCorrect(String DBUsername, byte[] hashedPassword) throws SQLException {
        //Connection connection = getConnection();
        String query = "SELECT hashedPassword FROM user WHERE username= ?";
        PreparedStatement pst = DBControl.dbConnection.prepareStatement(query);
        pst.setString(1, DBUsername);
        ResultSet rs = pst.executeQuery();
        rs.next();
        byte[] importedHashedPassword = rs.getBytes(1);
        return Arrays.equals(hashedPassword, importedHashedPassword);
    }

}