package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.Profile
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Comment
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.comment_row_layout.view.*

class comment_item(var comment:Comment,var context: Context):Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.comment_row_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val ref = FirebaseDatabase.getInstance().getReference("users/${comment.from_id}")

        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                // get user that posted the comment
                var user_comment:User? = p0.getValue(User::class.java)

                viewHolder.itemView.text_comment_row.text = comment.text
                viewHolder.itemView.username_comment_row.text = user_comment?.username
                Picasso.get().load(user_comment?.image_profile).into(viewHolder.itemView.image_profile_comment_row)

                viewHolder.itemView.setOnClickListener {
                    var intent = Intent(context,Profile::class.java)
                    intent.putExtra(keys.USER_ID,comment.from_id)
                    context.startActivity(intent)
                }
            }

        })

    }
}
