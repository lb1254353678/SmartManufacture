package goodbaby.myapplication.model;

import goodbaby.myapplication.presenter.LoginActivityCallback;

/**
 * Created by goodbaby on 16/12/21.
 */

public class UserModel {
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login(final String userName, final String password, final LoginActivityCallback callback){
     //模拟登录操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(userName.equals("111") && password.equals("123")){
                    UserModel userModel = new UserModel();
                    userModel.setUserName(userName);
                    userModel.setPassword(password);
                    callback.loginSuccess(userModel);
                }else {
                    callback.loginFailure();
                }
            }
        }).start();

    }
}
