package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var email:String? = null
    var password:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        //Chooses theme.
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn_login_activity.setOnClickListener {
            LoginUser()
        }

        bact_to_register.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun LoginUser(){
        var ref = FirebaseAuth.getInstance()
        email = email_textview_login_activity.text.toString()
        password = password_textview_login_activity.text.toString()

        if(email!!.trim().isEmpty() || password!!.trim().isEmpty()){
            val fillDaFields=getString(R.string.FillFields_L)
            Toast.makeText(this@LoginActivity,fillDaFields, Toast.LENGTH_SHORT).show()
            return
        }

        var dialog: ProgressDialog = ProgressDialog(this@LoginActivity)
        val loading=getString(R.string.Loading_L)
        val loggingIn=getString(R.string.LoggingIn_L)
        dialog.setTitle(loading)
        dialog.setMessage(loggingIn)
        dialog.show()

        ref.signInWithEmailAndPassword(email!!,password!!).addOnCompleteListener {
            Log.i("LoginActivity","new user sing in")
            Log.i("LoginActivity","user id: ${ref.uid}")
            dialog.hide()
            StartMainActivity()
            Log.i("LoginActivity","MainActivity is ready")
            this@LoginActivity.finish()
        }
    }

    private fun StartMainActivity(){
        var intent = Intent(this@LoginActivity, MainActivity::class.java)
        // make a transition before exit
        var compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity)
        startActivity(intent,compat.toBundle())
        Log.i("RegisterActivty","MainActivity is ready")
        this@LoginActivity.finish()
    }
    private fun modeLightOrNight(){
        val shrPr=this.getSharedPreferences("mode", Context.MODE_PRIVATE) ?: return
        val mode=shrPr.getString(getString(R.string.mode), "")
        if(mode=="ni"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
    }
}
