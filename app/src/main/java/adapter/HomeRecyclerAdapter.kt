package adapter

import activity.RestaurentMenu
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.manohar.R
import modal.Restaurents
import com.squareup.picasso.Picasso
import database.RestaurantDatabase
import database.RestaurantEntity

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurents>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val rest = itemList[position]

        holder.restName.text = rest.restName
        holder.restRating.text = rest.restRating
        holder.restcodt.text = rest.restCost
        // holder.imgRest.setImageResource(rest.imgRestaurents)
        //  holder.imgFav.setImageResource(rest.imgFav)
        Picasso.get().load(rest.imgRestaurents).error(R.drawable.food).into(holder.imgRest)
        holder.llContext.setOnClickListener {
           // Toast.makeText(context, "you clicked on ", Toast.LENGTH_SHORT).show()


            val intent = Intent(context, RestaurentMenu::class.java)
            intent.putExtra("restId", rest.id)
            intent.putExtra("restName", rest.restName)
           // val intent=Intent(context,HomeActivity::class.java)
            //intent.putExtra("restid",rest.id)
           //// println("helloworld"+rest.id)
            context.startActivity(intent)
        }

        val restEntity=RestaurantEntity(
            rest.id.toInt(),
            rest.restName,
            rest.restCost,
            rest.restRating,
            rest.imgRestaurents

        )



          val checkfav= DBAsyncTAsk(

              context,
              restEntity,
              1
          ).execute()
           val isfav=checkfav.get()


        if(isfav)
        {
        holder.btnFav.setBackgroundResource(R.drawable.ic_heart)
        }
        else
        {
            holder.btnFav.setBackgroundResource(R.drawable.ic_non_favourite)
        }

        holder.btnFav.setOnClickListener {
          if (!DBAsyncTAsk(context, restEntity, 1).execute().get())
          {

              val async=
                  DBAsyncTAsk(context, restEntity, 2)
                      .execute()
              val result=async.get()
              if (result)
              {Toast.makeText(context,"Added to favorites",Toast.LENGTH_SHORT).show()
                  holder.btnFav.setBackgroundResource(R.drawable.ic_heart)}


          }
            else
          {
              val async=
                  DBAsyncTAsk(context, restEntity, 3)
                      .execute()
              val result=async.get()
              if (result)
              {Toast.makeText(context,"Removed from favorites",Toast.LENGTH_SHORT).show()
                  holder.btnFav.setBackgroundResource(R.drawable.ic_non_favourite)}


          }
        }



    }


    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgRest: ImageView = view.findViewById(R.id.imgrest)
        val btnFav: ToggleButton = view.findViewById(R.id.btnfav)
        val restName: TextView = view.findViewById(R.id.tvrestName)
        val restRating: TextView = view.findViewById(R.id.tvrestRating)
        val restcodt: TextView = view.findViewById(R.id.tvrestCost)
        val llContext: RelativeLayout = view.findViewById(R.id.llContent)
    }
    class DBAsyncTAsk(val context: Context, private val restaurantEntity: RestaurantEntity, private val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
//        Mode 1 ->check db if the rest is fav or not
//        Mode 2 ->save the rest into db ad fav
//        Mode 3 ->remove the rest from db


         val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "rest-db").build()


        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    val rest: RestaurantEntity? = db.resDao().getRestById(restaurantEntity.res_id.toString())
                    db.close()
                    return rest!=null
                }

                2 -> {
                    db.resDao().insertRes(restaurantEntity)
                    db.close()
                    return true

                }

                3 -> {
                    db.resDao().deleteRes(restaurantEntity)
                    db.close()
                    return true
                }

            }

            return false
        }
    }


}