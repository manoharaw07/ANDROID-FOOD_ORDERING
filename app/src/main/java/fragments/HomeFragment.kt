package fragments

import adapter.HomeRecyclerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.manohar.R
import modal.Restaurents
import org.json.JSONException
import util.ConnectionManager
import java.util.*
import kotlin.collections.HashMap


class HomeFragment : Fragment() {
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    val list = arrayListOf<Restaurents>()
    var ratingComparator = Comparator<Restaurents>{ res1, res2 ->



        if (res1.restRating.compareTo(res2.restRating)==0) {
            // sort according to name if rating is same
            res1.restName.compareTo(res2.restName)
        } else {
            res1.restRating.compareTo(res2.restRating)
        }
    }

    var costComparator = Comparator<Restaurents>{ rest1, rest2 ->



        if (rest1.restCost.compareTo(rest2.restCost) == 0) {

            rest1.restName.compareTo(rest2.restName)
        } else {
            rest1.restCost.compareTo(rest2.restCost)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        this.setHasOptionsMenu(true)
        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = HomeRecyclerAdapter(activity as Context, list)
        recyclerHome.adapter = recyclerAdapter
        recyclerHome.layoutManager = layoutManager

        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar = view.findViewById(R.id.progressBar)


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    println("Response is $it")

                    try {

                        progressLayout.visibility = View.GONE
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if (success) {

                            val resArray = data.getJSONArray("data")


                            for (i in 0 until resArray.length()) {
                                val restaurantJsonObject = resArray.getJSONObject(i)
                                val restaurantObject =
                                    Restaurents(
                                        restaurantJsonObject.getString("id"),
                                        restaurantJsonObject.getString("name"),
                                        restaurantJsonObject.getString("cost_for_one"),
                                        restaurantJsonObject.getString("rating"),
                                        restaurantJsonObject.getString("image_url")

                                    )
                                list.add(restaurantObject)
                                recyclerAdapter =
                                    HomeRecyclerAdapter(
                                        activity as Context,
                                        list
                                    )

                                recyclerHome.adapter = recyclerAdapter

                                recyclerHome.layoutManager = layoutManager

                            }


                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some Unexpected Error Occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {


                    if (activity != null)
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occurred!",
                            Toast.LENGTH_SHORT
                        ).show()

                }) {

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "534a6f3dd3fb32"
                        return headers
                    }

                }


            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not  Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }








        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.sort){

            val alertDialog = AlertDialog.Builder(activity as Context)
            alertDialog.setTitle("Sort By?")
            val items =
                arrayOf("Cost(Low to High)", "Cost(High to Low)", "Rating")
            val checkedItem=4
            alertDialog.setSingleChoiceItems(
                items,
                checkedItem
            ) { dialog, which ->
                when (which) {
                    0 -> Collections.sort(list, costComparator)
                    1 -> {
                        Collections.sort(list, costComparator)
                        list.reverse()
                    }
                    2 -> {
                        Collections.sort(list, ratingComparator)
                        list.reverse()
                    }

                }
            }
            alertDialog.setPositiveButton("Ok"){text, listener->

                recyclerAdapter.notifyDataSetChanged()

            }
            alertDialog.setNegativeButton("Cancel"){text, listener->
            }
            val alert = alertDialog.create()
            alert.show()
        }

        return super.onOptionsItemSelected(item)
    }

}
