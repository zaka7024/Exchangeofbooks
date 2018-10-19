package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.post_row_layout.view.*

class post_item(var post:Post?):Item<ViewHolder>() {
    var user:User? = null

    override fun getLayout(): Int {
        return R.layout.post_row_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.pots_post_textview.text = post?.text
        // load image from cash or picasso
        Picasso.get().load(post?.post_image).into(viewHolder.itemView.post_image_view)
        if(user != null){
            viewHolder.itemView.post_username.text = user?.username
            Picasso.get().load(user?.image_profile).into(viewHolder.itemView.post_image_profile)
        }

    }
}