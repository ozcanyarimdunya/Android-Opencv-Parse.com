package ozcan.com.design8december3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private EditText    username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.eT_register_kadi);
        email = (EditText)findViewById(R.id.eT_register_email);
        password = (EditText)findViewById(R.id.eT_register_sifre);
        Button register = (Button) findViewById(R.id.btn_register_kaydol);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// Parse da yeni bir kullanıcı oluştur
                /// User tablosu
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());

                if ((username.getText() == null) || (email.getText() == null) || (password.getText() == null)) {
                    Toast.makeText(RegisterActivity.this, "Lütfen boş alanları doldurunuz !!", Toast.LENGTH_SHORT).show();
                } else {
                    /// Arka planda parse.com a kayıt yap
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(RegisterActivity.this, "Kayıt başarılı değil ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                ///Kayıt yaptıktan sonra giriş ekranına dön
                                ///giriş yapmasını iste
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                /// Bu ekranı kapatabiliriz artık
                                System.exit(0);
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_help:
                Toast.makeText(RegisterActivity.this, "Burada help bilgi ekranı gelsin", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        System.exit(0);
    }
}
