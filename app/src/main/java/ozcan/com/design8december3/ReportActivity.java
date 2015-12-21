package ozcan.com.design8december3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ReportActivity extends AppCompatActivity {

    private ImageView mImage;
    private Bitmap bitmap;

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Toast.makeText(ReportActivity.this, "Opencv Loaded succesfully !!", Toast.LENGTH_SHORT).show();
                    //setContentView(R.layout.activity_report);
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

        if(!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,this,mOpenCVCallBack)){
            Toast.makeText(ReportActivity.this, "Cannot connect Opencv Manager !!", Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        final String object_id = intent.getStringExtra("resim");

        mImage = (ImageView) findViewById(R.id.imageView_report_result);
        Button btn_resim_al = (Button) findViewById(R.id.btn_resim);

        /******************************************************************************************/
        btn_resim_al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = new ParseQuery<>("image_upload");
                query.getInBackground(object_id, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        final ParseFile fileObject = (ParseFile) parseObject.get("ImageFile");
                        fileObject.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if (e != null) {
                                    Toast.makeText(ReportActivity.this, "hata !!", Toast.LENGTH_SHORT).show();
                                } else {
                                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                                    builder.setTitle("Birini seçiniz ?");
                                    builder.setPositiveButton("Gray Scale", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ToGrayScale(bitmap);
                                        }
                                    });
                                    builder.setNegativeButton("Edge Detection", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            EdgeDetection(bitmap);
                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        });
                    }
                });
            }
        });
        /******************************************************************************************/

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
                    Toast.makeText(ReportActivity.this, "İşlenen resim kaydedildi ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReportActivity.this, "İşlenen resmin yüklenmesinde hata oluştu " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    protected void EdgeDetection(Bitmap bmpEdge){

        Mat edgeMat = new Mat();
        Utils.bitmapToMat(bmpEdge, edgeMat);

        /**Convert to gray image**/
        Mat edgeGray = new Mat();
        Imgproc.cvtColor(edgeMat,edgeGray,Imgproc.COLOR_BGR2GRAY,1);

        /**Do canny**/
        Mat outCannyMat = new Mat();
        Imgproc.Canny(edgeGray,outCannyMat,80,100,3,false);

        /**Output**/
        Bitmap btmp;
        Mat mRgb = new Mat();
        try {
            Imgproc.cvtColor(outCannyMat, mRgb, Imgproc.COLOR_GRAY2BGRA, 4);
            btmp = Bitmap.createBitmap(mRgb.cols(), mRgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mRgb, btmp);
            mImage.setImageBitmap(btmp);
            Upload(btmp);
        }catch (CvException e){
            Toast.makeText(ReportActivity.this, "Hata : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void ToGrayScale(Bitmap bmpGrayScale){
        /*****************/
         Mat mat = new Mat(bmpGrayScale.getWidth(), bmpGrayScale.getHeight(), CvType.CV_8UC1);
         Mat grayMat = new Mat(bmpGrayScale.getWidth(), bmpGrayScale.getHeight(), CvType.CV_8UC1);
         Utils.bitmapToMat(bmpGrayScale, mat);
         Utils.bitmapToMat(bmpGrayScale,grayMat);

         Bitmap bmp;
         int colorChannels = Imgproc.COLOR_BGR2GRAY;
         try{
         Imgproc.cvtColor(mat, grayMat, colorChannels);
         bmp = Bitmap.createBitmap(grayMat.cols(),grayMat.rows(),Bitmap.Config.ARGB_8888);
         Utils.matToBitmap(grayMat,bmp);
         mImage.setImageBitmap(bmp);
         Upload(bmp);
         }
         catch (CvException exception){
         Toast.makeText(ReportActivity.this, "Hata : "+exception.getMessage(), Toast.LENGTH_SHORT).show();
         }
         /*****************/
    }
}
