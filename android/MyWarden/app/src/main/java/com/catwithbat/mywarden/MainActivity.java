package com.catwithbat.mywarden;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.catwithbat.mywarden.fragments.FragmentWorkDay;
import com.catwithbat.mywarden.drawerutils.Utils;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;

public class MainActivity extends ActionBarActivity {

    private boolean isWorking;

    private Drawer.Result drawerResult = null;
    private AccountHeader.Result headerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDrawer);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.frag_work_name);
        setSupportActionBar(toolbar);
        headerResult = Utils.getAccountHeader(MainActivity.this, savedInstanceState);
        drawerResult = Utils.createCommonDrawer(MainActivity.this, toolbar, headerResult);
        drawerResult.setSelectionByIdentifier(1, false); // Set proper selection

// Покажем drawer автоматически при запуске
       // drawerResult.openDrawer();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentWorkDay(), FragmentWorkDay.TAG).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen())
            // Закрываем меню, если оно показано и при этом нажата системная кнопка "Назад"
            drawerResult.closeDrawer();
        //Фрагмент у нас в стеке один, следовательно если не находится фрагмента по тегу, значит в стеке другой.
        if(getSupportFragmentManager().findFragmentByTag(FragmentWorkDay.TAG) == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentWorkDay(), FragmentWorkDay.TAG).commit();
        else
            super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("isWorking", true);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        isWorking = savedInstanceState.getBoolean("isWorking");
    }

    public void setWorking(boolean b){
        isWorking = b;
    }

    public boolean isWorking(){
        return isWorking;
    }
}
