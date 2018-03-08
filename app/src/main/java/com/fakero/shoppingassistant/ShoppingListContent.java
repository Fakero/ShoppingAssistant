package com.fakero.shoppingassistant;

import java.util.ArrayList;

/**
 * Created by fakero on 8.8.2017.
 */

public class ShoppingListContent {
    private static final ArrayList<ShoppingListItem> shoppingList = new ArrayList<>();

    public static ArrayList<ShoppingListItem> getShoppingList() {
        return shoppingList;
    }

    public static void addItemToList(ShoppingListItem item) {
        shoppingList.add(item);
    }

    public static void modifyListItem(int position ,ShoppingListItem item) {
        shoppingList.set(position, item);
    }

    public static ShoppingListItem getItemFromList(int id) {
        return shoppingList.get(id);
    }

    public static void removeItemFromList(int index) {
        shoppingList.remove(index);
    }

    public static void clearList() {
        shoppingList.clear();
    }
}
