package ru.apps.e1em.storecatalogtemplate.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ru.apps.e1em.storecatalogtemplate.R;
import ru.apps.e1em.storecatalogtemplate.interfaces.OnProductDetailPictureClickedListener;
import ru.apps.e1em.storecatalogtemplate.model.Product;
import ru.apps.e1em.storecatalogtemplate.model.ProductsList;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailFragment extends Fragment {
    private static final String PRODUCT_INDEX_KEY = "key";
    private Product currentProduct;
    // Объявление объектов интерфейса.
    private OnProductDetailPictureClickedListener callbackActivityFromProductDetailPictureClicked;


    // Связь фрагмента с активностью.
    @Override
    public void onAttach(Context context) {
        // Связь с активностью.
        super.onAttach(context);
        callbackActivityFromProductDetailPictureClicked = (OnProductDetailPictureClickedListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);
        List<Product> products = ProductsList.getInstance(getContext()).getProducts();
        currentProduct = products.get(getArguments().getInt(PRODUCT_INDEX_KEY, 0));

        initUI(root);

        return root;
    }

    // Конфигурация меню в этом фрагменте.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem pageView = menu.findItem(R.id.page_view);
        MenuItem checkIssue = menu.findItem(R.id.check_issue);

        pageView.setVisible(true);
        checkIssue.setVisible(false);
    }

    // Обработка нажатий на элементы меню.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.page_view) {
            Uri openProductLink = Uri.parse(getString(currentProduct.gethRefId()));
            Intent intent = new Intent(Intent.ACTION_VIEW, openProductLink);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Инициализация интерфейса.
    private void initUI(final View root) {
        // Наследование ActionBar из активности.
        ActionBar toolbar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(currentProduct.getTitleId()));
        }

        // Установка меню.
        setHasOptionsMenu(true);

        TextView productTitleTextView = root.findViewById(R.id.product_detail_title_text_view);
        productTitleTextView.setText(getString(currentProduct.getTitleId()));

        TextView productDescriptionTextView = root.findViewById(R.id.product_detail_description_text_view);
        productDescriptionTextView.setText(getString(currentProduct.getDescriptionId()));

        TextView productPriceTextView = root.findViewById(R.id.product_detail_price_text_view);
        productPriceTextView.setText(String.valueOf(currentProduct.getPrice()));

        TextView productQuantityOfTextView = root.findViewById(R.id.product_detail_quantity_text_view);
        if (currentProduct.getQuantity() > 0) {
            productQuantityOfTextView.setText(R.string.quantity_yes);
            productQuantityOfTextView.setTextColor(getResources().getColor(R.color.colorGreen));
        } else {
            productQuantityOfTextView.setText(R.string.quantity_no);
            productQuantityOfTextView.setTextColor(getResources().getColor(R.color.colorRed));
        }

        ImageView productImageView = root.findViewById(R.id.product_detail_image_image_view);
        Picasso.get().load(getString(currentProduct.getImageLinkId())).into(productImageView);

        // Нажатие на изображение продукта. Переход на фрагмент FullScreenPicture.
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Вызов метода интерфейса с передачей нужного аргумента во фрагмент.
                callbackActivityFromProductDetailPictureClicked.onProductDetailPictureClicked(currentProduct.getImageLinkId(), currentProduct.getTitleId());
            }
        });

        final Button toBasketButton = root.findViewById(R.id.product_detail_to_basket_button);
        if (currentProduct.getQuantity() == 0) {
            toBasketButton.setEnabled(false);
            toBasketButton.setText(R.string.quantity_no);
        } else if (currentProduct.isInBasket()) {
            toBasketButton.setEnabled(false);
            toBasketButton.setText(R.string.in_basket);
        }
        toBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProduct.setInBasket(true);
                if (currentProduct.isInBasket()) {
                    toBasketButton.setEnabled(false);
                    toBasketButton.setText(R.string.in_basket);
                }
                Snackbar.make(v,
                        getString(currentProduct.getTitleId()) + " " + getString(R.string.product_added_to_basket),
                        Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        final ImageView toFavoriteImageView = root.findViewById(R.id.product_detail_to_favorite_image_view);
        toFavoriteImageView.setColorFilter(currentProduct.isFavorite() ? getContext().getResources().getColor(R.color.colorInFavoriteRed) : getContext().getResources().getColor(R.color.colorNotInFavoriteRed));
        toFavoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProduct.setFavorite(!currentProduct.isFavorite());
                toFavoriteImageView.setColorFilter(currentProduct.isFavorite() ?
                        getContext().getResources().getColor(R.color.colorInFavoriteRed) : getContext().getResources().getColor(R.color.colorNotInFavoriteRed));
            }
        });
    }

    // Связь фрагмента с менеджером фрагментов в активности.
    public static ProductDetailFragment newInstance(int position) {
        ProductDetailFragment fragment = new ProductDetailFragment();

        // Прием переданных данных.
        Bundle bundle = new Bundle();
        bundle.putInt(PRODUCT_INDEX_KEY, position);
        fragment.setArguments(bundle);

        return fragment;
    }
}
