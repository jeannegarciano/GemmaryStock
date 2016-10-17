package navigation.garciano.com.navigation;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
/**
 * Created by admin on 9/28/2016.
 */

public class LoginActivity extends AppCompatActivity{

    String dataUrl = "http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?";
    String userName;
    String passWord;
    String sPin;

    URL url;
    HttpURLConnection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
    }

    public void login(View view) {
        EditText user = (EditText)findViewById(R.id.username);
        EditText pass = (EditText)findViewById(R.id.password);
        EditText pin = (EditText)findViewById(R.id.spin);
        TextInputLayout u = (TextInputLayout)findViewById(R.id.input_layout_username);
        TextInputLayout ps = (TextInputLayout)findViewById(R.id.input_layout_pass);
        TextInputLayout p = (TextInputLayout)findViewById(R.id.input_layout_name);
        Button login = (Button)findViewById(R.id.loginButton);

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

            Toast.makeText(this, "" +userName + "" + pass, Toast.LENGTH_LONG);

//            new  login().execute();
        }
    }
    //Region THREADS
    private class login extends AsyncTask<Void,Void,String>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection connection = null;

            dataUrl = dataUrl + "cmd=cmdLogin";
            dataUrl  = dataUrl + "&Username="+userName;
            dataUrl = dataUrl + "&Password="+passWord;
            dataUrl = dataUrl + "&SPIN="+sPin;
            Log.d("Data", "" + dataUrl);

            try{

                URL url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(dataUrl);
                wr.flush();
                wr.close();

                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                String responseStr = response.toString();
                Log.d("Server response",responseStr);

            }
            catch(MalformedURLException error) {

                error.printStackTrace();
                //Handles an incorrectly entered URL
            }
            catch(SocketTimeoutException error) {
                //Handles URL access timeout.
                error.printStackTrace();
            }
            catch (Exception e){

                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    //End Region
}
