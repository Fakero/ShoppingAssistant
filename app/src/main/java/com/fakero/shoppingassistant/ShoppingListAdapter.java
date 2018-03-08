package com.fakero.shoppingassistant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by fakero on 6.8.2017.
 */

public class ShoppingListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ShoppingListItem> itemList;
    private static DecimalFormat df = new DecimalFormat("#.00");

    public ShoppingListAdapter(Activity activity, List<ShoppingListItem> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int location) {
        return itemList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.shopping_list_item, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.shoppingListItemName);
        TextView price = (TextView) convertView.findViewById(R.id.shoppingListItemPrice);
        TextView amount = (TextView) convertView.findViewById(R.id.shoppingListItemAmount);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.shoppingListItemCheckBox);

        ShoppingListItem item = itemList.get(position);

        name.setText(item.getName());
        price.setText(String.valueOf(df.format(item.getTotalPrice())));
        amount.setText(String.valueOf(item.getAmount()));
        checkBox.setChecked(item.getChecked());

        return convertView;
    }
}
