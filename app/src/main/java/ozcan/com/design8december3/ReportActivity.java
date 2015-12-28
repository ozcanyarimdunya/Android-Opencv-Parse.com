package ozcan.com.design8december3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;

import javax.xml.validation.TypeInfoProvider;

public class ReportActivity extends AppCompatActivity {

    private Bitmap  bitmap;

    ///opencv için gerekli bir sınıf mecbur tanımlanmalı
    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        ///Bu kısım openCv yi denemek için
        ///Başarılı olma durumunu kontrol etmek için

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    //Toast.makeText(ReportActivity.this, "Opencv yüklemesi başarılı !!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        final String object_id = intent.getStringExtra("resim");

        if(ParseUser.getCurrentUser() == null){
            startActivity(new Intent(ReportActivity.this,LoginActivity.class));
        }

        if(!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mOpenCVCallBack)){
            Toast.makeText(ReportActivity.this, "Cannot connect Opencv Manager !!", Toast.LENGTH_SHORT).show();
        }

        ParseQuery<ParseObject> query = new ParseQuery<>("image_upload");
        query.getInBackground(object_id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                final ParseFile fileObject = (ParseFile) parseObject.get("ImageFile");
                fileObject.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if (e != null) {
                            Toast.makeText(ReportActivity.this, "hata " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            ToGrayScale(bitmap);

                            EdgeDetection(bitmap);
                        }
                    }
                });
            }
        });
    }

    protected void EdgeDetection(Bitmap bmpEdge){

        ImageView edgeDetectionImage = (ImageView) findViewById(R.id.imageView_egdeDetection);

        Mat edgeMat = new Mat();
        Utils.bitmapToMat(bmpEdge, edgeMat);

        Mat edgeGray = new Mat();
        Imgproc.cvtColor(edgeMat, edgeGray, Imgproc.COLOR_BGR2GRAY, 1);

        Mat outCannyMat = new Mat();
        Imgproc.Canny(edgeGray,outCannyMat,80,100,3,false);

        Bitmap btmp;
        Mat mRgb = new Mat();
        try {
            Imgproc.cvtColor(outCannyMat, mRgb, Imgproc.COLOR_GRAY2BGRA, 4);
            btmp = Bitmap.createBitmap(mRgb.cols(), mRgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mRgb, btmp);
            edgeDetectionImage.setImageBitmap(btmp);
            Upload(btmp);
        }catch (CvException e){
            Toast.makeText(ReportActivity.this, "Hata : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void ToGrayScale(Bitmap bmpGrayScale){

        ImageView grayScaleImage = (ImageView) findViewById(R.id.imageView_grayScale);

        Mat mat = new Mat(bmpGrayScale.getWidth(), bmpGrayScale.getHeight(), CvType.CV_8UC1);
        Mat grayMat = new Mat(bmpGrayScale.getWidth(), bmpGrayScale.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bmpGrayScale, mat);
        Utils.bitmapToMat(bmpGrayScale, grayMat);

        Bitmap bmp;
        int colorChannels = Imgproc.COLOR_BGR2GRAY;
        try{
            Imgproc.cvtColor(mat, grayMat, colorChannels);
            bmp = Bitmap.createBitmap(grayMat.cols(),grayMat.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(grayMat,bmp);
            grayScaleImage.setImageBitmap(bmp);
            Upload(bmp);
        }
        catch (CvException exception){
            Toast.makeText(ReportActivity.this, "Hata : "+exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void Upload(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageByte = stream.toByteArray();
        ParseFile parseFile = new ParseFile(ParseUser.getCurrentUser().getUsername()+".PNG",imageByte);
        parseFile.saveInBackground();
        ParseObject parseObject = new ParseObject("ImageResultAfterOpencv");
        parseObject.put("ImageFile",parseFile);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //Toast.makeText(ReportActivity.this, "İşlenen resim kaydedildi ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReportActivity.this, "İşlenen resmin yüklenmesinde hata oluştu " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.kapat:
                System.exit(0);
                break;
            case R.id.login_help:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setIcon(R.drawable.info_icon);
                alertDialog.setTitle("\tYüklediğiniz resimlerin analiz edilmiş hali");
                alertDialog.setMessage("\t-*- Üstteki resim Edge Detection \n" +
                                       "\t-*- Alttaki resim Gray Scale     \n");
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
        startActivity(new Intent(this, CameraActivity.class));
    }

}
