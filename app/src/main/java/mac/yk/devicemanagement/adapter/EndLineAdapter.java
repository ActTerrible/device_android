package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.EndLine;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/7/18.
 */

public class EndLineAdapter extends RecyclerView.Adapter<EndLineAdapter.LineHolder> {
    Context context;
    ArrayList<EndLine> lines;



    public EndLineAdapter(Context context, ArrayList<EndLine> lines) {
        this.context = context;
        this.lines = lines;
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_end_line, parent, false);
        LineHolder holder = new LineHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LineHolder holder, int position) {

        EndLine line = lines.get(position);
            if (checkStatus(line) == false) {
                L.e("cao", "red");
                holder.ll.setBackgroundColor(context.getResources().getColor(R.color.red));
            }

        holder.id.setText(position + 1 + "");
        holder.sensor.setText(convert(line.getSensor()));
        holder.battery.setText(convert(line.getBattery()));
        holder.time.setText("数据上传时间:" + ConvertUtils.Date2String(new Date(line.getTime())));
        holder.radio.setText(convert(line.getRadio_station()));
        holder.temperature.setText(line.getTemperature() + "");
    }

    private boolean checkStatus(EndLine line) {
        if (line.getBattery() == 0 || line.getSensor() == 0 || line.getRadio_station() == 0) {
            L.e("cao", "false");
            return false;
        } else {
            return true;
        }
    }

    String convert(int i) {
        if (i == 1) {
            return "√";
        } else {
            return "×";
        }
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    public class LineHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id)
        public TextView id;
        @BindView(R.id.temperature)
        TextView temperature;
        @BindView(R.id.sensor)
        TextView sensor;
        @BindView(R.id.radio)
        TextView radio;
        @BindView(R.id.battery)
        TextView battery;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.ll)
        LinearLayout ll;

        public LineHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
