package ru.apps.e1em.storecatalogtemplate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.apps.e1em.storecatalogtemplate.R;
import ru.apps.e1em.storecatalogtemplate.interfaces.OnIssueButtonClickedListener;
import ru.apps.e1em.storecatalogtemplate.interfaces.OnListItemSelectedListener;
import ru.apps.e1em.storecatalogtemplate.model.Product;
import ru.apps.e1em.storecatalogtemplate.model.ProductsList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BasketListFragment extends Fragment {
    private List<Product> basketList;
    private BasketListFragment.BasketProductsAdapter basketProductsAdapter;
    // Объявление объектов интерфейса.
    private OnListItemSelectedListener callbackActivityFromListItemSelected;
    private OnIssueButtonClickedListener callbackActivityFromIssueButtonClicked;

    // Связь фрагмента с активностью.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbackActivityFromListItemSelected = (OnListItemSelectedListener) context;
        callbackActivityFromIssueButtonClicked = (OnIssueButtonClickedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_basket_list, container, false);

        initUI(root);

        return root;
    }

    // Конфигурация меню в этом фрагменте.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem pageView = menu.findItem(R.id.page_view);
        MenuItem checkIssue = menu.findItem(R.id.check_issue);

        pageView.setVisible(false);
        checkIssue.setVisible(true);
    }

    // Обработка нажатий на элементы меню.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.check_issue) {
            callbackActivityFromIssueButtonClicked.onIssueButtonClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Инициализация интерфейса.
    private void initUI(View root) {
        // Наследование ActionBar из активности.
        ActionBar toolbar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.basket));
        }

        initBasketList(ProductsList.getInstance(getContext()).getProducts());

        // Установка меню.
        setHasOptionsMenu(true);

        basketProductsAdapter = new BasketListFragment.BasketProductsAdapter(getContext(), basketList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView basketRecyclerView = root.findViewById(R.id.basket_list_recycler_view);
        basketRecyclerView.setLayoutManager(linearLayoutManager);
        basketRecyclerView.setAdapter(basketProductsAdapter);
    }

    // Связь фрагмента с менеджером фрагментов в активности.
    @NonNull
    public static BasketListFragment newInstance() {
        return new BasketListFragment();
    }

    private void initBasketList(List<Product> products) {
        basketList = new ArrayList<>();

        for (Product product : products) {
            if (product.isInBasket())
                basketList.add(product);
        }
    }

    // Создание списка в RecyclerView через адаптер.
    private class BasketProductsAdapter extends RecyclerView.Adapter<BasketListFragment.BasketProductsViewHolder> {
        Context context;
        List<Product> basketProducts;

        private BasketProductsAdapter(Context context, List<Product> basketProducts) {
            this.context = context;
            this.basketProducts = basketProducts;
        }

        @Override
        public BasketListFragment.BasketProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.basket_list_item, parent, false);

            return new BasketListFragment.BasketProductsViewHolder(itemView);
        }

        // Инициализация элементов списка контентом.
        @Override
        public void onBindViewHolder(final BasketListFragment.BasketProductsViewHolder holder, int position) {
            holder.titleTextView.setText(getString(basketProducts.get(position).getTitleId()));
            holder.priceTextView.setText(String.valueOf(basketProducts.get(position).getPrice()));
            Picasso.with(context).load(getString(basketProducts.get(position).getImageLinkId())).into(holder.imageView);
            holder.productCounterTextView.setText(String.valueOf(basketProducts.get(position).getProductCount()));

            // Увеличить счетчик продукта.
            holder.addProductImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basketProducts.get(holder.getAdapterPosition()).plusProductCount();
                    holder.productCounterTextView.setText(String.valueOf(basketProducts.get(holder.getAdapterPosition()).getProductCount()));
                }
            });

            // Уменьшить счетчик продукта.
            holder.removeProductImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basketProducts.get(holder.getAdapterPosition()).minusProductCount();
                    holder.productCounterTextView.setText(String.valueOf(basketProducts.get(holder.getAdapterPosition()).getProductCount()));
                }
            });

            // Удаление холдера из списка.
            holder.deleteFromFavoriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basketProducts.get(holder.getAdapterPosition()).setInBasket(false);
                    basketProducts.get(holder.getAdapterPosition()).setProductCount(0);
                    basketList.remove(holder.getAdapterPosition());
                    basketProductsAdapter.notifyItemRemoved(holder.getAdapterPosition());
                    basketProductsAdapter.notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                }
            });

            // Нажатие на элемент списка. Переход на фрагмент ProductDetail холдера.
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbackActivityFromListItemSelected.onListItemSelected(basketProducts.get(holder.getAdapterPosition()).getId());
                }
            });
        }

        // Количество элементов в списке.
        @Override
        public int getItemCount() {
            if (basketList != null) {
                return basketList.size();
            } else {
                return 0;
            }
        }
    }

    // Элемент списка (Holder).
    private static class BasketProductsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;
        ImageView deleteFromFavoriteImageView;
        ImageView addProductImageView;
        TextView productCounterTextView;
        ImageView removeProductImageView;

        private BasketProductsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.basket_list_item_card_view);
            imageView = itemView.findViewById(R.id.basket_list_item_image_image_view);
            titleTextView = itemView.findViewById(R.id.basket_list_item_title_text_view);
            priceTextView = itemView.findViewById(R.id.basket_list_item_price_text_view);
            deleteFromFavoriteImageView = itemView.findViewById(R.id.basket_list_item_delete_image_view);
            addProductImageView = itemView.findViewById(R.id.basket_list_item_plus_image_view);
            productCounterTextView = itemView.findViewById(R.id.basket_list_item_counter_text_view);
            removeProductImageView = itemView.findViewById(R.id.basket_list_item_minus_image_view);
        }
    }
}
