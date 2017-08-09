package belizenearme.infoservices.lue.belize;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class SignUpCodeVerifyActivity extends AppCompatActivity {

    TextView verifyText;
    TextInputLayout verificationCodeLayout;
    EditText verificationCodeEdit;
    Button verifydBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_code_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);

        initialize();

    }

    private void initialize()
    {
        verificationCodeLayout=(TextInputLayout)findViewById(R.id.verificationCodeLayout);
        verificationCodeEdit=(EditText)findViewById(R.id.verificationCodeEdit);
        verifydBtn=(Button)findViewById(R.id.verifydBtn);
        verifyText=(TextView)findViewById(R.id.verifyText);
    }

}
