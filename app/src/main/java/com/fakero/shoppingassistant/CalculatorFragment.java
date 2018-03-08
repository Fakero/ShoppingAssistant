package com.fakero.shoppingassistant;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;


public class CalculatorFragment extends Fragment {

    private int amount = 1;
    private Integer position = null;

    private static DecimalFormat df = new DecimalFormat("#.00");

    EditText nameText;
    EditText priceText;
    EditText discountText;
    EditText amountText;
    EditText totalText;
    Button plus;
    Button minus;
    Button calc;
    Button addToCart;

    ShoppingListItem item;

    public void setItem(int position) {
        this.item = ShoppingListContent.getItemFromList(position);
        if (item != null) {
            this.position = position;
            Log.d("CalculatorFragment", "item not null");
            nameText.setText(item.getName());
            priceText.setText(String.valueOf(item.getPrice()));
            amountText.setText(String.valueOf(item.getAmount()));
            amount = item.getAmount();
        }
        else {
            Log.d("CalculatorFragment", "item null");
            setAmount(0);
        }
    }

    public CalculatorFragment() {}

    /**
     * @return A new instance of fragment CalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        nameText = (EditText) view.findViewById(R.id.calculatorItemNameEditText);
        priceText = (EditText) view.findViewById(R.id.calculatorPriceEditText);
        discountText = (EditText) view.findViewById(R.id.calculatorDiscountEditText);
        amountText = (EditText) view.findViewById(R.id.calculatorAmountEditText);
        totalText = (EditText) view.findViewById(R.id.calculatorTotalEditText);
        plus = (Button) view.findViewById(R.id.calculatorPlusButton);
        minus = (Button) view.findViewById(R.id.calculatorMinusButton);
        calc = (Button) view.findViewById(R.id.calculatorCalculateButton);
        addToCart = (Button) view.findViewById(R.id.calculatorAddToCartButton);

        setAmount(0);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusAmount();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusAmount();
            }
        });
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToCart(position);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        Log.d("CalculatorFragment", "onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("CalculatorFragment", "onResume");
        super.onResume();
    }

    private void clearFields() {
        item = null;
        position = null;
        priceText.setText("");
        setAmount(0);
        nameText.setText("");
        totalText.setText("");
    }

    private void addItemToCart(Integer position) {
        if (priceText.getText().length() > 0 && amountText.getText().length() > 0) {
            int cartAmount = Integer.parseInt(amountText.getText().toString());
            if (cartAmount > 0) {
                String newName;
                if (nameText.getText().length() > 0) {
                    newName = nameText.getText().toString();
                }
                else {
                    newName = getString(R.string.item) + " " + String.valueOf(ShoppingListContent.getShoppingList().size() + 1);
                }
                double cartPrice;
                if (totalText.getText().length() > 0) {
                    cartPrice = Double.parseDouble(totalText.getText().toString()) / cartAmount;
                }
                else {
                    cartPrice = Double.parseDouble(priceText.getText().toString());
                }
                if (item != null  && position != null) {
                    if (item.getName().equals(newName)) {
                        item.setPrice(cartPrice);
                        item.setAmount(cartAmount);
                        item.setChecked(true);
                        ShoppingListContent.modifyListItem(position, item);
                        notifyListAndClearFields();
                    }
                    else {
                        ShoppingListItem newCartItem = new ShoppingListItem(newName, cartPrice, cartAmount);
                        newCartItem.setChecked(true);
                        ShoppingListContent.addItemToList(newCartItem);
                        notifyListAndClearFields();
                    }
                }
                else {
                    ShoppingListItem newCartItem = new ShoppingListItem(newName, cartPrice, cartAmount);
                    newCartItem.setChecked(true);
                    ShoppingListContent.addItemToList(newCartItem);
                    notifyListAndClearFields();
                }
            }
            else {
                Toast.makeText(getContext(), getString(R.string.toastLowAmount), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getContext(), getString(R.string.toastCheckCalculatorFields), Toast.LENGTH_LONG).show();
        }
    }

    private void notifyListAndClearFields() {
        Activity fragHolder = getActivity();
        if (fragHolder instanceof FragmentHolder) {
            ((FragmentHolder) fragHolder).onItemAddedToCart();
        }
        clearFields();
    }

    private void setAmount(int set) {
        if(set == 0) {
            amount = 1;
        } else if(set == -1 && amount > 1) {
            amount--;
        } else if(set == 1) {
            amount++;
        }
        amountText.setText(Integer.toString(amount));
    }

    private void calculate() {
        double total;
        double price;
        if(discountText.getText().length() > 0 && priceText.getText().length() > 0 && amountText.getText().length() > 0) {
            amount = Integer.parseInt(amountText.getText().toString());
            if (amount > 0) {
                double percent = Double.parseDouble(discountText.getText().toString());
                if (percent < 1 || percent >= 100) {
                    price = Double.parseDouble(priceText.getText().toString());
                    total = price * amount;
                    totalText.setText(df.format(total));
                }
                else {
                    price = Double.parseDouble(priceText.getText().toString());
                    if(amount < 1) {
                        amount = 1;
                    }
                    if(percent < 1 || percent > 100) {
                        total = price * amount;
                    } else {
                        total = (1 - percent / 100) * price * amount;
                    }
                    totalText.setText(df.format(total));
                }
            }
            else {
                Toast.makeText(getContext(), getString(R.string.toastLowAmount), Toast.LENGTH_SHORT).show();
            }
        }
        else if(priceText.getText().length() > 0 && amountText.getText().length() > 0) {
            amount = Integer.parseInt(amountText.getText().toString());
            if (amount > 0) {
                price = Double.parseDouble(priceText.getText().toString());
                total = price * amount;
                totalText.setText(df.format(total));
            }
            else {
                Toast.makeText(getContext(), getString(R.string.toastLowAmount), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getContext(), getString(R.string.toastPriceAndAmountRequired), Toast.LENGTH_SHORT).show();
        }
    }

    public void plusAmount() {
        setAmount(1);
    }

    public void minusAmount() {
        setAmount(-1);
    }
}
