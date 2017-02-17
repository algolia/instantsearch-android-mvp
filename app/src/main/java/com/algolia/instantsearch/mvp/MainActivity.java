package com.algolia.instantsearch.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.InstantSearchHelper;

public class MainActivity extends AppCompatActivity {
    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_SEARCH_API_KEY = "3d9875e51fbd20c7754e65422f7ce5e1";
    private static final String ALGOLIA_INDEX_NAME = "bestbuy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Searcher searcher = new Searcher(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
        final InstantSearchHelper helper = new InstantSearchHelper(this, searcher);
        helper.search(); // First empty search to display default results
    }
}