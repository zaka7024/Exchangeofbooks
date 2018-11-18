package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser

import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.notification_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Notification
import kotlinx.android.synthetic.main.fragment_fragment_activities.*

class FragmentActivities : Fragment() {

    internal var adapter:GroupAdapter<ViewHolder> = GroupAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fragment_activities, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getActivities()
        activitiesRecyclerView.adapter = adapter
        activitiesRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        activitiesRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL ))

        
    }

    fun getActivities(){
        var ref = FirebaseDatabase.getInstance().getReference("notifications/${CurrentUser?.id}")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var temp_list:ArrayList<Notification> = ArrayList()
                adapter.clear()
                p0.children.forEach {
                    var notifi = it.getValue(Notification::class.java)
                    temp_list.add(notifi!!)
                }
                temp_list.reverse()
                temp_list.forEach {
                    adapter.add(notification_item(it))
                }

            }

        })
    }

}
