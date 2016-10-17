package navigation.garciano.com.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by admin on 7/25/2016.
 */
public class Approval extends Fragment {
    View rootView;

    private ExpandableListView expandableListView;
    private List<String> parentHeaderInformation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_listview, container, false);


//        parentHeaderInformation = new ArrayList<String>();
//
//        parentHeaderInformation.add("Cars");
//
//        parentHeaderInformation.add("Houses");
//
//        parentHeaderInformation.add("Football Clubs");
//
//        HashMap<String, List<String>> allChildItems = returnGroupedChildItems();
//
//        expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
//
//        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getApplicationContext(), parentHeaderInformation, allChildItems);
//
//        expandableListView.setAdapter(expandableListViewAdapter);

        return rootView;
    }
//
//    private HashMap<String, List<String>> returnGroupedChildItems(){
//
//        HashMap<String, List<String>> childContent = new HashMap<String, List<String>>();
//
//        List<String> cars = new ArrayList<String>();
//
//        cars.add("Volvo");
//
//        cars.add("BMW");
//
//        cars.add("Toyota");
//
//        cars.add("Nissan");
//
//        List<String> houses = new ArrayList<String>();
//
//        houses.add("Duplex");
//
//        houses.add("Twin Duplex");
//
//        houses.add("Bungalow");
//
//        houses.add("Two Storey");
//
//        List<String> footballClubs = new ArrayList<String>();
//
//        footballClubs.add("Liverpool");
//
//        footballClubs.add("Arsenal");
//
//        footballClubs.add("Stoke City");
//
//        footballClubs.add("West Ham");
//
//        childContent.put(parentHeaderInformation.get(0), cars);
//
//        childContent.put(parentHeaderInformation.get(1), cars);
//
//        childContent.put(parentHeaderInformation.get(2), cars);
//
//        return childContent;
//
//    }



}
