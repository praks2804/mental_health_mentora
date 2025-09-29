package com.simats.mental_health.utils;

import android.app.Activity;
import android.util.Log;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.QueryProductDetailsParams;

import java.util.ArrayList;
import java.util.List;

public class BillingManager implements PurchasesUpdatedListener {

    private static final String TAG = "BillingManager";
    private BillingClient billingClient;
    private Activity activity;
    private ProductDetails subscriptionProduct;

    // Replace with your Play Console product ID (monthly subscription)
    private static final String SUBSCRIPTION_ID = "your_monthly_subscription_id";

    public BillingManager(Activity activity) {
        this.activity = activity;
        billingClient = BillingClient.newBuilder(activity)
                .enablePendingPurchases()
                .setListener(this)
                .build();
    }

    public void startConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing Client Connected");
                    querySubscription();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.d(TAG, "Billing Service Disconnected");
            }
        });
    }

    private void querySubscription() {
        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
        productList.add(QueryProductDetailsParams.Product.newBuilder()
                .setProductId(SUBSCRIPTION_ID)
                .setProductType(BillingClient.ProductType.SUBS)
                .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, productDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !productDetailsList.isEmpty()) {
                subscriptionProduct = productDetailsList.get(0);
                Log.d(TAG, "Subscription details loaded: " + subscriptionProduct.getTitle());
            }
        });
    }

    public void launchPurchaseFlow() {
        if (subscriptionProduct != null) {
            BillingFlowParams.ProductDetailsParams productDetailsParams =
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(subscriptionProduct)
                            .build();

            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(List.of(productDetailsParams))
                    .build();

            billingClient.launchBillingFlow(activity, billingFlowParams);
        } else {
            Log.d(TAG, "Subscription product is null");
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "User canceled the purchase");
        } else {
            Log.d(TAG, "Purchase failed: " + billingResult.getDebugMessage());
        }
    }
    // Interface callback for subscription check
    public interface SubscriptionCheckListener {
        void onCheck(boolean isActive);
    }

    // Add this method in BillingManager
    public void checkActiveSubscription(SubscriptionCheckListener listener) {
        billingClient.queryPurchasesAsync(
                BillingClient.ProductType.SUBS,
                (billingResult, purchasesList) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        boolean active = false;
                        for (Purchase purchase : purchasesList) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                active = true;
                                break;
                            }
                        }
                        listener.onCheck(active);
                    } else {
                        listener.onCheck(false);
                    }
                }
        );
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();

                billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Log.d(TAG, "Purchase acknowledged");
                        // ✅ Unlock subscription features for the user
                    }
                });
            }
        }
    }
}
