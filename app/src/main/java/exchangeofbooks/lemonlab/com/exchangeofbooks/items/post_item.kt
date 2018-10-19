package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.post_row_layout.view.*

class post_item():Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.post_row_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        /*var ref = FirebaseDatabase.getInstance().getReference("users/${user_post?.from_id}")
        var user: User? =null
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(User::class.java)
            }

        })
        viewHolder.itemView.post_username.text = user?.username*/
    }
}