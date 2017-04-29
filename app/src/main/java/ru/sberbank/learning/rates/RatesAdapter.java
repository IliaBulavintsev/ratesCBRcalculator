package ru.sberbank.learning.rates;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.sberbank.learning.rates.networking.Currency;


public class RatesAdapter extends BaseAdapter {

    private List<Currency> rates;

    public RatesAdapter (List<Currency> rates) {
        this.rates = rates;
    }


    @Override
    public int getCount() {
        return rates.size();
    }

    @Override
    public Currency getItem(int position) {
        return rates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.rates_item, parent, false);
            ViewHolder holder = new ViewHolder();

            holder.name = (TextView) view.findViewById(R.id.text_name);
            holder.code = (TextView) view.findViewById(R.id.text_code);
            holder.rate = (TextView) view.findViewById(R.id.text_rate);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        Currency rate = getItem(position);

        holder.code.setText(Double.toString(rate.getValue()));
        holder.name.setText(Integer.toString(rate.getNominal().intValue()) + rate.getName());
        holder.rate.setText(rate.getCharCode());

        return view;
    }

    private static class ViewHolder {
        private TextView name;
        private TextView rate;
        private TextView code;
    }


}
