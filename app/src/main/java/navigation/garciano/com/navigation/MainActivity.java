package navigation.garciano.com.navigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper myDb;
    TextView text;
    String txt_Result;

    ArrayList<HashMap<String, String>> branchList;
    private static final String TAG_ID = "ID";
    private static final String TAG_BRANCHID = "BranchID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        BarChart barChart = (BarChart) findViewById(R.id.chartdefault);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        text = (TextView)findViewById(R.id.text1);
//        Bundle extras = getIntent().getExtras();
//        String name = extras.getString("userName");
//        text.setText(name);

        new fetchData().execute();

    }

    @Override
    public void onBackPressed() {

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.profile) {
//
//
//            return true;
//        }
//
//        else if (id == R.id.logout)
//        {
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        onNavigationItemSelected(menuItem);
                        return true;
                    }
                });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

      ///  Class fragmentClass;
       // int id = item.getItemId();


        switch(item.getItemId()) {
            case R.id.nav_reports:
                fragment = new TrialFragment();
                break;
            case R.id.nav_sales:
                fragment = new TrialFragment();
                break;
            case R.id.nav_item:
                fragment = new SalesFragment();
                break;
            case R.id.nav_return:
                fragment = new SalesFragment();
                break;
            case R.id.nav_returnItems:
                fragment = new ReturnItemsFromCustomer();
                break;
            case R.id.nav_pullout:
                fragment = new SalesFragment();
                break;
            case R.id.nav_layaway:
                fragment = new SalesFragment();
                break;
            case R.id.nav_approval:
                fragment = new Approval();
        }

//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frag, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class fetchData extends AsyncTask<Void, Void, String> {

        //Kailangan ni siya for getting the string from API
        public String getStringFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0) {
                    out.append(new String(b, 0, n));
                }
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?cmd=getAllTxn&branchid=OSMENA&datefrom=7-1-2016&dateto=7-30-2016");

            String text;

            try {
                //Send request
                HttpResponse response = httpClient.execute(httpGet, localContext);
                //receive response
                HttpEntity entity = response.getEntity();
                //parse to string
                text = getStringFromEntity(entity);
            } catch (Exception e) {
                //If wala siyay makuha na string kay no result iya e return.
                text = "no result";
            }

            //This return statement will return either a string or the "no result". Mao ni ang ma pasa sa onPostExecute.
            return text;
        }






        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                // Hashmap for ListView
               // ArrayList<HashMap<String, String>> branchList = new ArrayList<HashMap<String, String>>();
                //Mao ni ang code if kuhaon gayud nimu ang object.
                JSONArray myarray = new JSONArray(s);



                // Getting JSON Array node
                //Mao ni ang code if from object jsonObj ganahan ka kuhaon ang array na sulod ana nga object.
                //Makuha na niya ang arrays na contacts.


                for (int i=0;i<myarray.length();i++) {
                    JSONObject jsonObj = myarray.getJSONObject(i);
                    String id = jsonObj.getString("BranchID");
                    myDb.insertData(myDb, id);
                }
               // String branchid = jsonObj.getString("BranchName");





               // return branchList;
            } catch (JSONException e) {
                e.printStackTrace();
               // return null;
            }

//
//            //Iyang tawgon ang ArrayList na wala pay sulod na gi declare sa ibabaw. So isulod nag data na naa sa ParseJSON. Ang (s) mao
//            //na siya ang string.
//           // branchList = ParseJSON(s);
//
//            //Updating received data from JSON into ListView
//
//           boolean isInserted = myDb.insertData(myDb,TAG_ID, TAG_BRANCHID);
//
//            if(isInserted == true)
//            {
//
//                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String text) {
        if (text != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> branchList = new ArrayList<HashMap<String, String>>();
                //Mao ni ang code if kuhaon gayud nimu ang object.
                JSONArray myarray = new JSONArray(text);

                JSONObject jsonObj = myarray.getJSONObject(0);

                // Getting JSON Array node
                //Mao ni ang code if from object jsonObj ganahan ka kuhaon ang array na sulod ana nga object.
                //Makuha na niya ang arrays na contacts.


                String id = jsonObj.getString(TAG_ID);
                String branchid = jsonObj.getString(TAG_BRANCHID);





                return branchList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }

    }
    }






