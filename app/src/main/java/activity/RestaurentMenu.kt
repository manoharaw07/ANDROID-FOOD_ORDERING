package activity

import adapter.MenuRecyclerAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.manohar.R
import database.OrderedMenuDatabase
import database.OrderedMenuEntity
import org.json.JSONException
import util.ConnectionManager


class RestaurentMenu : AppCompatActivity() {


    lateinit var recyclerMenu: RecyclerView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: MenuRecyclerAdapter
    lateinit var btnProceed: Button
    var dbRestlist = listOf<OrderedMenuEntity>()
    var listofDishe = mutableListOf<String>()


    // var restid: String? = "100"
    var list = arrayListOf<OrderedMenuEntity>()
    var list2 = arrayListOf<OrderedMenuEntity>()


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var restid: String? = "000"
        private var restName: String? = "000"
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurent_menu)


        restid = intent.getStringExtra("restId")
        restName = intent.getStringExtra("restName")
        print("restidis$restid")

        recyclerMenu = findViewById(R.id.recyclerMenu)
        layoutManager = LinearLayoutManager(this)
        // recyclerAdapter = MenuRecyclerAdapter(this, list)
        btnProceed = findViewById(R.id.btnProceed)
        btnProceed.visibility = View.GONE

        dbRestlist = RetrieveMenu(this, 2).execute().get() as ArrayList<OrderedMenuEntity>


        setupToolbar()
        btnProceed.setOnClickListener()
        {

            intent = Intent(this@RestaurentMenu, CartActivity::class.java)
            intent.putExtra("restId", restid)
            intent.putExtra("restName", restName)

            startActivity(intent)

        }

        if (ConnectionManager().checkConnectivity(this)) {

            val queue = Volley.newRequestQueue(this)

            val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restid"

            if (ConnectionManager().checkConnectivity(this)) {
                val jsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                        println("Response is $it")

                        try {

                            //   progressLayout.visibility = View.GONE
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")

                            if (success) {

                                val menuArray = data.getJSONArray("data")


                                for (i in 0 until menuArray.length()) {
                                    val menuJsonObject = menuArray.getJSONObject(i)
                                    val menuObject =
                                        OrderedMenuEntity(
                                            menuJsonObject.getString("id"),
                                            menuJsonObject.getString("name"),
                                            menuJsonObject.getString("cost_for_one")

                                        )
                                    list.add(menuObject)
                                    recyclerAdapter =
                                        MenuRecyclerAdapter(
                                            this,
                                            list, object : MenuRecyclerAdapter.OnItemClickListener {
                                                override fun onAddItemClick(foodItem: OrderedMenuEntity) {
                                                    list2.add(foodItem)
                                                    if (list2.size > 0) {
                                                        btnProceed.visibility = View.VISIBLE
                                                        MenuRecyclerAdapter.isCartEmpty = false
                                                    }
                                                }

                                                override fun onRemoveItemClick(foodItem: OrderedMenuEntity) {
                                                    list2.remove(foodItem)
                                                    if (list2.isEmpty()) {
                                                        btnProceed.visibility = View.GONE
                                                        MenuRecyclerAdapter.isCartEmpty = true
                                                    }
                                                }
                                            }
                                        )

                                    recyclerMenu.adapter = recyclerAdapter

                                    recyclerMenu.layoutManager = layoutManager

                                }

                                /*val restaurantEntity = RestaurantEntity(
                            res_id?.toInt() as Int,
                            txtRestaurantName.text.toString(),
                            txtRestaurantRating.text.toString(),
                            txtPrice.text.toString(),


                        )*/

                            } else {
                                Toast.makeText(
                                    this,
                                    "Some Error Occurred!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this,
                                "Some Unexpected Error Occurred!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }, Response.ErrorListener {

                        // Here we will handle the error
                        Toast.makeText(
                            this,
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
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not  Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    //   activity?.finish()
                }

            }

        }

        else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not  Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                //   activity?.finish()
            }

        }


    }

    class RetrieveMenu(val context: Context, val mode: Int) :
        AsyncTask<Void, Void, List<OrderedMenuEntity>>() {
        val db =
            Room.databaseBuilder(context, OrderedMenuDatabase::class.java, "orderedMenu")
                .build()

        override fun doInBackground(vararg params: Void?): List<OrderedMenuEntity> {

            if (mode == 1) {

                db.orderedMenuDao().deleteAll()
                db.close()

            }

            return db.orderedMenuDao().getAllmenu()
            db.close()
        }
    }


    override fun onBackPressed() {


        if (this.list2.size > 0) {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Confirmation")
                .setMessage("Going back will reset cart items.Do You still want to proceed")
                .setPositiveButton("Yes") { _, _ ->

                    RetrieveMenu(this, 1).execute()
                    super.onBackPressed()
                }
                .setNegativeButton("No") { _, _ ->
                    Display.DEFAULT_DISPLAY
                }
                .create()
                .show()
        } else {
            super.onBackPressed()
        }


    }

    private fun setupToolbar() {
        val restName = intent.getStringExtra("restName")
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ordering From : $restName"

        toolbar.setTitleTextAppearance(this, R.style.PoppinsTextAppearance)

    }


}

