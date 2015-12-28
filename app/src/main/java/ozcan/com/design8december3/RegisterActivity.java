package ozcan.com.design8december3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
                final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                dialog.setTitle("Lütfen bekleyiniz");
                dialog.setMessage("Kayıt yapılıyor");
                dialog.show();
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());

                if ((username.getText() == null) || (email.getText() == null) || (password.getText() == null)) {
                    Toast.makeText(RegisterActivity.this, "Lütfen boş alanları doldurunuz !!", Toast.LENGTH_SHORT).show();
                    dialog.hide();
                } else {
                    /// Arka planda parse.com a kayıt yap
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                dialog.hide();
                                Toast.makeText(RegisterActivity.this, "Kayıt başarılı değil ! " + e.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                ///Kayıt yaptıktan sonra giriş ekranına dön
                                ///giriş yapmasını iste
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                /// Bu ekranı kapatabiliriz artık
                                System.exit(0);
                                dialog.hide();
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
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setIcon(R.drawable.info_icon);
                alertDialog.setTitle("\tBen Takip v1.0 Uygulamasına Hoşgeldiniz..");
                alertDialog.setMessage("\tUygulamaya kaydolmak için;\n" +
                        "\t*-Size özel bir kullanıcı adı,\n" +
                        "\t*-Geçerli bir e-posta,\n" +
                        "\t*-En az 4 karakterli bir şifre belirleyin\n" +
                        "\tBu işlemleri yaptıktan sonra KAYDOL'a basınız .");
                alertDialog.setButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
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