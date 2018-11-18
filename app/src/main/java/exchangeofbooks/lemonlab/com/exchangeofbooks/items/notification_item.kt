package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Notification
import kotlinx.android.synthetic.main.notification_item_image.view.*

class notification_item(var notification:Notification):Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.notification_item_image
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.notification_text_view.text = notification.text
    }
}