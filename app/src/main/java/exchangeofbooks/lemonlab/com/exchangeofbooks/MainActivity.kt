package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.Menu

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
    }


}
