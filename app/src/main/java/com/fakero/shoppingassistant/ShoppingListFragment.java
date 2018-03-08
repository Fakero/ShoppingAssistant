package com.fakero.shoppingassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingListFragment.OnItemSelectedListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static DecimalFormat df = new DecimalFormat("#.00");

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnItemSelectedListener mListener;

    ShoppingListAdapter adapter;
    private ListView listView;
    private EditText totalText;

    public ShoppingListFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance(String param1) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        listView = (ListView) view.findViewById(R.id.shoppingListListView);
        totalText = (EditText) view.findViewById(R.id.shoppingCartTotalEditText);
        adapter = new ShoppingListAdapter(getActivity(), ShoppingListContent.getShoppingList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogUpdateListItem(view, i);
                Log.d("ShoppingListFragment", "onItemClick, i: " + String.valueOf(i));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemSelected(i);
                return false;
            }
        });
        FloatingActionButton newItemButton = (FloatingActionButton) view.findViewById(R.id.addNewShoppingListItem);
        newItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddNewItemToList();
                Log.d("ShoppingListFragment", "onClick");
            }
        });
        Button emptyCart = (Button) view.findViewById(R.id.shoppingCartEmptyButton);
        emptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShoppingListContent.getShoppingList().size() > 0) {
                    for (int i = 0; i < ShoppingListContent.getShoppingList().size(); i++) {
                        ShoppingListItem item = ShoppingListContent.getItemFromList(i);
                        if (item.getChecked()) {
                            item.setChecked(false);
                        }
                        ShoppingListContent.modifyListItem(i, item);
                    }
                    totalText.setText("");
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void onListItemSelected(int position) {
        if (mListener != null) {
            mListener.onItemSelect(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            mListener = (OnItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnItemSelectedListener {
        void onItemSelect(int position);
    }

    public void dialogAddNewItemToList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_shopping_list, null))
                .setTitle(getString(R.string.dialogNewItemTitle))
                .setPositiveButton(getString(R.string.btnAdd), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog dialog = (Dialog) dialogInterface;
                        final EditText name = (EditText) dialog.findViewById(R.id.dialogItemNameEditText);
                        final EditText price = (EditText) dialog.findViewById(R.id.dialogItemPriceEditText);
                        final EditText amount = (EditText) dialog.findViewById(R.id.dialogItemAmountEditText);
                        String itemName;
                        double itemPrice;
                        int itemAmount;
                        if (name.getText().length() != 0) {
                            itemName = name.getText().toString();
                            if (!itemName.matches("[a-zA-ZÄ-Öä-öåÅ0-9.@,?/: _-]*")) {
                                Toast.makeText(getContext(), "Sallitut erikoismerkit: @ . _ - , ? / :", Toast.LENGTH_LONG).show();
                            }
                            else if (Double.parseDouble(price.getText().toString()) > 999.99) {
                                Toast.makeText(getContext(), getString(R.string.toastHighPrice), Toast.LENGTH_LONG).show();
                            }
                            else if (Integer.parseInt(amount.getText().toString()) > 999) {
                                Toast.makeText(getContext(), getString(R.string.toastHighAmount), Toast.LENGTH_LONG).show();
                            }
                            else {
                                if (price.getText().length() != 0) {
                                    itemPrice = Double.parseDouble(price.getText().toString());
                                }
                                else {
                                    itemPrice = 0.0;
                                }
                                if (amount.getText().length() != 0) {
                                    itemAmount = Integer.valueOf(amount.getText().toString());
                                }
                                else {
                                    itemAmount = 1;
                                }
                                ShoppingListItem newItem = new ShoppingListItem(itemName, itemPrice, itemAmount);
                                ShoppingListContent.addItemToList(newItem);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .setNeutralButton(getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    public void dialogUpdateListItem(View view, final int position) {
        ShoppingListItem originalItem = ShoppingListContent.getItemFromList(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_shopping_list, null);
        final ShoppingListItem modifiedItem;
        final EditText name = (EditText) dialogView.findViewById(R.id.dialogItemNameEditText);
        final EditText price = (EditText) dialogView.findViewById(R.id.dialogItemPriceEditText);
        final EditText amount = (EditText) dialogView.findViewById(R.id.dialogItemAmountEditText);
        if (originalItem != null) {
            modifiedItem = originalItem;
            name.setText(originalItem.getName());
            price.setText(String.valueOf(originalItem.getPrice()));
            amount.setText(String.valueOf(originalItem.getAmount()));
        }
        else {
            modifiedItem = null;
        }
        builder.setView(dialogView)
                .setTitle(getString(R.string.dialogModifyListItemTitle))
                .setPositiveButton(getString(R.string.btnSave), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String itemName;
                        double itemPrice;
                        int itemAmount;
                        if (name.getText().length() != 0) {
                            itemName = name.getText().toString();
                            if (!itemName.matches("[a-zA-ZÄ-Öä-öåÅ0-9.@,?/: _-]*")) {
                                Toast.makeText(getContext(), "Sallitut erikoismerkit: @ . _ - , ? / :", Toast.LENGTH_LONG).show();
                            }
                            else if (Double.parseDouble(price.getText().toString()) > 999.99) {
                                Toast.makeText(getContext(), getString(R.string.toastHighPrice), Toast.LENGTH_LONG).show();
                            }
                            else if (Integer.parseInt(amount.getText().toString()) > 999) {
                                Toast.makeText(getContext(), getString(R.string.toastHighAmount), Toast.LENGTH_LONG).show();
                            }
                            else {
                                if (price.getText().length() != 0) {
                                    itemPrice = Double.parseDouble(price.getText().toString());
                                }
                                else {
                                    itemPrice = 0.0;
                                }
                                if (amount.getText().length() != 0) {
                                    itemAmount = Integer.valueOf(amount.getText().toString());
                                }
                                else {
                                    itemAmount = 1;
                                }
                                if (modifiedItem != null) {
                                    modifiedItem.setName(itemName);
                                    modifiedItem.setAmount(itemAmount);
                                    modifiedItem.setPrice(itemPrice);
                                    ShoppingListContent.modifyListItem(position, modifiedItem);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                })
                .setNegativeButton(getString(R.string.btnDelete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (modifiedItem != null) {
                            ShoppingListContent.removeItemFromList(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNeutralButton(getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    public void notifyList() {
        Log.d("ShoppingListFragment", "notifyList");
        if (ShoppingListContent.getShoppingList().size() > 0) {
            double total = 0;
            for (ShoppingListItem item : ShoppingListContent.getShoppingList()) {
                if (item.getChecked()) {
                    total += item.getTotalPrice();
                }
            }
            if (total > 0) {
                totalText.setText(df.format(total));
            }
            else {
                totalText.setText("");
            }
        }
        adapter.notifyDataSetChanged();
    }
}
