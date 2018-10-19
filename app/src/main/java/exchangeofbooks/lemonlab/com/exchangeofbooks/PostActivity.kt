package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //set data to spinner
        book_category_spinner.adapter = ArrayAdapter<String>(this@PostActivity,android.R.layout.simple_spinner_dropdown_item
        ,resources.getStringArray(R.array.book_category))
    }
}
