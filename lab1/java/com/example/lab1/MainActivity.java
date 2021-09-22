package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Change(View view) {
        Fragment fragment=null;
        switch(view.getId()){
            case R.id.button_basic:
                fragment=new BasicFragment();
                break;
            case R.id.button_advanced:
                fragment=new AdvancedFragment();
                break;
        }
        //помещение нужного фрагмента на главный экран
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activityFragment,fragment);
        fragmentTransaction.commit();
    }
}