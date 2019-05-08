package ru.apps.e1em.storecatalogtemplate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

public class FavoriteListFragment extends Fragment {
    private List<Product> favoriteProductsList;
    private FavoriteListFragment.FavoriteProductsAdapter favoriteProductsAdapter;
    // Объявление объектов интерфейса.
    private OnListItemSelectedListener callbackActivityFromListItemSelected;

    // Связь фрагмента с активностью.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbackActivityFromListItemSelected = (OnListItemSelectedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        initUI(root);

        return root;
    }

    // Инициализация интерфейса.
    private void initUI(View root) {
        // Наследование ActionBar из активности.
        ActionBar toolbar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.favorites));
        }

        initFavoriteList(ProductsList.getInstance(getContext()).getProducts());

        favoriteProductsAdapter = new FavoriteListFragment.FavoriteProductsAdapter(getContext(), favoriteProductsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView favoriteProductsListRecyclerView = root.findViewById(R.id.favorite_list_recycler_view);
        favoriteProductsListRecyclerView.setLayoutManager(linearLayoutManager);
        favoriteProductsListRecyclerView.setAdapter(favoriteProductsAdapter);
    }

    // Связь фрагмента с менеджером фрагментов в активности.
    @NonNull
    public static FavoriteListFragment newInstance() {
        return new FavoriteListFragment();
    }

    private void initFavoriteList(List<Product> products) {
        favoriteProductsList = new ArrayList<>();

        for (Product product : products) {
            if (product.isFavorite())
                favoriteProductsList.add(product);
        }
    }

    // Создание списка в RecyclerView через адаптер.
    private class FavoriteProductsAdapter extends RecyclerView.Adapter<FavoriteListFragment.FavoriteProductsViewHolder> {
        Context context;
        List<Product> favoriteProducts;

        private FavoriteProductsAdapter(Context context, List<Product> favoriteProducts) {
            this.context = context;
            this.favoriteProducts = favoriteProducts;
        }

        @Override
        public FavoriteListFragment.FavoriteProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.favorite_list_item, parent, false);

            return new FavoriteListFragment.FavoriteProductsViewHolder(itemView);
        }

        // Инициализация элементов списка контентом.
        @Override
        public void onBindViewHolder(final FavoriteListFragment.FavoriteProductsViewHolder holder, int position) {
            holder.titleTextView.setText(getString(favoriteProducts.get(position).getTitleId()));
            holder.priceTextView.setText(String.valueOf(favoriteProducts.get(position).getPrice()));
            Picasso.with(context).load(getString(favoriteProducts.get(position).getImageLinkId())).into(holder.imageView);

            // Удаление холдера из списка.
            holder.deleteFromFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteProducts.get(holder.getAdapterPosition()).setFavorite(false);
                    favoriteProductsList.remove(holder.getAdapterPosition());
                    favoriteProductsAdapter.notifyItemRemoved(holder.getAdapterPosition());
                    favoriteProductsAdapter.notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                }
            });

            if (favoriteProducts.get(position).getQuantity() == 0) {
                holder.toBasketButton.setEnabled(false);
                holder.toBasketButton.setText(R.string.quantity_no);
            } else if (favoriteProducts.get(position).isInBasket()) {
                holder.toBasketButton.setEnabled(false);
                holder.toBasketButton.setText(R.string.in_basket);
            }
            holder.toBasketButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteProducts.get(holder.getAdapterPosition()).setInBasket(true);
                    if (favoriteProducts.get(holder.getAdapterPosition()).isInBasket()) {
                        holder.toBasketButton.setEnabled(false);
                        holder.toBasketButton.setText(R.string.in_basket);
                    }
                    Snackbar.make(v,
                            getString(favoriteProducts.get(holder.getAdapterPosition()).getTitleId()) + " " + getString(R.string.product_added_to_basket),
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            });

            // Нажатие на элемент списка. Переход на фрагмент ProductDetail холдера.
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbackActivityFromListItemSelected.onListItemSelected(favoriteProducts.get(holder.getAdapterPosition()).getId());
                }
            });
        }

        // Количество элементов в списке.
        @Override
        public int getItemCount() {
            if (favoriteProductsList != null) {
                return favoriteProductsList.size();
            } else {
                return 0;
            }
        }
    }

    // Элемент списка (Holder).
    private static class FavoriteProductsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;
        Button toBasketButton;
        ImageView deleteFromFavorite;

        private FavoriteProductsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.favorite_item_view_card_view);
            imageView = itemView.findViewById(R.id.favorite_list_item_image_image_view);
            titleTextView = itemView.findViewById(R.id.favorite_list_item_title_text_view);
            priceTextView = itemView.findViewById(R.id.favorite_list_item_price_text_view);
            toBasketButton = itemView.findViewById(R.id.favorite_item_view_to_basket_button);
            deleteFromFavorite = itemView.findViewById(R.id.favorite_list_item_delete_image_view);
        }
    }

}
