package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Notification
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.notification_item_image.view.*

class notification_item(var notification:Notification):Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.notification_item_image
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.notification_text_view.text = notification.text
        getUser(notification.from_id,viewHolder)
    }

    fun getUser(id:String,viewHolder: ViewHolder){
        var ref = FirebaseDatabase.getInstance().getReference("users/${id}")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var user = p0.getValue(User::class.java)
                Picasso.get().load(user?.image_profile).into(viewHolder.itemView.notification_image_view)
            }
        })
    }
}