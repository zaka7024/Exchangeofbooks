package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments


import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exchangeofbooks.lemonlab.com.exchangeofbooks.Adapter.ViewPagerAdapter

import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import kotlinx.android.synthetic.main.fragment_activities.*

class ActivitiesFragment : Fragment() {
    var adapter:ViewPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activities, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ViewPagerAdapter(activity!!.supportFragmentManager)
        adapter!!.addFragment(UserpostFragment(),"USER POST")
        adapter!!.addFragment(FragmentActivities(),"ACTIVATES")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

}
