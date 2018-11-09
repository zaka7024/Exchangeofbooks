package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.SettingsActivity
import kotlinx.android.synthetic.main.option_switch_row.view.*

class option_item_switch(var type:String,var activity:Activity):Item<ViewHolder>() {

    val shrPr=activity.getSharedPreferences("mode", Context.MODE_PRIVATE)
    override fun getLayout(): Int {
        return R.layout.option_switch_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.option_text_view.text = "Dark Mode"
        var mode = shrPr.getString(activity.getString(exchangeofbooks.lemonlab.com.exchangeofbooks.R.string.mode),"li")

        if(mode == "ni")
            viewHolder.itemView.switch_btn_switch_item.isChecked = true
        else
            viewHolder.itemView.switch_btn_switch_item.isChecked = false

        viewHolder.itemView.switch_btn_switch_item.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    setMode("ni")
                }
                else if(!isChecked)
                    setMode("li")
                var intetn = Intent(activity,SettingsActivity::class.java)
                activity.startActivity(intetn)
                activity.finish()
            }

        })
    }

    private fun setMode(newMode:String){// pass "ni" for night mode, li for light mode.
        with(shrPr.edit()){
            remove(activity.getString(exchangeofbooks.lemonlab.com.exchangeofbooks.R.string.mode))
            putString(activity.getString(exchangeofbooks.lemonlab.com.exchangeofbooks.R.string.mode), newMode)
            apply()
        }

    }


}