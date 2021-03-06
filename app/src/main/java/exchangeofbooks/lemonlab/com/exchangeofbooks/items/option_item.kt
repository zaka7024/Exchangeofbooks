package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.FaqActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.SettingsActivity
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
                var dialog = AlertDialog.Builder(activity)
                dialog.setTitle("About us")
                dialog.setMessage("Lemon Lab Team")
                dialog.show()
                dialog.setPositiveButton("OK",DialogInterface.OnClickListener{
                    dialog, which ->
                    dialog.dismiss()
                })
            }
        }else if(optionType == "FAQ"){
            viewHolder.itemView.option_text_view_option_row.text = "FAQ"

            viewHolder.itemView.setOnClickListener {
            //    Toast.makeText(activity,"FAQ",Toast.LENGTH_SHORT).show()
                val i=Intent(activity, FaqActivity::class.java)
                startActivity(activity, i, null)
            }
        }else if(optionType == "SHARE_APP"){
            viewHolder.itemView.option_text_view_option_row.text = "Share"

            viewHolder.itemView.setOnClickListener {
                var intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                activity.startActivity(Intent.createChooser(intent,"شارك لـ"))
            }
        }

    }
}