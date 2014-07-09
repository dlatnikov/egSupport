package com.egrocery;

import com.factual.driver.*;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;

/**
 * Created by akolesnik on 6/18/14.
 */

/**
 * Factual java api info:
 * https://github.com/Factual/factual-java-driver/
 * https://github.com/Factual/factual-java-driver/wiki/Getting-Started
 * https://github.com/Factual/factual-java-driver/wiki/Query-Filters
 * Factual site address:
 * https://www.factual.com/
 * Factual product categories list:
 * http://developer.factual.com/working-with-product-categories/
 * Link to catalog:
 * https://www.factual.com/data/t/products-cpg-nutrition
 */
public class FactualGrabber {

    private static Connection connection = ConnectionManager.getConnection();
    private static PreparedStatement insertProducts = null;
    private static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {

        long offset = 0;
        storeRowsByOffset(offset);
    }

    private static void store20RowsPerCategory() throws InterruptedException {
        try {
            insertProducts = connection.prepareStatement(insertStatement);
        } catch (SQLException e) {
            System.err.println("Exception during initializing database connection.");
            e.printStackTrace();
            System.exit(-2);
        }

        Factual factual = new Factual("xJ3uWDNzml71jEkpxwtbnVUJ7F7Gp5i8PfqbG504", "HonaHHNVJbBFrzjDRdIdd25bRtBixf8DE6Awvgiv");
        String catalogName = "products-cpg-nutrition";

        int fetchedRows = 0;
        int storedRows = 0;

        for (String categoryName : categories) {
            Query query = getQuery(categoryName, 0);
            ReadResponse response = factual.fetch(catalogName, query);
            fetchedRows += response.getIncludedRowCount();

            try {
                storedRows += storeData(response);
            } catch (SQLException e) {
                System.err.println("Fetched row count = " + fetchedRows);
                System.err.println("Stored row count = " + storedRows);
                System.err.println();
                e.printStackTrace();
                System.exit(-100);
            }

            System.out.println("Fetched row count = " + fetchedRows);
            System.out.println("Stored row count = " + storedRows);
            Thread.sleep(14000);
        }
        System.out.println("Data stored successfully!");
    }

    private static void storeRowsByOffset(long offset) throws InterruptedException {
        try {
            insertProducts = ConnectionManager.getConnection(ConnectionManager.ConnectionType.factual_warehouse)
                    .prepareStatement(insertStatement);
        } catch (SQLException e) {
            System.err.println("Exception during initializing database connection.");
            e.printStackTrace();
            System.exit(-2);
        }

        Factual factual = new Factual("xJ3uWDNzml71jEkpxwtbnVUJ7F7Gp5i8PfqbG504", "HonaHHNVJbBFrzjDRdIdd25bRtBixf8DE6Awvgiv");
        String catalogName = "products-cpg-nutrition";

        int fetchedRows = 0;
        int storedRows = 0;
        long _offset = offset;

        for (String categoryName : categories) {
            for (int i = 0; i < 5; i++) {
                Query query = getQuery(categoryName, _offset);
                //maximum page size, default value.
                _offset += 20;
                ReadResponse response = factual.fetch(catalogName, query);
                fetchedRows += response.getIncludedRowCount();

                try {
                    storedRows += storeData(response);
                } catch (SQLException e) {
                    System.err.println("Fetched row count = " + fetchedRows);
                    System.err.println("Stored row count = " + storedRows);
                    System.err.println();
                    e.printStackTrace();
                    System.exit(-100);
                }

                System.out.println("Fetched row count = " + fetchedRows);
                System.out.println("Stored row count = " + storedRows);
                Thread.sleep(14000);
            }
            System.out.println("Offset for category " + categoryName + " = " + _offset);
            _offset = offset;
        }
        System.out.println("Data stored successfully!");
    }

    private static Query getQuery(String categoryName, long offset) {
        Query query = new Query();
        query = query
                .offset(offset)
                .field("category").isEqual(categoryName)
                .and(
                        query.field("upc").notBlank())
                .or(
                        query.field("ingredients").notBlank(),
                        query.field("servings").notBlank(),
                        query.field("calories").notBlank(),
                        query.field("fat_calories").notBlank(),
                        query.field("total_fat").notBlank(),
                        query.field("sat_fat").notBlank(),
                        query.field("polyunsat_fat").notBlank(),
                        query.field("monounsat_fat").notBlank(),
                        query.field("trans_fat").notBlank(),
                        query.field("cholesterol").notBlank(),
                        query.field("sodium").notBlank(),
                        query.field("potassium").notBlank(),
                        query.field("total_carb").notBlank(),
                        query.field("dietary_fiber").notBlank(),
                        query.field("soluble_fiber").notBlank(),
                        query.field("insoluble_fiber").notBlank(),
                        query.field("sugar_alcohol").notBlank(),
                        query.field("sugars").notBlank(),
                        query.field("protein").notBlank(),
                        query.field("calcium").notBlank(),
                        query.field("iron").notBlank(),
                        query.field("vitamin_a").notBlank(),
                        query.field("vitamin_c").notBlank()
                );
        return query;
    }

    private static int storeData(Tabular response) throws SQLException {
        int rowsAffected = 0;

        for (Map<String, Object> row : response.getData()) {
            insertProducts.setObject(1, row.get("factual_id"), Types.VARCHAR);
            insertProducts.setObject(2, row.get("brand"), Types.VARCHAR);
            insertProducts.setObject(3, row.get("product_name"), Types.VARCHAR);
            insertProducts.setObject(4, row.get("size"), Types.VARCHAR);
            insertProducts.setObject(5, row.get("upc"), Types.VARCHAR);
            insertProducts.setObject(6, row.get("ean13"), Types.VARCHAR);
            insertProducts.setObject(7, row.get("category"), Types.VARCHAR);
            insertProducts.setObject(8, row.get("manufacturer"), Types.VARCHAR);

            insertProducts.setObject(9, getPrice(row.get("avg_price"), random), Types.DECIMAL);

            insertProducts.setObject(10, row.get("ingredients"), Types.VARCHAR);
            insertProducts.setObject(11, row.get("serving_size"), Types.VARCHAR);
            insertProducts.setObject(12, row.get("servings"), Types.DECIMAL);
            insertProducts.setObject(13, row.get("calories"), Types.DECIMAL);
            insertProducts.setObject(14, row.get("fat_calories"), Types.DECIMAL);
            insertProducts.setObject(15, row.get("total_fat"), Types.DECIMAL);
            insertProducts.setObject(16, row.get("sat_fat"), Types.DECIMAL);
            insertProducts.setObject(17, row.get("polyunsat_fat"), Types.DECIMAL);
            insertProducts.setObject(18, row.get("monounsat_fat"), Types.DECIMAL);
            insertProducts.setObject(19, row.get("trans_fat"), Types.DECIMAL);
            insertProducts.setObject(20, row.get("cholesterol"), Types.DECIMAL);
            insertProducts.setObject(21, row.get("sodium"), Types.DECIMAL);
            insertProducts.setObject(22, row.get("potassium"), Types.DECIMAL);
            insertProducts.setObject(23, row.get("total_carb"), Types.DECIMAL);
            insertProducts.setObject(24, row.get("dietary_fiber"), Types.DECIMAL);
            insertProducts.setObject(25, row.get("soluble_fiber"), Types.DECIMAL);
            insertProducts.setObject(26, row.get("insoluble_fiber"), Types.DECIMAL);
            insertProducts.setObject(27, row.get("sugar_alcohol"), Types.DECIMAL);
            insertProducts.setObject(28, row.get("sugars"), Types.DECIMAL);
            insertProducts.setObject(29, row.get("protein"), Types.DECIMAL);
            insertProducts.setObject(30, row.get("calcium"), Types.DECIMAL);
            insertProducts.setObject(31, row.get("iron"), Types.DECIMAL);
            insertProducts.setObject(32, row.get("vitamin_a"), Types.DECIMAL);
            insertProducts.setObject(33, row.get("vitamin_c"), Types.DECIMAL);
            insertProducts.setObject(34, row.get("image_urls"), Types.VARCHAR);
            insertProducts.setObject(35, row.get("upc_e"), Types.VARCHAR);

            rowsAffected += insertProducts.executeUpdate();
        }
        return rowsAffected;
    }

    private static double getPrice(Object price, Random random) {
        if (price != null) {
            Double dPrice = Double.valueOf(price.toString());
            if (!Double.isNaN(dPrice) && !Double.isInfinite(dPrice)) {
                return dPrice;
            }
        }

        double minPrice = 1;
        double maxPrice = 100;
        double priceValue = minPrice + (maxPrice - minPrice) * random.nextDouble();
        DecimalFormat df = new DecimalFormat("##.##");
        return Double.valueOf(df.format(priceValue));
    }

    private static String insertStatement = "INSERT INTO `products-cpg-nutrition`(`factual_id`," +
            "`brand`," +
            "`product_name`," +
            "`size`," +
            "`upc`," +
            "`ean13`," +
            "`category`," +
            "`manufacturer`," +
            "`avg_price`," +
            "`ingredients`," +
            "`serving_size`," +
            "`servings`," +
            "`calories`," +
            "`fat_calories`," +
            "`total_fat`," +
            "`sat_fat`," +
            "`polyunsat_fat`," +
            "`monounsat_fat`," +
            "`trans_fat`," +
            "`cholesterol`," +
            "`sodium`," +
            "`potassium`," +
            "`total_carb`," +
            "`dietary_fiber`," +
            "`soluble_fiber`," +
            "`insoluble_fiber`," +
            "`sugar_alcohol`," +
            "`sugars`," +
            "`protein`," +
            "`calcium`," +
            "`iron`," +
            "`vitamin_a`," +
            "`vitamin_c`," +
            "`image_urls`," +
            "`upc_e`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    private static String[] categories = {
            "Vitamins & Supplements",
            "Personal Care",
            "Pet Care",
            "Nutritional Bars, Drinks, and Shakes",
            "Tea & Coffee",
            "Fragrances",
            "Lip Makeup",
            "Eye & Eyebrow Makeup",
            "Food",
            "Face Makeup",
            "Extracts, Herbs & Spices",
            "Candy",
            "Snacks",
            "Body Lotions, Oils, Creams, Sprays",
            "Health & Medicine",
            "Skin Care",
            "Nail Makeup",
            "Alcoholic Beverages",
            "Baking Ingredients",
            "Cosmetics",
            "Hair Styling Aids",
            "Breakfast Foods",
            "Hair Care",
            "Body Soaps & Gels",
            "Cookies",
            "Frozen Foods",
            "Dairy & Dairy-Substitute Products",
            "Meat, Poultry, Seafood Products",
            "Bread",
            "Ice Cream & Frozen Desserts",
            "Baby Diapers & Diaper Care",
            "Sauces",
            "Hair Conditioners",
            "Juices",
            "Condiments",
            "Vegetables",
            "Chocolate",
            "Prepared Meals",
            "Bath Products",
            "Cold, Flu, Cough, Sore Throat",
            "Hair Shampoo",
            "Face Treatments",
            "Nuts",
            "Weight Loss Products & Supplements",
            "Hair Color",
            "Sexual Wellness",
            "Dog Supplies",
            "Pain Relief Medicine",
            "Crackers",
            "Shaving Products"
    };
}
