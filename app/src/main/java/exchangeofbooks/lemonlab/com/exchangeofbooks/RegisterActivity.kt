package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.transition.TransitionInflater
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.loadin_layout.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var username:String? = null
    var email:String? = null
    var password:String? = null
    var imageProfileUri: Uri? = null
    internal lateinit var dialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        //Chooses theme.
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= 21){
            var transition_infalter = TransitionInflater.from(this)
            var transition = transition_infalter.inflateTransition(R.transition.transition_rgister_main)
            window.exitTransition = transition
        }


        setContentView(R.layout.activity_register)

        //init
        dialog = ProgressDialog(this@RegisterActivity)

        // check if user is signed in
        if(FirebaseAuth.getInstance().uid != null){
            StartMainActivity()
        }

        // pick image
        select_image_profile_register_activity.setOnClickListener {
            PickProfileImage()
        }

        register_btn_register_activity.setOnClickListener {
            if(imageProfileUri == null){
                val chooseProfileImg=getString(R.string.chooseImg_R)
                Toast.makeText(this@RegisterActivity,chooseProfileImg,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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

    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun RegisterNewUser(){
        // get email and password from user
        email = email_textview_register_activity.text.toString()
        password = password_textview_register_activity.text.toString()
        username = username_textview_register_activity.text.toString()
        // connect to firebase and create new user
        var ref = FirebaseAuth.getInstance()

        if(email!!.trim().isEmpty() || password!!.trim().isEmpty() || username!!.trim().isEmpty()){
            val fillDaFields=getString(R.string.FillFields_L)
            Toast.makeText(this@RegisterActivity,fillDaFields,Toast.LENGTH_SHORT).show()
            return
        }


        val loading=getString(R.string.Loading_L)
        val registeringUser=getString(R.string.Registering_R)
        dialog.setTitle(loading)
        dialog.setMessage(registeringUser)
        dialog.show()
        ref.createUserWithEmailAndPassword(email!!,password!!).addOnCompleteListener {
            Log.i("RegisterActivty","new user sign up")
            Log.i("RegisterActivty","user id: ${ref.uid}")
            SaveImageInTheStorage()
            this@RegisterActivity.finish()
        }

    }

    private fun StartMainActivity(){
        var intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        // make a transition before exit
        var compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity)
        startActivity(intent,compat.toBundle())
        Log.i("RegisterActivty","MainActivity is ready")
        this@RegisterActivity.finish()
    }

    private fun PickProfileImage(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,0)
    }

    private fun SaveImageInTheStorage(){
        if(imageProfileUri == null) return
        var filename = UUID.randomUUID().toString()
        var ref = FirebaseStorage.getInstance().getReference("images/$filename")
        ref.putFile(imageProfileUri!!).addOnSuccessListener {
            Log.i("RegisterActivty","image profile uploaded to firebase storage")
            ref.downloadUrl.addOnSuccessListener {
                Log.i("RegisterActivty","image url $it")
                SaveUserToDatabase(it.toString())
            }
        }
    }

    private fun SaveUserToDatabase(profile_image_url:String){
        var uid = FirebaseAuth.getInstance().uid
        var new_user = User(uid!!,username!!,email!!,profile_image_url)
        var database = FirebaseDatabase.getInstance().getReference("users/$uid")
        database.setValue(new_user).addOnSuccessListener {
            Log.i("RegisterActivty","user saved to firebase")
            dialog.hide()
            StartMainActivity()
        }
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
