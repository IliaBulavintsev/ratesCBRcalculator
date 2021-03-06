package ru.sberbank.learning.rates;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ru.sberbank.learning.rates.networking.CurrenciesList;
import ru.sberbank.learning.rates.networking.Currency;
import ru.sberbank.learning.rates.storage.CurrenciesStorage;

import static android.content.ContentValues.TAG;

public class RatesActivity extends Activity {

    private ListView ratesListView;
    private RatesAdapter ratesAdapter;
    private LoadRates loadRates;
    private TextView stubTextView;
    private CurrenciesStorage currenciesStorage;
    private TextView noData;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        ratesListView = (ListView) findViewById(R.id.rates_list);
        stubTextView = (TextView) findViewById(R.id.text_stub);
        noData = (TextView) findViewById(R.id.text_no_data);
        refreshButton = (Button) findViewById(R.id.button_refresh);

        currenciesStorage = ((Storage)getApplication()).getStorage();

        if (!currenciesStorage.isReady()){
            Log.e(TAG, "onCreate: without Cache!");
            loadRates = new LoadRates();
            loadRates.execute();
        } else {
            Log.e(TAG, "onCreate: with Cache!");
            settingAdapter();
            setClickListener();
        }

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadingFrame();
                loadRates = new LoadRates();
                loadRates.execute();
            }
        });
    }

    private void settingAdapter() {
        showResultFrame();
        ratesAdapter = new RatesAdapter(currenciesStorage.getLoadedList().getCurrencies());
        ratesListView.setAdapter(ratesAdapter);

    }

    private void showResultFrame(){
        refreshButton.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        stubTextView.setVisibility(View.GONE);
    }

    private void showErrorFrame(){
        refreshButton.setVisibility(View.VISIBLE);
        noData.setVisibility(View.VISIBLE);
        stubTextView.setVisibility(View.GONE);
    }

    private void showDownloadingFrame(){
        refreshButton.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        stubTextView.setVisibility(View.VISIBLE);
    }

    private void setClickListener() {

        ratesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Currency currency = ratesAdapter.getItem(position);
                Intent intent = new Intent(RatesActivity.this, CalculatorActivity.class);
                intent.putExtra(CalculatorActivity.INIT_VAL, currency.getNominal());
                intent.putExtra(CalculatorActivity.CODE, currency.getCharCode());
                intent.putExtra(CalculatorActivity.RATE, currency.getValue());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadRates = null;
    }

    private class LoadRates extends AsyncTask<Void, Void, Void>{
        private CurrenciesList loadlist;
        private int code;

        @Override
        protected Void doInBackground(Void... params) {
            String url = "http://www.cbr.ru/scripts/XML_daily.asp";

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setInstanceFollowRedirects(true);
                HttpURLConnection.setFollowRedirects(true);
                code = con.getResponseCode();
                loadlist = CurrenciesList.readFromStream(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e(TAG, "onPostExecute: "+code );
            if (code != 200){
                Toast.makeText(RatesActivity.this, "Somthing gone wrong! \n code: " + code,
                        Toast.LENGTH_LONG).show();
                showErrorFrame();
            } else {
                currenciesStorage.setLoadedList(loadlist);
                settingAdapter();
                setClickListener();
            }

        }
    }
}
