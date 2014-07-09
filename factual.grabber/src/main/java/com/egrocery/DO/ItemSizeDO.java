package com.egrocery.DO;

import com.egrocery.DataExporter;
import com.egrocery.Util.DataExporterUtils;
import com.google.api.client.repackaged.com.google.common.base.Strings;

import java.text.DecimalFormat;

/**
 * Created by akolesnik on 7/9/14.
 */
public class ItemSizeDO {
    private String itemSize;
    private String extendedSize;
    private String packSize;

    private ItemSizeDO() {
    }

    public static ItemSizeDO getItemSize(String size, String servings, String yom) {
        ItemSizeDO result = new ItemSizeDO();

        if (Strings.isNullOrEmpty(size) || Strings.isNullOrEmpty(servings)) {
            result.itemSize = "1";
            result.packSize = "1";
            result.extendedSize = "1 " + yom;
        } else {
            try {
                double sizeCount = Double.parseDouble(DataExporterUtils.getUnitCount(size)) /
                        Double.parseDouble(servings);

                //check that sizeCount is a valid double
                if (sizeCount == Double.POSITIVE_INFINITY ||
                        sizeCount == Double.NEGATIVE_INFINITY ||
                        sizeCount == Double.NaN) {
                    throw new ArithmeticException();
                }

                sizeCount = sizeCount < 0.01 ? 0.01 : sizeCount;
                DecimalFormat df = new DecimalFormat("##.##");
                result.packSize = servings;
                result.extendedSize = df.format(sizeCount) + " " + yom;
                result.itemSize = df.format(sizeCount);
                //for catching NumberFormatException or ArithmeticException
            } catch (RuntimeException e) {
                result.itemSize = "1";
                result.packSize = "1";
                result.extendedSize = "1 " + yom;
            }
        }
        return result;
    }

    public String getItemSize() {
        return itemSize;
    }

    public String getExtendedSize() {
        return extendedSize;
    }

    public String getPackSize() {
        return packSize;
    }
}
