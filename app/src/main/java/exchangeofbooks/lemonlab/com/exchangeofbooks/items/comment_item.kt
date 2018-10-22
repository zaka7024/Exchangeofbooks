package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
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


        var ref = FirebaseDatabase.getInstance().getReference("users/${comment.from_id}")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var user = p0.getValue(User::class.java)
                Log.i("comment_item","comment user: ${user}")
                    viewHolder.itemView.username_comment_row.text = user?.username
                Picasso.get().load(user?.image_profile).into(viewHolder.itemView.image_profile_comment_row)

            }
        })
    }
}
