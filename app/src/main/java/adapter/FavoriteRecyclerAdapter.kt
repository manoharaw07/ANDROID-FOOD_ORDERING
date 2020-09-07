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
import com.squareup.picasso.Picasso
import database.RestaurantDatabase
import database.RestaurantEntity

class FavoriteRecyclerAdapter(val context: Context,
                              var itemList: List<RestaurantEntity>
) :
    RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite, parent, false)
       // println("hellofav"+itemList)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }




    override fun onBindViewHolder(holder:FavoriteViewHolder, position: Int) {

        val rest = itemList[position]
        print("favadapter are"+rest)
        holder.restName.text = rest.resName
        holder.restRating.text = rest.resRating
        holder.restcodt.text = rest.resPrice+"/person"


        Picasso.get().load(rest.resImage).error(R.drawable.food).into(holder.imgRest)
        holder.llContext.setOnClickListener {
            // Toast.makeText(context, "you clicked on ", Toast.LENGTH_SHORT).show()


            val intent = Intent(context, RestaurentMenu::class.java)
            intent.putExtra("restId", rest.res_id.toString())
            intent.putExtra("restName", rest.resName)
            // val intent=Intent(context,HomeActivity::class.java)
            //intent.putExtra("restid",rest.id)
            //// println("helloworld"+rest.id)
            context.startActivity(intent)
        }


        val restEntity=RestaurantEntity(
            rest.res_id.toInt(),
            rest.resName,
            rest.resPrice,
            rest.resRating,
            rest.resImage

        )



        val checkfav= FavoriteRecyclerAdapter.DBAsyncTAsk(

            context ,
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

            if (!HomeRecyclerAdapter.DBAsyncTAsk(context, restEntity, 1).execute().get())
            {

                val async=
                    HomeRecyclerAdapter.DBAsyncTAsk(context, restEntity, 2)
                        .execute()
                val result=async.get()
                if (result)
                {Toast.makeText(context,"Added to favorites",Toast.LENGTH_SHORT).show()
                    holder.btnFav.setBackgroundResource(R.drawable.ic_heart)}


            }
            else
            {
                val async=
                    HomeRecyclerAdapter.DBAsyncTAsk(context, restEntity, 3)
                        .execute()
                val result=async.get()
                if (result)
                {


                    Toast.makeText(context,"Removed from favorites",Toast.LENGTH_SHORT).show()
                    holder.btnFav.setBackgroundResource(R.drawable.ic_non_favourite)
                }


            }
        }

    }




    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgRest: ImageView = view.findViewById(R.id.imgfavrest)
        val btnFav: ToggleButton = view.findViewById(R.id.btnfavfav)
        val restName: TextView = view.findViewById(R.id.tvrestfavName)
        val restRating: TextView = view.findViewById(R.id.tvrestfavRating)
        val restcodt: TextView = view.findViewById(R.id.tvrestfavCost)
        val llContext: RelativeLayout = view.findViewById(R.id.rlContent)
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