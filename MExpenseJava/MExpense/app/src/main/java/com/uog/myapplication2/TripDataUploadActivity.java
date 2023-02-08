package com.uog.myapplication2;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uog.myapplication2.database.Expenses;
import com.uog.myapplication2.database.TripExportData;
import com.uog.myapplication2.util.Constants;
import com.uog.myapplication2.util.ExpenseSearchTask;
import com.uog.myapplication2.util.TripDataExportTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class TripDataUploadActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_data_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView =findViewById(R.id.webView);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        exportForUpload();
    }

    private void exportForUpload(){
        TripDataExportTask tripDataExportTask =new TripDataExportTask(this);
        tripDataExportTask.setListener(new TripDataExportTask.TripDataExportListener() {
            @Override
            public void result(List<TripExportData> tripExportData) {
                try {

                    URL pageURL = new URL(getString(R.string.url));
                    HttpsURLConnection con = (HttpsURLConnection)pageURL.openConnection();
                    con.setHostnameVerifier(DUMMY_VERIFIER);// NOT required for valid ssl domain

                    JSONArray jsonArray = new JSONArray();
                    for( TripExportData data : tripExportData ){
                        JSONObject object = new JSONObject();
                        object.put("name", data.getName());
                        object.put("destination",data.getDestination());
                        Date tripDate =new Date( data.getDate() );
                        String tripDateStr = new SimpleDateFormat(Constants.DATE_FORMAT).format(tripDate);
                        object.put("date", tripDateStr);
                        object.put("expenseType", data.getExpenseType());
                        object.put("amount", data.getAmount());
                        Date expenseDate =new Date( data.getDate() );
                        String expenseDateStr = new SimpleDateFormat(Constants.DATE_FORMAT).format(expenseDate);
                        object.put("expenseTime", expenseDateStr);
                        object.put("comment", data.getComment());
                        jsonArray.put(object);
                    }

                    JSONObject rootObject = new JSONObject();
                    rootObject.put("userId","YourIDHere");
                    rootObject.put("detailList",jsonArray);
                    String jsonString = rootObject.toString();
                    JsonThread myTask = new JsonThread(TripDataUploadActivity.this, con, jsonString);
                    Thread t1 = new Thread(myTask, "JSON Thread");
                    t1.start();

                } catch (Exception e) { e.printStackTrace(); }

            }
        });
        tripDataExportTask.execute();
    }
    // Verifier that verifies all hosts
    private static final HostnameVerifier DUMMY_VERIFIER = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    class JsonThread implements Runnable {
        private AppCompatActivity activity;
        private HttpURLConnection con;
        private String jsonPayLoad;

        public JsonThread(AppCompatActivity activity, HttpURLConnection con, String jsonPayload) {
            this.activity = activity;
            this.con = con;
            this.jsonPayLoad = jsonPayload;
        }

        @Override
        public void run() {
            String response = "";
            if (prepareConnection()) {
                response = postJson();
            } else {
                response = "Error preparing the connection";
            }
            showResult(response);
        }


        private void showResult(String response) {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    String page = generatePage(response);
                    ((TripDataUploadActivity)activity).webView.loadData(page, "text/html", "UTF-8");
                }
            });
        }

        private String postJson() {
            String response = "";
            try {
//                String postParameters = "jsonpayload=" + URLEncoder.encode(jsonPayLoad, "UTF-8");
                con.setFixedLengthStreamingMode(jsonPayLoad.getBytes().length);
                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print(jsonPayLoad);
                out.close();
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = readStream(con.getInputStream());
                } else {
                    response = "Error contacting server: " + responseCode;
                }
            } catch (Exception e) {
                response = e.toString();//"Error executing code";
            }
            return response;
        }

        private String readStream(InputStream in) {
            StringBuilder sb = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        private String generatePage(String content) {
            return "<html><body><p>" + content + "</p></body></html>";
        }


        private boolean prepareConnection() {
            try {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");// text/plain
                return true;

            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}