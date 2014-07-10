package com.egrocery;

import com.egrocery.DO.ImageDO;
import com.egrocery.DO.ItemSizeDO;
import com.egrocery.DO.PromotionDO;
import com.egrocery.Util.DataExporterUtils;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.mysql.jdbc.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;

/**
 * Created by akolesnik on 6/20/14.
 */
public class DataExporter {
    private static String fileFolder = Config.getConfig().getImpexExportFolderName();

    private static String productsFileName = fileFolder + "/products-factual.impex";
    private static String categoriesFileName = fileFolder + "/categories-factual.impex";
    private static String prodcatFileName = fileFolder + "/prodcat-factual.impex";
    private static String pricerowFileName = fileFolder + "/price-row-factual.impex";
    private static String sizeFacetFileName = fileFolder + "/size-factual.impex";
    private static String valuePreparedFileName = fileFolder + "/value-prepared-factual.impex";
    private static String nutrientsFileName = fileFolder + "/nutrient-factual.impex";

    private static String imagesPreparedFileName = fileFolder + "/product-images-factual.impex";

    public static void main(String[] args) throws SQLException, FileNotFoundException, URISyntaxException {
//        Used for one time run to fill category index table. This data will be used in other selects.
//        DataExporterUtils.fillCategoriesTable(ConnectionManager.getConnection());
        writeToFile(productsFileName, productsFileStaticHead, "", productDataExtractor());
//        writeToFile(categoriesFileName, categoriesFileStaticHead, "", categoryDataExtractor());
//        writeToFile(prodcatFileName, prodcatFileStaticHead, "", prodcatDataExtractor());
//        writeToFile(pricerowFileName, pricerowFileStaticHead, "", pricerowDataExtractor());
//        writeToFile(sizeFacetFileName, sizeFacetFileStaticHead, "", sizeFacetDataExtractor());
//        writeToFile(nutrientsFileName, nutrientFileStaticHead, "", nutrientDataExtractor());

        writeToFile(pricerowFileName, pricerowFileStaticHead, "", pricerowDataExtractor());
//
        writeToFile(sizeFacetFileName, sizeFacetFileStaticHead, "", sizeFacetDataExtractor());
        writeToFile(valuePreparedFileName, valuePreparedFileStaticHead, "", valuePreparedDataExtractor());

        //images writing

//        writeImages();

    }

    //Used to write all data about images into one file. Write sequence is important.
    private static void writeImages() throws SQLException, URISyntaxException, FileNotFoundException {
        Map<String, ImageDO> productCodeToImages = getValidImage();

        PrintWriter imagesPrintWriter = new PrintWriter(imagesPreparedFileName);

        writeToFile(imagesPrintWriter, media90x90StaticHead, "", mediaDataExtractor(productCodeToImages));
        writeToFile(imagesPrintWriter, media200x200StaticHead, "", mediaDataExtractor(productCodeToImages));
        writeToFile(imagesPrintWriter, media600x600StaticHead, "", mediaDataExtractor(productCodeToImages));

        writeToFile(imagesPrintWriter, mediaContainerStaticHead, "", mediaContainerDataExtractor(productCodeToImages));

        writeToFile(imagesPrintWriter, productThmbnailStaticHead, "", productImagesDataExtractor(productCodeToImages, "90WX90H/"));
        writeToFile(imagesPrintWriter, productPictureStaticHead, "", productImagesDataExtractor(productCodeToImages, "200WX200H/"));
        writeToFile(imagesPrintWriter, productGallereyImagesStaticHead, "", productImagesDataExtractor(productCodeToImages, ""));

        imagesPrintWriter.flush();
        imagesPrintWriter.close();
    }

    private static List<List<String>> productDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String basicProductDataQuery = "select upc code, product_name name, product_name description, ean13 ean," +
                " \"approved\" approvalstatus, size, \"9\" deptNumber, brand, ingredients from `products-cpg-nutrition`";


        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(basicProductDataQuery);

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
            String sellbyCode = DataExporterUtils.getUnitType(DataExporterUtils.getUnit(size));
            row.add(sellbyCode);

            row.add(resultSet.getString("deptNumber"));
            row.add(resultSet.getString("brand"));

            row.add(resultSet.getString("description"));

            //remove unnecessary symbols
            String ingredients = resultSet.getString("ingredients");
            if (!Strings.isNullOrEmpty(ingredients)) {
                ingredients = ingredients.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "");
            } else {
                ingredients = "";
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
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(basicCategoryDataQuery);
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
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(basicCategoryDataQuery);

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
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(basicProdcatDataQuery);

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
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(basicProdcatDataQuery);

        // seed is important to have repeatability
        Random random = new Random(1234);
        List<Integer> storeIndexes = new ArrayList<>();
        storeIndexes.add(420); // default shop - should always exist
        storeIndexes.add(421);
        storeIndexes.add(422);
        storeIndexes.add(423);

        PromotionDO promotion;
        Double price;

        while (resultSet.next()) {
            for (Integer store : storeIndexes) {
                ArrayList<String> row = new ArrayList<>();
                //    "Product(code,$catalog_version)[unique=true]" - upc = {885898000079}
                row.add(resultSet.getString("upc"));
                //    "store(groceryStoreNo)[unique=true]" - store number = 420 || 421
                //row.add(Integer.valueOf(df.format((store1 + (store2 - store1) * random.nextDouble()))).toString());
                row.add(store.toString());
                //    "dateRange[unique=true,dateformat=yyyyMMdd]" - date range = 20131127,20990101
                row.add("20131127,20990101");
                //    "minqtd" - QUANTITY =1
                row.add("1");
                //    "price" - generated price = {avg_price}
                price = Double.valueOf(resultSet.getString("avg_price"));
                price *= random.nextDouble() * (1.05 - 0.95) + 0.95;
                price = Math.round(price * 100) / 100.0;
                row.add(price.toString());

                promotion = PromotionDO.getPromotion(price);
                //BEGIN PROMOTION DATA
                //    "promoType" - promotion type = 0[- no promo] || 1 || 2 || 3
                row.add(promotion.getType().toString());
                //    "promoForQuantity" = "1"
                row.add(promotion.getForQuantity().toString());
                //    "promoPrice" - generated price for product with promotion, promo price should be less then general price = {"90.00"}
                row.add(promotion.getPromoPrice());
                //    "promoStart" - start date - "20131120"
                row.add(promotion.getStartDate());
                //    "promoEnd" - end date - "20141212"
                row.add(promotion.getEndDate());
                //    "saveAmount" - amount your saved = "price - promoPrice"
                row.add(promotion.getSaveAmount());
                //    "promoPriceText" - text = "You Save 'saveAmount'"
                row.add(promotion.getPriceText());
                //END PROMOTION DATA


                //"aisleNumber" = {"3"}
                row.add("3");
                //"aisleSide" = {"B"}
                row.add("B");
                //"ageRestriction" = {"0"}
                row.add("0");
                //"crvAmount" = {" 0.30"}
                //Currently, CRV is 5 cents for containers less than 24 ounces and 10 cents for containers 24 ounces or larger.
                if (resultSet.getString("upc").equals("018200002311")) {
                    row.add(" 0.05");
                } else if (resultSet.getString("upc").equals("074682103502") ||
                        resultSet.getString("upc").equals("799210375021")) {
                    row.add(" 0.10");
                } else {
                    row.add("");
                }

                //"salesTaxRate" = {" 0.08500"}
                row.add(" 0.08500");

                // necessary for demo
                // two products will be not available in one store
                if (!store.equals(420) &&
                        ((resultSet.getString("upc").equals("070569272408")) || (resultSet.getString("upc").equals("072310008236")))) {
                    continue;
                }

                rows.add(row);
            }
        }

        return rows;
    }

    private static List<List<String>> sizeFacetDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String dataQuery = "select upc code, size, servings from `products-cpg-nutrition`";


        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(dataQuery);

        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>(5);

            String code = resultSet.getString("code");

            String size = resultSet.getString("size");
            String servings = resultSet.getString("servings");

            String yom = DataExporterUtils.getUnit(size);

            ItemSizeDO itemSizeDO = ItemSizeDO.getItemSize(size, servings, yom);

            row.add(code);
            row.add(itemSizeDO.getPackSize());
            row.add(itemSizeDO.getItemSize());
            row.add(itemSizeDO.getExtendedSize());

            row.add(yom);

            rows.add(row);
        }

        return rows;
    }

    private static List<List<String>> valuePreparedDataExtractor() throws SQLException {
        List<List<String>> rows = new ArrayList<>();

        String dataQuery = "select upc code, size, servings, ean13 from `products-cpg-nutrition`";

        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(dataQuery);

        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>(7);

            String code = resultSet.getString("code") + "_0";
            String valuePreparedType = "0";
            String addedItem = "";
            String ean = resultSet.getString("ean13");

            String size = resultSet.getString("size");
            String servings = resultSet.getString("servings");

            String yom = DataExporterUtils.getUnit(size);

            ItemSizeDO itemSizeDO = ItemSizeDO.getItemSize(size, servings, yom);

            row.add(code);
            row.add(valuePreparedType);
            row.add(addedItem);
            row.add(itemSizeDO.getItemSize());
            row.add(yom);
            row.add(itemSizeDO.getPackSize());
            row.add(ean);

            rows.add(row);
        }

        return rows;
    }

    //Begin images data extractors

    private static List<List<String>> mediaDataExtractor(Map<String, ImageDO> productCodeToImages) {
        List<List<String>> rows = new ArrayList<>();

//        ;$dimension;$dimension/220041412.jpg;;;images;http://localhost:9001/shop/_ui/desktop/theme-raleys/images/300.jpg;

        Set<String> productCodes = productCodeToImages.keySet();

        for (String productCode : productCodes) {
            List<String> row = new ArrayList<String>();

            ImageDO image = productCodeToImages.get(productCode);

            row.add("$dimension");
            row.add("$dimension/" + image.getImageId());
            row.add("");
            row.add("");
            row.add("images");
            row.add(image.getImageUrl());

            rows.add(row);
        }


        return rows;
    }

    private static List<List<String>> mediaContainerDataExtractor(Map<String, ImageDO> productCodeToImages) {
        List<List<String>> rows = new ArrayList<>();

//        ;220041412.jpg;90WX90H/220041412.jpg,600WX600H/220041412.jpg,200WX200H/220041412.jpg;;

        Set<String> productCodes = productCodeToImages.keySet();

        for (String productCode : productCodes) {
            List<String> row = new ArrayList<String>(3);

            ImageDO image = productCodeToImages.get(productCode);

            String imageId = image.getImageId();

            row.add(imageId);
            row.add("90WX90H/" + imageId + ",600WX600H/" + imageId + ",200WX200H/" + imageId);
            row.add("");

            rows.add(row);
        }

        return rows;
    }

    private static List<List<String>> productImagesDataExtractor(Map<String, ImageDO> productCodeToImages,
                                                                 String dimension) {
        List<List<String>> rows = new ArrayList<>();

//        ;885898000079;;90WX90H/220041412.jpg;

        Set<String> productCodes = productCodeToImages.keySet();

        for (String productCode : productCodes) {
            List<String> row = new ArrayList<String>();

            ImageDO image = productCodeToImages.get(productCode);

            row.add(productCode);
            row.add("");
            row.add(dimension + image.getImageId());

            rows.add(row);
        }


        return rows;
    }

    //by default do not write valid image urls into DB.
    private static Map<String, ImageDO> getValidImage() throws SQLException, URISyntaxException {
        return getValidImage(false);
    }

    private static Map<String, ImageDO> getValidImage(boolean isFillProductImagesTable) throws SQLException, URISyntaxException {
        Map result = new HashMap();

        String basicProductDataQuery = "select upc code, image_urls from `products-cpg-nutrition`";

        //used as a parameter for fillProductImagesTable(...) method.
        PreparedStatement insertProductImage = ConnectionManager.getConnection().prepareStatement("insert into ProductImage (code, imageUrl) values (?, ?)");

        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(basicProductDataQuery);

        while (resultSet.next()) {

            String productCode = resultSet.getString("code");
            String image_urls = resultSet.getString("image_urls");
            String availableUrl = "";

            List<String> urls = Arrays.asList(image_urls.substring(1, image_urls.length() - 1).split(","));


            if (StringUtils.isNullOrEmpty(image_urls)) {
                continue;
            }

            HttpClient client = HttpClients.createDefault();

            for (String url : urls) {
                String formattedUrl = "";
                HttpGet getImage = null;
                try {
                    formattedUrl = url.substring(1, url.length() - 1);
                    getImage = new HttpGet(formattedUrl);

                    //Catch any exception related to url/uri formatting. It could be caused by wrong url format in external catalog.
                } catch (Exception e) {
                    continue;
                }

                try {
                    HttpResponse response = client.execute(getImage);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        availableUrl = formattedUrl;
                        System.out.println("product : " + productCode + " , image url : " + availableUrl);
                        result.put(productCode, new ImageDO(productCode + ".image", availableUrl));
                        //for 1st run only - stores verified images url to db table.
                        if(isFillProductImagesTable) {
                            DataExporterUtils.fillProductImagesTable(productCode, availableUrl, insertProductImage);
                        }
                        break;
                    }
                } catch (IOException e) {
                    continue;
                }
            }
        }

        return result;

    }

    //End images data extractors


    private static void writeToFile(String fileName, String staticHead, String staticHeadDelimiter, List<List<String>> rows)
            throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(fileName);

        writeToFile(printWriter, staticHead, staticHeadDelimiter, rows);

        printWriter.flush();
        printWriter.close();
    }

    private static void writeToFile(PrintWriter file, String staticHead, String staticHeadDelimiter, List<List<String>> rows)
            throws FileNotFoundException {
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
            "\n" +
            "\"INSERT_UPDATE RaleysPriceRow\";\"Product(code,$catalog_version)[unique=true]\";\"store(groceryStoreNo)[unique=true]\";\"dateRange[unique=true,dateformat=yyyyMMdd]\";\"minqtd\";\"price\";\"promoType\";\"promoForQuantity\";\"promoPrice\";\"promoStart\";\"promoEnd\";\"saveAmount\";\"promoPriceText\";\"aisleNumber\";\"aisleSide\";\"ageRestriction\";\"crvAmount\";\"salesTaxRate\";\"unit(code)[default=pieces]\";\"currency(isocode)[default=USD]\";\"net[default=true]\";\"$catalog_version\"";

    private static String sizeFacetFileStaticHead = "$catalog-id=raleysProductCatalog\n" +
            "$catalog-version=Staged\n" +
            "$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\n" +
            "\n" +
            "UPDATE Product;code[unique=true];packSize;extendedSize;uom;$catalog_version\n";

    private static String valuePreparedFileStaticHead = "INSERT_UPDATE ValuePrepared; code[unique=true]; valuePreparedType;addedItem; servingSizeText; servingSizeUOM ; servingsPerContainer ;ean\n";

    //Begin images insertion heads

    private static String media90x90StaticHead = "$dimension=90WX90H\n" +
            "$productCatalog=raleysProductCatalog\n" +
            "$productCV='raleysProductCatalog:Staged'\n" +
            "$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCV]\n" +
            "$media=@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator]\n" +
            "$medias=medias(code, $catalogVersion)\n" +
            "\n" +
            "INSERT_UPDATE Media[parallel=false];mediaFormat(qualifier);code[unique=true];mime[default='image/jpeg'];$catalogVersion;folder(qualifier);URL;\n";

    private static String media200x200StaticHead = "\n\n$dimension=200WX200H\n" +
            "$productCatalog=raleysProductCatalog\n" +
            "$productCV='raleysProductCatalog:Staged'\n" +
            "$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCV]\n" +
            "$media=@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator]\n" +
            "$medias=medias(code, $catalogVersion)\n" +
            "\n" +
            "INSERT_UPDATE Media[parallel=false];mediaFormat(qualifier);code[unique=true];mime[default='image/jpeg'];$catalogVersion;folder(qualifier);URL;\n";

    private static String media600x600StaticHead = "\n\n$dimension=600WX600H\n" +
            "$productCatalog=raleysProductCatalog\n" +
            "$productCV='raleysProductCatalog:Staged'\n" +
            "$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCV]\n" +
            "$media=@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator]\n" +
            "$medias=medias(code, $catalogVersion)\n" +
            "\n" +
            "INSERT_UPDATE Media[parallel=false];mediaFormat(qualifier);code[unique=true];mime[default='image/jpeg'];$catalogVersion;folder(qualifier);URL;\n";

    private static String mediaContainerStaticHead = "\n\n$productCatalog=raleysProductCatalog\n" +
            "$productCV='raleysProductCatalog:Staged'\n" +
            "$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCV]\n" +
            "$media=@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator]\n" +
            "$medias=medias(code, $catalogVersion)\n" +
            "\n" +
            "INSERT_UPDATE MediaContainer;qualifier[unique=true];$medias;$catalogVersion;\n";

    private static String productThmbnailStaticHead = "\n\n$productCatalog=raleysProductCatalog\n" +
            "$productCV='raleysProductCatalog:Staged'\n" +
            "$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCV]\n" +
            "$thumbnail=thumbnail(code, $catalogVersion)\n" +
            "$picture=picture(code, $catalogVersion)\n" +
            "$normal=normal(code, $catalogVersion)\n" +
            "$galleryImages=galleryImages(qualifier, $catalogVersion)\n" +
            "\n" +
            "INSERT_UPDATE Product;code[unique=true];$catalogVersion;$thumbnail;\n";

    private static String productThmbnailsStaticHead = "\n\n$productCatalog=raleysProductCatalog\n" +
            "$productCV='raleysProductCatalog:Staged'\n" +
            "$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCV]\n" +
            "$thumbnails=thumbnails(code, $catalogVersion)\n" +
            "$picture=picture(code, $catalogVersion)\n" +
            "$normal=normal(code, $catalogVersion)\n" +
            "$galleryImages=galleryImages(qualifier, $catalogVersion)\n" +
            "\n" +
            "INSERT_UPDATE Product;code[unique=true];$catalogVersion;$thumbnails;\n";

    private static String productPictureStaticHead = "\n\n$productCatalog=raleysProductCatalog\n" +
            "$productCV='raleysProductCatalog:Staged'\n" +
            "$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCV]\n" +
            "$thumbnail=thumbnail(code, $catalogVersion)\n" +
            "$picture=picture(code, $catalogVersion)\n" +
            "$normal=normal(code, $catalogVersion)\n" +
            "$galleryImages=galleryImages(qualifier, $catalogVersion)\n" +
            "\n" +
            "INSERT_UPDATE Product;code[unique=true];$catalogVersion;$picture;\n";

    private static String productGallereyImagesStaticHead = "\n\n$productCatalog=raleysProductCatalog\n" +
            "$productCV='raleysProductCatalog:Staged'\n" +
            "$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCV]\n" +
            "$thumbnail=thumbnail(code, $catalogVersion)\n" +
            "$picture=picture(code, $catalogVersion)\n" +
            "$normal=normal(code, $catalogVersion)\n" +
            "$galleryImages=galleryImages(qualifier, $catalogVersion)\n" +
            "\n" +
            "INSERT_UPDATE Product;code[unique=true];$catalogVersion;$galleryImages;$normal\n";

    //End images insertion heads

    private static String nutrientFileStaticHead = "INSERT_UPDATE Nutrient;master(name,type)[unique=true];quantity[default='0'];uom[default='n/a'];pct;isOrContains;valuePreparedType;ean[unique=true]\n";
}

