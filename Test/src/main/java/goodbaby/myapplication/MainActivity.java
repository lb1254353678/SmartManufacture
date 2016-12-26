package goodbaby.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */

    public static final String saveFileName = "/GBFile/apk";
    // 文件存储
    private File updateDir = null;
    private File updateFile = null;

    private Button get_request,get_request_asyn,post_request,post_request_asyn;
    private OkHttpClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        get_request = (Button)findViewById(R.id.get_request);
        get_request_asyn = (Button)findViewById(R.id.get_request_asyn);
        post_request = (Button)findViewById(R.id.post_request);
        post_request_asyn = (Button)findViewById(R.id.post_request_asyn);

        get_request.setOnClickListener(this);
        get_request_asyn.setOnClickListener(this);
        post_request.setOnClickListener(this);
        post_request_asyn.setOnClickListener(this);

        client = new OkHttpClient();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_request:
                getRequestFunct();
            break;
            case R.id.get_request_asyn:
                getRequestAsyn();
            break;
            case R.id.post_request:
            break;
            case R.id.post_request_asyn:
            break;

        }
    }

    //同步get方式提交
    private void getRequestFunct() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://apicloud.mob.com/v1/weather/query?key=146d30f8f3b93&city=长沙&province=湖南";
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String string = response.body().string();
                        Log.e("****","success code:" + response.code());
                        Log.e("*****","response:" + string);
                    }else{
                        Log.e("*****","getRequest is request error");
                        Log.e("****","code:" + response.code());

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getRequestAsyn(){
        try {
            Log.e("wxy","main thread id is "+Thread.currentThread().getId());
            String url = "https://www.pgyer.com/apiv1/app/install?aId=7a47b3852b77668e85ca16dadedad4df&_api_key=93feb168f72e2891daf95f4b0a6772f5";
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("*****","" + e.getMessage());
                    //Log.e("######","" + call.request().);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                        updateDir = new File(Environment.getExternalStorageDirectory() , saveFileName);
                        updateFile = new File(updateDir. getPath() , getString(R.string.app_name) + ".apk");
                        //file = new File(Environment.getExternalStorageDirectory() + saveFileName, getString(R.string.app_name) + ".apk");
                        if (!updateDir.exists()) {
                            updateDir.mkdirs();
                        }
                        if (!updateFile.exists()) {
                            updateFile.createNewFile();
                        }
                    }

                    InputStream is = null;
                    FileOutputStream fos = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    try {
                        long total = response.body().contentLength();
                        long current = 0;
                        is = response.body().byteStream();
                        fos = new FileOutputStream(updateFile);
                        while ((len = is.read(buf)) != -1){
                            current += len;
                            Log.e("*****","current:" + current);
                            fos.write(buf,0,len);
                        }
                        fos.flush();

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        try {
                            if(is != null){
                                is.close();
                            }
                            if(fos != null){
                                fos.close();
                            }
                        }catch (Exception e){

                        }
                    }

//                    InputStream is = null;
//                    byte[] buf = new byte[2048];
//                    int len = 0;
//                    FileOutputStream fos = null;
//                    try {
//                        long total = response.body().contentLength();
//                        Log.e(TAG, "total------>" + total);
//                        long current = 0;
//                        is = response.body().byteStream();
//                        fos = new FileOutputStream(file);
//                        while ((len = is.read(buf)) != -1) {
//                            current += len;
//                            fos.write(buf, 0, len);
//                            Log.e(TAG, "current------>" + current);
//                            progressCallBack(total, current, callBack);
//                        }
//                        fos.flush();
//                        successCallBack((T) file, callBack);
//                    } catch (IOException e) {
//                        Log.e(TAG, e.toString());
//                        failedCallBack("下载失败", callBack);
//                    } finally {
//                        try {
//                            if (is != null) {
//                                is.close();
//                            }
//                            if (fos != null) {
//                                fos.close();
//                            }
//                        } catch (IOException e) {
//                            Log.e(TAG, e.toString());
//                        }
//                    }
                }

            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

