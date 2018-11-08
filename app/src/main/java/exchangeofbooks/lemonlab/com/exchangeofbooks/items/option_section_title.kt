package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import kotlinx.android.synthetic.main.option_title_row.view.*

class option_section_title(var title:String):Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.option_title_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.option_section_text_view.text = title
    }
}