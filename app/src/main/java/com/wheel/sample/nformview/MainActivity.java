package com.wheel.sample.nformview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wheel.nebula.BaseAdapter;
import com.wheel.nebula.FormView;

public class MainActivity extends AppCompatActivity {

    String[][] mData = new String[][]{{"1asd", "1asd", "1asd", "1asd"}
            ,{"2ysd", "2asd", "2asd", "北京市"}
            ,{"3gsd", "3asd", "3fsd", "3额sd"}
            ,{"4ksd", "4jsd", "4asd", "4asd"}};
    FormView mFormView;
    BaseAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFormView = (FormView) findViewById(R.id.form_view);

        mAdapter = new MyAdapter(mData);
        mAdapter.setTitle(new String[]{"1", "2", "3", "4"});
        mFormView.setAdapter(mAdapter);
    }



    private static class MyAdapter extends BaseAdapter {
        String[][] mData;
        public MyAdapter(String[][] data) {
            this.mData = data;
        }

        @Override
        public int getRowCount() {
            return mData.length;
        }

        @Override
        public int getColumnCount() {
            return mData[0].length;
        }

        @Override
        public String getCellContent(int rowNumber, int colNumber) {
            return mData[rowNumber][colNumber];
        }

    }

}
