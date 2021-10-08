package xyz.developerbab.lostandfoundapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.developerbab.lostandfoundapp.Adapter.Viewdoc2Adapter;
import xyz.developerbab.lostandfoundapp.model.Mydocument;
import xyz.developerbab.lostandfoundapp.singleton.RESTApiClient;

public class MydocumentActivity extends AppCompatActivity implements Viewdoc2Adapter.OnItemClickListener {
    private String user, level, jsonresponse, jsonresponse2, itemclicked,itemclicked2, action;
    private ProgressBar pbmydocs;
    private RecyclerView recyclerView, recyclerviewviewdocsfound;
    private ArrayList<Mydocument> list_data, list_data2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydocument);
        Intent it = getIntent();
        user = it.getStringExtra("user");
        level = it.getStringExtra("level");


        pbmydocs = findViewById(R.id.pbmydocs);
        recyclerView = findViewById(R.id.recyclerviewviewdocslost);
        recyclerviewviewdocsfound = findViewById(R.id.recyclerviewviewdocsfound);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        list_data = new ArrayList<>();


        recyclerviewviewdocsfound.setHasFixedSize(true);
        recyclerviewviewdocsfound.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        list_data2 = new ArrayList<>();

        if (level.equals("1")) {
            recyclerView.setVisibility(View.VISIBLE);

            lostgetjsondata();
        } else if (level.equals("2")) {
            recyclerviewviewdocsfound.setVisibility(View.VISIBLE);

            foundgetjsondata();
        }


    }

    // create API
    public void lostgetjsondata() {

        pbmydocs.setVisibility(View.VISIBLE);

        // create user body object
        Mydocument obj = new Mydocument();
        obj.setId(user);

        Map<String, String> param = new HashMap<>();
        param.put("id", user);

        getlost(param);
    }

    private void getlost(final Map<String, String> obj) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().mylost(obj);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("onSuccess", response.body().toString());


                    String resStr = null;
                    try {
                        resStr = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json = new JSONObject(resStr);

                        jsonresponse = json.toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (Integer.toString(response.code()).equals("200")) {

                        displayrclost();

                    } else {
                        Toast.makeText(MydocumentActivity.this, "Error occured,check your internet connection ", Toast.LENGTH_LONG).show();
                    }

                    //   Toast.makeText(MydocumentActivity.this, "okay "+response.body(), Toast.LENGTH_LONG).show();

                    pbmydocs.setVisibility(View.GONE);

                } else {

                    pbmydocs.setVisibility(View.GONE);
                    Toast.makeText(MydocumentActivity.this, "No data is found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pbmydocs.setVisibility(View.GONE);
                Toast.makeText(MydocumentActivity.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayrclost() {
        try {
            JSONObject object = new JSONObject(jsonresponse);
            JSONArray array = object.getJSONArray("found");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                JSONObject jsonObjectcat = jsonObject.getJSONObject("category");

                String docname = jsonObject.getString("name");

                String doccategory = jsonObjectcat.getString("name");
                String docdescription = jsonObject.getString("description");
                String datelostorfound = jsonObject.getString("created_at");
                String status = jsonObject.getString("status");
                String reference=jsonObject.getString("references");

                String id = jsonObject.getString("id");

                Mydocument model = new Mydocument();
                model.setNames(docname);
                model.setCategorynames(doccategory);
                model.setDescription(docdescription);
                model.setDate(datelostorfound);
                model.setReference(reference);
                model.setStatus(status);
                model.setId(id);

                list_data.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Viewdoc2Adapter adapter = new Viewdoc2Adapter(MydocumentActivity.this, list_data);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(MydocumentActivity.this);

    }


    @Override
    public void onItemClick(int position) {

        action = list_data.get(position).getStatus();
        itemclicked = list_data.get(position).getId();
        Toast.makeText(this, itemclicked, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(MydocumentActivity.this);
        builder.setTitle("Action");
        builder.setCancelable(false);
        builder.setMessage("Do you want to perform an action like delete or update,but if not press cancel");
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   deletedocumentlost();

            }
        });


        if (action.equals("searching")||action.equals("matched")) {
            builder.setPositiveButton("APPROVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    approvelost();
                }
            });
            builder.show();

        } else {
            Toast.makeText(this, "You are not allowed to perform any action on this document", Toast.LENGTH_LONG).show();
        }

    }





    // create API
    public void foundgetjsondata() {

        pbmydocs.setVisibility(View.VISIBLE);

        // create user body object
        Mydocument obj = new Mydocument();
        obj.setId(user);

        Map<String, String> param = new HashMap<>();
        param.put("id", user);

        getfound(param);
    }

    private void getfound(final Map<String, String> obj) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().myfound(obj);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("onSuccess", response.body().toString());


                    String resStr = null;
                    try {
                        resStr = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json = new JSONObject(resStr);

                        jsonresponse2 = json.toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (Integer.toString(response.code()).equals("200")) {

                        displayrcfound();

                    } else {
                        Toast.makeText(MydocumentActivity.this, "Error occured,check your internet connection ", Toast.LENGTH_LONG).show();
                    }

                    //   Toast.makeText(MydocumentActivity.this, "okay "+response.body(), Toast.LENGTH_LONG).show();

                    pbmydocs.setVisibility(View.GONE);

                } else {

                    pbmydocs.setVisibility(View.GONE);
                    Toast.makeText(MydocumentActivity.this, "No data is found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pbmydocs.setVisibility(View.GONE);
                Toast.makeText(MydocumentActivity.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayrcfound() {
        try {
            JSONObject object = new JSONObject(jsonresponse2);
            JSONArray array = object.getJSONArray("found");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                JSONObject jsonObjectcat = jsonObject.getJSONObject("category");

                String docname = jsonObject.getString("name");

                String doccategory = jsonObjectcat.getString("name");
                String docdescription = jsonObject.getString("description");
                String datelostorfound = jsonObject.getString("created_at");
                String status = jsonObject.getString("status");
                String reference=jsonObject.getString("references");

                String id = jsonObject.getString("id");

                Mydocument model = new Mydocument();
                model.setNames(docname);
                model.setCategorynames(doccategory);
                model.setDescription(docdescription);
                model.setDate(datelostorfound);
                model.setStatus(status);
                model.setReference(reference);
                model.setId(id);

                list_data2.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Viewdoc2Adapter adapter = new Viewdoc2Adapter(MydocumentActivity.this, list_data2);
        recyclerviewviewdocsfound.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new Viewdoc2Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String action2 = list_data2.get(position).getStatus();
                itemclicked2 = list_data2.get(position).getId();
                Toast.makeText(MydocumentActivity.this, itemclicked2, Toast.LENGTH_SHORT).show();


                AlertDialog.Builder builder = new AlertDialog.Builder(MydocumentActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Action");
                builder.setMessage("Do you want to perform an action like delete or update,but if not press cancel");
                builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            deletedocumentfound();


                    }
                });

                if (action2.equals("searching")) {
                    builder.show();
                } else {
                    Toast.makeText(MydocumentActivity.this, "You are not allowed to perform any action on this document", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    // create API
    public void approvelost() {

        // create user body object
        Mydocument obj = new Mydocument();
        obj.setId(itemclicked);

        Map<String, String> param = new HashMap<>();
        param.put("id", itemclicked);

        approve(param);
    }

    // create API
    public void deletedocumentlost() {

        // create user body object
        Mydocument obj = new Mydocument();
        obj.setId(itemclicked);

        Map<String, String> param = new HashMap<>();
        param.put("id", itemclicked);

        deletedoc(param);
    }

    private void deletedoc(final Map<String, String> obj) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().deletelost(obj);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String resStr = null;
                try {
                    resStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject json = new JSONObject(resStr);

                   String jsonresponse2 = json.toString();

                    Toast.makeText(MydocumentActivity.this, "Deleted successfully"+jsonresponse2, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (response.code() == 200) {
                    Log.i("onSuccess", response.body().toString());
                    Toast.makeText(MydocumentActivity.this, "Successfuly deleted document", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MydocumentActivity.this, "Not deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MydocumentActivity.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    // create API
    public void deletedocumentfound() {

        // create user body object
        Mydocument obj = new Mydocument();
        obj.setId(itemclicked2);

        Map<String, String> param = new HashMap<>();
        param.put("id", itemclicked2);

        deletedocf(param);
    }

    private void deletedocf(final Map<String, String> obj) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().deletefound(obj);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String resStr = null;
                try {
                    resStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject json = new JSONObject(resStr);

                    String jsonresponse2 = json.toString();

                    Toast.makeText(MydocumentActivity.this, "Deleted successfully"+jsonresponse2, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (response.code() == 200) {
                    Log.i("onSuccess", response.body().toString());
                    Toast.makeText(MydocumentActivity.this, "Successfuly deleted document", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MydocumentActivity.this, "Not deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MydocumentActivity.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void approve(final Map<String, String> obj) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().approvelost(obj);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    Log.i("onSuccess", response.body().toString());
                    Toast.makeText(MydocumentActivity.this, "Successfuly Approved document", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MydocumentActivity.this, "Not approved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MydocumentActivity.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
