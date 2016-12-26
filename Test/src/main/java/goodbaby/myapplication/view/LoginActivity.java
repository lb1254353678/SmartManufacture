package goodbaby.myapplication.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import goodbaby.myapplication.R;
import goodbaby.myapplication.model.UserModel;
import goodbaby.myapplication.presenter.LoginActivityPresenter;

/**
 * Created by goodbaby on 16/12/21.
 */

public class LoginActivity extends AppCompatActivity implements LoginActivityInterface,View.OnClickListener{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button btn_login,btn_clear;

    private LoginActivityPresenter loginActivityPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView)findViewById(R.id.email);
        mPasswordView = (EditText)findViewById(R.id.password);
        btn_login = (Button)findViewById(R.id.email_sign_in_button);
        btn_clear = (Button)findViewById(R.id.clear);
        btn_login.setOnClickListener(this);
        btn_clear.setOnClickListener(this);

        loginActivityPresenter = new LoginActivityPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.email_sign_in_button:
                //loginActivityPresenter.login();
                new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
                        .setMessage("发现新版本,是否升级该版本？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //startDownloadTask(LoginActivity.this,appBean.getDownloadURL());
                                finish();
                            }
                        }).show();
                break;
            case R.id.clear:
                loginActivityPresenter.clear();

                break;

        }
    }


    @Override
    public String getUserName() {
        return mEmailView.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPasswordView.getText().toString();
    }

    @Override
    public void clearUserName() {
        mEmailView.setText("");
        mEmailView.setFocusable(true);
    }

    @Override
    public void clearPassword() {
        mPasswordView.setText("");
        mEmailView.setFocusable(true);
    }

    @Override
    public void userNameEmptyError() {
        Toast.makeText(LoginActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void passwordEmptyError() {
        Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toMainActivity(UserModel userModel) {
        Toast.makeText(LoginActivity.this,"Login Success,to MainActivity",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFailure() {
        Toast.makeText(LoginActivity.this,"Login Failure.",Toast.LENGTH_SHORT).show();
    }



}
