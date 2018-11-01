package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.CommentsActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.profile_post_row.view.*

class post_profile_item(var post: Post,var activity: Activity):Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.profile_post_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.post_profile_item_text.text = post.text

        viewHolder.itemView.setOnClickListener {
            var intent = Intent(activity,CommentsActivity::class.java)
            intent.putExtra(keys.POST_ID,post.post_id)
            intent.putExtra(keys.USER_POST_FROM_ID,post.from_id)
            activity.startActivity(intent)
            activity.finish()
        }
    }

}