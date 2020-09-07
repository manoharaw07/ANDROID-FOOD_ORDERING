package fragments

import adapter.OrderHistoryRecyclerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import modal.OrderHistory
import com.example.manohar.R


class OrderHistoryFragment : Fragment() {

    lateinit var orderHistoryAdapter:OrderHistoryRecyclerAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var recyclerOrderHistory: RecyclerView
//    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var noOrders: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_order_history, container, false)

       // progressLayout=view.findViewById(R.id.progressLayout)
        //progressBar=view.findViewById(R.id.progressBar)
     //   noOrders=view.findViewById(R.id.noOrders)
        recyclerOrderHistory = view.findViewById(R.id.recyclerOrderHistoryFragment) //  added this line

        sharedPreferences=context!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val userId=sharedPreferences.getString("User_id","")

        print("userid$userId")

        val orderHistoryList= arrayListOf<OrderHistory>()

        val queue = Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/orders/fetch_result/$userId"

        val jsonObjectRequest = object :
            JsonObjectRequest(Method.GET,url , null, Response.Listener {
//                progressLayout.visibility = View.GONE

                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val resArray = data.getJSONArray("data")
                        if (resArray.length() == 0) {
                            //noOrders.visibility = View.VISIBLE
                        } else {
                            for (i in 0 until resArray.length()) {
                                val orderObject = resArray.getJSONObject(i)
                                val foodItems = orderObject.getJSONArray("food_items")
                                val orderDetails = OrderHistory(
                                    orderObject.getInt("order_id"),
                                    orderObject.getString("restaurant_name"),
                                    orderObject.getString("order_placed_at"),
                                    foodItems
                                )
                                orderHistoryList.add(orderDetails)
                                if (orderHistoryList.isEmpty()) {

//                                 //   noOrders.visibility = View.VISIBLE
                                } else {

                                  //  noOrders.visibility = View.GONE
                                    if (activity != null) {
                                        orderHistoryAdapter = OrderHistoryRecyclerAdapter(
                                            activity as Context,
                                            orderHistoryList
                                        )
                                        val mLayoutManager =
                                            LinearLayoutManager(activity as Context)
                                        recyclerOrderHistory.layoutManager = mLayoutManager
                                      recyclerOrderHistory.itemAnimator = DefaultItemAnimator()
                                        recyclerOrderHistory.adapter = orderHistoryAdapter
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                Toast.makeText(activity as Context, it.message, Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "326a253b6b58cc"
                return headers
            }
        }
        queue.add(jsonObjectRequest)

        return view
    }

}