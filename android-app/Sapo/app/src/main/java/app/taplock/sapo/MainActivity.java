package app.taplock.sapo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;

import app.taplock.sapo.sign_in.SignIn;

public class MainActivity extends AppCompatActivity {

    // creating variables for our edittext,
    // button, textview and progressbar.
    private EditText nameEdt, jobEdt;
    private Button postDataBtn;
    private TextView responseTV;
    private ProgressBar loadingPB;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
        finish();

        /*AES xd = new AES();

        String key = "1234567812345678";
        String initVector = "jvHJ1XFt0IXBrxxx";
        String value = "naim";

        xd.main(key,initVector,value);

        TextView _01 = findViewById(R.id.hola);
        TextView _02 = findViewById(R.id.adios);
        TextView _03 = findViewById(R.id.buenas);

        _01.setText(xd.decryptedResult);
        _02.setText(xd.encryptedResult);*/


        /*Uri uri = getIntent().getData();

        // checking if the uri is null or not.
        if (uri != null) {
            // if the uri is not null then we are getting the
            // path segments and storing it in list.


            List<String> parameters = uri.getPathSegments();

            // after that we are extracting string from that parameters.
            String param = parameters.get(parameters.size() - 1);

            Toast.makeText(getApplicationContext(), param,Toast.LENGTH_LONG).show();

            // on below line we are setting
            // that string to our text view
            // which we got as params.
        }*/

    }
}