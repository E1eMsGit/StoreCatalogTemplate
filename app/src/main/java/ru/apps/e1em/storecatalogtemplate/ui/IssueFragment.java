package ru.apps.e1em.storecatalogtemplate.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.apps.e1em.storecatalogtemplate.R;
import ru.apps.e1em.storecatalogtemplate.model.Product;
import ru.apps.e1em.storecatalogtemplate.model.ProductsList;

import java.net.URLEncoder;
import java.util.List;

public class IssueFragment extends Fragment {
    private StringBuffer issue;
    private int result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_issue, container, false);

        initUI(root);

        return root;
    }

    // Связь фрагмента с менеджером фрагментов в активности.
    @NonNull
    public static IssueFragment newInstance() {
        return new IssueFragment();
    }

    private void initUI(View root) {
        // Наследование ActionBar из активности.
        ActionBar toolbar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.issue));
        }

        initIssue(ProductsList.getInstance(getContext()).getProducts());

        Button sendIssueButton = root.findViewById(R.id.issue_send_issue_button);
        sendIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIssue(issue);
            }
        });

        TextView issueListTextView = root.findViewById(R.id.issue_issue_list_text_view);
        if (issue.length() == 0) {
            issueListTextView.setText(R.string.basket_is_empty);
            sendIssueButton.setEnabled(false);
        } else {
            issueListTextView.setText(issue);
        }

        TextView issueResultPriceTextView = root.findViewById(R.id.issue_issue_result_price_text_view);
        issueResultPriceTextView.setText(String.format("%s", result));
    }

    private void initIssue(List<Product> products) {
        issue = new StringBuffer();
        result = 0;

        for (Product product : products) {
            if (product.isInBasket()) {
                issue.append(getString(product.getTitleId()))
                        .append("\t x \t")
                        .append(Integer.toString(product.getProductCount()))
                        .append("\t = \t")
                        .append(Integer.toString(product.getProductCount() * product.getPrice()))
                        .append(getString(R.string.currency))
                        .append("\n\n");
                result += product.getProductCount() * product.getPrice();
            }
        }
    }

    private void sendIssue(StringBuffer issue) {
        String message = "Здравствуйте! Хочу приобрести:\n" + issue.toString();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String phone = ""; // Номер моб. телефона
        boolean isWhatsAppInstalled = isWhatsAppInstalledChecking();

        if (isWhatsAppInstalled) {
            try {
                String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Установите WhatsApp", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        }
    }

    private boolean isWhatsAppInstalledChecking() {
        PackageManager pm = getContext().getPackageManager();
        boolean app_installed;

        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }

        return app_installed;
    }
}
