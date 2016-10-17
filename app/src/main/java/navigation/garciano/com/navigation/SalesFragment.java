package navigation.garciano.com.navigation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SalesFragment extends Fragment implements View.OnClickListener{

    View rootView;
    ArrayList<HashMap<String, String>> stockList;
    ArrayList<HashMap<String, String>> branchList;
    private static final String TAG_BRACELETAMOUNT = "braceamount";
    private static final String TAG_RINGAMOUNT = "ringamount";
    private static final String TAG_NECKLACEAMOUNT = "necklaceamount";
    private static final String TAG_EARRINGSAMOUNT = "earringsamount";
    private static final String TAG_RINGCOUNT = "ringcount";
    private static final String TAG_BRACELETCOUNT = "braceletcount";
    private static final String TAG_EARRINGSCOUNT = "earringscount";
    private static final String TAG_NECKLACECOUNT = "necklacecount";

    private static final String TAG_ID = "ID";
    private static final String TAG_BRANCHNAME = "BranchID";
    BarChart salesChart;
    TextView ring;
    TextView earring;
    TextView necklace;
    TextView bracelet;
    private EditText start;
    private EditText end;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int sYear, sMonth, sDay, sHour, sMinute;
    private Spinner branchSpinner;
    String [] SPINNERLIST = {"JULIANA", "CAPITOL", "OSMENA"};


    String dataUrl = "http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?";

    @Nullable
    @Override

    //http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?cmd=getAllTxn&branchid=OSMENA&datefrom=7-1-2016&dateto=7-30-2016

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_salesreport, container, false);


        new fetchData().execute();



        start = (EditText) rootView.findViewById(R.id.startdate);
        end = (EditText) rootView.findViewById(R.id.enddate);
        salesChart = (BarChart) rootView.findViewById(R.id.chartSales);
        ring = (TextView)rootView.findViewById(R.id.ringsales);
        earring = (TextView)rootView.findViewById(R.id.earringsales);
        necklace = (TextView)rootView.findViewById(R.id.necklacesales);
        bracelet = (TextView)rootView.findViewById(R.id.braceletsales);

        start.setOnClickListener(this);
        end.setOnClickListener(this);

//        DatabaseHelper db = new DatabaseHelper(getContext());
//
//        ArrayList<String> list = db.getBranches();
//        Log.d("Data",""+list);
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, R.id.text, list);
//        MaterialBetterSpinner betterSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.branchId);
//        betterSpinner.setAdapter(adapter);




        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        final MaterialBetterSpinner betterSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.branchId);
        betterSpinner.setAdapter(arrayAdapter);

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
//        branchSpinner = (Spinner)rootView.findViewById(R.id.branchId);
//        branchSpinner.setAdapter(arrayAdapter);





//        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                Toast.makeText(getActivity(), branchSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
//            }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//
//            }
//
//        });

        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (v == start) {

            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            start.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    },sYear, sMonth, sDay);
            long now = System.currentTimeMillis() - 10000;
            datePickerDialog.getDatePicker().setMaxDate(now);
            datePickerDialog.show();
        }
        else if (v == end) {

            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            end.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    },sYear, sMonth, sDay);
            long now = System.currentTimeMillis() - 10000;
            datePickerDialog.getDatePicker().setMaxDate(now);
            datePickerDialog.show();
        }
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

        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids)
        {
//            dataUrl = dataUrl + "cmd=getAllTxn";
//            dataUrl  = dataUrl + "&branchid="+username;
//            dataUrl = dataUrl + "&Password="+password;
//            dataUrl = dataUrl + "&SPIN="+spin;

//            HttpClient httpClient = new DefaultHttpClient();
//            HttpContext localContext = new BasicHttpContext();
//            HttpGet httpGet = new HttpGet("http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?cmd=getAllTxn&branchid=OSMENA&datefrom=7-1-2016&dateto=7-30-2016");
//
//            String text;
//
//            try {
//                //Send request
//                HttpResponse response = httpClient.execute(httpGet, localContext);
//                //receive response
//                HttpEntity entity = response.getEntity();
//                //parse to string
//                text = getStringFromEntity(entity);
//            } catch (Exception e) {
//                //If wala siyay makuha na string kay no result iya e return.
//                text = "no result";
//            }
//
//            //This return statement will return either a string or the "no result". Mao ni ang ma pasa sa onPostExecute.
//            return text;

            String chaine = new String("");
            try{
                URL url = new URL("http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?cmd=getAllTxn&branchid=OSMENA&datefrom=7-1-2016&dateto=10-14-2016");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("User-Agent", "");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    chaine = chaine + line;
                }

            } catch (IOException e) {
                // writing exception to log
                e.printStackTrace();
            }

            return chaine;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            stockList = ParseJSON(s);
}
        private ArrayList<HashMap<String, String>> ParseJSON(String text) {
            if (text != null) {
                try {
                    // Hashmap for ListView
                    ArrayList<HashMap<String, String>> stockList = new ArrayList<HashMap<String, String>>();
                    //Mao ni ang code if kuhaon gayud nimu ang object.
                    JSONArray myarray = new JSONArray(text);

                    JSONObject jsonObj = myarray.getJSONObject(0);

                    // Getting JSON Array node
                    //Mao ni ang code if from object jsonObj ganahan ka kuhaon ang array na sulod ana nga object.
                    //Makuha na niya ang arrays na contacts.

                        int ba = jsonObj.getInt(TAG_BRACELETAMOUNT);
                        int ra = jsonObj.getInt(TAG_RINGAMOUNT);
                        int na = jsonObj.getInt(TAG_NECKLACEAMOUNT);
                        int ea = jsonObj.getInt(TAG_EARRINGSAMOUNT);
                        int rc = jsonObj.getInt(TAG_RINGCOUNT);
                        int bc = jsonObj.getInt(TAG_BRACELETCOUNT);
                        int ec = jsonObj.getInt(TAG_EARRINGSCOUNT);
                        int nc = jsonObj.getInt(TAG_NECKLACECOUNT);
                        ring.setText("" +rc);
                        earring.setText("" +ec);
                        necklace.setText("" +nc);
                        bracelet.setText("" +bc);

                        Log.d("Data",""+ba);


                        BarChart salesChart = (BarChart) rootView.findViewById(R.id.chartSales);
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(ra, 0));
                        entries.add(new BarEntry(ba, 1));
                        entries.add(new BarEntry(ea, 2));
                        entries.add(new BarEntry(na, 3));
                       // entries.add(new BarEntry(500f, 4));
                       // entries.add(new BarEntry(400f, 5));


                    salesChart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
                    salesChart.getXAxis().setTextColor(Color.WHITE);
                    salesChart.getLegend().setTextColor(Color.WHITE);
                    salesChart.getAxisRight().setTextColor(Color.WHITE);
                    salesChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
                    salesChart.setDrawGridBackground(false);
                        BarDataSet dataset = new BarDataSet(entries, "# of Sales");
                        dataset.setValueTextSize(40f);


                        ArrayList<String> labels = new ArrayList<String>();
                        labels.add("Ring");
                        labels.add("Bracelet");
                        labels.add("Earrings");
                        labels.add("Neclace");



                        BarData data = new BarData(labels, dataset);
                        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                        data.setValueTextSize(20f);
                        salesChart.setData(data);

                        //barChart.setBackgroundColor(Color.rgb(0, 155, 0));
                        salesChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
                        salesChart.setDrawGridBackground(false);
                        salesChart.animateY(4000);

//                    YAxis yAxisRight = salesChart.getAxisRight();
//                    YAxis yAxisLeft = salesChart.getAxisLeft();
//                    XAxis xAxis = salesChart.getXAxis();
//                    Legend l = salesChart.getLegend();
//                    yAxisRight.setTextColor(R.color.white);
//                    yAxisLeft.setTextColor(R.color.white);


                        // tmp hashmap for single contact
//                        HashMap<String, String> sales = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                        sales.put(TAG_BRACELETAMOUNT, ba);
//                        sales.put(TAG_RINGAMOUNT, ra);
//                        sales.put(TAG_NECKLACEAMOUNT, na);
//                        sales.put(TAG_EARRINGSAMOUNT,ea);
//                        sales.put(TAG_RINGCOUNT, rc);
//                        sales.put(TAG_BRACELETCOUNT, bc);
//                        sales.put(TAG_EARRINGSCOUNT, ec);
//                        sales.put(TAG_NECKLACECOUNT, nc);
//
//                        // adding contact to contact list
//                        stockList.add(sales);

            return stockList;
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


}

