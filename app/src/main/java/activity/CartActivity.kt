package activity

import adapter.CartItemRecyclerAdapter
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.example.manohar.R
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
//import com.example.manohar.R
import database.OrderedMenuDatabase
import database.OrderedMenuEntity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import util.ConnectionManager

@Suppress("UNREACHABLE_CODE")
class CartActivity : AppCompatActivity() {
    lateinit var recyclerCart: RecyclerView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartItemRecyclerAdapter
    lateinit var restNameTv:TextView
    var list = arrayListOf<OrderedMenuEntity>()
    var totalPrice: Int = 0
    lateinit var successfullyadded:RelativeLayout
    lateinit var nothingadded:LinearLayout
    lateinit var btnPlaceOrder: Button
    var dishId = mutableListOf<String>()
    lateinit var sharedPreferences: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        intialization()
        setupToolbar()
        if(list.isEmpty())
        {
            successfullyadded.visibility=View.GONE
            nothingadded.visibility=View.VISIBLE
        }
        else{
            successfullyadded.visibility=View.VISIBLE
            nothingadded.visibility=View.GONE

        }

        list = CartActivity.RetrieveMenu(this, 2).execute().get() as ArrayList<OrderedMenuEntity>
        for (i in list) {
            totalPrice += i.cost.toInt()
        }

        if (ConnectionManager().checkConnectivity(this)) {
            btnPlaceOrder.setOnClickListener() {
                sendRequest()
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

        btnPlaceOrder.setText("place order(total Rs:$totalPrice)")




    }

    private fun setupToolbar()
    {   val restName=intent.getStringExtra("restName")
        toolbar = findViewById(R.id.toolbar)
        restNameTv=findViewById(R.id.restName)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"

        toolbar.setTitleTextAppearance(this, R.style.PoppinsTextAppearance)
        restNameTv.setText("Ordering from : $restName")
    }

    private fun sendRequest()
    {
        val userId = sharedPreferences.getString("User_id", null)

        val restid = intent.getStringExtra("restId")

        list = CartActivity.RetrieveMenu(this, 2).execute().get() as ArrayList<OrderedMenuEntity>

        for (i in list) {
            totalPrice += i.cost.toInt()
            println("costofdish${i.cost.toInt()}")
            dishId.add(i.ordered_dish_id)
        }

        print("total$totalPrice")

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"
        val jsonParam = JSONObject()
        jsonParam.put("user_id", userId)
        jsonParam.put("restaurant_id", restid)
        jsonParam.put("total_cost", totalPrice.toString())
        val foodArray = JSONArray()
        for (i in 0 until list.size) {
            val foodId = JSONObject()
            foodId.put("food_item_id", list[i].ordered_dish_id)
            foodArray.put(i, foodId)
        }
        jsonParam.put("food", foodArray)

        val jsonRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonParam,
            com.android.volley.Response.Listener {

                println("Response for cart activity is $it")



                try{
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    if(success){

                        val intent=Intent(this,SuccessfullOrderActivity::class.java)

                        startActivity(intent)
                        finish()

                    }

                    else{

                        val errorMessage = data.getString("errorMessage")

                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }catch(e: JSONException){
                    e.printStackTrace()
                }



            },
            com.android.volley.Response.ErrorListener {

                println("Error is $it")


            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "534a6f3dd3fb32"
                return headers
            }
        }

        queue.add(jsonRequest)

    }

    private fun intialization()
    {

        recyclerCart = findViewById(R.id.recyclercartItom)
        layoutManager = LinearLayoutManager(this)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        list = CartActivity.RetrieveMenu(this, 2).execute().get() as ArrayList<OrderedMenuEntity>
        recyclerAdapter = CartItemRecyclerAdapter(this, list)
        recyclerCart.adapter = recyclerAdapter
        recyclerCart.layoutManager = layoutManager
        successfullyadded=findViewById(R.id.successfullyadded)
        nothingadded=findViewById(R.id.nothindadded)

    }

    class RetrieveMenu(val context: Context, val mode: Int) :
        AsyncTask<Void, Void, List<OrderedMenuEntity>>() {
        val db =
            Room.databaseBuilder(context, OrderedMenuDatabase::class.java, "orderedMenu").build()

        override fun doInBackground(vararg params: Void?): List<OrderedMenuEntity> {

            if (mode == 1) {

                db.orderedMenuDao().deleteAll()
                db.close()

            }

            return db.orderedMenuDao().getAllmenu()
            db.close()
        }
    }


}