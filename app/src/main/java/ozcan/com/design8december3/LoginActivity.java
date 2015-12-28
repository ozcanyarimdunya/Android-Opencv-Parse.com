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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText    username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.eT_login_kadi);
        password = (EditText)findViewById(R.id.eT_login_sifre);
        Button login = (Button) findViewById(R.id.btn_login_giris);
        Button register = (Button) findViewById(R.id.btn_login_kaydol);

        ///Giriş yap butonu
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setTitle("Lütfen bekleyin");
                dialog.setMessage("Giriş yapılıyor...");
                dialog.show();
                ///Arka planda girş yap parse.com a
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        /// eğer bir hata varsa
                        if (e != null) {
                            Toast.makeText(LoginActivity.this, "Kullanıcı adı veya şifre yanlış" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, BodySelectionActivity.class);
                            startActivity(intent);
                            ///Giriş yaptıktan sonra bu ekranı kapat
                            ///Zaten artık geri dönme gibi bir durum yok
                            System.exit(0);
                            dialog.hide();
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
                ///Kayıt yapma ekranını aç
                ///Bu ekranı kapat artık
                ///System.exit(0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ///Menü tanımlama
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            if(item.getItemId()==R.id.login_help) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("\tBen Takip v1.0 Uygulamasına Hoşgeldiniz..");
                alertDialog.setMessage("\tBen analizi yapabilmek için kayıt olmanız gerekmektedir.\n" +
                        "\tKaydolmak için KAYDOL'a basınız\n\n" +
                        "\tEğer kayıtlı bir kullanıcıysanız,\n" +
                        "\tKullanıcı adı ve şifrenizi girerek GİRİŞ YAP'a basınız..\n");
                alertDialog.setButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }

        return super.onOptionsItemSelected(item);
    }

}