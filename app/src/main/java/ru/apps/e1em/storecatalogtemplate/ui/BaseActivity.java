package ru.apps.e1em.storecatalogtemplate.ui;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.apps.e1em.storecatalogtemplate.R;
import ru.apps.e1em.storecatalogtemplate.interfaces.OnIssueButtonClickedListener;
import ru.apps.e1em.storecatalogtemplate.interfaces.OnListItemSelectedListener;
import ru.apps.e1em.storecatalogtemplate.interfaces.OnProductDetailPictureClickedListener;

// Переходы на все фрагменты приложения осуществляются из этой активности.
public class BaseActivity extends AppCompatActivity implements OnProductDetailPictureClickedListener,
        OnListItemSelectedListener, OnIssueButtonClickedListener {

    private static final String VISIBLE_FRAGMENT_TAG = "visible_fragment";
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private FragmentManager fragmentManager;
    private ImageButton homeButton;
    private ImageButton basketButton;
    private ImageButton favoriteButton;
    private RelativeLayout navigationBar;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();

        if (savedInstanceState == null) {
            // Помещаем в контейнер BaseActivity фрагмент productsListFragment.
            fragmentManager.beginTransaction().add(R.id.container, new ProductsListFragment(), VISIBLE_FRAGMENT_TAG).commit();
            setSelectedNavigationBarItem(0);
        }
    }

    // Конфигурация меню на стартовом фрагменте.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem pageView = menu.findItem(R.id.page_view);
        MenuItem checkIssue = menu.findItem(R.id.check_issue);

        pageView.setVisible(false);
        checkIssue.setVisible(false);

        return true;
    }

    // Методы которые через переопределение методов интерфейсов меняют фрагмент в контейнере.
    @Override
    public void onProductDetailPictureClicked(int imageLinkId, int titleId) {
        FullScreenPictureFragment fullScreenPictureFragment = FullScreenPictureFragment.newInstance(imageLinkId, titleId);

        fragmentManager.beginTransaction().replace(R.id.container, fullScreenPictureFragment, VISIBLE_FRAGMENT_TAG).addToBackStack(null).commit();
    }

    @Override
    public void onListItemSelected(int position) {
        ProductDetailFragment detailFragment = ProductDetailFragment.newInstance(position);

        fragmentManager.beginTransaction().replace(R.id.container, detailFragment, VISIBLE_FRAGMENT_TAG).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
    }

    @Override
    public void onIssueButtonClicked() {
        IssueFragment issueFragment = IssueFragment.newInstance();

        fragmentManager.beginTransaction().replace(R.id.container, issueFragment, VISIBLE_FRAGMENT_TAG).addToBackStack(null).commit();
    }

    private void initUI() {
        setContentView(R.layout.activity_base);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationBar = findViewById(R.id.navigation_bar_relative_layout);

        homeButton = findViewById(R.id.navigation_bar_home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNavigationBarItem(0);
            }
        });

        basketButton = findViewById(R.id.navigation_bar_basket_button);
        basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNavigationBarItem(1);
            }
        });

        favoriteButton = findViewById(R.id.navigation_bar_favorite_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNavigationBarItem(2);
            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        Fragment fragment = fragmentManager.findFragmentByTag(VISIBLE_FRAGMENT_TAG);

                        if (fragment instanceof ProductsListFragment) {
                            currentPosition = 0;
                        }

                        if (fragment instanceof BasketListFragment) {
                            currentPosition = 1;
                        }

                        if (fragment instanceof FavoriteListFragment) {
                            currentPosition = 2;
                        }

                        if (fragment instanceof ProductDetailFragment) {
                            currentPosition = 3;
                        }

                        if (fragment instanceof FullScreenPictureFragment || fragment instanceof IssueFragment) {
                            navigationBar.setVisibility(View.GONE);
                        } else {
                            navigationBar.setVisibility(View.VISIBLE);
                        }

                        setSelectedNavigationBarItem(currentPosition);
                    }
                });
    }

    private void selectNavigationBarItem(int position) {
        currentPosition = position;
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new ProductsListFragment();
                break;
            case 1:
                fragment = BasketListFragment.newInstance();
                break;
            case 2:
                fragment = FavoriteListFragment.newInstance();
                break;
            default:
                fragment = new ProductsListFragment();
        }

        if (position != 0) {
            fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        fragmentManager.beginTransaction().replace(R.id.container, fragment, VISIBLE_FRAGMENT_TAG).addToBackStack(BACK_STACK_ROOT_TAG).commit();
        setSelectedNavigationBarItem(position);
    }

    private void setSelectedNavigationBarItem(int position) {
        if (position == 0) {
            homeButton.setColorFilter(getResources().getColor(R.color.colorPrimary));
            basketButton.setColorFilter(getResources().getColor(R.color.colorAccent));
            favoriteButton.setColorFilter(getResources().getColor(R.color.colorAccent));
        }

        if (position == 1) {
            homeButton.setColorFilter(getResources().getColor(R.color.colorAccent));
            basketButton.setColorFilter(getResources().getColor(R.color.colorPrimary));
            favoriteButton.setColorFilter(getResources().getColor(R.color.colorAccent));
        }

        if (position == 2) {
            homeButton.setColorFilter(getResources().getColor(R.color.colorAccent));
            basketButton.setColorFilter(getResources().getColor(R.color.colorAccent));
            favoriteButton.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }

        if (position == 3) {
            homeButton.setColorFilter(getResources().getColor(R.color.colorAccent));
            basketButton.setColorFilter(getResources().getColor(R.color.colorAccent));
            favoriteButton.setColorFilter(getResources().getColor(R.color.colorAccent));
        }
    }
}
