package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.option_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.option_item_switch
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.option_section_title
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    var adapter:GroupAdapter<ViewHolder> = GroupAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        //Chooses theme.
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        // change appbar title
        supportActionBar?.title = "Settings"

        recyvler_View_settings_activity.adapter = adapter
        recyvler_View_settings_activity.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        setSettingsInRV()
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

    fun setSettingsInRV(){

        //add theme section
        adapter.add(option_section_title("App Theme"))
        // add theme mode option
        adapter.add(option_item_switch("THEME_MODE",this@SettingsActivity))

        //add Social section
        adapter.add(option_section_title("Share App"))
        // add share option
        adapter.add(option_item(this@SettingsActivity,"SHARE_APP"))


        //add faq section
        adapter.add(option_section_title("Help"))

        // add faq option
        adapter.add(option_item(this@SettingsActivity,"FAQ"))
        // add faq option
        adapter.add(option_item(this@SettingsActivity,"USE_IT_IF_YOU_NEED"))


        //add About section
        adapter.add(option_section_title("About Us"))
        // add About option
        adapter.add(option_item(this@SettingsActivity,"ABOUT_US"))

        //add Account section
        adapter.add(option_section_title("Account"))
        // add signout option
        adapter.add(option_item(this@SettingsActivity,"SIGN_OUT"))
    }

    // restart app
    override fun onBackPressed() {
        super.onBackPressed()
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}
