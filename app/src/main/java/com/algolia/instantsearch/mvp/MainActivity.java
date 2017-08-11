package com.algolia.instantsearch.mvp;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.algolia.instantsearch.events.ErrorEvent;
import com.algolia.instantsearch.events.ResultEvent;
import com.algolia.instantsearch.events.SearchEvent;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.InstantSearchHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_SEARCH_API_KEY = "3d9875e51fbd20c7754e65422f7ce5e1";
    private static final String ALGOLIA_INDEX_NAME = "bestbuy";
    private PopupWindow mPopupWindow;
    private InstantSearchHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Searcher searcher = new Searcher(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
        helper = new InstantSearchHelper(this, searcher);
        helper.search(); // First empty search to display default results
        createPopup();

        final Button buttonFilters = (Button) findViewById(R.id.btn_filter);
        buttonFilters.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mPopupWindow == null) {
                    createPopup();
                    showPopup();
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

            private void showPopup() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mPopupWindow.showAsDropDown(buttonFilters, 0, 0, Gravity.START);
                } else {
                    mPopupWindow.showAtLocation(findViewById(R.id.activity_main), Gravity.CENTER, 0, 0);
                }
            }
        });

        findViewById(R.id.btn_debug).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onRefine(FacetRefinementEvent event) {
        final String text = event.operation + " " + event.attribute + "=" + event.value;
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        Log.e("PLN", text);
    }

    @Subscribe
    public void onSearch(SearchEvent event) {
        final String text = "Search:" + event.query.toString();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        Log.e("PLN", text);
    }

    @Subscribe
    public void onError(ErrorEvent event) {
        final String text = "Error:" + event.error.getMessage();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        Log.e("PLN", text);
    }

    @Subscribe
    public void onResult(ResultEvent event) throws JSONException {
        final String text = "Hits:" + event.content.get("nbHits").toString();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        Log.e("PLN", text);
    }

    private void createPopup() {
        // Create ViewGroup by inflating layout
        View filters = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.filters, null);

        // Register all AlgoliaFacetFilters
        helper.registerFacetFilters((ViewGroup) filters);

        // Create PopupWindow with the filters
        mPopupWindow = new PopupWindow(filters, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPopupWindow.setElevation(5.0f);
        }
    }
}