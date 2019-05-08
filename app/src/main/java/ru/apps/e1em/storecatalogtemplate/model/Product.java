package ru.apps.e1em.storecatalogtemplate.model;

import android.content.Context;

public class Product {
    private int id;
    private int titleId;
    private int descriptionId;
    private int hRefId;
    private int imageLinkId;
    private int quantity;
    private int price;
    private int productCount = 1;
    private boolean isFavorite;
    private boolean inBasket;

    public Product(Context context, int id, int titleId, int descriptionId, int imageLinkId, int hRefId,
                   int quantity, int price) {
        this.id = id;
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.imageLinkId = imageLinkId;
        this.hRefId = hRefId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getDescriptionId() {
        return descriptionId;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getImageLinkId() {
        return imageLinkId;
    }

    public int gethRefId() {
        return hRefId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isInBasket() {
        return inBasket;
    }

    public void setInBasket(boolean inBasket) {
        this.inBasket = inBasket;
    }

    public void plusProductCount() {
        productCount++;
    }

    public void minusProductCount() {
        if (productCount > 1) {
            productCount--;
        }
    }
}
