package belizenearme.infoservices.lue.belize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import belizenearme.infoservices.lue.belize.fragments.ProductListFragment;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class CategoryActivity extends AppCompatActivity {

    ListView categoryListView,subcategoryListView;
    List<Category> categoryList;
    List<SubCategory> subCategoryList;
    CategoryListAdapter categoryAdapter;
    SubCategoryListAdapter subCategoryListAdapter;
    int catId;
    String catName="";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UtilityClass.setStatusBarColor(this);

        categoryListView=(ListView)findViewById(R.id.categoryListView);
        subcategoryListView=(ListView)findViewById(R.id.subcategoryListView);


        getCategories();


        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                catId=categoryList.get(position).getId();
                catName=categoryList.get(position).getCategory_name();
                getSubCategories(String.valueOf(catId));
            }
        });

        subcategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle=new Bundle();
                bundle.putInt(AppConstants.CATEGROY_ID,catId);
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


    private void getCategories()
    {
        new DownloadThread(this, Urls.getCategory,null, new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {
                        categoryList=new ArrayList<Category>();
                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            categoryList.add(new Category(jsonArray.getJSONObject(i)));
                        }
                        categoryAdapter=new CategoryListAdapter(context,R.layout.category_view,categoryList);
                        categoryListView.setAdapter(categoryAdapter);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
