package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.PostActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.Profile

import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.post_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList

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

        var adapter = GroupAdapter<ViewHolder>()

        post_recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        post_recyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL ))
        post_recyclerView.adapter = adapter


        var get_post = FirebaseDatabase.getInstance().getReference("posts")
        var up:Post? = null


        // get data from firebase database

        get_post.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                adapter.clear()
                var temp_list:ArrayList<Post>? = ArrayList<Post>()
                p0.children.forEach {
                    up = it.getValue(Post::class.java)
                    temp_list!!.add(up!!)
                }

                // revers post then add it in recyclerView adapter
                temp_list?.reverse()
                Log.i("HomeFragemnt","post size: ${temp_list?.size}")
                if(temp_list != null){
                    temp_list?.forEach {
                        if(it != null)
                            adapter.add(post_item(it,context))
                    }
                }
            }

        })



    }
}
// we wiil use the snackbar to update pur recyclerview when a new post has added to firebase data base

/*
        val vieww=activity.findViewById<View>(R.id.coordinatorLayout)
val snackB= Snackbar.make(vieww, "MSG(eg : new posts)", Snackbar.LENGTH_LONG)
snackB.setAction("Refresh"){
        //if pressed refresh, what to do
}
snackB.setActionTextColor(Color.GREEN)
snackB.show()
 */

