package goodbaby.smartmanufacture.presenter;

import android.content.Context;

import goodbaby.smartmanufacture.model.UserModel;
import goodbaby.smartmanufacture.view.MainActivityInter;

/**
 * Created by goodbaby on 16/12/21.
 */

public class MainActivityPresenter implements MainActivityCallback{

    private MainActivityInter view;
    private UserModel userModel;

    public MainActivityPresenter(MainActivityInter view){
        this.view = view;
        userModel = new UserModel();
    }

    /**
     * 通过model加载数据
     */
    public void loadData(){
        userModel.loadUserData(this);
    }

    @Override
    public void onLoadSuccess() {
        view.updateFirstName(userModel.firstName);
        view.updateLastName(userModel.lastName);
        view.showToastInfo("load success~~");

    }

    @Override
    public void onLoadFailure() {

    }
}
