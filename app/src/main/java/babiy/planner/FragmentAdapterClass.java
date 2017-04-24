package babiy.planner;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import babiy.planner.fragment.Completed;
import babiy.planner.fragment.Current;


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
                Current currentTab = new Current();
                return currentTab;

            case 1:
                Completed completedTab = new Completed();
                return completedTab;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}