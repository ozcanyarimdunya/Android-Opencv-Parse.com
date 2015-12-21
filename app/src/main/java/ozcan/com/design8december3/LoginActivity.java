package ozcan.com.design8december3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private Button login,register;
    private EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(this, "Your_App_ID", "Your_Client_ID");

        username = (EditText)findViewById(R.id.eT_login_kadi);
        password = (EditText)findViewById(R.id.eT_login_sifre);
        login = (Button)findViewById(R.id.btn_login_giris);
        register = (Button)findViewById(R.id.btn_login_kaydol);

        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this,BodySelectionActivity.class);
            startActivity(intent);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e != null){
                            Toast.makeText(LoginActivity.this, "Kullanıcı adı veya şifre yanlış" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent = new Intent(LoginActivity.this,BodySelectionActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_login,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            if(item.getItemId()==R.id.login_help) {
                Toast.makeText(LoginActivity.this, "Help butonu seçildi", Toast.LENGTH_SHORT).show();
            }

        return super.onOptionsItemSelected(item);
    }
}