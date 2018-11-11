package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
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

        if(optionType == "SIGN_OUT"){
            viewHolder.itemView.option_text_view_option_row.text = "Sign Out"

            viewHolder.itemView.setOnClickListener {
                val intent = activity.intent
                keys.SIGN_OUT = "TRUE"
                //intent.putExtra(keys.SIGN_OUT,true)
                activity.setResult(Activity.RESULT_OK,intent)
                FirebaseAuth.getInstance().signOut()
                activity.finish()
            }
        }else if(optionType == "ABOUT_US"){

            viewHolder.itemView.option_text_view_option_row.text = "About us"

            viewHolder.itemView.setOnClickListener {
                Toast.makeText(activity,"ABOUT_US",Toast.LENGTH_SHORT).show()
            }
        }else if(optionType == "HELP"){
            viewHolder.itemView.option_text_view_option_row.text = "FAQ"

            viewHolder.itemView.setOnClickListener {
                Toast.makeText(activity,"FAQ",Toast.LENGTH_SHORT).show()
            }
        }else if(optionType == "SHARE_APP"){
            viewHolder.itemView.option_text_view_option_row.text = "Share"

            viewHolder.itemView.setOnClickListener {
                Toast.makeText(activity,"Share",Toast.LENGTH_SHORT).show()
            }
        }

    }
}