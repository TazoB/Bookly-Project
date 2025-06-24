package Model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int age;
    private byte[] profilePic;
    private String question;
    private String answer;
    private Timestamp dateCreated;

    public User(int id, String username, String password, String firstName, String lastName, int age, byte[] profilePic, String question, String answer, Timestamp dateCreated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.profilePic = profilePic;
        this.question = question;
        this.answer = answer;
        this.dateCreated = dateCreated;
    }

    public User(String username, String password, String firstName, String lastName, int age, byte[] profilePic, String question, String answer, Timestamp dateCreated) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.profilePic = profilePic;
        this.question = question;
        this.answer = answer;
        this.dateCreated = dateCreated;
    }

    public User(String username, String password, String firstName, String lastName, int age, byte[] profilePic, String question, String answer) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.profilePic = profilePic;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
