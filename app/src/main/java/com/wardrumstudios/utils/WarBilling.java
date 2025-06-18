package com.wardrumstudios.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;

public class WarBilling extends WarBase {
    private static final int OS_Cached = 4;
    private static final int OS_Cancelled = 2;
    private static final int OS_Error = 5;
    private static final int OS_Purchased = 0;
    private static final int OS_Refunded = 3;
    private static final int OS_Restored = 1;
    private static final int REQUEST_CODE = 10002;
    private static final String TAG = "OSWrapper";
    private static final boolean billLogging = false;
    private String mBillingKey = "UNUSED";
    /*IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            WarBilling.this.OutputLog("Query inventory finished.");
            if (result.isFailure()) {
                WarBilling.this.complain("Failed to query inventory: " + result);
                if (result.toString().contains("prices")) {
                    ArrayList<String> toCheck = new ArrayList<>();
                    for (int i = 0; i < WarBilling.this.skus.size(); i++) {
                        WarBilling.this.OutputLog("Checking sku without price " + ((SkuEntry) WarBilling.this.skus.get(i)).id);
                        toCheck.add(((SkuEntry) WarBilling.this.skus.get(i)).id);
                    }
                    WarBilling.this.mHelper.queryInventoryAsync(false, toCheck, this);
                    return;
                }
                WarBilling.this.changeConnection(false);
                return;
            }
            WarBilling.this.OutputLog("Query successful. Inventory: " + inventory);
            boolean foundDetails = false;
            for (int i2 = 0; i2 < WarBilling.this.skus.size(); i2++) {
                boolean hasPurchased = inventory.hasPurchase(((SkuEntry) WarBilling.this.skus.get(i2)).id);
                SkuDetails details = inventory.getSkuDetails(((SkuEntry) WarBilling.this.skus.get(i2)).id);
                if (details != null) {
                    WarBilling.this.OutputLog("SKU '" + ((SkuEntry) WarBilling.this.skus.get(i2)).id + "' : '" + details.getType() + "' '" + details.getPrice() + "' '" + details.getTitle() + "' " + hasPurchased);
                    ((SkuEntry) WarBilling.this.skus.get(i2)).priceFormat = details.getPrice();
                    ((SkuEntry) WarBilling.this.skus.get(i2)).cachedDetails = details;
                    foundDetails = true;
                } else {
                    WarBilling.this.OutputLog("SKU '" + ((SkuEntry) WarBilling.this.skus.get(i2)).id + "' : no details : " + hasPurchased);
                    ((SkuEntry) WarBilling.this.skus.get(i2)).priceFormat = "";
                }
                if (hasPurchased) {
                    ((SkuEntry) WarBilling.this.skus.get(i2)).purchased = true;
                    WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(i2)).id, 4);
                }
            }
            if (!foundDetails) {
                WarBilling.this.changeConnection(false);
            } else {
                WarBilling.this.changeConnection(true);
            }
        }
    };*/
    /* access modifiers changed from: private */
    /*public IabHelper mHelper;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            WarBilling.this.OutputLog("Purchase finished: " + result + ", purchase: " + purchase);
            int skuIndex = WarBilling.this.mPurchasingIndex;
            if (purchase == null || ((skuIndex = WarBilling.this.GetSKU(purchase.getSku())) >= 0 && skuIndex < WarBilling.this.skus.size())) {
                switch (result.getResponse()) {
                    case IabHelper.IABHELPER_UNKNOWN_PURCHASE_RESPONSE:
                    case 5:
                        WarBilling.this.OutputLog("Dev error.");
                        if (skuIndex != -1) {
                            WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(skuIndex)).id, 5);
                            return;
                        }
                        return;
                    case IabHelper.IABHELPER_USER_CANCELLED:
                    case 1:
                        WarBilling.this.OutputLog("Purchase cancelled.");
                        if (skuIndex != -1) {
                            ((SkuEntry) WarBilling.this.skus.get(skuIndex)).purchased = false;
                            WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(skuIndex)).id, 2);
                            return;
                        }
                        return;
                    case 0:
                        WarBilling.this.OutputLog("Purchase successful.");
                        if (skuIndex != -1) {
                            ((SkuEntry) WarBilling.this.skus.get(skuIndex)).purchased = true;
                            WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(skuIndex)).id, 0);
                            WarBilling.this.purchaseEvent(((SkuEntry) WarBilling.this.skus.get(skuIndex)).cachedDetails, purchase);
                            return;
                        }
                        return;
                    case 3:
                        WarBilling.this.OutputLog("Billing not available.");
                        if (skuIndex != -1) {
                            WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(skuIndex)).id, 5);
                            return;
                        }
                        return;
                    case 4:
                        WarBilling.this.OutputLog("Item not available.");
                        if (skuIndex != -1) {
                            WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(skuIndex)).id, 5);
                            return;
                        }
                        return;
                    case 6:
                        WarBilling.this.OutputLog("IAP Error.");
                        if (skuIndex != -1) {
                            WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(skuIndex)).id, 5);
                            return;
                        }
                        return;
                    case 7:
                        WarBilling.this.OutputLog("Already owned.");
                        if (skuIndex != -1) {
                            ((SkuEntry) WarBilling.this.skus.get(skuIndex)).purchased = true;
                            WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(skuIndex)).id, 2);
                            return;
                        }
                        return;
                    case 8:
                        WarBilling.this.OutputLog("Not wtf.");
                        if (skuIndex != -1) {
                            ((SkuEntry) WarBilling.this.skus.get(skuIndex)).purchased = false;
                            WarBilling.this.notifyChange(((SkuEntry) WarBilling.this.skus.get(skuIndex)).id, 2);
                            return;
                        }
                        return;
                    default:
                        WarBilling.this.OutputLog("Unknown response");
                        if (skuIndex != -1) {
                            try {
                                ((SkuEntry) WarBilling.this.skus.get(skuIndex)).purchased = false;
                                WarBilling.this.notifyChange(purchase.getSku(), 5);
                                return;
                            } catch (Exception e) {
                                WarBilling.this.OutputLog("Unknown response Error " + result.getResponse());
                                return;
                            }
                        } else {
                            return;
                        }
                }
            } else {
                Log.d(WarBilling.TAG, "Error skuIndex " + skuIndex + " out of range");
            }
        }
    };*/
    /* access modifiers changed from: private */
    public int mPurchasingIndex = -1;
    /* access modifiers changed from: private */
    public ArrayList<SkuEntry> skus;

    public native void changeConnection(boolean z);

    public native void notifyChange(String str, int i);

    public void onDestroy() {
        /*if (this.mHelper != null) {
            this.mHelper.dispose();
            this.mHelper = null;
        }*/
        super.onDestroy();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.skus = new ArrayList<>();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        OutputLog("onActivityResult(" + requestCode + "," + resultCode + "," + data);
        /*if (this.mHelper == null || !this.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            OutputLog("onActivityResult handled by IABUtil.");
        }*/
    }

    /* access modifiers changed from: private */
    public void OutputLog(String toLog) {
    }

    private class SkuEntry {
        //SkuDetails cachedDetails;
        String id;
        String priceFormat;
        boolean purchased;

        private SkuEntry() {
        }
    }

    public void AddSKU(String id) {
        SkuEntry newEntry = new SkuEntry();
        newEntry.id = id;
        newEntry.purchased = false;
        this.skus.add(newEntry);
    }

    /* access modifiers changed from: private */
    public int GetSKU(String id) {
        for (int i = 0; i < this.skus.size(); i++) {
            if (this.skus.get(i).id.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public boolean InitBilling() {
        String key = getBillingKey();
        changeConnection(false);
        if (key.contains("UNUSED")) {
            OutputLog("No key provided by app.");
            complain("No key provided by app.********************************************");
            changeConnection(false);
            return false;
        }
        OutputLog("Creating IAB helper");
        /*this.mHelper = new IabHelper(this, key);
        this.mHelper.enableDebugLogging(false, TAG);*/
        OutputLog("Starting setup.");
        /*this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                WarBilling.this.OutputLog("Setup finished.");
                if (!result.isSuccess()) {
                    WarBilling.this.complain("Problem setting up in-app billing: " + result);
                    return;
                }
                ArrayList<String> toCheck = new ArrayList<>();
                for (int i = 0; i < WarBilling.this.skus.size(); i++) {
                    WarBilling.this.OutputLog("Checking sku " + ((SkuEntry) WarBilling.this.skus.get(i)).id);
                    toCheck.add(((SkuEntry) WarBilling.this.skus.get(i)).id);
                }
                WarBilling.this.OutputLog("Setup successful. Querying inventory.");
                try {
                    WarBilling.this.mHelper.queryInventoryAsync(true, toCheck, WarBilling.this.mGotInventoryListener);
                } catch (IllegalStateException ex) {
                    WarBilling.this.OutputLog("inventory error " + ex.getMessage());
                }
            }
        });*/
        return true;
    }

    public boolean RequestPurchase(String id) {
        int index = GetSKU(id);
        this.mPurchasingIndex = index;
        if (index == -1 || this.skus.get(index).purchased) {
            OutputLog("Not requesting purchase " + id + " " + index);
            return false;
        }
        OutputLog("Requesting purchase " + id + " " + index + " " + this.skus.get(index).purchased);
        this.delayInputForStore = true;
        //this.mHelper.launchPurchaseFlow(this, id, 10002, this.mPurchaseFinishedListener);
        return true;
    }

    public String LocalizedPrice(String id) {
        int index = GetSKU(id);
        if (index != -1) {
            return this.skus.get(index).priceFormat;
        }
        return "";
    }

    public void SetBillingKey(String key) {
        this.mBillingKey = key;
    }

    /* access modifiers changed from: protected */
    public String getBillingKey() {
        return this.mBillingKey;
    }

    /* access modifiers changed from: package-private */
    public void complain(String message) {
        Log.e(TAG, "Billing Error: " + message);
        alert("Error: " + message);
    }

    /* access modifiers changed from: package-private */
    public void alert(String message) {
        Log.d(TAG, message);
    }

    /* access modifiers changed from: protected */
    /*public void purchaseEvent(SkuDetails item, Purchase purchase) {
    }*/
}
