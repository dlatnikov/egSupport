package com.egrocery.Util;

import com.google.api.client.repackaged.com.google.common.base.Strings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akolesnik on 7/9/14.
 */
public class DataExporterUtils {
    public static String getUnitCount(String size) {
        if (Strings.isNullOrEmpty(size)) {
            return "1";
        }

        String sz = size;

        //Begin parsing formats: ["6 mg","180 count"], ["6 mg; 180 count"]
        int delimiterIndex = size.indexOf(",") == -1 ? size.indexOf(";") == -1 ? -1 : size.indexOf(";") : size.indexOf(",");
        if (delimiterIndex != -1) {
            sz = sz.substring(0, delimiterIndex);
        }
        sz = sz.replace("\"", "").replace("[", "").replace("]", "");

        int endIndex = sz.indexOf(" ");
        //to avoid cases like: "0-30"
        endIndex = sz.indexOf("-") == -1 ? endIndex : sz.indexOf("-");

        //to avoid cases like: size = ["6","4 oz; 113 g; cup"]
        if (endIndex != -1) {
            sz = sz.substring(0, endIndex);
        }

        //End parsing

        return sz;
    }

    public static String getUnit(String size) {
        if (size == null) {
            return "oz";
        }

        String sz = size;

        //Begin parsing formats: ["6 mg","180 count"], ["6 mg; 180 count"]
        int delimiterIndex = size.indexOf(",") == -1 ? size.indexOf(";") == -1 ? -1 : size.indexOf(";") : size.indexOf(",");
        if (delimiterIndex != -1) {
            sz = sz.substring(0, delimiterIndex);
        }
        sz = sz.replace("\"", "").replace("[", "").replace("]", "");
        sz = sz.substring(sz.indexOf(" ") + 1, sz.length());
        //End parsing

        return sz;
    }

    public static String getUnitType(String unit) {
        String unitType = "E";

        List<String> unitWeightTypeList = new ArrayList<String>() {{
            add("lb");
            add("#");
            add("oz");
            add("mg");
            add("g");
            add("kg");
        }};

        if (unitWeightTypeList.contains(unit.toLowerCase())) {
            return "W";
        }

        return unitType;
    }

    public static void fillCategoriesTable(Connection connection) throws SQLException {
        PreparedStatement selectCategories = connection.prepareStatement("select distinct category from `products-cpg-nutrition`");
        PreparedStatement insertCategories = connection.prepareStatement("insert into categories (category_id, category_name) values (?, ?)");
        ResultSet categories = selectCategories.executeQuery();
        int categoryIndex = 0;
        while (categories.next()) {
            categoryIndex++;
            String categoryName = categories.getObject("category").toString();
            insertCategories.setObject(1, categoryIndex, Types.INTEGER);
            insertCategories.setObject(2, categoryName, Types.VARCHAR);
            insertCategories.executeUpdate();
        }
    }

    public static void fillProductImagesTable(String code, String url, PreparedStatement insertCategories) {
        try {
            insertCategories.setObject(1, code, Types.VARCHAR);
            insertCategories.setObject(2, url, Types.VARCHAR);
            insertCategories.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
