package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.manohar.R
import kotlinx.android.synthetic.main.activity_successfull_order.view.*

class SuccessfullOrderActivity : AppCompatActivity() {

    lateinit var goBack:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successfull_order)

        goBack=findViewById(R.id.done)
        goBack.setOnClickListener()
        {
            val intent=Intent(this,HomeActivity::class.java)
            RestaurentMenu.RetrieveMenu(this, 1).execute()
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent=Intent(this,HomeActivity::class.java)
        RestaurentMenu.RetrieveMenu(this, 1).execute()
        startActivity(intent)
        super.onBackPressed()
    }

}
