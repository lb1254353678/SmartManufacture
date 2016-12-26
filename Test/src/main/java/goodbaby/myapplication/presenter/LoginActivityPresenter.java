package goodbaby.myapplication.presenter;

import android.os.Handler;

import goodbaby.myapplication.model.UserModel;
import goodbaby.myapplication.view.LoginActivityInterface;

/**
 * Created by goodbaby on 16/12/21.
 */

public class LoginActivityPresenter {
    private LoginActivityInterface view;
    private UserModel userModel;
    Handler myHandler = new Handler();

    public LoginActivityPresenter(LoginActivityInterface view){
        this.view = view;
        userModel = new UserModel();
    }

    public void login(){
        if(view.getUserName().isEmpty()){
            view.userNameEmptyError();
            return;
        }
        if (view.getPassword().isEmpty()){
            view.passwordEmptyError();
            return;
        }
        userModel.login(view.getUserName(), view.getPassword(), new LoginActivityCallback() {
            @Override
            public void loginSuccess(final UserModel userModel) {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.toMainActivity(userModel);
                    }
                });
            }

            @Override
            public void loginFailure() {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.loginFailure();
                    }
                });
            }
        });
    }

    public void clear(){
        view.clearUserName();
        view.clearPassword();
    }

}
