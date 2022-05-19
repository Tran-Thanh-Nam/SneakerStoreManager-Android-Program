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

public class AdidasAdapter extends BaseAdapter {

    Context context;
    ArrayList<Sanpham> arrayadidas;

    public AdidasAdapter(Context context, ArrayList<Sanpham> arrayadidas) {
        this.context = context;
        this.arrayadidas = arrayadidas;
    }

    @Override
    public int getCount() {
        return arrayadidas.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayadidas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public  class ViewHolder{
        public TextView txtadidas,txtgiaadidas,txtmotaadidas;
        public ImageView imgadidas;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_adidas,null);
            viewHolder.txtadidas = (TextView) view.findViewById(R.id.textviewadidas);
            viewHolder.txtgiaadidas = (TextView) view.findViewById(R.id.textviewgiaadidas);
            viewHolder.txtmotaadidas = (TextView) view.findViewById(R.id.textviewmotaadidas);
            viewHolder.imgadidas = (ImageView) view.findViewById(R.id.imageviewadidas);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txtadidas.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        viewHolder.txtgiaadidas.setText("Giá: " + decimalFormat.format(sanpham.getGiasanpham())+" Đ");
        viewHolder.txtmotaadidas.setMaxLines(2);
        viewHolder.txtmotaadidas.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotaadidas.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.load)
                .error(R.drawable.error)
                .into(viewHolder.imgadidas);
        return view;
    }
}
