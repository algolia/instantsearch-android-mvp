package com.algolia.instantsearch.mvp;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.InstantSearchHelper;

public class MainActivity extends AppCompatActivity {
    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_SEARCH_API_KEY = "3d9875e51fbd20c7754e65422f7ce5e1";
    private static final String ALGOLIA_INDEX_NAME = "bestbuy";
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Searcher searcher = new Searcher(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
        InstantSearchHelper helper = new InstantSearchHelper(this, searcher);
        helper.search(); // First empty search to display default results

        final Button buttonFilters = (Button) findViewById(R.id.btn_filter);
        buttonFilters.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mPopupWindow == null) {
                    createPopup();
                } else {
                    togglePopup();
                }
            }

            private void togglePopup() {
                if (!mPopupWindow.isShowing()) {
                    showPopup();
                } else {
                    mPopupWindow.dismiss();
                }
            }

            private void createPopup() {
                // Create ViewGroup by inflating layout
                View filters = ((LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.filters, null);

                // Link checkbox to Searcher
                CheckBox boxShipping = (CheckBox) filters.findViewById(R.id.checkbox_shipping);
                searcher.addFacet("shipping");
                boxShipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        searcher.updateFacetRefinement("shipping", "Free shipping", isChecked)
                                .search();
                    }
                });

                // Create PopupWindow with the filters
                mPopupWindow = new PopupWindow(filters, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mPopupWindow.setElevation(5.0f);
                }
                showPopup();
            }

            private void showPopup() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mPopupWindow.showAsDropDown(buttonFilters, 0, 0, Gravity.START);
                } else {
                    mPopupWindow.showAtLocation(findViewById(R.id.activity_main), Gravity.CENTER, 0, 0);
                }
            }
        });
    }
}