package goodbaby.smartmanufacture.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import goodbaby.smartmanufacture.R;
import goodbaby.smartmanufacture.custom.CircleProgressView;

/**
 * Created by goodbaby on 17/2/15.
 */

public class TestActivity extends AppCompatActivity {
    private CircleProgressView circleProgressView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_test);
        circleProgressView = (CircleProgressView)findViewById(R.id.circleProgressView);
    }
}
