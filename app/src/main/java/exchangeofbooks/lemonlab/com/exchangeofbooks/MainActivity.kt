package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.Fade
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
import com.google.firebase.storage.FirebaseStorage
import com.roughike.bottombar.OnTabSelectListener
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.ChatFragment
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.HomeFragment
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.ProfileFragment
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.UserpostFragment
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    companion object {
        var CurrentUser:User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
        checkIfUserLoged()

        bottom_navigation.setOnTabSelectListener(object :OnTabSelectListener{
            override fun onTabSelected(tabId: Int) {
                when(tabId){
                    R.id.home -> {
                        replaceFragment(HomeFragment())
                        true
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
            R.id.sign_out ->{
                FirebaseAuth.getInstance().signOut()
                CurrentUser = null
                var intent = Intent(this,RegisterActivity::class.java)
                startActivity(intent)
                this.finish()
            }

            R.id.settings_btn ->{
                var intent = Intent(this@MainActivity,SettingsActivity::class.java)
                startActivity(intent)
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

}
