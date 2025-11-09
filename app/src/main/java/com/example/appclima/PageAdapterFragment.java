package com.example.appclima;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapterFragment extends FragmentStatePagerAdapter {

    private int numeroPaginas;
    private ClimaFragment climaFragment;
    private MapaFragment sobreFragment;

    public PageAdapterFragment(FragmentManager fragmentManager, int numeroPaginas) {
        super(fragmentManager);
        this.numeroPaginas = numeroPaginas;
        criaFragment();
    }

    private void criaFragment() {
        climaFragment = new ClimaFragment();
        sobreFragment = new MapaFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return climaFragment;
            case 1:
                return sobreFragment;

        }
        return climaFragment;
    }

    @Override
    public int getCount() {
        return numeroPaginas;
    }
}
