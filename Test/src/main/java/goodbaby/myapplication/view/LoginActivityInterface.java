package goodbaby.myapplication.view;

import goodbaby.myapplication.model.UserModel;

/**
 * Created by goodbaby on 16/12/21.
 */

public interface LoginActivityInterface {

    String getUserName();
    String getPassword();

    void clearUserName();
    void clearPassword();

    void userNameEmptyError();
    void passwordEmptyError();

    void toMainActivity(UserModel userModel);
    void loginFailure();
}
