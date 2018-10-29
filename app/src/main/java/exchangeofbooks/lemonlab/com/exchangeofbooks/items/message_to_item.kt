package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Message
import kotlinx.android.synthetic.main.activity_chat_log.view.*
import kotlinx.android.synthetic.main.message_to_row.view.*

class message_to_item(var message:Message): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_to_text_view.setText(message.text)
    }
}