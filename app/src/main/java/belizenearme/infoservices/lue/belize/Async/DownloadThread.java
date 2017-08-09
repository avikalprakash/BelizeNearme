package belizenearme.infoservices.lue.belize.Async;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.os.AsyncTask;

import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;


public class DownloadThread extends AsyncTask<String, Void, String> {
    Activity context;
    private String response="", jsonEntity, url;
    String id="";
    private ProgressDialog dialog;
    String msg = null;
    Dialog d;
    public AsyncResponse delegate = null;
    ProgressDialog pd;
    boolean isDialog;

    public interface AsyncResponse {
        void processFinish(String output);
    }
    public DownloadThread(Activity context, String url, String jsonEntity, AsyncResponse delegate,boolean isDialog) {
        this.id= id;
        this.url=url;
        this.jsonEntity=jsonEntity;
        this.context=context;
        this.delegate=delegate;
        this.url=url;
        this.isDialog=isDialog;
    }

    @Override
    protected void onPreExecute() {
        if(isDialog) {
            pd = new ProgressDialog(context, R.style.ProgressDialogTheme);
            pd.setCancelable(false);
            pd.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.my_progress_indeterminate));
            pd.setMessage(context.getString(R.string.loading));
            pd.show();
        }
    }
    @Override
    protected String doInBackground(String... params){
        try {
            if(UtilityClass.isOnline(context)){
                if (jsonEntity != null)
                    response = WebHandler.callByPost(jsonEntity, url);
                else response = WebHandler.callByGet(url);
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getMessage();
            return response= null;
        }
        return response==null?"": response;
    }
    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
       if(isDialog) pd.dismiss();
    }
}

