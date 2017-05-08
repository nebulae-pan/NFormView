package com.nebula.sample.nformview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.nebula.wheel.AbsFormCell;
import com.nebula.wheel.BaseAdapter;
import com.nebula.wheel.FormCell;
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
    BaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFormView = (FormView) findViewById(R.id.form_view);

        mAdapter = new MyAdapter(this, mData);
        mFormView.setAdapter(mAdapter);
//        startActivity(new Intent(this,ScrollingActivity.class));
    }


    static class MyAdapter extends BaseAdapter<FormCell> {
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
            return 3;
        }

        @Override
        public FormCell createCell(int rowNumber, int colNumber) {
            return new FormCell(mContext);
        }

        @Override
        public void bindCell(final FormCell cell, int rowNumber, int columnNumber) {
            cell.setContent(mData[rowNumber][columnNumber]);
            cell.setOnCellClickListener(new AbsFormCell.OnCellClickListener() {
                @Override
                public void onCellClick(AbsFormCell formCell) {
                    FormCell cell1 = (FormCell) formCell;
//                    Toast.makeText(mContext, cell1.getContent(), Toast.LENGTH_SHORT).show();
                    Log.e("asd", cell1.getContent());
                }
            });
        }

    }

}
