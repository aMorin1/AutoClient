package alan.AutoClient.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.beber.androidjson.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    // Déclaration de l'url JSON ARRAY
    private String urlJsonArry = "http://amorin.dev-sio.xyz/API_Rest/back/emprunteur";

    private static String TAG = MainActivity.class.getSimpleName();
    private Button btnMakeArrayRequest;

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView txtResponse;


    // temporary string to show the parsed response
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMakeArrayRequest = (Button) findViewById(R.id.btnArrayRequest);
        txtResponse = (TextView) findViewById(R.id.txtResponse);
        txtResponse.setMovementMethod(ScrollingMovementMethod.getInstance());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Chargement en cours...");
        pDialog.setCancelable(false);


        btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // making json array request
                makeJsonArrayRequest();
            }
        });

    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {
        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonemprunteur = (JSONObject) response
                                        .get(i);

                                String nom = jsonemprunteur.getString("nom");
                                String prenom = jsonemprunteur.getString("prenom");

                                jsonResponse += "Nom: " + nom + "\n\n";
                                jsonResponse += "Prénom: " + prenom + "\n\n\n";


                            }

                            txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        alan.AutoClient.android.AppController.getInstance().addToRequestQueue(req);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}