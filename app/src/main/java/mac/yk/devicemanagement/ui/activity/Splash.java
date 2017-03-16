package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.SpUtil;

public class Splash extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);;context=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String name=SpUtil.getLoginUser(context);
                if (name.equals("")){
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    MyApplication.getInstance().setUserName(name);
                    boolean gesture=SpUtil.getGesture(context);
                    if(gesture){
                        MFGT.gotoValidateGestureActivity((Activity) context);
                    }else {
                        gotoMainActivity();
                    }
                }
            }
        }).start();

    }

    private void gotoMainActivity() {
        Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            gotoMainActivity();
        }
    }
}
