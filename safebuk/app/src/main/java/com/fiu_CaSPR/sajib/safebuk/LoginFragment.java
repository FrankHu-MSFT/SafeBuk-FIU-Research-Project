package com.fiu_CaSPR.sajib.safebuk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiu_CaSPR.sajib.safebuk.R;

/**
 * Created by ivan.minev on 22.1.2015 г..
 */
public class LoginFragment extends Fragment
{


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.login_screen_view, container, false);
    }
}
