package exchangeofbooks.lemonlab.com.exchangeofbooks.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.AsyncTask



class ViewPagerAdapter(var manager:FragmentManager):FragmentPagerAdapter(manager) {

    var fragmentsList = ArrayList<Fragment>()
    var titileList = ArrayList<String>()

    override fun getItem(p0: Int): Fragment {
        return fragmentsList[p0]
    }

    override fun getCount(): Int {
        return fragmentsList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titileList[position]
    }

    fun addFragment(frgmanet:Fragment,title:String){
        fragmentsList.add(frgmanet)
        titileList.add(title)
    }



}