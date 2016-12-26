package goodbaby.myapplication.presenter;

import goodbaby.myapplication.model.UserModel;

/**
 * Created by goodbaby on 16/12/21.
 */

public interface LoginActivityCallback {
    void loginSuccess(UserModel userModel);
    void loginFailure();

}
