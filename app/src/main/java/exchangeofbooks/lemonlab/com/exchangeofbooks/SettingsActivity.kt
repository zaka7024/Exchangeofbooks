package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.option_item_switch
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
        // add theme mode option
        adapter.add(option_item_switch("theme_mode",this@SettingsActivity))
    }
}
