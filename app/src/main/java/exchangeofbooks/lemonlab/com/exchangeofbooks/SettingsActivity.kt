package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //Chooses theme.
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        // change appbar title
        supportActionBar?.title = "Settings"
    }




    private fun setMode(newMode:String){// pass "ni" for night mode, li for light mode.
        val shrPr=this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(shrPr.edit()){
            remove(getString(R.string.mode))
            putString(getString(R.string.mode), newMode)
            apply()
        }
    }
    private fun modeLightOrNight(){
        val shrPr=this.getPreferences(Context.MODE_PRIVATE) ?: return
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
