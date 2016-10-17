package navigation.garciano.com.navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;


/**
 * Created by admin on 7/15/2016.
 */
public class Login extends AppCompatActivity implements View.OnClickListener{

    String dataUrl = "http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?";
    String userName;
    String passWord;
    String sPin;
    EditText user, pass, pin;
    TextInputLayout u, ps, p;
    Button login;

    URL url;
    HttpURLConnection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        user = (EditText)findViewById(R.id.username);
    pass = (EditText)findViewById(R.id.password);
    pin = (EditText)findViewById(R.id.spin);
    u = (TextInputLayout)findViewById(R.id.input_layout_username);
    ps = (TextInputLayout)findViewById(R.id.input_layout_pass);
    p = (TextInputLayout)findViewById(R.id.input_layout_name);
    login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener(this);
}

    @Override
    public void onClick(View view) {
        if(view == login){

            if (user.getText().toString().isEmpty()) {

                Toast.makeText(this, "Please enter username!", Toast.LENGTH_LONG).show();
            } else if (pass.getText().toString().isEmpty()) {

                Toast.makeText(this, "Please enter password!", Toast.LENGTH_LONG).show();

            } else if (pin.getText().toString().isEmpty()) {

                Toast.makeText(this, "Please enter SPIN!", Toast.LENGTH_LONG).show();
            } else {

                userName = user.getText().toString();
                passWord = pass.getText().toString();
                sPin = pin.getText().toString();

                Toast.makeText(this, "" +userName+ "" +passWord+ "" +sPin, Toast.LENGTH_LONG).show();

            new  login().execute();
            }

        }
    }


    //Region THREADS
    private class login extends AsyncTask<Void,Void,String>{


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
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... voids) {

//            HttpURLConnection connection = null;

//            http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?cmd=cmdLogin&Username=OSMENA_01&Password=password&SPIN=1234


            dataUrl = dataUrl + "cmd=cmdLogin";
            dataUrl  = dataUrl + "&Username="+userName;
            dataUrl = dataUrl + "&Password="+passWord;
            dataUrl = dataUrl + "&SPIN="+sPin;
            Log.d("Data", "" + dataUrl);

            //Toast.makeText(getApplicationContext(), dataUrl, Toast.LENGTH_LONG).show();

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(dataUrl);


//            try{
//                URL url = new URL(dataUrl);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setUseCaches(false);
//                connection.setDoInput(true);
//                connection.setDoOutput(true);
//
//                DataOutputStream wr = new DataOutputStream(
//                        connection.getOutputStream());
//                wr.writeBytes(dataUrl);
//                wr.flush();
//                wr.close();
//
//                InputStream is = connection.getInputStream();
//                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//                String line;
//                StringBuffer response = new StringBuffer();
//                while ((line = rd.readLine()) != null) {
//                    response.append(line);
//                    response.append('\r');
//                }
//                rd.close();
//                String responseStr = response.toString();
//                Log.d("Server response",responseStr);
//
//            }
//            catch(MalformedURLException error) {
//
//                error.printStackTrace();
//                //Handles an incorrectly entered URL
//            }
//            catch(SocketTimeoutException error) {
//                //Handles URL access timeout.
//                error.printStackTrace();
//            }
//            catch (Exception e){
//
//                e.printStackTrace();
//            }
//            finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }


            String text;

            try {
                //Send request
                HttpResponse response = httpClient.execute(httpGet, localContext);
                //receive response
                HttpEntity entity = response.getEntity();
                //parse to string
                text = getStringFromEntity(entity);

               if(text.equalsIgnoreCase("True"))
               {
                   Intent intent = new Intent(Login.this,MainActivity.class);
                   intent.putExtra("userName", userName);
                   startActivity(intent);

               }
            } catch (Exception e) {
                //If wala siyay makuha na string kay no result iya e return.
                text = "no result";
            }

            return text;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);


        }
    }
    //End Region

}
