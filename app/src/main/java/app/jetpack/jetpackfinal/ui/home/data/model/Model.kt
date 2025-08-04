package app.jetpack.jetpackfinal.ui.home.data.model

import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName

// Model classes with default values for Firebase deserialization
data class SpotlightData(
    @SerializedName("graphs") val graphs: Graph = Graph(),
    @SerializedName("marketData") val marketData: MarketData = MarketData(),
    @SerializedName("staticData") val staticData: StaticData = StaticData()
)

data class RecommendationData(
    @SerializedName("graphs") val graphs: Graph = Graph(),
    @SerializedName("marketData") val marketData: MarketData = MarketData(),
    @SerializedName("staticData") val staticData: StaticData = StaticData()
)

data class Graph(
    @get:PropertyName("1m") @set:PropertyName("1m")
    var oneMonth: GraphData = GraphData(),
    @get:PropertyName("1y") @set:PropertyName("1y")
    var oneYear: GraphData = GraphData(),
    @get:PropertyName("24h") @set:PropertyName("24h")
    var oneDay: GraphData = GraphData(),
    @get:PropertyName("4h") @set:PropertyName("4h")
    var fourHour: GraphData = GraphData(),
    @get:PropertyName("7d") @set:PropertyName("7d")
    var sevenDay: GraphData = GraphData(),
    @get:PropertyName("live") @set:PropertyName("live")
    var live : GraphData = GraphData()
)

data class GraphData(
    @get:PropertyName("market_cap") @set:PropertyName("market_cap")
    var marketCap: ArrayList<ArrayList<Any>> = arrayListOf(),
    @get:PropertyName("price") @set:PropertyName("price")
    var price: ArrayList<ArrayList<Any>> = arrayListOf()
) {
    fun getPriceValues(): List<Float> {
        return price.map { (it[1] as Number).toFloat() }
    }

    fun getMarketCapValues(): List<Float> {
        return marketCap.map { (it[1] as Number).toFloat() }
    }
}

data class MarketCap(
    @get:PropertyName("0") @set:PropertyName("0")
    var timestamp: String = "",
    @get:PropertyName("1") @set:PropertyName("1")
    var value: Double = 0.0
)

data class Price(
    @get:PropertyName("0") @set:PropertyName("0")
    var timestamp: String = "",
    @get:PropertyName("1") @set:PropertyName("1")
    var value: Double = 0.0
)



data class MarketData(
    @SerializedName("ath") var ath: Double = 0.0,
    @get:PropertyName("ath_change_percentage") @set:PropertyName("ath_change_percentage")
    var athChangePercentage: Double = 0.0,
    @get:PropertyName("ath_date") @set:PropertyName("ath_date")
    var athDate: String = "",
    @SerializedName("atl") var atl: Double = 0.0,
    @get:PropertyName("atl_change_percentage") @set:PropertyName("atl_change_percentage")
    var atlChangePercentage: Double = 0.0,
    @get:PropertyName("atl_date") @set:PropertyName("atl_date")
    var atlDate: String = "",
    @get:PropertyName("circulating_supply") @set:PropertyName("circulating_supply")
    var circulatingSupply: Long = 0,
    @get:PropertyName("current_price") @set:PropertyName("current_price")
    var currentPrice: Double = 0.0,
    @get:PropertyName("fully_diluted_valuation") @set:PropertyName("fully_diluted_valuation")
    var fullyDilutedValuation: Long = 0,
    @get:PropertyName("high_24h") @set:PropertyName("high_24h")
    var high24h: Double = 0.0,
    @get:PropertyName("last_updated") @set:PropertyName("last_updated")
    var lastUpdated: String = "",
    @get:PropertyName("low_24h") @set:PropertyName("low_24h")
    var low24h: Double = 0.0,
    @get:PropertyName("market_cap") @set:PropertyName("market_cap")
    var marketCap: Long = 0,
    @get:PropertyName("market_cap_change_24h") @set:PropertyName("market_cap_change_24h")
    var marketCapChange24h: Long = 0,
    @get:PropertyName("market_cap_change_percentage_24h") @set:PropertyName("market_cap_change_percentage_24h")
    var marketCapChangePercentage24h: Double = 0.0,
    @get:PropertyName("market_cap_rank") @set:PropertyName("market_cap_rank")
    var marketCapRank: Int = 0,
    @get:PropertyName("max_supply") @set:PropertyName("max_supply")
    var maxSupply: Long = 0,
    @get:PropertyName("price_change_24h") @set:PropertyName("price_change_24h")
    var priceChange24h: Double = 0.0,
    @get:PropertyName("price_change_percentage_1h_in_currency") @set:PropertyName("price_change_percentage_1h_in_currency")
    var priceChangePercentage1hInCurrency: Double = 0.0,
    @get:PropertyName("price_change_percentage_24h_in_currency") @set:PropertyName("price_change_percentage_24h_in_currency")
    var priceChangePercentage24hInCurrency: Double = 0.0,
    @get:PropertyName("price_change_percentage_30d_in_currency") @set:PropertyName("price_change_percentage_30d_in_currency")
    var priceChangePercentage30dInCurrency: Double = 0.0,
    @get:PropertyName("price_change_percentage_7d_in_currency") @set:PropertyName("price_change_percentage_7d_in_currency")
    var priceChangePercentage7dInCurrency: Double = 0.0,
    @get:PropertyName("total_supply") @set:PropertyName("total_supply")
    var totalSupply: Long = 0,
    @get:PropertyName("total_volume") @set:PropertyName("total_volume")
    var totalVolume: Long = 0
)

data class StaticData(
    @SerializedName("categories") val categories: List<String> = emptyList(),
    @SerializedName("contractAddress") val contractAddress: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("id") val id: String = "",
    @SerializedName("image") val image: StaticDataImage = StaticDataImage(),
    @SerializedName("name") val name: String = "",
    @SerializedName("symbol") val symbol: String = ""
)

data class StaticDataImage(
    @SerializedName("large") val large: String = "",
    @SerializedName("small") val small: String = "",
    @SerializedName("thumb") val thumb: String = ""
)

data class CryptoCard(
    var id: Int = 0,
    var name: String = "",
    var marketCap: String = "",
    var price: String = "",
    var change: Float = 0f,
    var iconUrl: String = ""
)

data class CoinNews(
    var id: Int = 0,
    var news: String = "",
    var highlightedValue: List<HighlightedValue> = emptyList()
)

data class CreateUserRequest(
    val email: String = "",
    val walletPubKey: String = ""
)