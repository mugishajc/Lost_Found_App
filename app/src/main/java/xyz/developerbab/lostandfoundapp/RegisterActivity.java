package xyz.developerbab.lostandfoundapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import xyz.developerbab.lostandfoundapp.model.User;
import xyz.developerbab.lostandfoundapp.singleton.RESTApiClient;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvloginh, tvmsgresult;
    private EditText etnames, etemail, etphone, etpassword;
    private Button btnsignup;
    private String names, email, phone, password, level;
    private ProgressDialog progressDialog;

    private CheckBox cxlost, cxfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("CREATE AN ACCOUNT");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        cxfound = findViewById(R.id.cxfound);
        cxlost = findViewById(R.id.cxlost);

        tvmsgresult = findViewById(R.id.tvmsgresult);
        btnsignup = findViewById(R.id.btnSignup);
        btnsignup.setOnClickListener(this);

        tvloginh = findViewById(R.id.tvloginh);
        tvloginh.setOnClickListener(this);

        etnames = findViewById(R.id.etnames);
        etemail = findViewById(R.id.etemail);
        etphone = findViewById(R.id.etphoner);
        etpassword = findViewById(R.id.etpasswordr);


        cxlost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    level = "1";
                    cxfound.setChecked(false);
                }
            }
        });

        cxfound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    level = "2";
                    cxlost.setChecked(false);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvloginh:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                RegisterActivity.this.finish();
                break;
            case R.id.btnSignup:
                names = etnames.getText().toString().trim();
                email = etemail.getText().toString().trim();
                phone = etphone.getText().toString().trim();
                password = etpassword.getText().toString().trim();
                if (names.isEmpty() || phone.isEmpty() || password.isEmpty()) {

                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();

                    Snackbar.make(v, "Please enter all fields", Snackbar.LENGTH_LONG)
                            .setAction("Fail ", null).show();
                } else {
                    callcreateapi();
                }
                break;

        }
    }

    // create API
    public void callcreateapi() {
        // create user body object
        User obj = new User();
        obj.setName(names);
        obj.setEmail(email);
        obj.setTelephone(phone);
        obj.setLevel(level);
        obj.setPassword(password);
        progressDialog.show();

        Map<String, String> param = new HashMap<>();
        param.put("name", names);
        param.put("email", email);
        param.put("telephone", phone);
        param.put("level", level);
        param.put("password", password);

        login(param);
    }

    private void login(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().createUser(user);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response


                try {
                    String jsondata = response.body().string();

                    if (jsondata != null) {
                        JSONObject reader = new JSONObject(jsondata);
                        String name = reader.getString("name");

                        //JSONObject data = reader.getJSONObject("Data");
                        //  String names = data.getString("fullname");

                             // tvmsgresult.setText(name + "'s account is created successfully");
                           //   Toast.makeText(RegisterActivity.this, name + "'s account created successfully ", Toast.LENGTH_LONG).show();

                        //JSONObject errordb = reader.getJSONObject("message");
                        // String feedback = errordb.getString("phone");


                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        RegisterActivity.this.finish();

                    } else {
                        Toast.makeText(RegisterActivity.this, "error occured on the server side", Toast.LENGTH_LONG).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                etnames.getText().clear();
                etphone.getText().clear();
                etemail.getText().clear();
                etpassword.getText().clear();

                tvmsgresult.setText("");
                progressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                //failure
                Toast.makeText(RegisterActivity.this, "failed plz" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


}
