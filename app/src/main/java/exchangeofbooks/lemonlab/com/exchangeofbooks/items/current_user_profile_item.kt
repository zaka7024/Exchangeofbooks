package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.current_user_post.view.*

class current_user_profile_item(var post:Post):Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.current_user_post
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.current_user_post_text_view.text = post.text
    }

}