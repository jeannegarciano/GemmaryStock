package navigation.garciano.com.navigation;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ItemonStock extends Fragment implements View.OnClickListener{

    View rootView;
    ArrayList<HashMap<String, String>> stockList;
    private static final String TAG_BRACELETAMOUNT = "braceamount";
    private static final String TAG_RINGAMOUNT = "ringamount";
    private static final String TAG_NECKLACEAMOUNT = "necklaceamount";
    private static final String TAG_EARRINGSAMOUNT = "earringsamount";
    private static final String TAG_RINGCOUNT = "ringcount";
    private static final String TAG_BRACELETCOUNT = "braceletcount";
    private static final String TAG_EARRINGSCOUNT = "earringscount";
    private static final String TAG_NECKLACECOUNT = "necklacecount";
    TextView ringOnStock;
    TextView earringOnStock;
    TextView necklaceOnStock;
    TextView braceletOnStock;
    private EditText startOnStock;
    private EditText endOnStock;
    BarChart itemOnStockChart;
    private DatePicker datePickerOnStock;
    private Calendar calendarOnStock;
    private TextView dateViewOnStock;
    private int sYear, sMonth, sDay, sHour, sMinute;
    private Spinner branchSpinner;
    String [] SPINNERLIST = {"HEADOFFICE", "Capitol", "JHANANOISDASD", "HIJK", "CXVCXV", "DF", "OSMENA"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_itemon_stock,container,false);

        new fetchData().execute();


        startOnStock = (EditText) rootView.findViewById(R.id.startdateOnStock);
        endOnStock = (EditText) rootView.findViewById(R.id.enddateOnStock);
        itemOnStockChart = (BarChart) rootView.findViewById(R.id.chartSalesOnStock);
        ringOnStock = (TextView)rootView.findViewById(R.id.ringsalesOnStock);
        earringOnStock = (TextView)rootView.findViewById(R.id.earringsalesOnStock);
        necklaceOnStock = (TextView)rootView.findViewById(R.id.necklacesalesOnStock);
        braceletOnStock = (TextView)rootView.findViewById(R.id.braceletsalesOnStock);

        startOnStock.setOnClickListener(this);
        endOnStock.setOnClickListener(this);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        MaterialBetterSpinner betterSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.branchIdOnStock);
        betterSpinner.setAdapter(arrayAdapter);
        return rootView;

    }

    @Override
    public void onClick(View v) {
        if (v == startOnStock) {

            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            startOnStock.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    },sYear, sMonth, sDay);
            datePickerDialog.show();
        }
        else if (v == endOnStock) {

            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            endOnStock.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
                    ringOnStock.setText("" +rc);
                    earringOnStock.setText("" +ec);
                    necklaceOnStock.setText("" +nc);
                    braceletOnStock.setText("" +bc);

                    Log.d("Data",""+ba);


//                        BarChart salesChart = (BarChart) rootView.findViewById(R.id.chartSales);
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(ra, 0));
                    entries.add(new BarEntry(ba, 1));
                    entries.add(new BarEntry(ea, 2));
                    entries.add(new BarEntry(na, 3));

                    // entries.add(new BarEntry(500f, 4));
                    // entries.add(new BarEntry(400f, 5));

                    itemOnStockChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
                    itemOnStockChart.setDrawGridBackground(false);
                    itemOnStockChart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
                    itemOnStockChart.getXAxis().setTextColor(Color.WHITE);
                    itemOnStockChart.getLegend().setTextColor(Color.WHITE);
                    itemOnStockChart.getAxisRight().setTextColor(Color.WHITE);
                    BarDataSet dataset = new BarDataSet(entries, "# of Items on stock");
                    dataset.setValueTextSize(40f);


                    ArrayList<String> labels = new ArrayList<String>();
                    labels.add("Ring");
                    labels.add("Bracelet");
                    labels.add("Earrings");
                    labels.add("Neclace");



                    BarData data = new BarData(labels, dataset);
                    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    data.setValueTextSize(20f);
                    itemOnStockChart.setData(data);

                    //barChart.setBackgroundColor(Color.rgb(0, 155, 0));
                    itemOnStockChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
                    itemOnStockChart.setDrawGridBackground(false);
                    itemOnStockChart.animateY(4000);

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
