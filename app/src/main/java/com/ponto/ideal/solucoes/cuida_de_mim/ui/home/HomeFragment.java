package com.ponto.ideal.solucoes.cuida_de_mim.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ponto.ideal.solucoes.cuida_de_mim.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static ViewPager vpclass;
    public static TabLayout tab_vp;
    ConstraintLayout clcontainer;
    FragmentManager fragmentManager;
    public static HomeViewModel homeViewModel;

    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth=FirebaseAuth.getInstance();
        initViews(root);
        return root;
    }

    private void initViews(View v) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        fragmentManager = getChildFragmentManager();
        vpclass = v.findViewById(R.id.vpclass);
        tab_vp = v.findViewById(R.id.tab_vp);
        setupViewPager(vpclass);
        tab_vp.setupWithViewPager (vpclass);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Lista_Medicao(),"Suas Medições");
        adapter.addFragment(new Nova_Medicao(), "AAD Medição");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}