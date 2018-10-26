package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.util.Log
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.wish
import kotlinx.android.synthetic.main.wishlist_item_row.view.*

class wish_item_others(var wish: wish):Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.wishlist_item_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.action_wishlist_item_btn.visibility = View.GONE
        viewHolder.itemView.wish_item_text.text = wish.text

        viewHolder.itemView.action_wishlist_item_btn.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("wishlist/${MainActivity.CurrentUser?.id}/${wish.id}")
            ref.removeValue().addOnCompleteListener {
                Log.i("ProfileFragment","wish deleted from database")
            }
        }
    }

}