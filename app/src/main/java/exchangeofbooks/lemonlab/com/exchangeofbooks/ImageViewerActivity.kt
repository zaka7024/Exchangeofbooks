package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import kotlinx.android.synthetic.main.activity_image_viewer.*

class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        var imageUrl = intent.extras.getString(keys.IMAGE_VIEWER)
        if(imageUrl!=null)
            Picasso.get().load(imageUrl).into(image_view_imave_viewer_activity)
    }
}
