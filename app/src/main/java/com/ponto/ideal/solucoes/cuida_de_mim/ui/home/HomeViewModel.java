package com.ponto.ideal.solucoes.cuida_de_mim.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    public HomeViewModel() {

    }



    private MutableLiveData<Integer> apdint = new MutableLiveData<>();
    public void setApdint(Integer i) {
        apdint.setValue(i);
    }
    public LiveData<Integer> getApdint() {
        return apdint;
    }





}