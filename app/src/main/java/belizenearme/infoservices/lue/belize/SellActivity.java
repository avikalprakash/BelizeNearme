package belizenearme.infoservices.lue.belize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import belizenearme.infoservices.lue.belize.camera.CameraActivity;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class SellActivity extends AppCompatActivity implements View.OnClickListener{

    final int CATEGORY_REQUEST_CODE=1;
    final int REQUEST_IMAGE_CAPTURE=99;
    final int REQUEST_GALLARY=98;
    final int REQUEST_VIDEO_SELECT=97;
    final int VIDEO_TRIM_RESULT_CODE =96;

    LinearLayout cameraLayout,gallaryLayout,videoLayout,imageLayout;
    RelativeLayout categoryLayout;
    Button continueBtn;
    Context context;
    ArrayList<File> imageFiles;
    String selectedVideoPath="";
    String trimmedVideoPath;
    File videoFile;
    AppCompatImageView videoThumbImageView;
    TextView categoryName,videoSizeText,videoSizeWarningText,remove_video;
    int catId,subCatId;
    String catname="";
    String subcatname="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UtilityClass.setStatusBarColor(this);
        context=this;
        imageFiles=new ArrayList<>();

        initialize();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initialize() {

        cameraLayout=(LinearLayout)findViewById(R.id.cameraLayout);
        gallaryLayout=(LinearLayout)findViewById(R.id.gallaryLayout);
       // videoLayout=(LinearLayout)findViewById(R.id.videoLayout);
        imageLayout=(LinearLayout)findViewById(R.id.imageLayout);
        categoryLayout=(RelativeLayout)findViewById(R.id.categoryLayout);
        continueBtn=(Button)findViewById(R.id.continueBtn);
        continueBtn.setEnabled(false);

        videoThumbImageView=(AppCompatImageView)findViewById(R.id.videoThumbImageView);
        videoSizeText=(TextView)findViewById(R.id.videoSizeText);
        videoSizeWarningText=(TextView)findViewById(R.id.videoSizeWarningText);
        remove_video=(TextView)findViewById(R.id.remove_video);
        categoryName=(TextView)findViewById(R.id.categoryName);

        cameraLayout.setOnClickListener(this);
        gallaryLayout.setOnClickListener(this);
       // videoLayout.setOnClickListener(this);
        categoryLayout.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
        remove_video.setOnClickListener(removeListener);

        // set selected photo and video

       /* Products products=SharedPreferenceClass.getSellProductDetails(context);
        setCategory(products.getCategory_name(),products.getSub_category_name());
        catId=products.getCategory_id();
        subCatId=products.getSub_category_id();
        StringTokenizer stringTokenizer=new StringTokenizer(products.getImages(),",");
        ArrayList<File> savedImages=new ArrayList<>();
        while (stringTokenizer.hasMoreTokens())
            savedImages.add(new File(stringTokenizer.nextToken()));

        for(File imgFile:savedImages)
        {
           if(imgFile!=null) setSelectedImage(imgFile);
        }

        if(!products.getVideo().equals("")) {
            selectedVideoPath=products.getVideo();
            setSelectedVideo();
        }
        checkForcompleteData();*/



    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cameraLayout:
               if(imageFiles.size()<5)
                   startActivityForResult(new Intent(context, CameraActivity.class),REQUEST_IMAGE_CAPTURE);
                break;
            case R.id.gallaryLayout:
                if(imageFiles.size()<5) {
                    Intent intent = new Intent(context, AlbumSelectActivity.class);
                    intent.putExtra(AppConstants.INTENT_EXTRA_LIMIT, 5);
                    startActivityForResult(intent, REQUEST_GALLARY);
                }
                break;
     /*       case R.id.videoLayout:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_VIDEO_SELECT);
                break;*/
            case R.id.categoryLayout:
                startActivityForResult(new Intent(context,CategoryActivity.class),CATEGORY_REQUEST_CODE);
                break;
            case R.id.continueBtn:
               gotoNext();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {

            if(requestCode==CATEGORY_REQUEST_CODE)
            {
                if(data!=null)
                {
                    catId=data.getIntExtra(AppConstants.CATEGROY_ID,0);
                    subCatId=data.getIntExtra(AppConstants.SUB_CATEGROY_ID,0);
                    catname=data.getStringExtra(AppConstants.CATEGORY_NAME);
                    subcatname=data.getStringExtra(AppConstants.SUBCATEGORY_NAME);
                    setCategory(catname,subcatname);
                }
            }

           else if(requestCode==REQUEST_IMAGE_CAPTURE)
            {
                    final String filename = data.getStringExtra(AppConstants.FILENAME);
                  if(filename!=null) setCaptureImage(filename);

            }

           else if(requestCode==REQUEST_GALLARY) {
                ArrayList<Image> images = data.getParcelableArrayListExtra(AppConstants.INTENT_EXTRA_IMAGES);
                for (int k = 0; k < images.size(); k++) {
                    final File imgFile = new File(images.get(k).path);
                    setSelectedImage(imgFile);
                }
                checkForcompleteData();

            }
/*            else if(requestCode==REQUEST_VIDEO_SELECT && data!=null)
            {
                selectedVideoPath = getPath(data.getData());

                if (selectedVideoPath != null) {
                    Intent videoTrimIntent=new Intent(context,VideoTrimmerActivity.class);
                    videoTrimIntent.putExtra(AppConstants.VIDEO_PATH,selectedVideoPath);
                    startActivityForResult(videoTrimIntent,VIDEO_TRIM_RESULT_CODE);
                }

            }

            else if(requestCode ==VIDEO_TRIM_RESULT_CODE && data != null)
            {

                deleteVideo();
                trimmedVideoPath=data.getStringExtra(AppConstants.TRIMMED_VIDEO_PATH);
                selectedVideoPath=trimmedVideoPath;
                setSelectedVideo();

            }*/
        }
    }


    private void setCategory(String catname,String subcatname)
    {
        if(!catname.equals("") && !subcatname.equals("")) {
            categoryName.setText(catname + "->" + subcatname);
            checkForcompleteData();
        }
    }


    private void setCaptureImage(final String filename)
    {
        if(imageFiles.size()<5) {
            final File file = UtilityClass.getImagePath(filename);
            imageFiles.add(file);
            final View image = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.capture_image_layout, null);
            TextView deleteText = (TextView) image.findViewById(R.id.deleteText);
            ImageView imageView = (ImageView) image.findViewById(R.id.imageView);
            Bitmap bitmap = UtilityClass.getImage(filename);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                imageLayout.addView(image);
                deleteText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageLayout.removeView(image);
                        if (deleteImage(UtilityClass.getImagePath(filename))) {
                            imageFiles.remove(file);
                        }
                    }
                });
            }

            checkForcompleteData();
        }
    }


    private void setSelectedImage(final File imgFile)
    {
            if(imageFiles.size()<5) {

                imageFiles.add(imgFile);
                final View image = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.capture_image_layout, null);
                TextView deleteText = (TextView) image.findViewById(R.id.deleteText);
                ImageView imageView = (ImageView) image.findViewById(R.id.imageView);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(imgFile));
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        imageLayout.addView(image);
                        deleteText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageLayout.removeView(image);
                                    imageFiles.remove(imgFile);


                            }
                        });
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                checkForcompleteData();
            }

    }


    private void setSelectedVideo()
    {
        videoFile=new File(selectedVideoPath);
        if(videoFile!=null) {
            long fileSizeInBytes = videoFile.length();
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = fileSizeInBytes / 1024;
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            float fileSizeInMB = fileSizeInKB / 1024;

            if (fileSizeInMB > 30) {
                videoSizeWarningText.setVisibility(View.VISIBLE);
                videoFile = null;
            } else videoSizeWarningText.setVisibility(View.INVISIBLE);

            videoSizeText.setVisibility(View.VISIBLE);
            if (fileSizeInMB > 0)
                videoSizeText.setText(fileSizeInMB + " MB");
            else videoSizeText.setText(fileSizeInKB + " KB");
            videoThumbImageView.setVisibility(View.VISIBLE);
            remove_video.setVisibility(View.VISIBLE);
            videoThumbImageView.setImageBitmap(UtilityClass.getVideoThumb(selectedVideoPath));

            checkForcompleteData();
        }
    }

    private boolean deleteImage(File file)
    {
        boolean isDelete=false;
        try {
            isDelete= file.delete();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return isDelete;

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }


    private void deleteVideo()
    {
        if(trimmedVideoPath!=null)
            deleteImage(new File(trimmedVideoPath));
    }


    private View.OnClickListener removeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            videoFile=null;
            videoThumbImageView.setVisibility(View.GONE);
            remove_video.setVisibility(View.GONE);
            videoSizeText.setVisibility(View.GONE);
            deleteVideo();
            if(videoSizeWarningText.getVisibility()==View.VISIBLE)
                videoSizeWarningText.setVisibility(View.INVISIBLE);

        }
    };

    private void gotoNext()
    {
        Products products=new Products();
        String images="";
        for(int i=0;i<imageFiles.size();i++)
        {

           if(i<imageFiles.size()-1)images+=imageFiles.get(i).getPath()+",";
            else images+=imageFiles.get(i).getPath();
        }
        products.setImages(images);
        if(videoFile!=null) products.setVideo(videoFile.getPath());
        products.setCategory_id(catId);
        products.setCategory_name(catname);
        products.setSub_category_name(subcatname);
        products.setSub_category_id(subCatId);
        SharedPreferenceClass.setSellProductDetails(context,products);
      startActivity(new Intent(context,SellMoreDetailsActivity.class));

    }

    private void checkForcompleteData()
    {
        if((imageFiles.size()>0 || videoFile!=null) && (catId>0 && subCatId>0))
            continueBtn.setEnabled(true);
        else continueBtn.setEnabled(false);
    }

    }
