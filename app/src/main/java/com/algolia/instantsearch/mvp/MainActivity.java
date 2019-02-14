package com.algolia.instantsearch.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;

public class MainActivity extends AppCompatActivity {
    private static final String ALGOLIA_APP_ID = "TDNMRH8LS3";
    private static final String ALGOLIA_SEARCH_API_KEY = "f3725af1304d27dbc84168d93883a652";
    private static final String ALGOLIA_INDEX_NAME = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Searcher searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
        final InstantSearch helper = new InstantSearch(this, searcher);
        helper.search(); // First empty search to display default results
    }
}