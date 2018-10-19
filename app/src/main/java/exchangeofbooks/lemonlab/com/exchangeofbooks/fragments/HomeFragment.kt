package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.PostActivity

import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.post_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //add new post
        add_post_btn.setOnClickListener {
            var intent = Intent(context,PostActivity::class.java)
            startActivity(intent)
        }

        var ref = FirebaseDatabase.getInstance().getReference("posts/${FirebaseAuth.getInstance().uid}").push()
        var id = UUID.randomUUID().toString()
        ref.setValue(Post(id,FirebaseAuth.getInstance().uid!!,"hello","10","12","",""))
        /*var get_post = FirebaseDatabase.getInstance().getReference("posts/${FirebaseAuth.getInstance().uid}")
        var up:Post? = null
        get_post.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    up = it.getValue(Post::class.java)
                }
            }

        })*/

        var adapter = GroupAdapter<ViewHolder>()
        adapter.add(post_item())
        adapter.add(post_item())
        adapter.add(post_item())
        adapter.add(post_item())

        post_recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        post_recyclerView.adapter = adapter
    }
}
