package belizenearme.infoservices.lue.belize;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.adapter.CategoryListAdapter;
import belizenearme.infoservices.lue.belize.adapter.SubCategoryListAdapter;
import belizenearme.infoservices.lue.belize.databind.Category;
import belizenearme.infoservices.lue.belize.databind.SubCategory;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class SubCategoryActivity extends AppCompatActivity {
    ListView categoryListView,subcategoryListView;
    List<Category> categoryList;
    List<SubCategory> subCategoryList;
    CategoryListAdapter categoryAdapter;
    SubCategoryListAdapter subCategoryListAdapter;
    int catId;
    int category_id=0;
    String catName="";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UtilityClass.setStatusBarColor(this);

        categoryListView=(ListView)findViewById(R.id.categoryListView);
        subcategoryListView=(ListView)findViewById(R.id.subcategoryListView);
        category_id= getIntent().getIntExtra("category_id",0);
        getSubCategories(String.valueOf(category_id));
        subcategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle=new Bundle();
                bundle.putInt(AppConstants.CATEGROY_ID,category_id);
                bundle.putInt(AppConstants.SUB_CATEGROY_ID,subCategoryList.get(position).getId());
                bundle.putString(AppConstants.CATEGORY_NAME,catName);
                bundle.putString(AppConstants.SUBCATEGORY_NAME,subCategoryList.get(position).getSub_category_name());
                Intent mainIntent=new Intent(context,MainActivity.class);
                mainIntent.putExtras(bundle);
                setResult(RESULT_OK,mainIntent);
                finish();
            }
        });
    }
    private void getSubCategories(String catId)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.accumulate("category_id",catId);
        } catch (JSONException e) {}
        new DownloadThread(this, Urls.getSubCategory,jsonObject.toString(), new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {
                        subCategoryList=new ArrayList<SubCategory>();
                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            subCategoryList.add(new SubCategory(jsonArray.getJSONObject(i)));
                        }
                        subCategoryListAdapter=new SubCategoryListAdapter(context,R.layout.category_view,subCategoryList);
                        subcategoryListView.setAdapter(subCategoryListAdapter);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
