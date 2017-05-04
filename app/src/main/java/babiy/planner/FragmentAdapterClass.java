package babiy.planner;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import babiy.planner.fragment.Fragment_Task_Completed;
import babiy.planner.fragment.Fragment_Task_Current;


public class FragmentAdapterClass extends FragmentStatePagerAdapter {

    int TabCount;

    public FragmentAdapterClass(FragmentManager fragmentManager, int CountTabs) {

        super(fragmentManager);

        this.TabCount = CountTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment_Task_Current fragmentTaskCurrentTab = new Fragment_Task_Current();
                return fragmentTaskCurrentTab;

            case 1:
                Fragment_Task_Completed fragmentTaskCompletedTab = new Fragment_Task_Completed();
                return fragmentTaskCompletedTab;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}