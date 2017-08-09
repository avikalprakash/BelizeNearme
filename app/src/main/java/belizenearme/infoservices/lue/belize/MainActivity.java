package belizenearme.infoservices.lue.belize;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.adapter.ProductListAdapter;
import belizenearme.infoservices.lue.belize.camera.CameraActivity;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.fragments.ChatFragment;
import belizenearme.infoservices.lue.belize.fragments.HomeFragment;
import belizenearme.infoservices.lue.belize.fragments.MyproductFragment;
import belizenearme.infoservices.lue.belize.fragments.NewDealFragment;
import belizenearme.infoservices.lue.belize.fragments.ProductListFragment;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    final int CATEGORY_REQUEST_CODE=1;
    Context context;
    LinearLayout homeBtn,categoryBtn,chatBtn,sellBtn;
    AppCompatImageView homeImageBtn,categoryImageBtn,chatImageBtn;
    AppCompatTextView homeText,categoryText,chatText;
    TextView usernameText,emailText,phoneText;
    CircularImageView userImageVIew;
    View header;
    public LinearLayout controlLayout;
    ScrollView scrollView;
    public static MainActivity mainActivity;
    int catId,subCatId;
    public DrawerLayout drawer;
    public BottomNavigationView navigation;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());

        UtilityClass.setStatusBarColor(this);
        context=this;
        mainActivity=this;


         navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView  navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header=navigationView.getHeaderView(0);
        initialize();
      /* Toast.makeText(getApplicationContext(),
                "User Login Status: " + session.isUserLoggedIn(),
                Toast.LENGTH_LONG).show();*/
        if (session.checkLogin())
            finish();
    }





    private void initialize()
    {


       /* homeBtn=(LinearLayout)findViewById(R.id.homeBtn);
        categoryBtn=(LinearLayout)findViewById(R.id.categoryBtn);
        chatBtn=(LinearLayout)findViewById(R.id.chatBtn);
        sellBtn=(LinearLayout)findViewById(R.id.sellBtn);*/

  /*      homeImageBtn=(AppCompatImageView)findViewById(R.id.homeImageBtn);
        categoryImageBtn=(AppCompatImageView)findViewById(R.id.categoryImageBtn);
        chatImageBtn=(AppCompatImageView)findViewById(R.id.chatImageBtn);
        homeText=(AppCompatTextView)findViewById(R.id.homeText);
        categoryText=(AppCompatTextView)findViewById(R.id.categoryText);
        chatText=(AppCompatTextView)findViewById(R.id.chatText);*/


        usernameText=(TextView)header.findViewById(R.id.usernameText);
        emailText=(TextView)header.findViewById(R.id.emailText);
        userImageVIew=(CircularImageView)header.findViewById(R.id.userImageVIew);
        phoneText=(TextView)header.findViewById(R.id.phoneText);
      //  controlLayout=(LinearLayout) header.findViewById(R.id.controlLayout);
        scrollView=(ScrollView) header.findViewById(R.id.scrollView);
      //  controlLayout=(LinearLayout) findViewById(R.id.controlLayout);



/*        homeBtn.setOnClickListener(this);
        categoryBtn.setOnClickListener(this);
        chatBtn.setOnClickListener(this);
        sellBtn.setOnClickListener(this);*/


        setUserInfo();
        addHomeFragment();

    }









    public void setUserInfo()
    {
        User user= SharedPreferenceClass.getUserInfo(this);
        if(user!=null)
        {
            usernameText.setText(user.getFirst_name()+" "+user.getLast_name());
            if(!user.getEmail().trim().equals("")) emailText.setText(user.getEmail());
            else emailText.setVisibility(View.GONE);
            if(!user.getPhone().trim().equals("")) phoneText.setText(user.getPhone());
            else phoneText.setVisibility(View.GONE);
            UtilityClass.getImage(context,user.getPhoto(),userImageVIew,R.drawable.user_default_image);
        }
    }



    @Override
    public void onClick(View v) {

     /*   switch (v.getId())
        {
            case R.id.homeBtn:
               addHomeFragment();
                break;
            case R.id.categoryBtn:
                startActivityForResult(new Intent(context,CategoryActivity.class),CATEGORY_REQUEST_CODE);
                break;

            case R.id.chatBtn:
                Fragment fragment=new ChatFragment();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame1,fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.sellBtn:
               startActivity(new Intent(context,SellActivity.class));
                break;
        }*/
    }


    private void addHomeFragment()
    {
        Fragment fragment=new HomeFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(AppConstants.CATEGROY_ID,catId);
        bundle.putInt(AppConstants.SUB_CATEGROY_ID,subCatId);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1,fragment);
        fragmentTransaction.commit();
    }

    private void addNewDeal()
    {
        Fragment fragment=new NewDealFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(AppConstants.CATEGROY_ID,catId);
        bundle.putInt(AppConstants.SUB_CATEGROY_ID,subCatId);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1,fragment);
        fragmentTransaction.commit();
    }

    private void myOrder()
    {
        Fragment fragment=new MyproductFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(AppConstants.CATEGROY_ID,catId);
        bundle.putInt(AppConstants.SUB_CATEGROY_ID,subCatId);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            startActivity(new Intent(context, ProfileActivity.class));
        }else if (id == R.id.myproduct){
            myOrder();
        } else if (id == R.id.post) {
            startActivity(new Intent(context,SellActivity.class));
        } else if (id == R.id.newDeal) {
            addNewDeal();
        } else if (id == R.id.inbox) {
            startActivity(new Intent(context, ChatActivity.class));
        } else if (id == R.id.faq) {
            startActivity(new Intent(context, FaqActivity.class));

        } else if (id == R.id.signOut) {
            signOut();

        }else if (id == R.id.settings) {
            startActivity(new Intent(context,SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    addHomeFragment();
                }
            }

        }

    }
    private void signOut()
    {
        //SharedPreferenceClass.setLogin(context,false);
        //startActivity(new Intent(context,WelcomeActivity.class));
        //finish();
        session.logoutUser();
        MainActivity.this.finish();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    addHomeFragment();
                    return true;
                case R.id.navigation_category:
                    startActivityForResult(new Intent(context,CategoryActivity.class),CATEGORY_REQUEST_CODE);
                    return true;
                case R.id.navigation_sell:
                    startActivity(new Intent(context,SellActivity.class));
                    return true;
                case R.id.navigation_chat:
                   startActivity(new Intent(context, ChatActivity.class));
                   /* ChatFragment chatFragment=new ChatFragment();
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame1,chatFragment);
                    fragmentTransaction.commitNowAllowingStateLoss();*/
                    return true;
            }
            return false;
        }

    };
}
