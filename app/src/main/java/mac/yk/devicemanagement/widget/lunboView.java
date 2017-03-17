package mac.yk.devicemanagement.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OkHttpUtils;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class lunboView extends ViewPager{
//    zhishiqiView zhishiView;
    boolean notchange;
    Handler handler;
    Timer mTimer;
    int count=0;
    IModel model;
    Device device;

    boolean Stop=false;

    public boolean isStop() {
        return Stop;
    }

    public  void setStop(boolean stop) {
        Stop = stop;
    }

    boolean cacheOK=false;
    ArrayList<Drawable> Images=new ArrayList<>();
    public lunboView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    class AutoLoopPlayAdapter extends PagerAdapter{

        Context context;
        public AutoLoopPlayAdapter(Context context) {
            this.context=context;
            setListener();
            init();
        }
        private void init() {
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (!notchange&&!Stop){
                        L.e("main","get");
                        setCurrentItem(getCurrentItem()+1);

                    }
                }
            };
        }

        public int getAdCount() {
            return count;
        }


        private void setListener() {
            setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    L.e("main","position:"+position);
//                    zhishiView.setFocus(position%count);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv=new ImageView(context);
            container.addView(iv);
            if (!cacheOK){
                String url=I.REQUEST.SERVER_ROOT+I.REQUEST.DOWNPIC+"&"+I.DEVICE.DNAME+"="+device.getDname()
                        +"&"+I.PIC.PID+"="+position%count+"&"+I.PIC.TYPE+"="+I.PIC.DEVICE;
                L.e("main",url);
                Glide.with(context).load(url)
                        .placeholder(R.drawable.nopic)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.nopic)
                        .into(iv);
                Images.add(position,iv.getDrawable());
//                cacheOK=Images.size()==count?true:false;
            }else {
                iv.setImageDrawable(Images.get(position%count));
                L.e("main", "memory");
            }

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void startPlay(zhishiqiView z){

//        zhishiView=z;
//        zhishiView.setCount(count);
        AutoLoopPlayAdapter adapter=new AutoLoopPlayAdapter(getContext());
        this.setAdapter(adapter);
        notchange=false;
        MyScroller scroller=new MyScroller(getContext(),new LinearInterpolator());
        scroller.setDuration(2000);
        try {
          Field field =ViewPager.class.getField("MyScroller");
            field.setAccessible(true);
            field.set(this, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mTimer=new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },1000,3000);

    }

    public void getPicCount(final zhishiqiView z) {
        device= MyApplication.getDevice();
        model= Model.getInstance();
        model.getCount(getContext(), device.getDname(),I.PIC.DEVICE,new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result.getRetCode()==I.RESULT.SUCCESS){
                    String s= result.getRetData().toString();
                    count= Integer.parseInt(s.substring(0,1));
                    startPlay(z);
                }else {
                    Toast.makeText(getContext(), "服务器没有图片", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyScroller extends Scroller{
        int duration;

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy,this.duration);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                notchange=true;
                break;
            case MotionEvent.ACTION_UP:
                notchange=false;
                break;
        }
        return super.onTouchEvent(ev);
    }

}
