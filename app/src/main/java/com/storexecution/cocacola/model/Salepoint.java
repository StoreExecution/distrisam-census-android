package com.storexecution.cocacola.model;

import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.annotations.SerializedName;
import com.storexecution.cocacola.R;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Salepoint extends RealmObject {

    @PrimaryKey
    String mobile_id;

    String posName;
    String ownerName;
    String ownerPhone;
    String managerName;
    String managerPhone;
    @SerializedName("created_mobile_date")
    long createdMobileDate;
    @SerializedName("modification_mobile_date")
    long mobileModificationDate;
    long startedMobileDate;
    long finishedMobileDate;
    @SerializedName("commune_id")
    int commune;
    @SerializedName("salepoint_types_id")
    int salepointType;
    int salepointSurface;
    @SerializedName("purchase_souce_id")
    int purchaseSource;
    int purchaseFrequency;

    int salepointZone;
    RealmList<Integer> visitdays;

    RealmList<TagElement> MetalRacks;
    RealmList<TagElement> ForexRacks;
    RealmList<TagElement> WrapedLinear;
    RealmList<TagElement> WrapedSkids;

    RealmList<TagElement> tindas;
    RealmList<TagElement> ears;
    RealmList<TagElement> potence;
    RealmList<TagElement> windowwrap;
    RealmList<TagElement> sidewalkStop;
    RealmList<TagElement> tables;
    RealmList<TagElement> chaires;
    RealmList<TagElement> parasols;

    String purchaseCocaColaHigh;
    String purchaseCocaColaLow;

    String purchasePepsiHigh;
    String purchasePepsiLow;

    String purchaseHamoudHigh;
    String purchaseHamoudLow;

    String purchaseRouibaHigh;
    String purchaseRouibaLow;

    String purchaseSodaHigh;
    String purchaseSodaLow;
    String purchaseRouibaPetHigh;
    String purchaseRouibaPetLow;

    boolean dnRouiba;
    boolean dnRouibaPet;

    String purchaseWaterHigh;
    String purchaseWaterLow;

    String purchaseJuiceHigh;
    String purchaseJuiceLow;


    String purchaseJuicePetHigh;
    String purchaseJuicePetLow;

    String purchaseEnergyDrinkHigh;
    String purchaseEnergyDrinkLow;

    String purchaseOtherHigh;
    String purchaseOtherLow;
    RealmList<Fridge> cocaColaFridges;
    RealmList<ConcurrentFridge> pepsiFridges;
    RealmList<ConcurrentFridge> hamoudFridges;
    RealmList<ConcurrentFridge> otherFridges;
    int cocaColaFridgesCount;

    int deliveryRating;
    int smileyRating;
    String bestDeliveryCompany;
    RealmList<Integer> deliveryPoints;
    int hasFridge;
    int sellSoda;
    int hasRgb;
    int hasKoRgb;
    int hasWarmStock;
    String emptyKoRgb;
    String fullKoRgb;

    String rgbLitterFull;
    String rgbLitterEmty;
    String rgbSmallFull;
    String rgbSmallEmpty;
    String barcode;
    String landmark;

    double latitude;
    double longitude;
    double accurency;
    boolean isMock;
    boolean synced;
    int externalFridge;
    int wantToSellSoda;
    int cocacolaCombo;
    int classification;
    int closed;
    int closedReason;

    String fridgeCount;

    int refuse;
    int refuseReason;
    String otherRefuseReason;
    int posSystem;

    public Salepoint() {
        MetalRacks = new RealmList<>();
        ForexRacks = new RealmList<>();
        WrapedLinear = new RealmList<>();
        WrapedSkids = new RealmList<>();
        tindas = new RealmList<>();
        ears = new RealmList<>();
        potence = new RealmList<>();
        windowwrap = new RealmList<>();
        sidewalkStop = new RealmList<>();
        tables = new RealmList<>();
        chaires = new RealmList<>();
        parasols = new RealmList<>();
        commune = 0;
        fridgeCount = "";
        mobile_id = "";
        posName = "";
        ownerName = "";
        ownerPhone = "";
        managerName = "";
        managerPhone = "";
        salepointType = 0;
        salepointSurface = 0;
        purchaseSource = 0;
        purchaseFrequency = 0;
        visitdays = new RealmList<>();
        salepointZone = 0;
        purchaseCocaColaHigh = "";
        purchaseCocaColaLow = "";
        purchasePepsiHigh = "";
        purchasePepsiLow = "";
        purchaseHamoudHigh = "";
        purchaseHamoudLow = "";

        purchaseSodaHigh = "";
        purchaseSodaLow = "";

        purchaseRouibaHigh = "";
        purchaseRouibaLow = "";
        purchaseRouibaPetHigh = "";
        purchaseRouibaPetLow = "";
        dnRouiba = false;
        dnRouibaPet = false;
        purchaseWaterHigh = "";
        purchaseWaterLow = "";
        purchaseJuiceHigh = "";
        purchaseJuiceLow = "";
        cocaColaFridgesCount = 0;
        cocaColaFridges = new RealmList<>();
        pepsiFridges = new RealmList<>();
        hamoudFridges = new RealmList<>();
        otherFridges = new RealmList<>();
        purchaseJuicePetHigh = "";
        purchaseJuicePetLow = "";
        purchaseEnergyDrinkHigh = "";
        purchaseEnergyDrinkLow = "";
        purchaseOtherHigh = "";
        purchaseOtherLow = "";

        deliveryRating = 0;
        bestDeliveryCompany = "";
        deliveryPoints = new RealmList<>();
        hasFridge = -1;
        sellSoda = -1;

        hasRgb = -1;
        hasKoRgb = -1;
        hasWarmStock = -1;
        emptyKoRgb = "";
        fullKoRgb = "";

        rgbLitterFull = "";
        rgbLitterEmty = "";
        rgbSmallFull = "";
        rgbSmallEmpty = "";
        synced = false;
        barcode = "";
        landmark = "";
        externalFridge = -1;
        wantToSellSoda = -1;
        cocacolaCombo = -1;
        createdMobileDate = 0;
        mobileModificationDate = 0;
        startedMobileDate = 0;
        finishedMobileDate = 0;
        smileyRating = -1;
        classification = 0;
        closed = -1;
        closedReason = 0;

        refuse = -1;
        refuseReason = 0;
        otherRefuseReason = "";
        posSystem = -1;
    }

    public void setVisitdays(RealmList<Integer> visitdays) {
        this.visitdays = visitdays;
    }

    public RealmList<TagElement> getMetalRacks() {
        return MetalRacks;
    }

    public void setMetalRacks(RealmList<TagElement> metalRacks) {
        MetalRacks = metalRacks;
    }

    public RealmList<TagElement> getForexRacks() {
        return ForexRacks;
    }

    public void setForexRacks(RealmList<TagElement> forexRacks) {
        ForexRacks = forexRacks;
    }

    public RealmList<TagElement> getWrapedLinear() {
        return WrapedLinear;
    }

    public void setWrapedLinear(RealmList<TagElement> wrapedLinear) {
        WrapedLinear = wrapedLinear;
    }

    public RealmList<TagElement> getWrapedSkids() {
        return WrapedSkids;
    }

    public void setWrapedSkids(RealmList<TagElement> wrapedSkids) {
        WrapedSkids = wrapedSkids;
    }

    public RealmList<TagElement> getTindas() {
        return tindas;
    }

    public void setTindas(RealmList<TagElement> tindas) {
        this.tindas = tindas;
    }

    public RealmList<TagElement> getEars() {
        return ears;
    }

    public void setEars(RealmList<TagElement> ears) {
        this.ears = ears;
    }

    public RealmList<TagElement> getPotence() {
        return potence;
    }

    public void setPotence(RealmList<TagElement> potence) {
        this.potence = potence;
    }

    public RealmList<TagElement> getWindowwrap() {
        return windowwrap;
    }

    public void setWindowwrap(RealmList<TagElement> windowwrap) {
        this.windowwrap = windowwrap;
    }

    public RealmList<TagElement> getSidewalkStop() {
        return sidewalkStop;
    }

    public void setSidewalkStop(RealmList<TagElement> sidewalkStop) {
        this.sidewalkStop = sidewalkStop;
    }

    public RealmList<TagElement> getTables() {
        return tables;
    }

    public void setTables(RealmList<TagElement> tables) {
        this.tables = tables;
    }

    public RealmList<TagElement> getChaires() {
        return chaires;
    }

    public void setChaires(RealmList<TagElement> chaires) {
        this.chaires = chaires;
    }

    public RealmList<TagElement> getParasols() {
        return parasols;
    }

    public void setParasols(RealmList<TagElement> parasols) {
        this.parasols = parasols;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public int getSalepointType() {
        return salepointType;
    }

    public void setSalepointType(int salepointType) {
        this.salepointType = salepointType;
    }

    public int getSalepointSurface() {
        return salepointSurface;
    }

    public void setSalepointSurface(int salepointSurface) {
        this.salepointSurface = salepointSurface;
    }

    public int getPurchaseSource() {
        return purchaseSource;
    }

    public void setPurchaseSource(int purchaseSource) {
        this.purchaseSource = purchaseSource;
    }

    public int getPurchaseFrequency() {
        return purchaseFrequency;
    }

    public void setPurchaseFrequency(int purchaseFrequency) {
        this.purchaseFrequency = purchaseFrequency;
    }


    public void setVisitdays(ArrayList<Integer> visitdays) {
        this.visitdays.clear();
        this.visitdays.addAll(visitdays);
    }

    public int getSalepointZone() {
        return salepointZone;
    }

    public void setSalepointZone(int salepointZone) {
        this.salepointZone = salepointZone;
    }


    public String getPurchaseCocaColaHigh() {
        return purchaseCocaColaHigh;
    }

    public void setPurchaseCocaColaHigh(String purchaseCocaColaHigh) {
        this.purchaseCocaColaHigh = purchaseCocaColaHigh;
    }

    public String getPurchaseCocaColaLow() {
        return purchaseCocaColaLow;
    }

    public void setPurchaseCocaColaLow(String purchaseCocaColaLow) {
        this.purchaseCocaColaLow = purchaseCocaColaLow;
    }

    public String getPurchasePepsiHigh() {
        return purchasePepsiHigh;
    }

    public void setPurchasePepsiHigh(String purchasePepsiHigh) {
        this.purchasePepsiHigh = purchasePepsiHigh;
    }

    public String getPurchasePepsiLow() {
        return purchasePepsiLow;
    }

    public void setPurchasePepsiLow(String purchasePepsiLow) {
        this.purchasePepsiLow = purchasePepsiLow;
    }

    public String getPurchaseHamoudHigh() {
        return purchaseHamoudHigh;
    }

    public void setPurchaseHamoudHigh(String purchaseHamoudHigh) {
        this.purchaseHamoudHigh = purchaseHamoudHigh;
    }

    public String getPurchaseHamoudLow() {
        return purchaseHamoudLow;
    }

    public void setPurchaseHamoudLow(String purchaseHamoudLow) {
        this.purchaseHamoudLow = purchaseHamoudLow;
    }

    public String getPurchaseRouibaHigh() {
        return purchaseRouibaHigh;
    }

    public void setPurchaseRouibaHigh(String purchaseRouibaHigh) {
        this.purchaseRouibaHigh = purchaseRouibaHigh;
    }

    public String getPurchaseRouibaLow() {
        return purchaseRouibaLow;
    }

    public void setPurchaseRouibaLow(String purchaseRouibaLow) {
        this.purchaseRouibaLow = purchaseRouibaLow;
    }

    public String getPurchaseRouibaPetHigh() {
        return purchaseRouibaPetHigh;
    }

    public void setPurchaseRouibaPetHigh(String purchaseRouibaPetHigh) {
        this.purchaseRouibaPetHigh = purchaseRouibaPetHigh;
    }

    public String getPurchaseRouibaPetLow() {
        return purchaseRouibaPetLow;
    }

    public void setPurchaseRouibaPetLow(String purchaseRouibaPetLow) {
        this.purchaseRouibaPetLow = purchaseRouibaPetLow;
    }


    public String getPurchaseWaterHigh() {
        return purchaseWaterHigh;
    }

    public void setPurchaseWaterHigh(String purchaseWaterHigh) {
        this.purchaseWaterHigh = purchaseWaterHigh;
    }

    public String getPurchaseWaterLow() {
        return purchaseWaterLow;
    }

    public void setPurchaseWaterLow(String purchaseWaterLow) {
        this.purchaseWaterLow = purchaseWaterLow;
    }

    public String getPurchaseJuiceHigh() {
        return purchaseJuiceHigh;
    }

    public void setPurchaseJuiceHigh(String purchaseJuiceHigh) {
        this.purchaseJuiceHigh = purchaseJuiceHigh;
    }

    public String getPurchaseJuiceLow() {
        return purchaseJuiceLow;
    }

    public void setPurchaseJuiceLow(String purchaseJuiceLow) {
        this.purchaseJuiceLow = purchaseJuiceLow;
    }

    public String getPurchaseJuicePetHigh() {
        return purchaseJuicePetHigh;
    }

    public void setPurchaseJuicePetHigh(String purchaseJuicePetHigh) {
        this.purchaseJuicePetHigh = purchaseJuicePetHigh;
    }

    public String getPurchaseJuicePetLow() {
        return purchaseJuicePetLow;
    }

    public void setPurchaseJuicePetLow(String purchaseJuicePetLow) {
        this.purchaseJuicePetLow = purchaseJuicePetLow;
    }

    public String getPurchaseEnergyDrinkHigh() {
        return purchaseEnergyDrinkHigh;
    }

    public void setPurchaseEnergyDrinkHigh(String purchaseEnergyDrinkHigh) {
        this.purchaseEnergyDrinkHigh = purchaseEnergyDrinkHigh;
    }

    public String getPurchaseEnergyDrinkLow() {
        return purchaseEnergyDrinkLow;
    }

    public void setPurchaseEnergyDrinkLow(String purchaseEnergyDrinkLow) {
        this.purchaseEnergyDrinkLow = purchaseEnergyDrinkLow;
    }

    public String getPurchaseOtherHigh() {
        return purchaseOtherHigh;
    }

    public void setPurchaseOtherHigh(String purchaseOtherHigh) {
        this.purchaseOtherHigh = purchaseOtherHigh;
    }

    public String getPurchaseOtherLow() {
        return purchaseOtherLow;
    }

    public void setPurchaseOtherLow(String purchaseOtherLow) {
        this.purchaseOtherLow = purchaseOtherLow;
    }

    public boolean isDnRouiba() {
        return dnRouiba;
    }

    public void setDnRouiba(boolean dnRouiba) {
        this.dnRouiba = dnRouiba;
    }

    public boolean isDnRouibaPet() {
        return dnRouibaPet;
    }

    public void setDnRouibaPet(boolean dnRouibaPet) {
        this.dnRouibaPet = dnRouibaPet;
    }

    public String getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public RealmList<ConcurrentFridge> getPepsiFridges() {
        return pepsiFridges;
    }

    public RealmList<Integer> getVisitdays() {
        return visitdays;
    }

    public void setPepsiFridges(RealmList<ConcurrentFridge> pepsiFridges) {
        this.pepsiFridges = pepsiFridges;
    }

    public RealmList<ConcurrentFridge> getHamoudFridges() {
        return hamoudFridges;
    }

    public void setHamoudFridges(RealmList<ConcurrentFridge> hamoudFridges) {
        this.hamoudFridges = hamoudFridges;
    }

    public RealmList<ConcurrentFridge> getOtherFridges() {
        return otherFridges;
    }

    public void setOtherFridges(RealmList<ConcurrentFridge> otherFridges) {
        this.otherFridges = otherFridges;
    }

    public void setDeliveryPoints(RealmList<Integer> deliveryPoints) {
        this.deliveryPoints = deliveryPoints;
    }

    public int getCocaColaFridgesCount() {
        return cocaColaFridgesCount;
    }

    public void setCocaColaFridgesCount(int cocaColaFridgesCount) {
        this.cocaColaFridgesCount = cocaColaFridgesCount;
    }

    public int getDeliveryRating() {
        return deliveryRating;
    }

    public void setDeliveryRating(int deliveryRating) {
        this.deliveryRating = deliveryRating;
    }

    public String getBestDeliveryCompany() {
        return bestDeliveryCompany;
    }

    public void setBestDeliveryCompany(String bestDeliveryCompany) {
        this.bestDeliveryCompany = bestDeliveryCompany;
    }

    public RealmList<Integer> getDeliveryPoints() {
        return deliveryPoints;
    }

    public int getHasFridge() {
        return hasFridge;
    }

    public void setHasFridge(int hasFridge) {
        this.hasFridge = hasFridge;
    }

    public int getSellSoda() {
        return sellSoda;
    }

    public void setSellSoda(int sellSoda) {
        this.sellSoda = sellSoda;
    }

    public int getHasRgb() {
        return hasRgb;
    }

    public void setHasRgb(int hasRgb) {
        this.hasRgb = hasRgb;
    }

    public int getHasKoRgb() {
        return hasKoRgb;
    }

    public void setHasKoRgb(int hasKoRgb) {
        this.hasKoRgb = hasKoRgb;
    }

    public int getHasWarmStock() {
        return hasWarmStock;
    }

    public void setHasWarmStock(int hasWarmStock) {
        this.hasWarmStock = hasWarmStock;
    }

    public String getEmptyKoRgb() {
        return emptyKoRgb;
    }

    public void setEmptyKoRgb(String emptyKoRgb) {
        this.emptyKoRgb = emptyKoRgb;
    }

    public String getFullKoRgb() {
        return fullKoRgb;
    }

    public void setFullKoRgb(String fullKoRgb) {
        this.fullKoRgb = fullKoRgb;
    }

    public String getRgbLitterFull() {
        return rgbLitterFull;
    }

    public void setRgbLitterFull(String rgbLitterFull) {
        this.rgbLitterFull = rgbLitterFull;
    }

    public String getRgbLitterEmty() {
        return rgbLitterEmty;
    }

    public void setRgbLitterEmty(String rgbLitterEmty) {
        this.rgbLitterEmty = rgbLitterEmty;
    }

    public String getRgbSmallFull() {
        return rgbSmallFull;
    }

    public void setRgbSmallFull(String rgbSmallFull) {
        this.rgbSmallFull = rgbSmallFull;
    }

    public String getRgbSmallEmpty() {
        return rgbSmallEmpty;
    }

    public void setRgbSmallEmpty(String rgbSmallEmpty) {
        this.rgbSmallEmpty = rgbSmallEmpty;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccurency() {
        return accurency;
    }

    public void setAccurency(double accurency) {
        this.accurency = accurency;
    }

    public boolean isMock() {
        return isMock;
    }

    public void setMock(boolean mock) {
        isMock = mock;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public RealmList<Fridge> getCocaColaFridges() {
        return cocaColaFridges;
    }

    public void setCocaColaFridges(RealmList<Fridge> cocaColaFridges) {
        this.cocaColaFridges = cocaColaFridges;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getExternalFridge() {
        return externalFridge;
    }

    public void setExternalFridge(int externalFridge) {
        this.externalFridge = externalFridge;
    }

    public int getWantToSellSoda() {
        return wantToSellSoda;
    }

    public void setWantToSellSoda(int wantToSellSoda) {
        this.wantToSellSoda = wantToSellSoda;
    }

    public int getCocacolaCombo() {
        return cocacolaCombo;
    }

    public void setCocacolaCombo(int cocacolaCombo) {
        this.cocacolaCombo = cocacolaCombo;
    }

    public int getCommune() {
        return commune;
    }

    public void setCommune(int commune) {
        this.commune = commune;
    }

    public long getCreatedMobileDate() {
        return createdMobileDate;
    }

    public void setCreatedMobileDate(long createdMobileDate) {
        this.createdMobileDate = createdMobileDate;
    }

    public long getMobileModificationDate() {
        return mobileModificationDate;
    }

    public void setMobileModificationDate(long mobileModificationDate) {
        this.mobileModificationDate = mobileModificationDate;
    }

    public long getStartedMobileDate() {
        return startedMobileDate;
    }

    public void setStartedMobileDate(long startedMobileDate) {
        this.startedMobileDate = startedMobileDate;
    }

    public long getFinishedMobileDate() {
        return finishedMobileDate;
    }

    public void setFinishedMobileDate(long finishedMobileDate) {
        this.finishedMobileDate = finishedMobileDate;
    }

    public String getFridgeCount() {
        return fridgeCount;
    }

    public void setFridgeCount(String fridgeCount) {
        this.fridgeCount = fridgeCount;
    }

    public int getSmileyRating() {
        return smileyRating;
    }

    public void setSmileyRating(int smileyRating) {
        this.smileyRating = smileyRating;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getClosedReason() {
        return closedReason;
    }

    public void setClosedReason(int closedReason) {
        this.closedReason = closedReason;
    }

    public int getRefuse() {
        return refuse;
    }

    public void setRefuse(int refuse) {
        this.refuse = refuse;
    }

    public int getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(int refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getOtherRefuseReason() {
        return otherRefuseReason;
    }

    public void setOtherRefuseReason(String otherRefuseReason) {
        this.otherRefuseReason = otherRefuseReason;
    }

    public String getPurchaseSodaHigh() {
        return purchaseSodaHigh;
    }

    public void setPurchaseSodaHigh(String purchaseSodaHigh) {
        this.purchaseSodaHigh = purchaseSodaHigh;
    }

    public String getPurchaseSodaLow() {
        return purchaseSodaLow;
    }

    public void setPurchaseSodaLow(String purchaseSodaLow) {
        this.purchaseSodaLow = purchaseSodaLow;
    }

    public int getPosSystem() {
        return posSystem;
    }

    public void setPosSystem(int posSystem) {
        this.posSystem = posSystem;
    }
}


