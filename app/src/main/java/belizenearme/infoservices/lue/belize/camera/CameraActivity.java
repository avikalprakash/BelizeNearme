package belizenearme.infoservices.lue.belize.camera;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;


public class CameraActivity extends Activity implements View.OnClickListener,View.OnTouchListener, Camera.AutoFocusCallback{

    MMPermission permission;
    boolean init;
    int width, height;
    ImageButton settingBtn;
    LinearLayout progressbarLayout;

    FrameLayout preview;
    Context context;
    Button btnCapture;
    private Camera mCamera;
    private CameraPreview mPreview;
    AlertDialog.Builder alert;
    private static final String TAG = "MyActivity";
    private static byte[] CompressedImageByteArray;
    private static Bitmap CompressedImage;
    Chronometer chronometer;
    TextView cameraResolText;
    int paramIndex;
    ImageButton btnCamType;
    boolean backCam=true;
    int camType;
    List<PixelData> pixelDataList;
    public static boolean meteringAreaSupported;
    int focusAreaSize=100;
    FocusRectangle focusRectangle;


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        init = false;
        permission = new MMPermission(this, Manifest.permission.CAMERA);
        if (permission.result == -1 || permission.result == 0) {
            try {
                if (!init) initializeCamera(camType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (permission.result == 1) {
            if (!init) initializeCamera(camType);
        }
        super.onResume();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        UtilityClass.setStatusBarColor(this);
        context = this;
        settingBtn = (ImageButton) findViewById(R.id.settingBtn);
        btnCapture = (Button) findViewById(R.id.btnCapture);
        cameraResolText = (TextView) findViewById(R.id.cameraResolText);
        btnCamType=(ImageButton)findViewById(R.id.btnCamType);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        progressbarLayout = (LinearLayout) findViewById(R.id.progressbarLayout);

        camType= CameraInfo.CAMERA_FACING_BACK;
        settingBtn.setOnClickListener(this);
        btnCapture.setOnClickListener(this);
        btnCamType.setOnClickListener(this);

        preview.setOnTouchListener(this);


    }


    private List<PixelData> getCameraConfig() {
      List<PixelData> pixelDatas=new ArrayList<>();
        if (mCamera != null) {
            Parameters param = mCamera.getParameters();
            List<Size>  sizes = param.getSupportedPictureSizes();
            for (int i = 0; i < sizes.size(); i++) {
                int  mp=(Math.round((float)(sizes.get(i).width*sizes.get(i).height)/1000000));
                if((mp/2>0) && (mp%2==0))
                {
                    String res=mp+" MP";
                    int j;
                    for( j=0;j<pixelDatas.size();j++)
                        if(res.equals(pixelDatas.get(j).getPiexelName())) break;
                    if(j==pixelDatas.size())
                    {
                         pixelDatas.add(new PixelData(sizes.get(i),res));
                    }


                }
            }



        }
        return pixelDatas;
    }

    private void setParams(int index) {
        Parameters param = mCamera.getParameters();

        List<String> focusModes = param.getSupportedFocusModes();
        if (focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            param.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        param.setPictureSize(pixelDataList.get(index).getSize().width,
                pixelDataList.get(index).getSize().height);
        if(camType== CameraInfo.CAMERA_FACING_BACK) {
            mCamera.setParameters(param);
        }
    }

    private void initializeCamera(int camType) {
        init = true;
         mCamera = getCameraInstance(camType);
        if (mCamera != null) {
      if(camType==CameraInfo.CAMERA_FACING_FRONT) btnCapture.setEnabled(true);
            pixelDataList=getCameraConfig();
            if (pixelDataList.size() > 0)
                cameraResolText.setText(pixelDataList.get(paramIndex).getPiexelName());
            setParams(paramIndex);
            alert = new AlertDialog.Builder(this);
            Display getOrient = getWindowManager().getDefaultDisplay();
            int rotation = getOrient.getRotation();
            switch (rotation) {
                case Surface.ROTATION_0:
                    mCamera.setDisplayOrientation(90);
                    break;
                case Surface.ROTATION_90:
                    break;
                case Surface.ROTATION_180:
                    break;
                case Surface.ROTATION_270:
                    mCamera.setDisplayOrientation(90);
                    break;
                default:
                    break;
            }
            try {

                mPreview = new CameraPreview(this, mCamera);

                preview.addView(mPreview);

            } catch (Exception e) {
                finish();
            }

        }
    }


    public static Camera getCameraInstance(int cameraType) {
        // Camera c = null;
        try {

            int numberOfCameras = Camera.getNumberOfCameras();
            int cameraId = 0;
            for (int i = 0; i < numberOfCameras; i++) {
                CameraInfo info = new CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == cameraType) {
                    // Log.d(DEBUG_TAG, "Camera found");
                    cameraId = i;
                    break;
                }
            }
            return Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            return null;
        }
    }


    PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {

                progressbarLayout.setVisibility(View.VISIBLE);
                Bitmap bmp = BitmapFactory
                        .decodeByteArray(data, 0, data.length);
                Matrix mat = new Matrix();
                if(camType== CameraInfo.CAMERA_FACING_FRONT)
                {
                    mat.postRotate(-90);
                }
                else mat.postRotate(90);
                Bitmap bMapRotate;
                bMapRotate= Bitmap.createBitmap(bmp, 0, 0,
                        bmp.getWidth(), bmp.getHeight(), mat, true);
                setCameraImage(bMapRotate);

            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
        }
    };

    ShutterCallback shutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };
    /**
     * Handles data for raw picture
     */
    PictureCallback rawCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    public void onCaptureClick() {
        if (mCamera != null)
            mCamera.takePicture(shutterCallback, rawCallback, mPicture);
    }

    private void setCameraImage(Bitmap bitmap) {

        Display getOrient = getWindowManager().getDefaultDisplay();
        int rotation = getOrient.getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                break;
            case Surface.ROTATION_270:
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                break;
            case Surface.ROTATION_90:
                break;
            case Surface.ROTATION_180:
                break;
            default:
                break;
        }

        String fileName = "";

        try {
            fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";

            saveImage(bitmap, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.recycle();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(AppConstants.FILENAME, fileName);
        setResult(RESULT_OK, returnIntent);
        finish();

    }


    public void saveImage(Bitmap image, String imagename) throws FileNotFoundException {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), AppConstants.FOLDERNAME);
        String fileName = imagename;
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("App", "failed to create directory");
            }
        } else {

            File file = new File(mediaStorageDir.getAbsolutePath() + File.separator + fileName);
            FileOutputStream fo = new FileOutputStream(file);
            if (image != null) {
                image.compress(Bitmap.CompressFormat.JPEG, 50, fo);
            }
        }


    }


    public void showSortMenu(final List<PixelData> pixelDataList) {
        if (pixelDataList != null) {
            PopupMenu popup = new PopupMenu(context, settingBtn);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.menu_camera_settings, popup.getMenu());
            int i = 1;
            for (PixelData pixelData : pixelDataList) {
                popup.getMenu().add(0, i, 0, pixelData.getPiexelName());
                i++;
            }
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() != R.id.camset) {
                        int i = item.getItemId()-1;
                        if (i >=0 && i <pixelDataList.size()) {
                            cameraResolText.setText(pixelDataList.get(i).getPiexelName());
                            paramIndex = i;
                            preview.removeAllViews();
                            initializeCamera(camType);
                        }
                        return true;
                    } else return false;

                }
            });

            popup.show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.settingBtn:

                showSortMenu(getCameraConfig());
                break;

            case R.id.btnCapture:
          onCaptureClick();
                break;

            case R.id.btnCamType:

                if(mCamera!=null){
                    mCamera.stopPreview();
                }
                if(camType== CameraInfo.CAMERA_FACING_BACK)
                {
                    camType= CameraInfo.CAMERA_FACING_FRONT;
                    paramIndex=0;

                }
                else
                {
                    camType= CameraInfo.CAMERA_FACING_BACK;
                    paramIndex=0;

                }
                preview.removeAllViews();
                initializeCamera(camType);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(camType== CameraInfo.CAMERA_FACING_BACK)  focusOnTouch(event);
        return false;
    }

    protected void focusOnTouch(MotionEvent event) {

        try {
            if (mCamera != null) {
                mCamera.cancelAutoFocus();
                Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f);
                Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f);

                Parameters parameters = mCamera.getParameters();
                parameters.setFocusMode(Parameters.FLASH_MODE_AUTO);
                List<Camera.Area> areaList = new ArrayList<>();
                areaList.add(new Camera.Area(focusRect, 100));
                parameters.setFocusAreas(areaList);
                areaList.clear();
                areaList.add(new Camera.Area(meteringRect, 100));
                if (meteringAreaSupported) {
                    parameters.setMeteringAreas(areaList);
                }

                mCamera.setParameters(parameters);
                mCamera.autoFocus(this);

                try {
                    preview.removeView(focusRectangle);
                } catch (Exception e) {
                }
                ;
                focusRectangle = new FocusRectangle(context, focusRect);
                focusRectangle.setFocused(false);

                preview.addView(focusRectangle);

            }
        }catch (Exception e){}
    }


    private class FocusRectangle extends View{
        Rect focusRect;
        int color=Color.WHITE;
        public FocusRectangle(Context context,Rect focusRect) {
            super(context);
            this.focusRect=focusRect;
        }

        public void setFocused(boolean focused)
        {
           if(focused) this.color=Color.GREEN;
            else this.color=Color.WHITE;
        }


        @Override
        protected void onDraw(Canvas canvas) {

            Paint myPaint = new Paint();
           myPaint.setColor(color);
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setStrokeWidth(5);
          //  canvas.drawRect(focusRect.left,focusRect.top, focusRect.right,focusRect.bottom, myPaint);
            int cx=(focusRect.left+focusRect.right)/2;
            int cy=(focusRect.top+focusRect.bottom)/2;
            int r=focusRect.right-focusRect.left;
            canvas.drawCircle(cx,cy,r,myPaint);
        }
    }

    private Rect calculateTapArea(float x, float y, float coefficient) {
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

        int left = clamp((int) x - areaSize / 2, 0, mPreview.getWidth() - areaSize);
        int top = clamp((int) y - areaSize / 2, 0, mPreview.getHeight() - areaSize);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);

       // matrix.mapRect(rectF);


        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if(success)
        {
            try {
                btnCapture.setEnabled(true);
                preview.removeView(focusRectangle);
                focusRectangle.setFocused(true);
                preview.addView(focusRectangle);
                camera.cancelAutoFocus();
            }catch (Exception e){}
        }
        else {

            try {
                btnCapture.setEnabled(false);
                preview.removeView(focusRectangle);
                focusRectangle.setFocused(false);
                preview.addView(focusRectangle);
            }catch (Exception e){}
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
