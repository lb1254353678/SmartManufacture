package goodbaby.smartmanufacture.model;

import goodbaby.smartmanufacture.presenter.MainActivityCallback;

/**
 * Created by goodbaby on 16/12/21.
 */

public class UserModel {
    public String firstName;
    public String lastName;


    public UserModel(){
        this.firstName = "";
        this.lastName = "";
    }

    public void loadUserData(MainActivityCallback callback){
        //模拟网络请求的数据
        this.firstName = "Jack";
        this.lastName = "Rose";
        callback.onLoadSuccess();
    }
}
