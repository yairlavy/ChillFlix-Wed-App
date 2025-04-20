package com.example.androidchillflix.main.components;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidchillflix.R;
import com.example.androidchillflix.main.activities.SearchActivity;

public class TopNavBar {

    private final EditText searchBox;
    private final Activity activity;

    public TopNavBar(Activity activity) {
        this.activity = activity;
        searchBox = activity.findViewById(R.id.editTextText);

        setListeners();
    }

    private void setListeners() {
        searchBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String query = searchBox.getText().toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    navigateToSearch(query);
                } else {
                    Toast.makeText(activity, "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
    }

    // New method to start SearchActivity
    private void navigateToSearch(String query) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra("search_query", query);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
