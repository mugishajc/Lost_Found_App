package xyz.developerbab.lostandfoundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xyz.developerbab.lostandfoundapp.Adapter.CustemAdapter;
import xyz.developerbab.lostandfoundapp.Adapter.ViewdocAdapter;
import xyz.developerbab.lostandfoundapp.interfaces.RESTApiInterface;
import xyz.developerbab.lostandfoundapp.model.Category;
import xyz.developerbab.lostandfoundapp.model.Document;
import xyz.developerbab.lostandfoundapp.model.Document2;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewdocAdapter.OnItemClickListener {

    private ImageView imgmenudrawer;
    private DrawerLayout drawer;

    private TextView tvnamesuser, tvphoneuser;
    private String phone, names, level, profile, user, version, jsonresponse, itemclicked;
    private CircleImageView img_profileuser;

    private RecyclerView recyclerviewmain;
    private ArrayList<Document2> list_data;

    private static final int TIME_INTERVAL = 3000;
    private long mBackPressed;


    private static int SPLASH_TIME_OUT = 5000;

    private ProgressBar progressbarget;
    private SwipeRefreshLayout swipergetdoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent i = getIntent();
        names = i.getStringExtra("names");
        phone = i.getStringExtra("phone");
        profile = i.getStringExtra("profile");
        level = i.getStringExtra("level");
        user = i.getStringExtra("user");


        swipergetdoc = findViewById(R.id.swipergetdoc);
        swipergetdoc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {


                        if (level.equals("1")) {

                            lostgetjsondata();
                        } else if (level.equals("2")) {
                            foundgetjsondata();
                        }


                        swipergetdoc.setRefreshing(false);
                    }
                }, SPLASH_TIME_OUT);


            }
        });

        recyclerviewmain = findViewById(R.id.recyclerviewmain);
        recyclerviewmain.setHasFixedSize(true);
        recyclerviewmain.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        list_data = new ArrayList<>();

        progressbarget = findViewById(R.id.progressbarget);

        if (level.equals("1")) {
            lostgetjsondata();
        } else if (level.equals("2")) {
            foundgetjsondata();
        }


        imgmenudrawer = findViewById(R.id.imgmenudrawer);
        imgmenudrawer.setOnClickListener(this);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);

        Menu menu = navigationView.getMenu();
        MenuItem nav_doc = menu.findItem(R.id.nav_My_document);
        MenuItem nav_all = menu.findItem(R.id.nav_allfound);
        String checlevel = level;
        if (checlevel.equals("1")) {
            nav_doc.setTitle("MY Lost Document");
            nav_all.setTitle("All Lost Documents");
        } else if (checlevel.equals("2")) {
            nav_all.setTitle("All Found Documents");
            nav_doc.setTitle("MY Found Document");
        }


        try {
            PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        tvnamesuser = header.findViewById(R.id.tvnamesuser);
        tvphoneuser = header.findViewById(R.id.tvphoneuser);
        img_profileuser = header.findViewById(R.id.img_profile);

        tvnamesuser.setText(names);


        /*
        String chechlevel = level;
        if (chechlevel.equals("1")) {
            String a = "I have Lost";
            tvphoneuser.setText(phone + "\n" + "Usertype : " + a);
        } else if (chechlevel.equals("2")) {
            String a = "I have Found";
            tvphoneuser.setText(phone + "\n" + "Usertype : " + a);
        }

*/
        tvphoneuser.setText(phone);

        if (profile.equals("null")) {

            Picasso.with(MainActivity.this)
                    .load("http://lostandfound.developerbab.xyz/backend/profiles/default.jpg")
                    .resize(70, 70)
                    .noFade()
                    .centerCrop()
                    .error(R.drawable.ic_person_black_24dp)
                    .into(img_profileuser);

        } else {

            Picasso.with(MainActivity.this)
                    .load("http://lostandfound.developerbab.xyz/backend/profiles/" + profile)
                    .resize(70, 70)
                    .noFade()
                    .centerCrop()
                    .error(R.drawable.ic_person_black_24dp)
                    .into(img_profileuser);

        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.nav_My_document:
                        drawer.close();
                        Intent intento = new Intent(MainActivity.this, MydocumentActivity.class);
                        intento.putExtra("user", user);
                        intento.putExtra("level", level);
                        startActivity(intento);
                        break;


                    case R.id.nav_allfound:
                        drawer.close();
                        break;

                    case R.id.nav_signout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(false);
                        builder.setMessage("Do you want to logout");
                        builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        break;

                    case R.id.nav_aboutus:
                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                        b.setMessage("LFDS, This group project is done by ABAYISENGA NADINE,HAKIZIMANA JEAN CLAUDE,NYIRANZABAMWITA MADALEINE"+"\n" + "powered by UR-CST" + "\n" + "App version =" + version);
                        b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        b.show();
                        break;

                    case R.id.nav_addnew:
                        Intent intent = new Intent(MainActivity.this, AddnewDocumentActivity.class);
                        intent.putExtra("level", level);
                        intent.putExtra("user", user);
                        intent.putExtra("profile", profile);
                        intent.putExtra("phone", phone);
                        intent.putExtra("names", names);
                        startActivity(intent);
                        finish();
                        break;

                    default:
                        break;

                }

                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgmenudrawer:
                drawer.open();
                break;
        }
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


    private void lostgetjsondata() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lostandfound.developerbab.xyz/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RESTApiInterface api = retrofit.create(RESTApiInterface.class);

        Call<String> call = api.getall();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        jsonresponse = response.body();

                        //      swipergetdoc.setVisibility(View.GONE);

                        displayrclost();


                        //        Toast.makeText(MainActivity.this, "okay "+response.body(), Toast.LENGTH_LONG).show();

                        progressbarget.setVisibility(View.GONE);

                    } else {

                        progressbarget.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "No data is found", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayrclost() {
        try {
            JSONObject object = new JSONObject(jsonresponse);
            JSONArray array = object.getJSONArray("lost");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonObject = array.getJSONObject(i);

                JSONObject jsonObjectcat = jsonObject.getJSONObject("category");
                JSONObject jsonObjectuser = jsonObject.getJSONObject("user");

                String username = jsonObjectuser.getString("name");


                String photourl = jsonObjectuser.getString("profile");

                String docname = jsonObject.getString("name");

                String doccategory = jsonObjectcat.getString("name");
                String docdescription = jsonObject.getString("description");
                String datelostorfound = jsonObject.getString("created_at");
                String status = jsonObject.getString("status");
                String userdate = jsonObject.getString("created_at");

                String id = jsonObject.getString("id");

                Document2 model = new Document2();
                model.setUsername(username);
                model.setUserphoto_url(photourl);
                model.setDocname(docname);
                model.setDoccategory(doccategory);
                model.setDocdesdcription(docdescription);
                model.setDocdate(datelostorfound);
                model.setStatus(status);
                model.setUserdate(userdate);
                model.setId(id);

                list_data.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ViewdocAdapter adapter = new ViewdocAdapter(this, list_data);
        recyclerviewmain.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(MainActivity.this);

    }

    @Override
    public void onItemClick(int position) {

    }


    private void foundgetjsondata() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lostandfound.developerbab.xyz/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RESTApiInterface api = retrofit.create(RESTApiInterface.class);

        Call<String> call = api.getallfound();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        jsonresponse = response.body();

                        //      swipergetdoc.setVisibility(View.GONE);

                        displayrcfound();


                        //        Toast.makeText(MainActivity.this, "okay "+response.body(), Toast.LENGTH_LONG).show();

                        progressbarget.setVisibility(View.GONE);

                    } else {

                        progressbarget.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "No data is found", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayrcfound() {
        try {
            JSONObject object = new JSONObject(jsonresponse);
            JSONArray array = object.getJSONArray("found");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonObject = array.getJSONObject(i);

                JSONObject jsonObjectcat = jsonObject.getJSONObject("category");
                JSONObject jsonObjectuser = jsonObject.getJSONObject("user");

                String username = jsonObjectuser.getString("name");


                String photourl = jsonObjectuser.getString("profile");

                String docname = jsonObject.getString("name");

                String doccategory = jsonObjectcat.getString("name");
                String docdescription = jsonObject.getString("description");
                String datelostorfound = jsonObject.getString("created_at");
                String status = jsonObject.getString("status");
                String userdate = jsonObject.getString("created_at");

                String id = jsonObject.getString("id");

                Document2 model = new Document2();
                model.setUsername(username);
                model.setUserphoto_url(photourl);
                model.setDocname(docname);
                model.setDoccategory(doccategory);
                model.setDocdesdcription(docdescription);
                model.setDocdate(datelostorfound);
                model.setStatus(status);
                model.setUserdate(userdate);
                model.setId(id);

                list_data.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ViewdocAdapter adapter = new ViewdocAdapter(this, list_data);
        recyclerviewmain.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(MainActivity.this);

    }


}