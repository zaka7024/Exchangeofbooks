package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDelegate
import android.transition.Slide
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.roughike.bottombar.OnTabSelectListener
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.ChatFragment
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.HomeFragment
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.ProfileFragment
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.UserpostFragment
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    companion object {
        var CurrentUser:User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Chooses theme, it changes theme in fragments too.
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= 21){
            var slide:Slide = Slide(this,null)
            slide.duration = 700
            //var fade = Fade(this,null)
            //fade.duration = 300
            //window.reenterTransition = fade
            window.enterTransition = slide
        }

        setContentView(R.layout.activity_main)
        checkIfUserLoged()

        var uid = FirebaseAuth.getInstance().uid
        var ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                CurrentUser = p0.getValue(User::class.java)
                Log.i("MainActivity","CurrentUser ->" + CurrentUser?.username)
            }

        })

        val manager = supportFragmentManager.beginTransaction()
        manager.add(R.id.fragment_container_mainActivity,HomeFragment())
        manager.commit()
        bottom_navigation.setOnTabSelectListener(object :OnTabSelectListener{
            override fun onTabSelected(tabId: Int) {
                when(tabId){
                    R.id.main -> {
                        replaceFragment(HomeFragment())
                    }
                    R.id.user_post ->{
                        replaceFragment(UserpostFragment())
                    }
                    R.id.user_profile ->{
                        replaceFragment(ProfileFragment())
                    }
                    R.id.user_chat ->{
                        replaceFragment(ChatFragment())
                    }
                    else->{
                        replaceFragment(HomeFragment())
                        true
                    }

                }
            }

        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            //val value = data.extras!!.getBoolean(keys.SIGN_OUT)
            //Toast.makeText(this,value.toString(),Toast.LENGTH_SHORT).show()
            if(keys.SIGN_OUT == "TRUE"){
                val intent = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(intent)
                keys.SIGN_OUT = "FALSE"
                this.finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun replaceFragment(fragment:Fragment){
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragment_container_mainActivity,fragment)
        manager.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){

            R.id.sign_out_temp->{
                FirebaseAuth.getInstance().signOut()
                var intent = Intent(this,RegisterActivity::class.java)
                startActivity(intent)
                this@MainActivity.finish()
            }

            R.id.settings_btn ->{
                var intent = Intent(this@MainActivity,SettingsActivity::class.java)
                startActivityForResult(intent, 0)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkIfUserLoged(){
        if(FirebaseAuth.getInstance().uid == null){
            var intent = Intent(this,RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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
