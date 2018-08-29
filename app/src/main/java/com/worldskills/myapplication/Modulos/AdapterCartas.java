package com.worldskills.myapplication.Modulos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.worldskills.myapplication.R;

import java.util.ArrayList;

public class AdapterCartas extends BaseAdapter {

    private Context context;
    private int itemLayout;
    private ArrayList<ItemCarta> cartas;

    private final int[] FIGURAS={
            R.drawable.i1,
            R.drawable.i2,
            R.drawable.i3,
            R.drawable.i4,
            R.drawable.i5,
            R.drawable.i6,
            R.drawable.i7,
            R.drawable.i8
    };

    public AdapterCartas(Context context, int itemLayout, ArrayList<ItemCarta> cartas) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.cartas = cartas;
    }

    @Override
    public int getCount() {
        return cartas.size();
    }

    @Override
    public Object getItem(int position) {
        return cartas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        Holder holder=new Holder();

        if (row == null) {

            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(itemLayout,null);

            holder.figura=row.findViewById(R.id.item_carta_figura);
            holder.fondoTapar=row.findViewById(R.id.item_carta_fondo_tapar);

            row.setTag(holder);


        }else {
            holder=(Holder)row.getTag();
        }
        ItemCarta carta=cartas.get(position);

        holder.figura.setImageResource(FIGURAS[carta.getNumero()]);
        holder.fondoTapar.setBackgroundResource(carta.getFondoTapar());

        return row;
    }

    class Holder {
        ImageView figura, fondoTapar;
    }
}
