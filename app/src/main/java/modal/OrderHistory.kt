package modal

import org.json.JSONArray

data class OrderHistory(
    var restId:Int,
    var restName:String,
    var orderDate:String,
    var foodItem: JSONArray
)