package com.egrocery;

import com.egrocery.DO.PromotionDO;
import com.google.api.client.repackaged.com.google.common.base.Strings;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by akolesnik on 6/20/14.
 */
public class DataExporter {
    private static String fileFolder = "/Users/Yama/work/gd/hybris/egrocery/impex_export";
    private static String productsFileName = fileFolder + "/products-factual.impex";
    private static String categoriesFileName = fileFolder + "/categories-factual.impex";
    private static String prodcatFileName = fileFolder + "/prodcat-factual.impex";
    private static String pricerowFileName = fileFolder + "/price-row-factual.impex";
    private static String sizeFacetFileName = fileFolder + "/size-factual.impex";
    private static String nutrientsFileName = fileFolder + "/nutrient-factual.impex";

    public static void main(String[] args) throws SQLException, FileNotFoundException {
//        fillCategoriesTable(ConnectionManager.getConnection());
        writeToFile(productsFileName, productsFileStaticHead, "", productDataExtractor());
//        writeToFile(categoriesFileName, categoriesFileStaticHead, "", categoryDataExtractor());
//        writeToFile(prodcatFileName, prodcatFileStaticHead, "", prodcatDataExtractor());
//        writeToFile(pricerowFileName, pricerowFileStaticHead, "", pricerowDataExtractor());
//        writeToFile(sizeFacetFileName, sizeFacetFileStaticHead, "", sizeFacetDataExtractor());
//        writeToFile(nutrientsFileName, nutrientFileStaticHead, "", nutrientDataExtractor());
    }

    private static List<List<String>> productDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String basicProductDataQuery = "select upc code, product_name name, product_name description, ean13 ean," +
                " \"approved\" approvalstatus, size, \"9\" deptNumber, brand, ingredients from `products-cpg-nutrition`";


        Connection connection = ConnectionManager.getConnection();
        Statement productStatement = connection.createStatement();
        ResultSet resultSet = productStatement.executeQuery(basicProductDataQuery);

        while (resultSet.next()) {
            List<String> row = new ArrayList<>();
            row.add(resultSet.getString("code"));
            row.add(resultSet.getString("name"));
            row.add(resultSet.getString("description"));
            row.add(resultSet.getString("ean"));
            row.add(resultSet.getString("approvalstatus"));

            //unit
            row.add("pieces");

            //sellbyCode
            String size = resultSet.getString("size");
            String sellbyCode = getUnitType(getUnit(size));
            row.add(sellbyCode);

            row.add(resultSet.getString("deptNumber"));
            row.add(resultSet.getString("brand"));

            row.add(resultSet.getString("description"));

            //remove unnecessary symbols
            String ingredients = resultSet.getString("ingredients");
            if (ingredients != null) {
                ingredients = ingredients.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "");
            }
            row.add(ingredients);

            rows.add(row);
        }

        return rows;
    }

    private static List<List<String>> nutrientDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String basicCategoryDataQuery = "select ean13, calories,fat_calories,total_fat,sat_fat,polyunsat_fat," +
                "monounsat_fat,trans_fat,cholesterol,sodium,potassium,total_carb,dietary_fiber,soluble_fiber," +
                "insoluble_fiber,sugar_alcohol,sugars,protein,calcium,iron,vitamin_a,vitamin_c " +
                "from `products-cpg-nutrition`";

        Connection connection = ConnectionManager.getConnection();
        Statement productStatement = connection.createStatement();
        ResultSet resultSet = productStatement.executeQuery(basicCategoryDataQuery);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        while (resultSet.next()) {

            for (int i = 2; i <= resultSetMetaData.getColumnCount(); i++) {
                List<String> row = new ArrayList<>();
                String columnName = resultSetMetaData.getColumnName(i);
                row.add(columnName + ":0");
                String nutrient = resultSet.getString(columnName);
                if (!Strings.isNullOrEmpty(nutrient)) {
                    row.add(nutrient);
                    row.add("g");
                } else {
                    row.add("0");
                    row.add("n/a");
                }
                row.add("");
                row.add("0");
                row.add("0");
                row.add(resultSet.getString("ean13"));
                rows.add(row);
            }
        }
        return rows;
    }

    private static List<List<String>> categoryDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String basicCategoryDataQuery = "select category_id, category_name from categories";

        Connection connection = ConnectionManager.getConnection();
        Statement productStatement = connection.createStatement();
        ResultSet resultSet = productStatement.executeQuery(basicCategoryDataQuery);

        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>(2);
            row.add("s" + resultSet.getString("category_id"));
            row.add(resultSet.getString("category_name"));

            rows.add(row);
        }

        return rows;
    }

    private static List<List<String>> prodcatDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String basicProdcatDataQuery = "select c.category_id category_id, p.upc upc from categories c " +
                "join `products-cpg-nutrition` p on c.category_name = p.category";

        Connection connection = ConnectionManager.getConnection();
        Statement productStatement = connection.createStatement();
        ResultSet resultSet = productStatement.executeQuery(basicProdcatDataQuery);

        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>(2);
            row.add("s" + resultSet.getString("category_id"));
            row.add(resultSet.getString("upc"));

            rows.add(row);
        }

        return rows;
    }

    private static List<List<String>> pricerowDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String basicProdcatDataQuery = "select upc, avg_price from `products-cpg-nutrition`";

        Connection connection = ConnectionManager.getConnection();
        Statement productStatement = connection.createStatement();
        ResultSet resultSet = productStatement.executeQuery(basicProdcatDataQuery);

        Random random = new Random();
        int store1 = 420;
        int store2 = 421;
        DecimalFormat df = new DecimalFormat("###");

        PromotionDO promotion;

        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>();
//    "Product(code,$catalog_version)[unique=true]" - upc = {885898000079}
            row.add(resultSet.getString("upc"));
//    "store(groceryStoreNo)[unique=true]" - store number = 420 || 421
            row.add(Integer.valueOf(df.format((store1 + (store2 - store1) * random.nextDouble()))).toString());
//    "dateRange[unique=true,dateformat=yyyyMMdd]" - date range = 20131127,20990101
            row.add("20131127,20990101");
//    "minqtd" - QUANTITY =1
            row.add("1");
//    "price" - generated price = {avg_price}
            row.add(resultSet.getString("avg_price"));

            promotion = PromotionDO.getPromotion(Double.valueOf(resultSet.getString("avg_price")));
//BEGIN PROMOTION DATA
//    "promoType" - promotion type = 0[- no promo] || 1 || 2 || 3
//            row.add("0");
            row.add(promotion.getType().toString());
//    "promoForQuantity" = "1"
//            row.add("1");
            row.add(promotion.getForQuantity().toString());
//    "promoPrice" - generated price for product with promotion, promo price should be less then general price = {"90.00"}
//            row.add("");
            row.add(promotion.getPromoPrice());
//    "promoStart" - start date - "20131120"
//            row.add("");
            row.add(promotion.getStartDate());
//    "promoEnd" - end date - "20141212"
//            row.add("");
            row.add(promotion.getEndDate());
//    "saveAmount" - amount your saved = "price - promoPrice"
//            row.add(".00");
            row.add(promotion.getSaveAmount());
//    "promoPriceText" - text = "You Save 'saveAmount'"
//            row.add("You Save .00");
            row.add(promotion.getPriceText());
//END PROMOTION DATA


//            "aisleNumber" = {"3"}
            row.add("3");
//            "aisleSide" = {"B"}
            row.add("B");
//            "ageRestriction" = {"0"}
            row.add("0");
//            "crvAmount" = {" 0.30"}
            row.add(" 0.30");
//            "salesTaxRate" = {" 0.08500"}
            row.add(" 0.08500");

            rows.add(row);
        }

        return rows;
    }

    private static List<List<String>> sizeFacetDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String basicProductDataQuery = "select upc code, ean13 ean " +
                "from `products-cpg-nutrition`";


        Connection connection = ConnectionManager.getConnection();
        Statement productStatement = connection.createStatement();
        ResultSet resultSet = productStatement.executeQuery(basicProductDataQuery);

        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>(5);
//            ;885898000079;1;1 EA;EA;
//            ;088395086526;1;1 OZ;OZ;
            row.add(resultSet.getString("code"));
            row.add("1");
            row.add("1");
            row.add("1 EA");
            row.add("EA");

            rows.add(row);

            row = new ArrayList<>(5);
            row.add(resultSet.getString("ean"));
            row.add("1");
            row.add("1");
            row.add("1 OZ");
            row.add("OZ");

            rows.add(row);
        }

        return rows;
    }

    private static String getUnit(String size) {
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

    private static String getUnitType(String unit) {
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

    private static void writeToFile(String fileName, String staticHead, String staticHeadDelimiter, List<List<String>> rows)
            throws FileNotFoundException {
        PrintWriter file = new PrintWriter(fileName);
        file.println(staticHead);
        file.print(staticHeadDelimiter);

        for (List<String> row : rows) {
            for (String cell : row) {
                file.print(";");
                file.print(cell);
            }
            file.println(";");
        }
        file.flush();
        file.close();
    }

    private static void fillCategoriesTable(Connection connection) throws SQLException {
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

    private static String productsFileStaticHead = "$catalog-id=raleysProductCatalog\n" +
            "$catalog-version=Staged\n" +
            "$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\n" +
            "INSERT_UPDATE Product;code[unique=true];name[lang=en];description[name=en];ean;approvalstatus(code);unit(code);sellbyCode;deptNumber;manufacturerName;productDetails;ingredients;$catalog_version";

    private static String categoriesFileStaticHead = "$contentCatalog=raleysContentCatalog\n" +
            "$contentCV=catalogVersion(CatalogVersion.catalog(Catalog.id[default=$contentCatalog]),CatalogVersion.version[default=Online])[default=$contentCatalog:Staged]\n" +
            "$catalogversion=catalogversion(catalog(id),version)[unique=true,default=raleysProductCatalog:Staged]\n" +
            "\n" +
            "INSERT_UPDATE RegularCategory;code[unique=true];name[lang=en];allowedPrincipals(uid)[default='customergroup'];$catalogversion";

    private static String prodcatFileStaticHead = "$catalog-id=raleysProductCatalog\n" +
            "$catalog-version=Staged\n" +
            "$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\n" +
            "$source=source(code, $catalog_version)[unique=true]\n" +
            "$target=target(code, $catalog_version)[unique=true]\n" +
            "$YCL=catalog-id='$catalog-id',catalog-version='$catalog-version',translator=com.raleys.dataimport.translator.DclCategoryTranslator;\n" +
            "INSERT_UPDATE CategoryProductRelation;$source;$target";

    private static String pricerowFileStaticHead = "\"$catalog-id=raleysProductCatalog\"\n" +
            "\"$catalog-version=Staged\"\n" +
            "\"$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\"\n" +
            "$store1=420\n" +
            "$store2=421\n" +
            "\n" +
            "\"INSERT_UPDATE RaleysPriceRow\";\"Product(code,$catalog_version)[unique=true]\";\"store(groceryStoreNo)[unique=true]\";\"dateRange[unique=true,dateformat=yyyyMMdd]\";\"minqtd\";\"price\";\"promoType\";\"promoForQuantity\";\"promoPrice\";\"promoStart\";\"promoEnd\";\"saveAmount\";\"promoPriceText\";\"aisleNumber\";\"aisleSide\";\"ageRestriction\";\"crvAmount\";\"salesTaxRate\";\"unit(code)[default=pieces]\";\"currency(isocode)[default=USD]\";\"net[default=true]\";\"$catalog_version\"";

    private static String sizeFacetFileStaticHead = "$catalog-id=raleysProductCatalog\n" +
            "$catalog-version=Staged\n" +
            "$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\n" +
            "\n" +
            "UPDATE Product;code[unique=true];packSize;extendedSize;uom;$catalog_version\n";

    private static String nutrientFileStaticHead = "INSERT_UPDATE Nutrient;master(name,type)[unique=true];quantity[default='0'];uom[default='n/a'];pct;isOrContains;valuePreparedType;ean[unique=true]\n";
}

