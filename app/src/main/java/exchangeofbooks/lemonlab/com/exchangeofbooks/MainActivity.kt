package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.Fade
import android.transition.Slide
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import exchangeofbooks.lemonlab.com.exchangeofbooks.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it?.itemId){
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                else->{
                    replaceFragment(HomeFragment())
                    true
                }

            }
        }

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
                var intent = Intent(this,RegisterActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
