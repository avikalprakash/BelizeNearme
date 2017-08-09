package belizenearme.infoservices.lue.belize;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import belizenearme.infoservices.lue.belize.camera.MMPermission;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class EditMyProduct extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout titleLayout, descriptionLayout, priceLayout, salesUnitLayout,discountLayout;
    EditText titleEdit, descriptionEdit, priceEdit, salesUnitEdit,discountEdit;
    RelativeLayout locationLayout;
    Button publishBtn;
    Context context;
    String locationName = "";
    String latitude = "";
    String longitude = "";
    String errorMsg = "";
    CoordinatorLayout coordinatorLayout;
    LocationManager mlocManager;
    AlertDialog.Builder alert;
    ProgressDialog progressDialog;
    TextView locationText;
    boolean isLocation;
    MMPermission permission;
    Products products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        alert = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait_for_location));
        titleLayout = (TextInputLayout) findViewById(R.id.titleLayout);
        descriptionLayout = (TextInputLayout) findViewById(R.id.descriptionLayout);
        priceLayout = (TextInputLayout) findViewById(R.id.priceLayout);
        salesUnitLayout = (TextInputLayout) findViewById(R.id.salesUnitLayout);
        discountLayout = (TextInputLayout) findViewById(R.id.discountLayout);

        titleEdit = (EditText) findViewById(R.id.titleEdit);
        descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
        priceEdit = (EditText) findViewById(R.id.priceEdit);
        salesUnitEdit = (EditText) findViewById(R.id.salesUnitEdit);
        discountEdit = (EditText) findViewById(R.id.discountEdit);
        locationLayout = (RelativeLayout) findViewById(R.id.locationLayout);
        publishBtn = (Button) findViewById(R.id.publishBtn);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        locationText= (TextView) findViewById(R.id.locationText);


        publishBtn.setOnClickListener(this);
       // products = SharedPreferenceClass.getSellProductDetails(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
         case   R.id.publishBtn:
             Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_LONG).show();
            break;
        }
    }
    private boolean validate() {
        boolean validate = true;

        if (titleEdit.getText().toString().trim().equals("")) {
            validate = false;
            titleLayout.setError(getString(R.string.enter) + " " + getString(R.string.product_title));
        } else products.setProduct_name(titleEdit.getText().toString().trim());

        if (descriptionEdit.getText().toString().trim().equals("")) {
            validate = false;
            descriptionLayout.setError(getString(R.string.enter) + " " + getString(R.string.product_description));
        } else products.setDescription(descriptionEdit.getText().toString().trim());

        if (priceEdit.getText().toString().trim().equals("")) {
            validate = false;
            priceLayout.setError(getString(R.string.enter) + " " + getString(R.string.price));
        } else products.setPrice(priceEdit.getText().toString().trim());

        if (discountEdit.getText().toString().trim().equals("")) {
            validate = false;
            discountLayout.setError(getString(R.string.enter) + " " + getString(R.string.discount));
        } else products.setDiscount(discountEdit.getText().toString().trim());

        if (salesUnitEdit.getText().toString().trim().equals("")) {
            validate = false;
            salesUnitLayout.setError(getString(R.string.enter) + " " + getString(R.string.sales_unit));
        } else products.setSales_unit(salesUnitEdit.getText().toString().trim());

        if (locationName.equals("") || latitude.equals("") || longitude.equals("")) {
            validate = false;
            errorMsg = getString(R.string.enter) + " " + getString(R.string.location);
            Snackbar.make(coordinatorLayout, errorMsg, Snackbar.LENGTH_SHORT).show();
        }else {
            products.setLatitude(latitude);
            products.setLongitude(longitude);
            products.setLocation(locationName);
        }

        return validate;
    }
}
