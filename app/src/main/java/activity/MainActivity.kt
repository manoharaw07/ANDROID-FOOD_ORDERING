package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.manohar.R


class MainActivity : AppCompatActivity() {

    lateinit var b: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val image=findViewById<ImageView>(R.id.imgLogo) as ImageView
        val name=findViewById<TextView>(R.id.txtAppName) as TextView
        animate(image,name)
        /* animate2(image,name)*/
        val b=findViewById<ImageView>(R.id.imgLogo)
        Handler().postDelayed({ val start= Intent(this@MainActivity,
            LoginActivity::class.java)
            startActivity(start) },2500)


    }
    fun animate(image: ImageView, name: TextView){
        val animation1: Animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        image.startAnimation(animation1)
        val animation2: Animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        name.startAnimation(animation2) }

    fun imageclickButton(view: View) {

        val i =Intent(this@MainActivity,LoginActivity::class.java)

        startActivity((i))
    }

}
