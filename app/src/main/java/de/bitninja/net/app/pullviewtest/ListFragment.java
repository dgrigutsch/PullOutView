package de.bitninja.net.app.pullviewtest;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ListView.OnItemClickListener {

    public static final String DATA_ARRAY = "data_array";

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

    ArrayAdapter<String> mAdapter2;

    ListView listView;

    int views[] = { android.R.id.text1 };
    private String fruits[] = {
            "Apple",
            "Banana",
            "Coconut"
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle.containsKey(DATA_ARRAY) && bundle.getStringArray(DATA_ARRAY)!= null){
            fruits = bundle.getStringArray(DATA_ARRAY);
        }

        listView = (ListView) view.findViewById(R.id.dummy_list);

//        mAdapter = new SimpleCursorAdapter(getActivity(),
//                android.R.layout.simple_list_item_1, null,
//                fruits, views, 0);

        mAdapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,fruits);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
//        getLoaderManager().initLoader(0, null, this);
//        listView.setAdapter(mAdapter);
        listView.setAdapter(mAdapter2);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}