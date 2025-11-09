package com.example.appclima.modelo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Previsao {

    private String city;
    private Integer temp;
    private Integer humidity;
    private List<PrevisaoDia> forecast;

    public String getCity() {
        return city;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }


    public List<PrevisaoDia> getForecast() {
        return forecast;
    }

    public void setForecast(List<PrevisaoDia> forecast) {
        this.forecast = forecast;
    }
}
