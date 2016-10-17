package navigation.garciano.com.navigation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by admin on 7/18/2016.
 */
public class ReturnItem extends android.support.v4.app.Fragment {
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_return,container,false);

        BarChart salesChart = (BarChart) rootView.findViewById(R.id.chartSales2);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1100f, 0));
        entries.add(new BarEntry(200f, 1));
        entries.add(new BarEntry(3000f, 2));
        entries.add(new BarEntry(4050f, 3));
        entries.add(new BarEntry(5500f, 4));
        entries.add(new BarEntry(600f, 5));


        BarDataSet dataset = new BarDataSet(entries, "# of Sales");
        dataset.setValueTextSize(30f);


        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Banilad");
        labels.add("Colon");
        labels.add("Pardo");
        labels.add("Mambaling");
        labels.add("Mandaue");
        labels.add("Lapu-Lapu");


        /* for create Grouped Bar chart
        ArrayList<BarEntry> group1 = new ArrayList<>();
        group1.add(new BarEntry(4f, 0));
        group1.add(new BarEntry(8f, 1));
        group1.add(new BarEntry(6f, 2));
        group1.add(new BarEntry(12f, 3));
        group1.add(new BarEntry(18f, 4));
        group1.add(new BarEntry(9f, 5));

        ArrayList<BarEntry> group2 = new ArrayList<>();
        group2.add(new BarEntry(6f, 0));
        group2.add(new BarEntry(7f, 1));
        group2.add(new BarEntry(8f, 2));
        group2.add(new BarEntry(12f, 3));
        group2.add(new BarEntry(15f, 4));
        group2.add(new BarEntry(10f, 5));

        BarDataSet barDataSet1 = new BarDataSet(group1, "Group 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(group2, "Group 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);
        */

        BarData data = new BarData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        salesChart.setData(data);
        salesChart.invalidate();
//        BarChart.invalidate();
        //barChart.setBackgroundColor(Color.rgb(0, 155, 0));
        salesChart.setDescription("This is the total items on stock in each branch");
        salesChart.setDescriptionTextSize(33f);
        salesChart.animateY(5000);
        salesChart.isHorizontalScrollBarEnabled();
        ;

        return rootView;

    }

}
