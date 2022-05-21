package it.cnr.istc.msanbot.table;

import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

public class TableModel {

    private List<String> currentDataTable = new ArrayList<>();

    private static TableModel _instance = null;

    private TableModel(){

    }

    public static TableModel getInstance(){
        if(_instance == null){
            _instance = new TableModel();
        }
        return _instance;
    }

    public List<String> getCurrentDataTable() {
        return currentDataTable;
    }

    public void addCurrentTable(String table){
        currentDataTable.add(table);
    }

    public void resetTables(){currentDataTable.clear();}

}
