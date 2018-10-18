package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AlertDialog
import android.transition.TransitionInflater
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.loadin_layout.*

class RegisterActivity : AppCompatActivity() {

    var username:String? = null
    var email:String? = null
    var password:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= 21){
            var transition_infalter = TransitionInflater.from(this)
            var transition = transition_infalter.inflateTransition(R.transition.transition_rgister_main)
            window.exitTransition = transition
        }
        setContentView(R.layout.activity_register)

        // check if user is signed in
        if(FirebaseAuth.getInstance().uid != null){
            startMainActivity()
        }

        register_btn_register_activity.setOnClickListener {
            RegisterNewUser()
        }

    }

    private fun RegisterNewUser(){
        // get email and password from user
        email = email_textview_register_activity.text.toString()
        password = password_textview_register_activity.text.toString()
        username = username_textview_register_activity.text.toString()
        // connect to firebase and create new user
        var ref = FirebaseAuth.getInstance()

        if(username!!.trim().isEmpty() || password!!.trim().isEmpty() || username!!.trim().isEmpty()){
            Toast.makeText(this@RegisterActivity,"please fill all fields",Toast.LENGTH_SHORT).show()
            return
        }

        var dialog:ProgressDialog = ProgressDialog(this@RegisterActivity)
        dialog.setTitle("Loading 😊")
        dialog.setMessage("Registration...")
        dialog.show()
        ref.createUserWithEmailAndPassword(email!!,password!!).addOnCompleteListener {
            Log.i("RegisterActivty","new user sign up")
            Log.i("RegisterActivty","user id: ${ref.uid}")
                dialog.hide()
                startMainActivity()
            }

    }

    fun startMainActivity(){
        var intent = Intent(this@RegisterActivity, MainActivity::class.java)
        // make a transition before exit
        var compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity)
        startActivity(intent,compat.toBundle())
        Log.i("RegisterActivty","MainActivity is ready")
        finish()
    }
}
