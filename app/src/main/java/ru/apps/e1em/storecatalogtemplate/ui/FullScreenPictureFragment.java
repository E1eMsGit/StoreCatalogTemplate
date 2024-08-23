package ru.apps.e1em.storecatalogtemplate.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.apps.e1em.storecatalogtemplate.R;
import com.squareup.picasso.Picasso;

public class FullScreenPictureFragment extends Fragment {
    private static final String PRODUCT_PICTURE_ID_KEY = "picture";
    private static final String PRODUCT_TITLE_ID_KEY = "title";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_full_screen_picture, container, false);

        initUI(root);

        return root;
    }

    // Инициализация интерфейса.
    private void initUI(View root) {
        ImageView productImageView = root.findViewById(R.id.full_screen_picture_image_image_view);
        Picasso.with(getContext()).load(getString(getArguments().getInt(PRODUCT_PICTURE_ID_KEY, 0))).into(productImageView);

        // Наследование ActionBar из активности.
        ActionBar toolbar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(getArguments().getInt(PRODUCT_TITLE_ID_KEY, 0)));
        }
    }

    // Связь фрагмента с менеджером фрагментов в активности.
    public static FullScreenPictureFragment newInstance(int imageLinkId, int titleId) {
        FullScreenPictureFragment fragment = new FullScreenPictureFragment();

        // Прием переданных данных.
        Bundle bundle = new Bundle();
        bundle.putInt(PRODUCT_PICTURE_ID_KEY, imageLinkId);
        bundle.putInt(PRODUCT_TITLE_ID_KEY, titleId);
        fragment.setArguments(bundle);

        return fragment;
    }
}
