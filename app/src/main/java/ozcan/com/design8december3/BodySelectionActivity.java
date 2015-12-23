package ozcan.com.design8december3;

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

public class BodySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_selection);

        if(ParseUser.getCurrentUser() == null){
            Intent intent = new Intent(BodySelectionActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        /****/
        TextView txt = (TextView)findViewById(R.id.txt_hosgeldin);
        txt.setText("Hoşgeldin "+ParseUser.getCurrentUser().getUsername()+" !");
        /****/

        Button arkaya_gec = (Button) findViewById(R.id.btn_arkaya_gec);
        arkaya_gec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodySelectionActivity.this, BodySelectionBackActivity.class);
                startActivity(intent);
            }
        });
        ImageView img = (ImageView) findViewById(R.id.imageView_body);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodySelectionActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_help:
                Toast.makeText(BodySelectionActivity.this, "Help butonu sonra yapılacaktır", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_app:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(BodySelectionActivity.this, "Çıkış yapılıyor", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BodySelectionActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(BodySelectionActivity.this, "Çıkış yapılamadı! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
