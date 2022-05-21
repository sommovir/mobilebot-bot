package it.cnr.istc.msanbot.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.cnr.istc.msanbot.R;
import it.cnr.istc.msanbot.logic.MediaEventListener;
import it.cnr.istc.msanbot.table.TableModel;

public class SlideAdapter extends PagerAdapter implements MediaEventListener {

    private Context context;
    private List<String> currentDataTable;
    private Map<String, Boolean> colorCellMap = new HashMap<>();

    public SlideAdapter(Context context) {
        this.context = context;
        this.currentDataTable = TableModel.getInstance().getCurrentDataTable();
        System.out.println("SLIDE CONSTRUCTOR");
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return currentDataTable.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position  The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_dialog, container, false);

        TableLayout tableLayout = view.findViewById(R.id.tableLayout);


        String table = this.currentDataTable.get(position);
        System.out.println("Posizione: " + position + "\nTable: " + currentDataTable.get(position));
        String[] rows = table.split("<ROW>");

        /*
        int row = 0;
        boolean continueTable = false;
        for (String d : rows) {
            String[] split = d.split("<CELL>");
            TableRow tableRow = new TableRow(context);
            tableRow.setPadding(3, 3, 3, 3);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams
                    (0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            layoutParams.setMargins(3, 3, 3, 3);
            tableRow.setLayoutParams(layoutParams);

            for (final String cella : split) {
                final TextView textView1 = new TextView(context);
                final String cellText;
                if (row == 0) {
                    textView1.setTypeface(null, Typeface.BOLD);
                    if (cella.contains("<CONTINUE>")) {
                        cellText = cella.replace("<CONTINUE>", "");
                        continueTable = true;
                    } else {
                        cellText = cella;
                    }
                } else {
                    cellText = cella;
                    GradientDrawable gd = new GradientDrawable(
                            //  GradientDrawable.Orientation.TOP_BOTTOM,
                            // new int[] {0xFFe5edef,0xFFcedde0});
                    );
                    // gd.setCornerRadius(6);
                    gd.setColor(0xFFe9eca0);  // #e9eca0
                    gd.setStroke(1, 0xFF000000);
                    textView1.setBackground(gd);
                }
                textView1.setPadding(5, 5, 5, 5);
                // textView1.setLayoutParams(new TableRow.LayoutParams
                //         (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                textView1.setTextSize(18);
                textView1.setText(cellText);
                colorCellMap.put("" + cellText, Boolean.FALSE);
                if (row != 0) {
                    textView1.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         if (colorCellMap.get("" + cellText)) {
                                                             System.out.println("TRUE");
                                                             textView1.setBackgroundColor(0xFFFFFFFF);
                                                             colorCellMap.put("" + cellText, Boolean.FALSE);
                                                         } else {
                                                             System.out.println("FALSE");
                                                             textView1.setBackgroundColor(0xFF00FF00);
                                                             colorCellMap.put("" + cellText, Boolean.TRUE);
                                                         }

                                                     }
                                                 }

                    );
                }


                tableRow.addView(textView1, layoutParams);

            }
            row++;
            tableLayout.addView(tableRow);
        }

         */

        container.addView(view);


        System.out.println("---------- TABLE FATTA EPICOOOOOOOO ----------");

        String tablehtml = "<table style=\"border: 1px solid black;\">";
        for(String r: rows){
            String riga = "<tr>";
            String[] cells = r.split("<CELL>");
            for(String c : cells){
                riga += "<td>" + c + "</td>";
            }
            riga += "</tr>";
            tablehtml += riga;
        }
        tablehtml += "</table>";

        System.out.println(tablehtml);

        TextView textView = view.findViewById(R.id.testoepico);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(tablehtml, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(tablehtml));
        }

        return view;


    }

    @Override
    public void showYoutubeVideoOnRobot(String videoLink) {

    }

    @Override
    public void showImageOnRobot(String imageLink) {

    }

    @Override
    public void showLinkOnRobot(String link) {

    }

    @Override
    public void showTableOnRobot(String table) {

    }

    @Override
    public void showCurrentTable() {

    }
}
