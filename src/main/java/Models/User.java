package Models;

import java.sql.Date;
import java.time.LocalDate;


/**
 * This class provides model for a user
 * @author  Hoang Dinh Minh
 */

public class User {
    public enum InsuranceType {
        Public,
        Private
    }

    public enum Gender {
        M,
        F,
        O
    }
    String accountID;
    String username;
    byte[] hashedPassword;
    byte[] salt;
    String email;
    String firstName;
    String lastName;
    String address;
    String insuranceID;
    String insuranceType;
    String gender;
    java.sql.Date dateOfBirth;

    /**
     * Constructor for Admin View
     * @author Hoang Dinh Minh
     * @param username
     * @param email
     * @param firstName
     * @param lastName
     * @param address
     * @param insuranceID
     * @param insuranceType
     * @param gender
     * @param dateOfBirth
     */
    public User (String username, String email, String firstName, String lastName, String address, String insuranceID, String insuranceType, String gender, java.sql.Date dateOfBirth)
    {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.insuranceID = insuranceID;
        this.insuranceType = insuranceType;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
    //Getter
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getAddress() {return address;}
    public String getInsuranceID() {return insuranceID;}
    public String getInsuranceType() {return insuranceType;}
    public String getGender() {return gender;}
    public Date getDateOfBirth() {return dateOfBirth;}

    //Setter
    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setAddress(String address) {this.address = address;}
    public void setInsuranceID(String insuranceID) {this.insuranceID = insuranceID;}
    public void setInsuranceType(String insuranceType) {this.insuranceType = insuranceType;}
    public void setGender(String gender) {this.gender = gender;}
    public void setDateOfBirth(Date dateOfBirth) {this.dateOfBirth = dateOfBirth;}
}
