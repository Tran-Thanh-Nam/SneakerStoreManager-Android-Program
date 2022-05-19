package com.hiu.vn.bangiayonline.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiu.vn.bangiayonline.R;
import com.hiu.vn.bangiayonline.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NikeAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraynike;

    public NikeAdapter(Context context, ArrayList<Sanpham> arraynike) {
        this.context = context;
        this.arraynike = arraynike;
    }

    @Override
    public int getCount() {
        return arraynike.size();
    }

    @Override
    public Object getItem(int i) {
        return arraynike.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public  class ViewHolder{
        public TextView txtnike,txtgianike,txtmotanike;
        public ImageView imgnike;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_nike,null);
            viewHolder.txtnike = (TextView) view.findViewById(R.id.textviewnike);
            viewHolder.txtgianike = (TextView) view.findViewById(R.id.textviewgianike);
            viewHolder.txtmotanike = (TextView) view.findViewById(R.id.textviewmotanike);
            viewHolder.imgnike = (ImageView) view.findViewById(R.id.imageviewnike);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txtnike.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        viewHolder.txtgianike.setText("Giá: " + decimalFormat.format(sanpham.getGiasanpham())+" Đ");
        viewHolder.txtmotanike.setMaxLines(2);
        viewHolder.txtmotanike.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotanike.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.load)
                .error(R.drawable.error)
                .into(viewHolder.imgnike);
        return view;
    }
}
