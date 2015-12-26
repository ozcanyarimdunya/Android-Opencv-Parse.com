package ozcan.com.design8december3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class BodySelectionBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_selection_back);

        TextView txt = (TextView)findViewById(R.id.txt_hosgeldin);

        if(ParseUser.getCurrentUser() == null){
            txt.setText("Hoşgeldiniz .. Devam edebilmek için lütfen giriş yapın!");
        }
        else {
            txt.setText("Hoşgeldin " + ParseUser.getCurrentUser().getUsername() + " !");
        }

        Button arkaya_gec = (Button) findViewById(R.id.btn_arkaya_gec);
        arkaya_gec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodySelectionBackActivity.this, BodySelectionActivity.class);
                startActivity(intent);
                ///System.exit(0);
            }
        });
        ImageView img = (ImageView) findViewById(R.id.imageView_body);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser() == null){
                    Intent intent = new Intent(BodySelectionBackActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                Intent intent = new Intent(BodySelectionBackActivity.this, CameraActivity.class);
                startActivity(intent);
                System.exit(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(ParseUser.getCurrentUser() != null) {
            getMenuInflater().inflate(R.menu.menu_app, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_anamenu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_help:
                Toast.makeText(BodySelectionBackActivity.this, "Help butonu sonra yapılacaktır", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_app:
                final ProgressDialog dialog = new ProgressDialog(BodySelectionBackActivity.this);
                dialog.setTitle("Lütfen bekleyiniz");
                dialog.setMessage("Çıkış yapılıyor");
                dialog.show();
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            startActivity(new Intent(BodySelectionBackActivity.this, LoginActivity.class));
                            System.exit(0);
                            dialog.hide();
                        } else {
                            Toast.makeText(BodySelectionBackActivity.this, "Çıkış yapılamadı! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.giris_yap_menu:
                startActivity(new Intent(this,LoginActivity.class));
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, BodySelectionActivity.class));
        System.exit(0);
    }
}
