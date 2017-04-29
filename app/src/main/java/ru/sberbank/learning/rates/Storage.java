package ru.sberbank.learning.rates;

import android.app.Application;

import ru.sberbank.learning.rates.storage.CurrenciesStorage;


public class Storage extends Application {

    private CurrenciesStorage storage = new CurrenciesStorage();

    public CurrenciesStorage getStorage() {
        return storage;
    }

}
