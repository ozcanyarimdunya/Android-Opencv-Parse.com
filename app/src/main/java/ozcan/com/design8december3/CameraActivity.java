package ozcan.com.design8december3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private Button btn_upload,btn_start;
    private ImageView picture;
    private Bitmap my_bitmap;
    private ByteArrayOutputStream stream = new ByteArrayOutputStream();
    private Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ResimYadaGaleri();
        picture = (ImageView)findViewById(R.id.imageView_Ben_Son);

        /*if(ParseUser.getCurrentUser() == null){
            Intent intent = new Intent(MainCamera.this,LoginActivity.class);
            startActivity(intent);
        }*/


        btn_upload = (Button)findViewById(R.id.btn_upload_resim);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload();
            }
        });

    }

    private void ResimYadaGaleri(){
        btn_start=(Button)findViewById(R.id.btn_start);
        if(!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(CameraActivity.this, "Kamera bulunamadı !!", Toast.LENGTH_SHORT).show();
        }
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*************************************/
                AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                builder.setTitle("Select one ?");

                builder.setPositiveButton("Open Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(0);
                        open_camera.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                        startActivityForResult(open_camera, 0);
                    }
                });

                builder.setNegativeButton("Select from gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent pick_photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pick_photo.setType("image/*");
                        startActivityForResult(pick_photo, 1);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                /*************************************/
            }
        });

    }

    public Uri getOutputMediaFileUri(int type){
        return  Uri.fromFile(getOutputMediaFile(type));
    }

    public File getOutputMediaFile(int type){
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.d(IMAGE_DIRECTORY_NAME,"Oops failed create"+IMAGE_DIRECTORY_NAME+" directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 0) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".png");
        }else {
            return null;
        }
        return mediaFile;
    }

    private void Upload(){
        try {

            if (stream.toByteArray().length==0){
                Toast.makeText(CameraActivity.this, "Resim boş olamaz !!", Toast.LENGTH_SHORT).show();
            }
            else {
                String name = ParseUser.getCurrentUser().getUsername();
                /************/
                final byte[] image = stream.toByteArray();
                ParseFile file = new ParseFile(name+".PNG", image);
                file.saveInBackground();
                final ParseObject img_upload = new ParseObject("image_upload");
                img_upload.put("ImageFile", file);
                img_upload.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Image uploaded !!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CameraActivity.this, ReportActivity.class);
                            intent.putExtra("resim",img_upload.getObjectId());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to upload image " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                /************/
            }
        }
        catch (Exception e){
            Toast.makeText(CameraActivity.this, "!! "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0: //camera opened
                if (requestCode == 0 && resultCode==RESULT_OK) {
                    /*my_bitmap  = (Bitmap) data.getExtras().get("data");
                    my_bitmap = Bitmap.createScaledBitmap(my_bitmap,600,600,true);
                   if (my_bitmap != null) {
                       my_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    }
                    picture.setImageBitmap(my_bitmap);*/

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 0;
                    my_bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                    if(my_bitmap != null){
                        my_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    }
                    picture.setImageBitmap(my_bitmap);
                }

            case 1: //chosen from gallery
                if(requestCode == 1 && resultCode == RESULT_OK && null!=data){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    picture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    my_bitmap = BitmapFactory.decodeFile(picturePath);
                    my_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help_app:
                Toast.makeText(CameraActivity.this, "Help butonu sonra yapılacaktır", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_app:
                Toast.makeText(CameraActivity.this, "Logout butonu sonra yapılacaktır", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
