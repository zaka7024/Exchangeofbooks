package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
    var imageProfileUri: Uri? = null

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
            StartMainActivity()
        }

        // pick image
        select_image_profile_register_activity.setOnClickListener {
            PickProfileImage()
        }

        register_btn_register_activity.setOnClickListener {
            RegisterNewUser()
        }

        // have an account
        have_account_btn.setOnClickListener {
            var intent = Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 0 && data != null){
            imageProfileUri = data?.data
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageProfileUri)
            image_profile_register_activity.setImageBitmap(bitmap)
            select_image_profile_register_activity.alpha = 0f
        }
    }

    private fun RegisterNewUser(){
        // get email and password from user
        email = email_textview_register_activity.text.toString()
        password = password_textview_register_activity.text.toString()
        username = username_textview_register_activity.text.toString()
        // connect to firebase and create new user
        var ref = FirebaseAuth.getInstance()

        if(email!!.trim().isEmpty() || password!!.trim().isEmpty() || username!!.trim().isEmpty()){
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
            StartMainActivity()
            }

    }

    private fun StartMainActivity(){
        var intent = Intent(this@RegisterActivity, MainActivity::class.java)
        // make a transition before exit
        var compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity)
        startActivity(intent,compat.toBundle())
        Log.i("RegisterActivty","MainActivity is ready")
        finish()
    }

    private fun PickProfileImage(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,0)
    }
}
