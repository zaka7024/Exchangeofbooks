package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.util.Log
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.profile_post_row.view.*

class post_profile_item(var post: Post):Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.profile_post_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.post_profile_item_text.text = post.text

        // we will remove this code soon

        /*viewHolder.itemView.action_wishlist_item_btn.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("wishlist/${MainActivity.CurrentUser?.id}/${wish.id}")
            ref.removeValue().addOnCompleteListener {
                Log.i("ProfileFragment","wish deleted from database")
            }
        }*/
    }

}