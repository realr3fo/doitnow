package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {

    private int id;
    private String username, email, gender, token;

    public User(int id, String username, String email, String gender, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getToken() {
        return token;
    }
}