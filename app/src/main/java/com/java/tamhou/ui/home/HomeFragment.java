package com.java.tamhou.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.java.tamhou.MainActivity;
import com.java.tamhou.R;
import com.java.tamhou.SharedPref;
import com.java.tamhou.ui.CategoryFragmentPagerAdapter;
import com.java.tamhou.ui.news.NewsFragment;
import com.java.tamhou.ui.storage.AccountCloud;
import com.java.tamhou.ui.storage.SPUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener {
    static private List<Integer> unusedCategory = new ArrayList<>();
    static private List<Integer> displayedCategory = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private TabLayout mTabs;
    private String[] mTitles ;
    private CategoryFragmentPagerAdapter mAdapter;
    private SharedPref sharedPref;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("enterHome", "Enter Home");
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mTitles = getResources().getStringArray(R.array.main_titles);
        mViewPager = root.findViewById(R.id.viewpager);
        mAdapter = new CategoryFragmentPagerAdapter(getChildFragmentManager());
        sharedPref = new SharedPref(getContext());
        unusedCategory = sharedPref.loadUnusedCategories();
        for (int i = 0; i < mTitles.length; ++i)
            if (!unusedCategory.contains(i)) {
                mAdapter.addFragment(NewsFragment.newInstance(i), mTitles[i]);
                if (!displayedCategory.contains(i))
                    displayedCategory.add(i);
            }
        // Set the pager adapter onto the view pager
        mViewPager.setAdapter(mAdapter);
        // Give the TabLayout the ViewPager
        mTabs = root.findViewById(R.id.sliding_tabs);
        mTabs.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
        // Set gravity for tab bar
        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        for (int i = 0; i < displayedCategory.size(); ++i)
            addMenuLongClickListener(i);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("destroy", "HomeFragment Destroy View");
        sharedPref.setUnusedCategories(unusedCategory);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void addMenuLongClickListener(final int tabIndex) {
        if (mTabs.getChildCount() == 0) return;

        LinearLayout tabStrip = (LinearLayout) mTabs.getChildAt(0);

        final View child = tabStrip.getChildAt(tabIndex);
        // First clean up any listener that might be setup previously
        child.setOnLongClickListener(null);

        child.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(child.getContext(), v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_remove_tab:
                                deleteTab(tabIndex);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popupMenu.getMenu().add(1, R.id.menu_remove_tab, 1, "Remove");
                popupMenu.show();

                return true;
            }
        });
    }

    private void deleteTab(int tabIndex) {
        Log.d(TAG, "deleteTab() - tabIndex: " + tabIndex + " " + displayedCategory.get(tabIndex));

        // Delete a tab from the adapter based on its index
        unusedCategory.add(displayedCategory.get(tabIndex));
        displayedCategory.remove(tabIndex);
        mAdapter.removeFragment(tabIndex);
        // After delete a tab, we need to re-create long-click listener for all tabs.
        // TODO: Here we could improve a little, by instead of re-create long-click listener for all tabs, just create for the new one.
        if (mTabs.getChildCount() > 0) {
            LinearLayout tabStrip = (LinearLayout) mTabs.getChildAt(0);

            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                // re-create the long-click listener
                addMenuLongClickListener(i);
            }
        }
        Toast.makeText(getContext(),"Category deleted", Toast.LENGTH_SHORT).show();
        //sharedPref.setUnusedCategories(unusedCategory);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addCategory:
                if (unusedCategory.size() == 0) {
                    Toast.makeText(getContext(),"No more categories to add", Toast.LENGTH_SHORT).show();
                    return true;
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(this.getContext());
                //builder.setIcon(android.R.drawable.ic_menu_add);
                builder.setTitle("Please choose Categories");
                final String[] itemsId=new String[unusedCategory.size()];
                final boolean[] checkedItems=new boolean[unusedCategory.size()]; //这里的true是默认第几个人已经被选中
                Collections.sort(unusedCategory);
                for (int i = 0; i < unusedCategory.size(); ++i) {
                    itemsId[i] = mTitles[unusedCategory.get(i)];
                    checkedItems[i] = false;
                }
                builder.setMultiChoiceItems(itemsId, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean ischeck) {
                        checkedItems[which]=ischeck;
                    }
                });
                //设置一个确定按钮
                builder.setPositiveButton("OK", null).setCancelable(false);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d("checkedItems", "checkedItems: "+checkedItems);
                        boolean hasSelected=false;
                        for(int i=0;i<itemsId.length;i++)
                        {
                            if (checkedItems[i]){
                                hasSelected = true;
                                mAdapter.addFragment(NewsFragment.newInstance(unusedCategory.get(i)), itemsId[i]);
                                displayedCategory.add(unusedCategory.get(i));
                            }
                        }
                        for (int i = itemsId.length - 1; i >= 0; --i)
                            if (checkedItems[i])
                                unusedCategory.remove(i);
                        if (hasSelected) {
                            LinearLayout tabStrip = (LinearLayout) mTabs.getChildAt(0);
                            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                                // re-create the long-click listener
                                addMenuLongClickListener(i);
                            }
                            //sharedPref.setUnusedCategories(unusedCategory);
                            Toast.makeText(getContext(),"Category added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),"Nothing selected", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        /*if (hasSelected) {
                            Log.d("", "OK");
                            dialog.dismiss();
                        }
                        else {
                            Log.d("", "Didn't choose anything");
                            return;
                        }*/
                    }
                });
                return true;
            case R.id.action_searchKeyword:
                final EditText editText = new EditText(getContext());
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(getContext());
                inputDialog.setTitle("Show me some news about: ").setView(editText).setCancelable(false);
                inputDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                inputDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = editText.getText().toString();
                                Fragment fragment = mAdapter.getItem(mViewPager.getCurrentItem());
                                ((NewsFragment)fragment).setKeyword(s);
                                Toast.makeText(getContext(),"Set keyword \"" + s + "\"", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                return true;
            case R.id.action_clearKeyword:
                Fragment fragment = mAdapter.getItem(mViewPager.getCurrentItem());
                ((NewsFragment)fragment).setKeyword("");
                Toast.makeText(getContext(),"Cleared keyword", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_blockWord:
                EditText editText2 = new EditText(getContext());
                AlertDialog.Builder inputDialog2 = new AlertDialog.Builder(getContext());
                inputDialog2.setTitle("Block news about: ").setView(editText2).setCancelable(false);
                inputDialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                inputDialog2.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = editText2.getText().toString();
                                NewsFragment.setBlockedWord(s);
                                Toast.makeText(getContext(),"Blocked \"" + s + "\"", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                return true;
            case R.id.action_clearBlock:
                NewsFragment.setBlockedWord("");
                Toast.makeText(getContext(),"Cleared block", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_cleanCache:
                //AccountCloud.save("user1", "word1", getContext());
                SPUtils.clear(getContext());
                return true;
            case R.id.action_upload:
                AccountCloud.save(MainActivity.username, MainActivity.password, getContext());
                Toast.makeText(getContext(), "Uploading " + MainActivity.username + "'s data", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_download:
                AccountCloud.load(MainActivity.username, MainActivity.password, getContext());
                Toast.makeText(getContext(), "Downloading " + MainActivity.username + "'s data", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_addCategory);
        item.setEnabled(true);
        item.setVisible(true);
        item = menu.findItem(R.id.action_searchKeyword);
        item.setEnabled(true);
        item.setVisible(true);
        item = menu.findItem(R.id.action_clearKeyword);
        item.setEnabled(true);
        item.setVisible(true);
        item = menu.findItem(R.id.action_blockWord);
        item.setEnabled(true);
        item.setVisible(true);
        item = menu.findItem(R.id.action_cleanCache);
        item.setEnabled(true);
        item.setVisible(true);
        item = menu.findItem(R.id.action_clearBlock);
        item.setEnabled(true);
        item.setVisible(true);
        item = menu.findItem(R.id.action_upload);
        item.setEnabled(true);
        item.setVisible(true);
        item = menu.findItem(R.id.action_download);
        item.setEnabled(true);
        item.setVisible(true);
    }
}