package com.commeduc.dev.ecoshoot;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.commeduc.dev.ecoshoot.oo.ShootingCandidate;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.SecureRandom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.GridView;
import android.widget.ImageView;

public class MyCameraActivity extends Activity {

    private static final int CAMERA_REQUEST = 1888;


    static String str_Camera_Photo_ImagePath = "";
    private static File imageFile;
    private static int Take_Photo = 2;
    private static String str_randomnumber = "";
    static String str_Camera_Photo_ImageName = "";
    public static String str_SaveFolderName;
    private static File wallpaperDirectory;
    Bitmap bitmap;
    int storeposition = 0;
    public static GridView gridview;
    public static ImageView imageView;

    private TextView candidateDisplay;
    private ShootingCandidate selectedCandidate;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        this.imageView = (ImageView) this.findViewById(R.id.imageDisplay);
        //Button photoButton = (Button) this.findViewById(R.id.shootingAndPreview);
        CircleImageView photoButton = (CircleImageView)this.findViewById(R.id.shootingAndPreview);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                imageFile = DataContainer.getStorageFileNameFor(selectedCandidate);

                startActivityForResult( new Intent( MediaStore.ACTION_IMAGE_CAPTURE).
                                putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile) ),
                        Take_Photo);

                //System.out.println("imageFile  " + imageFile.getAbsolutePath() );
                Log.d("THE_KING", "imageFile  " + imageFile.getAbsolutePath() );
            }
        });

        candidateDisplay = (TextView) findViewById(R.id.candidateDisplay);

        FloatingActionButton previous = (FloatingActionButton) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPreviousCandidate();
            }
        });

        FloatingActionButton next = (FloatingActionButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextCandidate();
            }
        });

        selectedCandidate = DataContainer.getFirstCandidateOfClass(DataContainer.getShootimgClass(), false);
        if( selectedCandidate != null ){
            loadCandidate(selectedCandidate);
        }
        else {
            Toast.makeText(getApplicationContext(), "Shooting complete for this class.", Toast.LENGTH_SHORT).show();
            candidateDisplay.setText( "Shooting termine.");

            selectedCandidate = DataContainer.getFirstCandidateOfClass(DataContainer.getShootimgClass(), true);
            loadCandidate(selectedCandidate);
        }

    }


    private void loadPreviousCandidate(){

        ShootingCandidate previousCandidate = DataContainer.getPreviousCandidateOfClass(selectedCandidate);
        if( previousCandidate != null ){
            loadCandidate(previousCandidate);
        }
    }

    private void loadNextCandidate(){

        ShootingCandidate nextCandidate = DataContainer.getNextCandidateOfClass(selectedCandidate);
        if( nextCandidate != null ){
            loadCandidate(nextCandidate);
        }
    }

    /**
     *
     * @param selectedCandidate
     */
    public void loadCandidate(ShootingCandidate selectedCandidate){

        if( this.selectedCandidate.equals(selectedCandidate)){
            Toast.makeText(getApplicationContext(), "Same candidate loaded. No picture taken.", Toast.LENGTH_SHORT).show();
        }

        this.selectedCandidate = selectedCandidate;
        candidateDisplay.setText( selectedCandidate.toDsiplayString() );

        if(selectedCandidate.isComplete()){
            Bitmap faceView = ( new_decode( DataContainer.getStorageFileNameFor(selectedCandidate)) );
            imageView.setImageBitmap(faceView);
        }
        else {
            imageView.setImageResource(R.drawable.ic_broken_image_black_48dp);
        }
    }




    // used to create randon numbers
    public String nextSessionId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Take_Photo) {
            File savedFile = DataContainer.getStorageFileNameFor(selectedCandidate);
            if ( savedFile.exists() ) {

                Bitmap faceView = ( new_decode( savedFile) );
                imageView.setImageBitmap(faceView);

            } else {
                bitmap = null;
            }
        }
    }

    public static Bitmap new_decode(File f) {

        // decode image size

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        o.inDither = false; // Disable Dithering mode

        o.inPurgeable = true; // Tell to gc that whether it needs free memory,
        // the Bitmap can be cleared

        o.inInputShareable = true; // Which kind of reference will be used to
        // recover the Bitmap data after being
        // clear, when it will be used in the future
        try {
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = 300;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 1.5 < REQUIRED_SIZE && height_tmp / 1.5 < REQUIRED_SIZE)
                break;
            width_tmp /= 1.5;
            height_tmp /= 1.5;
            scale *= 1.5;
        }

        // decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        // o2.inSampleSize=scale;
        o.inDither = false; // Disable Dithering mode

        o.inPurgeable = true; // Tell to gc that whether it needs free memory,
        // the Bitmap can be cleared

        o.inInputShareable = true; // Which kind of reference will be used to
        // recover the Bitmap data after being
        // clear, when it will be used in the future
        // return BitmapFactory.decodeStream(new FileInputStream(imageFile), null, o2);
        try {

//          return BitmapFactory.decodeStream(new FileInputStream(imageFile), null,
//                  null);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
            System.out.println(" IW " + width_tmp);
            System.out.println("IHH " + height_tmp);
            int iW = width_tmp;
            int iH = height_tmp;

            return Bitmap.createScaledBitmap(bitmap, iW, iH, true);

        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            e.printStackTrace();
            // clearCache();

            // System.out.println("bitmap creating success");
            System.gc();
            return null;
            // System.runFinalization();
            // Runtime.getRuntime().gc();
            // System.gc();
            // decodeFile(imageFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}