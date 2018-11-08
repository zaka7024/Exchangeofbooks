package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.RegisterActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import kotlinx.android.synthetic.main.option_item_row.view.*

class option_item(var activity:Activity,var optionType:String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.option_item_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.option_text_view_option_row.text = "Sign Out"

        viewHolder.itemView.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            var intent = activity.intent
            intent.putExtra(keys.SIGN_OUT,true)
            activity.setResult(Activity.RESULT_OK,intent)
            activity.finish()
        }
    }
}