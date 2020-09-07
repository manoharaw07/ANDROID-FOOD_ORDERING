package activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import android.content.SharedPreferences
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.manohar.R
import fragments.*
import kotlinx.android.synthetic.main.drawer_header.view.*

class HomeActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    lateinit var toolBar: androidx.appcompat.widget.Toolbar
    lateinit var navigationView: NavigationView
    var previousMenuItem : MenuItem?=null
     lateinit var profileName: TextView

     lateinit var mobileNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        frameLayout = findViewById(R.id.frameLayout)
        toolBar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)


         val headerView=navigationView.getHeaderView(0)



       profileName=headerView.profileName.findViewById(R.id.profileName)
       mobileNumber=headerView.mobileNumber.findViewById(R.id.mobileNumber)




        val name = sharedPreferences.getString("Name", null)
        val mobile = sharedPreferences.getString("Mobile", null)
        val address = sharedPreferences.getString("Address", null)
        val email = sharedPreferences.getString("Email", null)

       profileName.setText(name)
       mobileNumber.setText(mobile)

        setUpToolBar()
        openFragment(HomeFragment())
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomeActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.closed_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()




        navigationView.setNavigationItemSelectedListener {
        //given 7 lines are used to highlight the selected menu in toolbar
            if (previousMenuItem==null)
            {
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it



            when (it.itemId) {
                R.id.home -> {
                    var bundle=Bundle()
                    bundle.putString("name",name)
                    bundle.putString("mobile",mobile)
                    var frags: HomeFragment = HomeFragment()
                    frags.arguments=bundle
                    openFragment(frags)
                    drawerLayout.closeDrawers()


                }
                R.id.profile -> {


                    var bundle=Bundle()
                    bundle.putString("name",name)
                    bundle.putString("mobile",mobile)
                    bundle.putString("address",address)
                    bundle.putString("email",email)
                    var frags: ProfileFragment = ProfileFragment()
                    frags.arguments=bundle
                    openFragment(frags)
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()

                }

                R.id.order_history -> {



                    var frags: OrderHistoryFragment = OrderHistoryFragment()

                    openFragment(frags)
                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()

                }



                R.id.faq -> {
                    openFragment(FaqFragment())
                    supportActionBar?.title = "FAQs"
                    drawerLayout.closeDrawers()
                }
                R.id.fav_restaurant -> {
                    openFragment(FavoriteRestaurentsFragment())
                    supportActionBar?.title = "Favorite"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {

                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to exit")
                        .setPositiveButton("Yes") { _, _ ->

                            sharedPreferences.edit().clear().apply()
                            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                            super.onBackPressed()
                        }
                        .setNegativeButton("No") { _, _ ->
                            Display.DEFAULT_DISPLAY
                        }
                        .create()
                        .show()





                }
            }

            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Food Studio"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openFragment(fragment: Fragment) {
        val tansaction = supportFragmentManager.beginTransaction()
            tansaction.replace(R.id.frameLayout, fragment).commit()
            supportActionBar?.title = "All Restaurents"
    }

    override fun onBackPressed() {
       val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag){
            !is HomeFragment -> openFragment(HomeFragment())
            else ->  super.onBackPressed()
        }



    }



}
