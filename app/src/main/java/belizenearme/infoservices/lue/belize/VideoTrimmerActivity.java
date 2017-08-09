package belizenearme.infoservices.lue.belize;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;
import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class VideoTrimmerActivity extends AppCompatActivity implements OnTrimVideoListener,OnK4LVideoListener {

    K4LVideoTrimmer videoTrimmer;
    private ProgressDialog mProgressDialog;
    String videoPath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_trimmer);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.toolbar_color));
        UtilityClass.setStatusBarColor(this);
        videoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.trimming_progress));
        try
        {
            videoPath=getIntent().getStringExtra(AppConstants.VIDEO_PATH);
            if (videoTrimmer != null) {
                videoTrimmer.setDestinationPath(UtilityClass.getImageFolderPath(AppConstants.FOLDERNAME));
                videoTrimmer.setVideoURI(Uri.parse(videoPath));
                videoTrimmer.setMaxDuration(30);
                videoTrimmer.setOnTrimVideoListener(this);
                videoTrimmer.setVideoInformationVisibility(true);



            }
        }catch (Exception e){}
    }


    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(Uri uri) {
        mProgressDialog.cancel();
        Intent resultIntent=new Intent();
        resultIntent.putExtra(AppConstants.TRIMMED_VIDEO_PATH,uri.getPath());
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        videoTrimmer.destroy();
        finish();
    }

    @Override
    public void onError(String message) {

    }


    @Override
    public void onVideoPrepared() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
