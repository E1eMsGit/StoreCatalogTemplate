package ru.apps.e1em.storecatalogtemplate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.apps.e1em.storecatalogtemplate.R;
import ru.apps.e1em.storecatalogtemplate.interfaces.OnListItemSelectedListener;
import ru.apps.e1em.storecatalogtemplate.model.Product;
import ru.apps.e1em.storecatalogtemplate.model.ProductsList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsListFragment extends Fragment {
    private List<Product> products;
    private ProductsAdapter productsAdapter;
    // Объявление объектов интерфейса.
    private OnListItemSelectedListener callbackActivityFromListItemSelected;

    // Связь фрагмента с активностью.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbackActivityFromListItemSelected = (OnListItemSelectedListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_products_list, container, false);

        initUI(root);

        return root;
    }

    // Инициализация интерфейса.
    private void initUI(View root) {
        // Наследование ActionBar из активности.
        ActionBar toolbar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
        }

        products = ProductsList.getInstance(getContext()).getProducts();
        productsAdapter = new ProductsAdapter(getContext(), products);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        RecyclerView productsList = root.findViewById(R.id.products_list_recycler_view);
        productsList.setLayoutManager(gridLayoutManager);
        productsList.setAdapter(productsAdapter);
    }

    // Создание списка в RecyclerView через адаптер.
    private class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {
        Context context;
        List<Product> products;

        private ProductsAdapter(Context context, List<Product> products) {
            this.context = context;
            this.products = products;
        }

        @Override
        public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.products_list_item,
                    parent, false);
            return new ProductsViewHolder(itemView);
        }

        // Инициализация элементов списка контентом.
        @Override
        public void onBindViewHolder(final ProductsViewHolder holder, int position) {
            holder.titleTextView.setText(getString(products.get(position).getTitleId()));
            holder.priceTextView.setText(String.valueOf(products.get(position).getPrice()));
            Picasso.with(context).load(getString(products.get(position).getImageLinkId())).into(holder.imageView);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbackActivityFromListItemSelected.onListItemSelected(holder.getAdapterPosition());
                }
            });

            if (products.get(position).getQuantity() == 0) {
                holder.toBasketButton.setEnabled(false);
                holder.toBasketButton.setText(R.string.quantity_no);
            } else if (products.get(position).isInBasket()) {
                holder.toBasketButton.setEnabled(false);
                holder.toBasketButton.setText(R.string.in_basket);
            }
            holder.toBasketButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    products.get(holder.getAdapterPosition()).setInBasket(true);
                    if (products.get(holder.getAdapterPosition()).isInBasket()) {
                        holder.toBasketButton.setEnabled(false);
                        holder.toBasketButton.setText(R.string.in_basket);
                    }
                    Snackbar.make(v,
                            getString(products.get(holder.getAdapterPosition()).getTitleId()) + " " + getString(R.string.product_added_to_basket),
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            });

            holder.toFavoriteImageButton.setColorFilter(products.get(holder.getAdapterPosition()).isFavorite() ?
                    getContext().getResources().getColor(R.color.colorInFavoriteRed) : getContext().getResources().getColor(R.color.colorNotInFavoriteRed));
            holder.toFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    products.get(holder.getAdapterPosition()).setFavorite(!products.get(holder.getAdapterPosition()).isFavorite());
                    holder.toFavoriteImageButton.setColorFilter(products.get(holder.getAdapterPosition()).isFavorite() ?
                            getContext().getResources().getColor(R.color.colorInFavoriteRed) : getContext().getResources().getColor(R.color.colorNotInFavoriteRed));
                }
            });
        }

        // Количество элементов в списке.
        @Override
        public int getItemCount() {
            if (products != null) {
                return products.size();
            } else {
                return 0;
            }
        }
    }

    // Элемент списка (Holder).
    private static class ProductsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;
        Button toBasketButton;
        ImageView toFavoriteImageButton;

        private ProductsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.product_list_item_card_view);
            imageView = itemView.findViewById(R.id.product_list_item_image_image_view);
            titleTextView = itemView.findViewById(R.id.product_list_item_title_text_view);
            priceTextView = itemView.findViewById(R.id.product_list_item_price_text_view);
            toBasketButton = itemView.findViewById(R.id.product_list_item_to_basket_button);
            toFavoriteImageButton = itemView.findViewById(R.id.product_list_item_to_favorite_image_view);
        }
    }
}
