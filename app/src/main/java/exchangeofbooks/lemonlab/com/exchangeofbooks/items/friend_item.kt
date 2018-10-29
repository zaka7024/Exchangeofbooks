package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.ChatLogActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.user_chat_row.view.*

class friend_item(var user: User,var context: Context):Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_chat_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(user?.image_profile).into(viewHolder.itemView.user_image_profile_chat_fragment)
        viewHolder.itemView.friend_name_chat_fragment.text = user?.username

        viewHolder.itemView.setOnClickListener {
            var intent = Intent(context,ChatLogActivity::class.java)
            intent.putExtra(keys.USER_ID,user.id)
            intent.putExtra(keys.USER_NAME,user.username)
            context.startActivity(intent)
        }
    }
}