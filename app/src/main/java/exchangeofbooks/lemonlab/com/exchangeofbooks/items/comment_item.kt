package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Comment
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.comment_row_layout.view.*

class comment_item(var comment:Comment):Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.comment_row_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {


        viewHolder.itemView.text_comment_row.text = comment.text
        viewHolder.itemView.username_comment_row.text = CurrentUser?.username
        Picasso.get().load(CurrentUser?.image_profile).into(viewHolder.itemView.image_profile_comment_row)
    }
}
