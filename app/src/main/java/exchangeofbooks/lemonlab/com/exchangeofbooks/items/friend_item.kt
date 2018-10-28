package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.user_chat_row.view.*

class friend_item(var user: User):Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_chat_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(user?.image_profile).into(viewHolder.itemView.user_image_profile_chat_fragment)
        viewHolder.itemView.friend_name_chat_fragment.text = user?.username
    }
}