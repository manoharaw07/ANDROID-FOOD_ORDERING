package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.manohar.R
import database.OrderedMenuEntity

class CartItemRecyclerAdapter(context: Context, val itemList: ArrayList<OrderedMenuEntity>): RecyclerView.Adapter<CartItemRecyclerAdapter.CartItemViewHolder>() {




    class CartItemViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val itemName:TextView=view.findViewById(R.id.itemName)
        val itemPrice:TextView=view.findViewById(R.id.itemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_item, parent, false)

        return CartItemRecyclerAdapter.CartItemViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemList.size

    }


    override fun onBindViewHolder(        holder: CartItemRecyclerAdapter.CartItemViewHolder, position: Int)
    {
        val menu = itemList[position]
        holder.itemName.text = menu.dishName
        holder.itemPrice.text =menu.cost

    }
}