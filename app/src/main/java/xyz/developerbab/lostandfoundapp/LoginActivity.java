package xyz.developerbab.lostandfoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.developerbab.lostandfoundapp.model.User2;
import xyz.developerbab.lostandfoundapp.singleton.RESTApiClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etpassword, etphone;
    private Button btnsigin;
    private TextView txtforgot;
    private String phone, password;
    private TextView tvsignup;

    private ProgressDialog progressDialog;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Busy Authenticating ...");

        tvsignup = findViewById(R.id.tvsignup);
        tvsignup.setOnClickListener(this);

        etpassword = findViewById(R.id.etPasswordl);
        etphone = findViewById(R.id.etPhonel);
        txtforgot = findViewById(R.id.txtforgotpwd);
        txtforgot.setOnClickListener(this);
        btnsigin = findViewById(R.id.btnSignIn);
        btnsigin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtforgotpwd:
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage("Forgot password is still under development");
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginActivity.this, "okay murakoze kubyumva", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
            case R.id.tvsignup:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.btnSignIn:
                phone = etphone.getText().toString().trim();
                password = etpassword.getText().toString().trim();

                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();

                    Snackbar.make(v, "Please enter all fields", Snackbar.LENGTH_LONG)
                            .setAction("Fail ", null).show();
                } else {
                    calllogineapi();
                }

                break;


        }
    }


    // create API
    public void calllogineapi() {

        progressDialog.show();

        // create user body object
        User2 obj = new User2();
        obj.setEmail(phone);
        obj.setPassword(password);

        Map<String, String> param = new HashMap<>();
        param.put("email", phone);
        param.put("password", password);

        signin(param);
    }

    private void signin(final Map<String, String> user2) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().loginUser(user2);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response
                if (Integer.toString(response.code()).equals("200")) {


                    try {
                        String jsondata = response.body().string();

                        if (jsondata != null) {
                            JSONObject reader = new JSONObject(jsondata);

                            JSONObject data = reader.getJSONObject("user");
                            String id = data.getString("id");
                            String names = data.getString("name");
                            String fone = data.getString("telephone");
                            String emai = data.getString("email");
                            String profile = data.getString("profile");
                            String level = data.getString("level");


                            //   Toast.makeText(LoginActivity.this, "Successfully loged in " +
                            //             id + "\n" + names + "\n" + fone + "\n" + emai + "\n" + level, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("names", names);
                            intent.putExtra("phone", fone);
                            intent.putExtra("level", level);
                            intent.putExtra("profile", profile);
                            intent.putExtra("user", id);
                            startActivity(intent);
                            finish();


                        } else {
                            Toast.makeText(LoginActivity.this, "Error occured on server side", Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    etphone.getText().clear();
                    etpassword.getText().clear();

                    progressDialog.dismiss();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "failed plz, Check your internet connection" + t.getCause(), Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "press back again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }


}
