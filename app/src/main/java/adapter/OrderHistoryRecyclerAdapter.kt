package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import modal.OrderHistory
import com.example.manohar.R
import database.OrderedMenuEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderHistoryRecyclerAdapter(
    val context: Context,
    private val orderHistoryList: ArrayList<OrderHistory>
) :
    RecyclerView.Adapter<OrderHistoryRecyclerAdapter.OrderHistoryViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderHistoryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_order_history, p0, false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }

    override fun onBindViewHolder(p0: OrderHistoryViewHolder, p1: Int) {
        val orderHistoryObject = orderHistoryList[p1]
        p0.txtResName.text = orderHistoryObject.restName
        p0.txtDate.text = formatDate(orderHistoryObject.orderDate)
        setUpRecycler(p0.recyclerResHistory, orderHistoryObject)
    }

    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResName: TextView = view.findViewById(R.id.restName)
        val txtDate: TextView = view.findViewById(R.id.orderDate)
        val recyclerResHistory: RecyclerView = view.findViewById(R.id.recyclerOrderHistory)
    }

    private fun setUpRecycler(recyclerResHistory: RecyclerView, orderHistoryList: OrderHistory) {
        val foodItemsList = ArrayList<OrderedMenuEntity>()
        for (i in 0 until orderHistoryList.foodItem.length()) {
            val foodJson = orderHistoryList.foodItem.getJSONObject(i)
            foodItemsList.add(
                OrderedMenuEntity(
                    foodJson.getString("food_item_id"),
                    foodJson.getString("name"),
                    foodJson.getString("cost")
                )
            )
        }
        val cartItemAdapter = CartItemRecyclerAdapter(context,foodItemsList)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerResHistory.layoutManager = mLayoutManager
        recyclerResHistory.itemAnimator = DefaultItemAnimator()
        recyclerResHistory.adapter = cartItemAdapter
    }

    private fun formatDate(dateString: String): String? {
        val inputFormatter = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH)
        val date: Date = inputFormatter.parse(dateString) as Date

        val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return outputFormatter.format(date)
    }


}