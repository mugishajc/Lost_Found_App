package xyz.developerbab.lostandfoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xyz.developerbab.lostandfoundapp.Adapter.CustemAdapter;
import xyz.developerbab.lostandfoundapp.interfaces.RESTApiInterface;
import xyz.developerbab.lostandfoundapp.model.Category;
import xyz.developerbab.lostandfoundapp.model.Document;
import xyz.developerbab.lostandfoundapp.singleton.RESTApiClient;

public class AddnewDocumentActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String HI = "http://lostandfound.developerbab.xyz/api/mobile/getCategory";
    private ArrayList<Category> list_data;
    private CustemAdapter adapter;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private TextView tvcategoria;

    private EditText etreferencedoc, etnamedoc, etdescdoc;
    private Button btnaddnewdocument;
    private ImageButton ibcanceladddoc;
    private String category,idi, reference, name, desc, version, user, level, profile, phone, names, jsonresponse;
    private ProgressDialog progressDialog;
    private ListView lvcategory;

    private ProgressBar progressbarcategory;
    private static final int TIME_INTERVAL = 3000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_document);

        lvcategory = findViewById(R.id.lvcategory);
        progressbarcategory = findViewById(R.id.progressbarcategory);

        list_data = new ArrayList<>();
        tvcategoria = findViewById(R.id.tvcategoria);

        AlertDialog.Builder builder = new AlertDialog.Builder(AddnewDocumentActivity.this);
        builder.setTitle("Important guidance");
        builder.setMessage("To add new document,please select document category (eg:passpot,id,etc.. and enter document reference ,name which is on that document and briefly description ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

        getJsonresponse();

        lvcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = list_data.get(position).getXyz();
                idi=list_data.get(position).getCategoryname();

                tvcategoria.setText("Document : " + list_data.get(position).getXyz());

            }
        });


        Intent i = getIntent();
        user = i.getStringExtra("user");
        names = i.getStringExtra("names");
        phone = i.getStringExtra("phone");
        profile = i.getStringExtra("profile");
        level = i.getStringExtra("level");
        user = i.getStringExtra("user");


        progressDialog = new ProgressDialog(AddnewDocumentActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading ...");


        btnaddnewdocument = findViewById(R.id.btnaddnewdocument);
        btnaddnewdocument.setOnClickListener(this);

        ibcanceladddoc = findViewById(R.id.ibcanceladddoc);
        ibcanceladddoc.setOnClickListener(this);
        etnamedoc = findViewById(R.id.etnamedoc);
        etreferencedoc = findViewById(R.id.etreferencedoc);
        etdescdoc = findViewById(R.id.etdescriptiondoc);


        try {
            PackageInfo pInfo = AddnewDocumentActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibcanceladddoc:
                Intent intent = new Intent(AddnewDocumentActivity.this, MainActivity.class);
                intent.putExtra("names", names);
                intent.putExtra("phone", phone);
                intent.putExtra("level", level);
                intent.putExtra("profile", profile);
                intent.putExtra("user", user);
                startActivity(intent);
                AddnewDocumentActivity.this.finish();
                break;

            case R.id.btnaddnewdocument:

                name = etnamedoc.getText().toString().trim();
                reference = etreferencedoc.getText().toString().trim();
                desc = etdescdoc.getText().toString().trim();
                if (name.isEmpty() || reference.isEmpty() || desc.isEmpty() || (tvcategoria.getText().toString().equals(null))) {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show();
                } else {
                    if (level.equals("1")) {

                        calladdnewlostdocapi();

                    } else {
                        callinsertfounddocument();
                    }
                }

                break;

        }
    }

    // create API
    public void calladdnewlostdocapi() {
        progressDialog.show();

        // create user body object
        Document obj = new Document();
        obj.setUser(user);
        obj.setReference(reference);
        obj.setCategory(idi);
        obj.setName(name);
        obj.setDescription(desc);

        Map<String, String> param = new HashMap<>();
        param.put("user", user);
        param.put("reference", reference);
        param.put("category", idi);
        param.put("name", name);
        param.put("description", desc);

        insertdata(param);
    }


    private void insertdata(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().addocument(user);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response

                if (response.isSuccessful()) {
                    Toast.makeText(AddnewDocumentActivity.this, "Document is saved successfully", Toast.LENGTH_SHORT).show();

                    etnamedoc.getText().clear();
                    etreferencedoc.getText().clear();
                    etdescdoc.getText().clear();
                    progressDialog.dismiss();

                } else {

                    progressDialog.dismiss();
                    Toast.makeText(AddnewDocumentActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                //failure
                Toast.makeText(AddnewDocumentActivity.this, "failed plz" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override

    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "press back again to exit" + "\n" + "App Version is" + version, Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    // create API
    public void callinsertfounddocument() {
        progressDialog.show();

        // create user body object
        Document obj = new Document();
        obj.setUser(user);
        obj.setReference(reference);
        obj.setCategory(idi);
        obj.setName(name);
        obj.setDescription(desc);

        Map<String, String> param = new HashMap<>();
        param.put("user", user);
        param.put("reference", reference);
        param.put("category", idi);
        param.put("name", name);
        param.put("description", desc);

        founddoc(param);
    }

    private void founddoc(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().foundocs(user);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response

                if (response.isSuccessful()) {
                    Toast.makeText(AddnewDocumentActivity.this, "Document  is saved successfully", Toast.LENGTH_SHORT).show();

                    etnamedoc.getText().clear();
                    etreferencedoc.getText().clear();
                    etdescdoc.getText().clear();
                    progressDialog.dismiss();

                } else {

                    progressDialog.dismiss();
                    Toast.makeText(AddnewDocumentActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                //failure
                Toast.makeText(AddnewDocumentActivity.this, "failed plz" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    private void getJsonresponse() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lostandfound.developerbab.xyz/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RESTApiInterface api = retrofit.create(RESTApiInterface.class);

        Call<String> call = api.getcategory();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        jsonresponse = response.body().toString();

                        displaylv();

                        // Toast.makeText(AddnewDocumentActivity.this, "okay "+response.body(), Toast.LENGTH_LONG).show();

                        progressbarcategory.setVisibility(View.GONE);

                    } else {

                        progressbarcategory.setVisibility(View.GONE);
                        Toast.makeText(AddnewDocumentActivity.this, "No category is found", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AddnewDocumentActivity.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displaylv() {
        try {
            JSONObject object = new JSONObject(jsonresponse);
            JSONArray array = object.getJSONArray("category");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonObject = array.getJSONObject(i);
                String x = jsonObject.getString("name");
                String name = jsonObject.getString("id");

                Category model = new Category();
                model.setCategoryname(name);
                model.setXyz(x);
                list_data.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustemAdapter adapter = new CustemAdapter(this, list_data);
        lvcategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}



