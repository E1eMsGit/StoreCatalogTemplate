package ru.apps.e1em.storecatalogtemplate.model;

import android.content.Context;

import ru.apps.e1em.storecatalogtemplate.R;

import java.util.ArrayList;
import java.util.List;

public class ProductsList {
    private static ProductsList ourInstance = null;
    private List<Product> products;
    private Context context;

    private ProductsList(Context context) {
        this.context = context;
        initProductsList();
    }

    public static ProductsList getInstance(Context context) {
        if (ourInstance == null) {
            return ourInstance = new ProductsList(context);
        } else {
            return ourInstance;
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    private void initProductsList() {
        products = new ArrayList<>();
        products.add(new Product(context, 0, R.string.arduino_uno_name, R.string.arduino_uno_description,
                R.string.arduino_uno_image_href, R.string.arduino_uno_href, 5, 1000));
        products.add(new Product(context, 1, R.string.arduino_mega_name, R.string.arduino_mega_description,
                R.string.arduino_mega_image_href, R.string.arduino_mega_href, 30, 2000));
        products.add(new Product(context, 2, R.string.arduino_tian_name, R.string.arduino_tian_description,
                R.string.arduino_tian_image_href, R.string.arduino_tian_href, 30, 7000));
        products.add(new Product(context, 3, R.string.arduino_leonardo_name, R.string.arduino_leonardo_description,
                R.string.arduino_leonardo_image_href, R.string.arduino_leonardo_href, 32, 2500));
        products.add(new Product(context, 4, R.string.iskra_js_name, R.string.iskra_js_description,
                R.string.iskra_js_image_href, R.string.iskra_js_href, 0, 5000));
        products.add(new Product(context, 5, R.string.arduino_micro_name, R.string.arduino_micro_description,
                R.string.arduino_micro_image_href, R.string.arduino_micro_href, 150, 1500));
    }
}
