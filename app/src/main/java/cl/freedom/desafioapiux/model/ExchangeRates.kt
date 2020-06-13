package cl.freedom.desafioapiux.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExchangeRates {
    @SerializedName("rates")
    @Expose
    var rates: Rates? = null
    @SerializedName("base")
    @Expose
    var base: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param date
     * @param rates
     * @param base
     */
    constructor(rates: Rates, base: String, date: String) : super() {
        this.rates = rates
        this.base = base
        this.date = date
    }

}