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
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.manohar.R
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    lateinit var etAddress: EditText
    lateinit var etRegisterPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etName= findViewById(R.id.etName)
        etEmail =findViewById(R.id.etEmail)
        etMobile=findViewById(R.id.etMobileNumber)
        etAddress=findViewById(R.id.etDeliveryadd)
        etRegisterPassword=findViewById(R.id.etPassword)
        etConfirmPassword=findViewById(R.id.etConfirmPassword)
        btnRegister=findViewById(R.id.btRegister)
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)
        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobile = etMobile.text.toString()
            val address = etAddress.text.toString()
            val registerPassword = etRegisterPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()


            if(name.isEmpty())
            {
                etName.setError("Please enter a valid name")
                etName.requestFocus()
            }
            if(email.isEmpty())
            {
                etEmail.setError("Please enter a valid name")
                etEmail.requestFocus()
            }
            if(mobile.isEmpty())
            {
                etMobile.setError("Please enter a valid email")
                etMobile.requestFocus()
            }
            if(address.isEmpty())
            {
                etAddress.setError("Please enter a address")
                etAddress.requestFocus()
            }
            if(registerPassword.isEmpty())
            {
                etRegisterPassword.setError("Please enter a password(min 6 digit)")
                etRegisterPassword.requestFocus()
            }
            if (registerPassword.length<6)
            {
                etRegisterPassword.setError("password must be more than 6 digit")
                etRegisterPassword.requestFocus()
            }
            if(confirmPassword.isEmpty())
        {
            etConfirmPassword.setError("Please enter a valid name")
            etConfirmPassword.requestFocus()
        }

            if (registerPassword == confirmPassword)
            {

                val queue=Volley.newRequestQueue(this@RegisterActivity)
                val url="http://13.235.250.119/v2/register/fetch_result"
                val jsonParam=JSONObject()
                jsonParam.put("name", name)
                jsonParam.put("mobile_number",mobile)
                jsonParam.put("password", registerPassword)
                jsonParam.put("address",address)
                jsonParam.put("email",email)


                val jsonRequest = object: JsonObjectRequest(
                    Method.POST, url,
                    jsonParam, com.android.volley.Response.Listener {
                        try{
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")

                            if(success){

                                val intent=Intent(this@RegisterActivity,HomeActivity::class.java)
                                val response = data.getJSONObject("data")
                                val userId:String=response.getString("user_id")
                                val name=response.getString("name")
                                val mobile= response.getString("mobile_number")
                                val address=response.getString("address")
                                val email=response.getString("email")


                                sharedPreferences.edit().putString("User_id", userId).apply()

                                sharedPreferences.edit().putString("Name", name ).apply()

                                sharedPreferences.edit().putString("Mobile", mobile).apply()

                                sharedPreferences.edit().putString("Address", address).apply()

                                sharedPreferences.edit().putString("Email", email ).apply()

                                startActivity(intent)
                                finish()

                            }

                            else{

                                btnRegister.visibility = View.VISIBLE

                                val errorMessage = data.getString("errorMessage")

                                Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }catch(e: JSONException){
                            e.printStackTrace()
                        }

                    }, com.android.volley.Response.ErrorListener {
                        println("Error is $it")
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "534a6f3dd3fb32"
                        return headers
                    }
                }

                queue.add(jsonRequest)

            }
            else{
                etConfirmPassword.setError("Password is not matched")
                etConfirmPassword.requestFocus()
            }
        }


    }
    override fun onPause() {
                             super.onPause()
                             finish()
                            }
}
