package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var email:String? = null
    var password:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn_login_activity.setOnClickListener {
            LoginUser()
        }

        bact_to_register.setOnClickListener {
            this.finish()
        }
    }

    private fun LoginUser(){
        var ref = FirebaseAuth.getInstance()
        email = email_textview_login_activity.text.toString()
        password = password_textview_login_activity.text.toString()

        if(email!!.trim().isEmpty() || password!!.trim().isEmpty()){
            Toast.makeText(this@LoginActivity,"please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        var dialog: ProgressDialog = ProgressDialog(this@LoginActivity)
        dialog.setTitle("Loading 😊")
        dialog.setMessage("Logging In...")
        dialog.show()

        ref.signInWithEmailAndPassword(email!!,password!!).addOnCompleteListener {
            Log.i("LoginActivity","new user sing in")
            Log.i("LoginActivity","user id: ${ref.uid}")
            dialog.hide()
            StartMainActivity()
            Log.i("LoginActivity","MainActivity is ready")
            finish()
        }
    }

    private fun StartMainActivity(){
        var intent = Intent(this@LoginActivity, MainActivity::class.java)
        // make a transition before exit
        var compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity)
        startActivity(intent,compat.toBundle())
        Log.i("RegisterActivty","MainActivity is ready")
        finish()
    }
}
