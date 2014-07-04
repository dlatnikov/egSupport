package com.egrocery.DO;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by akolesnik on 7/1/14.
 */
public class PromotionDO {
    //    private Double basePrice;
    private Integer type;
    private String promoPrice;
    private Integer forQuantity;
    private String startDate;
    private String endDate;
    private String saveAmount;
    private String priceText;

    // seed is important to have repeatability
    private static Random random = new Random(1234);

//    public Double getBasePrice() {
//        return basePrice;
//    }

    public String getPromoPrice() {
        return promoPrice;
    }

    public Integer getType() {
        return type;
    }

    public Integer getForQuantity() {
        return forQuantity;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getSaveAmount() {
        return saveAmount;
    }

    public String getPriceText() {
        return priceText;
    }

    private PromotionDO() {
    }

    public static PromotionDO getPromotion(Double price) {
        PromotionDO promotion = new PromotionDO();
        double promoPrice = 0.01;
//       "promoType" - promotion type = 0[no promo] || 1 [On special] || 2 [Price drop] || 3 [Dailies]
        // 0 should come more often
        for (int i=0; i<3; i++) {
            promotion.type = random.nextInt(4);
            if (promotion.type.equals(0)) {
                break;
            }
        }

//        "promoForQuantity" = "1"
        promotion.forQuantity = 1;

        if (promotion.type.equals(0)) {
            promotion.promoPrice = "";
            promotion.startDate = "";
            promotion.endDate = "";
            promotion.saveAmount = ".00";
            promotion.priceText = "You Save .00";

            return promotion;
        }

//    "promoPrice" - generated price for product with promotion, promo price should be less then general price = {"90.00"}
        if (promotion.type.equals(1)) {
            // on special
            promoPrice = price * 0.9;
        } else if (promotion.type.equals(2)) {
            // price drop
            promoPrice = price * 0.8;
        } else if (promotion.type.equals(3)) {
            // dailies
            promoPrice = price * 0.95;
        } else {
            promoPrice = price;
        }

        DecimalFormat df = new DecimalFormat("##.##");
        promotion.promoPrice = df.format(promoPrice);

//    "promoStart" - start date - "20131120"
        promotion.startDate = "20131120";

//    "promoEnd" - end date - "20201212"
        promotion.endDate = "20201212";

//    "saveAmount" - amount your saved = "price - promoPrice"
        promotion.saveAmount = df.format(price - promoPrice);

//    "promoPriceText" - text = "You Save 'saveAmount'"
        promotion.priceText = "You Save " + promotion.saveAmount;

        return promotion;
    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 20; i++) {
//            double minPrice = 1;
//            double maxPrice = 10;
//            double priceValue = minPrice + (maxPrice - minPrice) * random.nextDouble();
//            DecimalFormat df = new DecimalFormat("##.##");
//            System.out.println(Double.valueOf(df.format(priceValue)).intValue());
//        }
//    }
}
