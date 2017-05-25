package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.ViewPagerAdapter;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class CountFragment extends BaseFragment {
    @BindView(R.id.vp)
    ViewPager vp;
    ViewPagerAdapter ViewPagerAdapter;

    User user;
    @BindView(R.id.select)
    LinearLayout selectL;

    TotalCountFragment TotalCountFragment;
    StatusCountFragment StatusCountFragment;
    ServiceCountFragment ServiceCountFragment;

    String TAG = "CountFragment";
    @BindView(R.id.service_count)
    TextView serviceCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count_detail, container, false);
        ButterKnife.bind(this, view);
        user = MyMemory.getInstance().getUser();
        setUpViewPager();
        return view;
    }

    private void setUpViewPager() {
        L.e(TAG, "set pager");
        ViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        if (user.getGrade() == 2) {
            ViewPagerAdapter.addFragment(new ServiceCountFragment(), "维修统计");
            selectL.setVisibility(View.GONE);
        } else {
            TotalCountFragment = new TotalCountFragment();
            StatusCountFragment = new StatusCountFragment();
            ViewPagerAdapter.addFragment(TotalCountFragment, "统计分析");
            ViewPagerAdapter.addFragment(StatusCountFragment, "状态统计");
            serviceCount.setVisibility(View.GONE);
            if (user.getGrade() == 0) {
                ServiceCountFragment = new ServiceCountFragment();
                ViewPagerAdapter.addFragment(ServiceCountFragment, "维修点统计");
                serviceCount.setVisibility(View.VISIBLE);
            }
        }
        vp.setAdapter(ViewPagerAdapter);
    }

    @OnClick({R.id.total_count, R.id.status_count, R.id.service_count})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_count:
                vp.setCurrentItem(0);
                break;
            case R.id.status_count:
                vp.setCurrentItem(1);
                break;
            case R.id.service_count:
                vp.setCurrentItem(2);
                break;
        }
    }


}
