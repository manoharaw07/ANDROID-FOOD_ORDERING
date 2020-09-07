package adapter

import activity.RestaurentMenu
import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.manohar.R
import database.OrderedMenuDatabase
import database.OrderedMenuEntity

class MenuRecyclerAdapter(val context: RestaurentMenu, val itemList: ArrayList<OrderedMenuEntity>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<MenuRecyclerAdapter.MenuViewHolder>() {
    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val list = mutableListOf<String>()
        val dishID: TextView = view.findViewById(R.id.idOfDish)
        val dishName: TextView = view.findViewById(R.id.tvdishName)
        val dishPrice: TextView = view.findViewById(R.id.tvdishprice)
        val add: Button = view.findViewById(R.id.btnadd)
    }


    companion object {
        var isCartEmpty = true
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuViewHolder {


        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_restaurent_menu, parent, false)

        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    interface OnItemClickListener {
        fun onAddItemClick(foodItem: OrderedMenuEntity)
        fun onRemoveItemClick(foodItem: OrderedMenuEntity)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = itemList[position]

        holder.dishID.text = (position + 1).toString()
        holder.dishName.text = menu.dishName
        holder.dishPrice.text = menu.cost.toString()


        val dishEntity = OrderedMenuEntity(

            menu.ordered_dish_id.toString(),
            menu.dishName,
            menu.cost

        )


        val checkadd = MenuRecyclerAdapter.DBAsyncTAsk(

            context,
            dishEntity,
            1
        ).execute()
        val isadd = checkadd.get()


        if (isadd) {
            holder.add.setBackgroundResource(R.color.colorAccent)
            holder.add.setText("Remove")
        } else {
            holder.add.setBackgroundResource(R.color.colorPrimaryDark)
            holder.add.setText("Add")
        }

        holder.add.setOnClickListener {

            if (!MenuRecyclerAdapter.DBAsyncTAsk(context, dishEntity, 1).execute().get()) {

                val async =
                    MenuRecyclerAdapter.DBAsyncTAsk(context, dishEntity, 2)
                        .execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                    holder.add.setBackgroundResource(R.color.colorAccent)
                    holder.add.setText("Remove")
                    listener.onAddItemClick(menu)
                }


            } else {
                val async =
                    MenuRecyclerAdapter.DBAsyncTAsk(context, dishEntity, 3)
                        .execute()
                val result = async.get()
                if (result) {


                    Toast.makeText(context,"Removed from cart", Toast.LENGTH_SHORT).show()
                    holder.add.setBackgroundResource(R.color.colorPrimaryDark)
                    holder.add.setText("Add")
                    listener.onRemoveItemClick(menu)
                }


            }
        }


    }


    class DBAsyncTAsk(
        val context: Context,
        private val orderedMenuEntity: OrderedMenuEntity,
        private val mode: Int
    ) :
        AsyncTask<Void, Void, Boolean>() {
//        Mode 1 ->check db if the rest is fav or not
//        Mode 2 ->save the rest into db ad fav
//        Mode 3 ->remove the rest from db


        val menudb =
            Room.databaseBuilder(context, OrderedMenuDatabase::class.java, "orderedMenu").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    val rest: OrderedMenuEntity? = menudb.orderedMenuDao()
                        .getmenuById(orderedMenuEntity.ordered_dish_id.toString())
                    menudb.close()
                    return rest != null
                }

                2 -> {
                    menudb.orderedMenuDao().insertmenu(orderedMenuEntity)
                    menudb.close()
                    return true

                }

                3 -> {
                    menudb.orderedMenuDao().deletemenu(orderedMenuEntity)
                    return true
                }

            }

            return false
        }
    }

}
