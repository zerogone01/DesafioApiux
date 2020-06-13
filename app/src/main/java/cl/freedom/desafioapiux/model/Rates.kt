package cl.freedom.desafioapiux.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rates {
    @SerializedName("CAD")
    @Expose
    var cad: Double? = null
    @SerializedName("GBP")
    @Expose
    var gbp: Double? = null
    @SerializedName("MXN")
    @Expose
    var mxn: Double? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param mxn
     * @param gbp
     * @param cad
     */
    constructor(cad: Double, gbp: Double, mxn: Double) : super() {
        this.cad = cad
        this.gbp = gbp
        this.mxn = mxn
    }

}