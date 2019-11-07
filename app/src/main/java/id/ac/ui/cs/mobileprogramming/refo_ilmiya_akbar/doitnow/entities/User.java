package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
@Entity
public class User {

    @PrimaryKey
    private int id;

    @ColumnInfo(name="username")
    private String username;

    @ColumnInfo(name="email")
    private String email;

    @ColumnInfo(name="gender")
    private String gender;

    @ColumnInfo(name="token")
    private String token;

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