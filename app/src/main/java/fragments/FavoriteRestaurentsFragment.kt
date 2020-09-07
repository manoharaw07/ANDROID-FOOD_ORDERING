package fragments

import adapter.FavoriteRecyclerAdapter
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.manohar.R
import database.RestaurantDatabase
import database.RestaurantEntity

/**
 * A simple [Fragment] subclass.
 */
class FavoriteRestaurentsFragment : Fragment() {


    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavoriteRecyclerAdapter
    var dbRestlist= listOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favorite_restaurents, container, false)
        recyclerView=view.findViewById(R.id.recyclerFavorite)
      //  progressBar=view.findViewById(R.id.progressBar)
      //  progressLayout=view.findViewById(R.id.progressLayout)
        layoutManager=LinearLayoutManager(activity as Context)

        dbRestlist=RetrieveFav(activity as Context).execute().get()
        println("hello"+dbRestlist)
        if (activity!= null) {
         //   progressBar.visibility = View.GONE
            recyclerAdapter = FavoriteRecyclerAdapter(activity as Context, dbRestlist)

            recyclerView.adapter = recyclerAdapter
            recyclerView.layoutManager = layoutManager

        }
        return view
    }
    class RetrieveFav(val context: Context):AsyncTask<Void,Void, List<RestaurantEntity>>(){
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"rest-db").build()
            print("favarite are"+db)
            return db.resDao().getAllRes()
        }

    }

}
