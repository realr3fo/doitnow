package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities;

import androidx.appcompat.app.AppCompatActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs.DatabaseClient;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.http_connections.RequestHandler;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.SharedPrefManager;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.http_connections.URLs;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.User;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword;
    RadioGroup radioGroupGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, TaskListActivity.class));
            return;
        }

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroupGender = findViewById(R.id.radioGender);


        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegistrationActivity.this,
                        LoginActivity.class));
            }
        });

    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String gender = ((RadioButton) findViewById(radioGroupGender
                .getCheckedRadioButtonId())).getText().toString();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.ask_username));
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.ask_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.ask_valid_email));
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.ask_password));
            editTextPassword.requestFocus();
            return;
        }

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);

                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
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

                        saveUser(user);

                        SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(user.getEmail());

                        finish();
                        startActivity(new Intent(getApplicationContext(), TaskListActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_registration)
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute();
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

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getApplicationContext(), R.string.saved, Toast.LENGTH_LONG).show();
            }
        }

        SaveUser st = new SaveUser();
        st.execute();
    }

}