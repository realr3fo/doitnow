package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.SharedPrefManager;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs.DatabaseClient;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.User;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.http_connections.RequestHandler;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.http_connections.URLs;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }

    private void userLogin() {
        final String username = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            editTextEmail.setError(getString(R.string.ask_email));
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.ask_password));
            editTextPassword.requestFocus();
            return;
        }

        class UserLogin extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("password", password);

                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    JSONObject obj = new JSONObject(s);
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"),
                                Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("user");

                        User user = new User(
                                userJson.getInt("ID"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("gender"),
                                userJson.getString("token")
                        );

                        checkUserExistence(user);

                        SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(user.getEmail());

                        finish();
                        startActivity(new Intent(getApplicationContext(), TaskListActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.invalid_email_password,
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    private void checkUserExistence(User userLogin) {
        final User currentUser = userLogin;
        class CheckUserExistence extends AsyncTask<Void, Void, List<User>> {
            @Override
            protected List<User> doInBackground(Void... voids) {
                return DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .userDao()
                        .getAll();
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                User user = new User(0, "", "", "", "");
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getEmail().equalsIgnoreCase(currentUser.getEmail())) {
                        user = users.get(i);
                    }
                }
                if (user.getId() == 0) {
                    saveUser(currentUser);
                }
            }

        }
        CheckUserExistence cue = new CheckUserExistence();
        cue.execute();
    }

    private void saveUser(User userData) {
        final User userDataFinal = userData;

        class SaveUser extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                User user = new User(
                        userDataFinal.getId(),
                        userDataFinal.getUsername(),
                        userDataFinal.getEmail(),
                        userDataFinal.getGender(),
                        userDataFinal.getToken()
                );


                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .userDao()
                        .insert(user);
                return null;
            }
        }

        SaveUser st = new SaveUser();
        st.execute();
    }
}