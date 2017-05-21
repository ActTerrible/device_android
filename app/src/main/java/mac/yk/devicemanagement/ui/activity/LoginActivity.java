package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.MyMemory;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.db.dbUser;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity{
    Context context;
    @BindView(R.id.userName)
    EditText name;
    @BindView(R.id.passwd)
    EditText passwd;

    ProgressDialog progressDialog;

    String TAG="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        progressDialog = new ProgressDialog(context);

    }
     Observer<User> observer=new Observer<User>() {
         @Override
         public void onCompleted() {

         }

         @Override
         public void onError(Throwable e) {
             progressDialog.dismiss();
             if (ExceptionFilter.filter(context,e)){
                 Toast.makeText(LoginActivity.this, "密码或者用户名错误!" , Toast.LENGTH_SHORT).show();
             }
         }

         @Override
         public void onNext(User user) {
             progressDialog.dismiss();
             ToastUtil.showToast(context,"登陆成功！");
             dbUser db=dbUser.getInstance(context);
               if (!db.select1(user.getAccounts())){
                   L.e(TAG,"execute");
                  if (db.insert(user)){
                      L.e(TAG,"insert suc");
                  }
               }
                Intent intent = new Intent(context, MainActivity.class);
                 MyMemory.getInstance().setUser(user);
             SpUtil.saveLoginUser(context,name.getText().toString());
             startActivity(intent);
             MFGT.finish((Activity) context);

         }
     };


    public void onLogin(View view) {
        progressDialog.show();
        ApiWrapper<ServerAPI> ApiWrapper =new ApiWrapper<>();
        subscription= ApiWrapper.targetClass(ServerAPI.class).getAPI().login(name.getText().toString(),
                passwd.getText().toString()).compose(ApiWrapper.<User>applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
}
