package com.nebula.sample.nformview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nebula.wheel.FormCell;
import com.nebula.wheel.StringFormCell;
import com.nebula.wheel.FormView;

public class MainActivity extends AppCompatActivity {

    static String[][] mData = new String[][]{{"1asd", "1asd", "1asd", "1asd", "2ysd", "2asd", "2asd", "北京市"}
            , {"2ysd", "2asd", "2asd", "北京市", "2ysd", "2asd", "2asd", "北京市"}
            , {"3gsd", "3asd", "3fsd", "3额sd", "2ysd", "2asd", "2asd", "北京市"}
            , {"3gsd", "3asd", "3fsd", "3额sd", "2ysd", "2asd", "2asd", "北京市"}
            , {"3gsd", "3asd", "3fsd", "3额sd", "2ysd", "2asd", "2asd", "北京市"}
            , {"1asd", "1asd", "1asd", "1asd", "2ysd", "2asd", "2asd", "北京市"}
            , {"1asd", "1asd", "1asd", "1asd", "2ysd", "2asd", "2asd", "北京市"}
            , {"1asd", "1asd", "1asd", "1asd", "2ysd", "2asd", "2asd", "北京市"}
            , {"1asd", "1asd", "1asd", "1asd", "2ysd", "2asd", "2asd", "北京市"}
            , {"1asd", "1asd", "1asd", "1asd", "2ysd", "2asd", "2asd", "北京市"}
            , {"1asd", "1asd", "1asd", "1asd", "2ysd", "2asd", "2asd", "北京市"}
            , {"1asd", "1asd", "1asd", "1asd", "2ysd", "2asd", "2asd", "北京市"}
            , {"4ksd", "4jsd", "4asd", "4asd", "2ysd", "2asd", "2asd", "北京市"}};
    FormView mFormView;
    FormView.BaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFormView = (FormView) findViewById(R.id.form_view);

        mAdapter = new MyAdapter(this, mData);
        mFormView.setAdapter(mAdapter);
//        startActivity(new Intent(this,ScrollingActivity.class));
    }


    static class MyAdapter extends FormView.BaseAdapter {
        String[][] mData;
        Context mContext;

        public MyAdapter(Context context, String[][] data) {
            this.mContext = context;
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
        public StringFormCell createCell(int rowNumber, int colNumber) {
            return new StringFormCell(mContext);
        }

        @Override
        public void bindCell(final FormCell cell, int rowNumber, int columnNumber) {
            StringFormCell stringFormCell = (StringFormCell) cell;
            stringFormCell.setContent(mData[rowNumber][columnNumber]);
            stringFormCell.setOnCellClickListener(new FormCell.OnCellClickListener() {
                @Override
                public void onCellClick(FormCell formCell) {
                    StringFormCell cell1 = (StringFormCell) formCell;
//                    Toast.makeText(mContext, cell1.getContent(), Toast.LENGTH_SHORT).show();
                    Log.e("asd", cell1.getContent());
                }
            });
        }

    }

}
