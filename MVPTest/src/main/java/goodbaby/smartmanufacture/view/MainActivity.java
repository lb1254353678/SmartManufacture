package goodbaby.smartmanufacture.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import goodbaby.smartmanufacture.R;
import goodbaby.smartmanufacture.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements MainActivityInter,View.OnClickListener{
    private TextView tv_first_name,tv_last_name;
    private Button btn_load;
    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_first_name = (TextView)findViewById(R.id.tv_first_name);
        tv_last_name = (TextView)findViewById(R.id.tv_last_name);
        btn_load = (Button)findViewById(R.id.btn_load);
        btn_load.setOnClickListener(this);

        mainActivityPresenter = new MainActivityPresenter(this);
    }

    @Override
    public void onClick(View v) {
        mainActivityPresenter.loadData();
    }

    @Override
    public void updateFirstName(String string) {
        tv_first_name.setText(string);
    }

    @Override
    public void updateLastName(String string) {
        tv_last_name.setText(string);
    }

    @Override
    public void showToastInfo(String toast) {
        Toast.makeText(MainActivity.this,toast,Toast.LENGTH_SHORT).show();
    }


}
