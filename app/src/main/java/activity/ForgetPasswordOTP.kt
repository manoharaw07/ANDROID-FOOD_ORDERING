package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.manohar.R
import kotlinx.android.synthetic.main.activity_forget_password_o_t_p.*
import org.json.JSONException
import org.json.JSONObject


class ForgetPasswordOTP : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    lateinit var etOtp: EditText
    lateinit var etNewPassword: EditText
    lateinit var etNewConfrimPassword: EditText
    lateinit var btnSubmit: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password_o_t_p)
        initialization()
        setupToolbar()
        btnSubmit.setOnClickListener {sendRequest()}

    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Forget Password"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextAppearance(this, R.style.PoppinsTextAppearance)
    }
    private fun sendRequest() {
        val intent = intent

        /* intent.putExtra("User", user)
         intent.putExtra("Password", pass)*/
        val queue = Volley.newRequestQueue(this@ForgetPasswordOTP)
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", intent.getStringExtra("mobile"))
        jsonParams.put("password", etNewPassword.text.toString())
        jsonParams.put("otp", etOtp.text.toString())

        val jsonRequest = object: JsonObjectRequest(
            Method.POST, url,
            jsonParams, Response.Listener {

                println("Response is $it")

                try{
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    if(success){

                        val response = data.getString("successMessage")

                        Toast.makeText(this@ForgetPasswordOTP,
                            response,
                            Toast.LENGTH_SHORT).show()



                        val intent = Intent(this@ForgetPasswordOTP, LoginActivity::class.java)

                        sharedPreferences.edit().clear().commit()

                        startActivity(intent)
                        finish()

                    }else{

                        btnSubmit.visibility = View.VISIBLE

                        Toast.makeText(this@ForgetPasswordOTP, "Some Error has Occurred", Toast.LENGTH_SHORT).show()
                    }
                }catch(e: JSONException){
                    e.printStackTrace()
                }

            }, Response.ErrorListener {
                println("Error is $it")
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "9bf534118365f1"
                return headers
            }
        }

        queue.add(jsonRequest)
    }
    private fun initialization() {  etOtp = findViewById(R.id.etOtp)
        etNewPassword = findViewById(R.id.etNewPassword)
        etNewConfrimPassword = findViewById(R.id.etNewConfirmPassword)
        btnSubmit = findViewById(R.id.btnSubmit)
        toolbar=findViewById(R.id.toolbar)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)}
}
