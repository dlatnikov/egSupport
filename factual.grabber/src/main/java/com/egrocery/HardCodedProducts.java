package com.egrocery;

import java.util.*;

public class HardCodedProducts {

    public static String getProductsFileName() {
        return "/products-pod.impex";
    }

    public static String getCategoriesFileName() {
        return "/categories-pod.impex";
    }

    public static String getProdCatFileName() {
        return "/prodcat-pod.impex";
    }

    public static String getPriceRowFileName() {
        return "/price-row-pod.impex";
    }

    public static String getSizeFacetFileName() {
        return "/size-pod.impex";
    }

    public static String getAvgWeightFileName() {
        return "/avg-weight-pod.impex";
    }

    public static String getValuePreparedFileName() {
        return "/value-prepared-pod.impex";
    }

    public static String getNutrientsFileName() {
        return "/nutrient-pod.impex";
    }

    public static String getImagesPreparedFileName() {
        return "/product-images-pod.impex";
    }

    public static String getProductFileHeader() {
        String productsFileHead = "$catalog-id=raleysProductCatalog\n" +
                "$catalog-version=Staged\n" +
                "$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\n" +
                "INSERT_UPDATE Product;code[unique=true];name[lang=en];description[name=en];ean;approvalstatus(code);unit(code);sellbyCode;deptNumber;manufacturerName;productDetails;ingredients;$catalog_version\n" +
                ";33383000015;Red Delicious 3lb;Red Delicious 3lb;033383000015;approved;pieces;W;9;Washington Fruit & Produce Co.;Red Delicious 3lb;Apple; \n" +
                ";33383000039;Red Delicious 5lb;Red Delicious 5lb;033383000039;approved;pieces;W;9;Washington Fruit & Produce Co.;Red Delicious 5lb;Apple; \n" +
                ";33383000053;Red Delicious;Red Delicious;033383000053;approved;pieces;W;9;Washington Fruit & Produce Co.;Red Delicious;Apple; \n" +
                ";33383000817;Golden Delicious 3lb;Golden Delicious 3lb;033383000817;approved;pieces;W;9;Washington Fruit & Produce Co.;Golden Delicious 3lb;Apple; \n" +
                ";33383000848;Golden Delicious ;Golden Delicious ;033383000848;approved;pieces;W;9;Washington Fruit & Produce Co.;Golden Delicious;Apple; \n" +
                ";33383001531;Granny Smith 5lb;Granny Smith 5lb;033383001531;approved;pieces;W;9;Washington Fruit & Produce Co.;Granny Smith 5lb;Apple; \n" +
                ";33383001562;Granny Smith;Granny Smith;033383001562;approved;pieces;W;9;Washington Fruit & Produce Co.;Granny Smith;Apple; \n" +
                ";33383004693;Braeburn 3lb;Braeburn 3lb;033383004693;approved;pieces;W;9;Washington Fruit & Produce Co.;Braeburn 3lb;Apple; \n" +
                ";33383004716;Braeburn 5lb;Braeburn 5lb;033383004716;approved;pieces;W;9;Washington Fruit & Produce Co.;Braeburn 5lb;Apple; \n" +
                ";33383005485;Honeycrisp 3lb;Honeycrisp 3lb;033383005485;approved;pieces;W;9;Washington Fruit & Produce Co.;Honeycrisp 3lb;Apple; \n" +
                ";33383005492;Jonagold 3lb;Jonagold 3lb;033383005492;approved;pieces;W;9;Washington Fruit & Produce Co.;Jonagold 3lb;Apple; \n" +
                ";33383007014;Fuji 3lb;Fuji 3lb;033383007014;approved;pieces;W;9;Washington Fruit & Produce Co.;Fuji 3lb;Apple; \n" +
                ";33383007038;Fuji 5lb;Fuji 5lb;033383007038;approved;pieces;W;9;Washington Fruit & Produce Co.;Fuji 5lb;Apple; \n" +
                ";33383007045;Fuji;Fuji;033383007045;approved;pieces;W;9;Washington Fruit & Produce Co.;Fuji;Apple; \n" +
                ";33383007410;Gala 3lb;Gala 3lb;033383007410;approved;pieces;W;9;Washington Fruit & Produce Co.;Gala 3lb;Apple; \n" +
                ";33383007434;Gala 5lb;Gala 5lb;033383007434;approved;pieces;W;9;Washington Fruit & Produce Co.;Gala 5lb;Apple; \n" +
                ";33383007441;Gala;Gala;033383007441;approved;pieces;W;9;Washington Fruit & Produce Co.;Gala;Apple; \n" +
                ";33383010243;Cripps Pink 3lb;Cripps Pink 3lb;033383010243;approved;pieces;W;9;Washington Fruit & Produce Co.;Cripps Pink 3lb;Apple; \n" +
                ";33383300504;D'anjou US#1 2 1/8 3lb;D'anjou US#1 2 1/8 3lb;033383300504;approved;pieces;W;9;Washington Fruit & Produce Co.;D'anjou US#1 2 1/8 3lb;Pear;\n" +
                ";33383300511;Bosc US#1 2 1/8 3lb;Bosc US#1 2 1/8 3lb;033383300511;approved;pieces;W;9;Washington Fruit & Produce Co.;Bosc US#1 2 1/8 3lb;Pear;\n" +
                ";33383300528;Bartlett US#1 2 1/8 3lb;Bartlett US#1 2 1/8 3lb;033383300528;approved;pieces;W;9;Washington Fruit & Produce Co.;Bartlett US#1 2 1/8 3lb;Pear;\n" +
                ";33383310633;Dark Sweet 1 Lb;Dark Sweet 1 lb;033383310633;approved;pieces;W;9;Washington Fruit & Produce Co.;Dark Sweet 1 Lb;Cherry;\n" +
                ";33383310640;Dark Sweet 2 Lb;Dark Sweet 2 lb;033383310640;approved;pieces;W;9;Washington Fruit & Produce Co.;Dark Sweet 2 Lb;Cherry;\n" +
                ";33383310664;Dark Sweet 4 Lb;Dark Sweet 4 lb;033383310664;approved;pieces;W;9;Washington Fruit & Produce Co.;Dark Sweet 4 Lb;Cherry;\n" +
                ";33383310695;Rainier 1 Lb;Rainier 1 lb;033383310695;approved;pieces;W;9;Washington Fruit & Produce Co.;Rainier 1 Lb;Cherry;\n" +
                ";33383310701;Rainier 2 Lb;Rainier 2 lb;033383310701;approved;pieces;W;9;Washington Fruit & Produce Co.;Rainier 2 Lb;Cherry;\n"
                ;

        return productsFileHead;
    }

    public static String getCategoryFileHeader() {
        String categoriesFileHead = "$contentCatalog=raleysContentCatalog\n" +
                "$contentCV=catalogVersion(CatalogVersion.catalog(Catalog.id[default=$contentCatalog]),CatalogVersion.version[default=Online])[default=$contentCatalog:Staged]\n" +
                "$catalogversion=catalogversion(catalog(id),version)[unique=true,default=raleysProductCatalog:Staged]\n" +
                "\n" +
                "INSERT_UPDATE RegularCategory;code[unique=true];name[lang=en];allowedPrincipals(uid)[default='customergroup'];$catalogversion\n" +

                ";s900;Fruits;\n" +
                ";s901;Apples;\n" +
                ";s902;Pears;\n" +
                ";s903;Cherries;\n"
                ;

        return categoriesFileHead;
    }

    public static String getProdCatFileHeader() {
        String prodCatFileHead = "$catalog-id=raleysProductCatalog\n" +
                "$catalog-version=Staged\n" +
                "$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\n" +
                "$source=source(code, $catalog_version)[unique=true]\n" +
                "$target=target(code, $catalog_version)[unique=true]\n" +
                "$YCL=catalog-id='$catalog-id',catalog-version='$catalog-version',translator=com.raleys.dataimport.translator.DclCategoryTranslator;\n" +
                "INSERT_UPDATE CategoryProductRelation;$source;$target\n" +
                ";s901;33383000015;\n" +
                ";s901;33383000039;\n" +
                ";s901;33383000053;\n" +
                ";s901;33383000817;\n" +
                ";s901;33383000848;\n" +
                ";s901;33383001531;\n" +
                ";s901;33383001562;\n" +
                ";s901;33383004693;\n" +
                ";s901;33383004716;\n" +
                ";s901;33383005485;\n" +
                ";s901;33383005492;\n" +
                ";s901;33383007014;\n" +
                ";s901;33383007038;\n" +
                ";s901;33383007045;\n" +
                ";s901;33383007410;\n" +
                ";s901;33383007434;\n" +
                ";s901;33383007441;\n" +
                ";s901;33383010243;\n" +
                ";s902;33383300504;\n" +
                ";s902;33383300511;\n" +
                ";s902;33383300528;\n" +
                ";s903;33383310633;\n" +
                ";s903;33383310640;\n" +
                ";s903;33383310664;\n" +
                ";s903;33383310695;\n" +
                ";s903;33383310701;\n"
                ;

        return prodCatFileHead;
    }

    public static String getPriceRowFileHeader(Collection<String> storeIndexes) {
        String priceRowFileHeader =
                "\"$catalog-id=raleysProductCatalog\"\n" +
                "\"$catalog-version=Staged\"\n" +
                "\"$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\"\n" +
                "\n" +
                "\"INSERT_UPDATE RaleysPriceRow\";\"Product(code,$catalog_version)[unique=true]\";\"store(groceryStoreNo)[unique=true]\";\"dateRange[unique=true,dateformat=yyyyMMdd]\";\"minqtd\";\"price\";\"promoType\";\"promoForQuantity\";\"promoPrice\";\"promoStart\";\"promoEnd\";\"saveAmount\";\"promoPriceText\";\"aisleNumber\";\"aisleSide\";\"ageRestriction\";\"crvAmount\";\"salesTaxRate\";\"unit(code)[default=pieces]\";\"currency(isocode)[default=USD]\";\"net[default=true]\";\"$catalog_version\"\n";

        Map<String,String> prices = new HashMap<>();
        prices.put("33383000015","1.9");
        prices.put("33383000039","1.9");
        prices.put("33383000053","1.9");
        prices.put("33383000817","1.9");
        prices.put("33383000848","1.9");
        prices.put("33383001531","1.9");
        prices.put("33383001562","1.9");
        prices.put("33383004693","1.9");
        prices.put("33383004716","1.9");
        prices.put("33383005485","1.9");
        prices.put("33383005492","1.9");
        prices.put("33383007014","1.9");
        prices.put("33383007038","1.9");
        prices.put("33383007045","1.9");
        prices.put("33383007410","1.9");
        prices.put("33383007434","1.9");
        prices.put("33383007441","1.9");
        prices.put("33383010243","1.9");
        prices.put("33383300504","1.9");
        prices.put("33383300511","1.9");
        prices.put("33383300528","1.9");
        prices.put("33383310633","2.5");
        prices.put("33383310640","2.5");
        prices.put("33383310664","2.5");
        prices.put("33383310695","2.5");
        prices.put("33383310701","2.5");

        for (String store : storeIndexes) {
            for (Map.Entry<String,String> entry : prices.entrySet()) {
                priceRowFileHeader +=  ";" + entry.getKey() + ";" + store + ";20131127,20990101;1;" + entry.getValue() + ";0;1;;;;.00;You Save .00;3;B;0;; 0.08500;\n";
            }
        }

        return priceRowFileHeader;
    }

    // getSize = true => size
    // false => avgWeight
    public static String getSizeFileHeader(boolean getSize) {
        String sizeFacetFileHead = "$catalog-id=raleysProductCatalog\n" +
                "$catalog-version=Staged\n" +
                "$catalog_version=catalogversion(catalog(id[default=$catalog-id]),version[default=$catalog-version])[unique=true,default=$catalog-id:$catalog-version]\n" +
                "\n";

        // in pounds
        Map<String,String> sizes = new HashMap<>();
        sizes.put("33383000015","3.0");
        sizes.put("33383000039","5.0");
        sizes.put("33383000053","1.0");
        sizes.put("33383000817","3.0");
        sizes.put("33383000848","1.0");
        sizes.put("33383001531","5.0");
        sizes.put("33383001562","1.0");
        sizes.put("33383004693","3.0");
        sizes.put("33383004716","5.0");
        sizes.put("33383005485","5.0");
        sizes.put("33383005492","3.0");
        sizes.put("33383007014","3.0");
        sizes.put("33383007038","5.0");
        sizes.put("33383007045","1.0");
        sizes.put("33383007410","3.0");
        sizes.put("33383007434","5.0");
        sizes.put("33383007441","1.0");
        sizes.put("33383010243","3.0");
        sizes.put("33383300504","3.0");
        sizes.put("33383300511","3.0");
        sizes.put("33383300528","3.0");
        sizes.put("33383310633","1.0");
        sizes.put("33383310640","2.0");
        sizes.put("33383310664","4.0");
        sizes.put("33383310695","1.0");
        sizes.put("33383310701","2.0");

        if (getSize) {
            sizeFacetFileHead += "UPDATE Product;code[unique=true];packSize;itemSize;extendedSize;uom;$catalog_version\n";


            for (Map.Entry<String,String> entry : sizes.entrySet()) {
                if (!entry.getValue().equals("1.0")) {
                    // will sell by weight and quantity
                    sizeFacetFileHead += ";" + entry.getKey() + ";1;1.0;Per Pound;lb;\n";
                }
                else {
                    // will sell by weight
                    String val = entry.getValue();
                    sizeFacetFileHead += ";" + entry.getKey() + ";" + Double.valueOf(val).intValue() + ";" + val + ";" + val + " lb;lb;\n";
                }
            }
        }
        else {
            sizeFacetFileHead += "INSERT_UPDATE Product;code[unique=true];approxAvgWgt;$catalog_version\n";

            for (Map.Entry<String,String> entry : sizes.entrySet()) {
                if (!entry.getValue().equals("1.0")) {
                    // will sell by weight and quantity
                    sizeFacetFileHead += ";" + entry.getKey() + ";" + entry.getValue() + ";\n";
                }
            }
        }

        return sizeFacetFileHead;
    }

    public static String getValuePreparedFileHeader() {
        String valuePreparedFileHead = "INSERT_UPDATE ValuePrepared; code[unique=true]; valuePreparedType;addedItem; servingSizeText; servingSizeUOM ; servingsPerContainer ;ean\n" +
                "\n" +
                ";33383000015_0;0;;1;250;g;1;033383000015;\n" +
                ";33383000039_0;0;;1;250;g;1;033383000039;\n" +
                ";33383000053_0;0;;1;250;g;1;033383000053;\n" +
                ";33383000817_0;0;;1;250;g;1;033383000817;\n" +
                ";33383000848_0;0;;1;250;g;1;033383000848;\n" +
                ";33383001531_0;0;;1;250;g;1;033383001531;\n" +
                ";33383001562_0;0;;1;250;g;1;033383001562;\n" +
                ";33383004693_0;0;;1;250;g;1;033383004693;\n" +
                ";33383004716_0;0;;1;250;g;1;033383004716;\n" +
                ";33383005485_0;0;;1;250;g;1;033383005485;\n" +
                ";33383005492_0;0;;1;250;g;1;033383005492;\n" +
                ";33383007014_0;0;;1;250;g;1;033383007014;\n" +
                ";33383007038_0;0;;1;250;g;1;033383007038;\n" +
                ";33383007045_0;0;;1;250;g;1;033383007045;\n" +
                ";33383007410_0;0;;1;250;g;1;033383007410;\n" +
                ";33383007434_0;0;;1;250;g;1;033383007434;\n" +
                ";33383007441_0;0;;1;250;g;1;033383007441;\n" +
                ";33383010243_0;0;;1;250;g;1;033383010243;\n" +
                ";33383300504_0;0;;1;150;g;1;033383300504;\n" +
                ";33383300511_0;0;;1;150;g;1;033383300511;\n" +
                ";33383300528_0;0;;1;150;g;1;033383300528;\n" +
                ";33383310633_0;0;;1;150;g;1;033383310633;\n" +
                ";33383310640_0;0;;1;150;g;1;033383310640;\n" +
                ";33383310664_0;0;;1;150;g;1;033383310664;\n" +
                ";33383310695_0;0;;1;150;g;1;033383310695;\n" +
                ";33383310701_0;0;;1;150;g;1;033383310701;\n"
                ;

        return valuePreparedFileHead;
    }

    public static String getNutrientsFileHeader() {
        String nutrientsFileHead = "INSERT_UPDATE Nutrient;master(name,type)[unique=true];quantity[default='0'];uom[default='n/a'];pct;isOrContains;valuePreparedType;ean[unique=true]\n" +
                "\n";

        Map<String,String> category = new HashMap<>();
        category.put("33383000015","Apple");
        category.put("33383000039","Apple");
        category.put("33383000053","Apple");
        category.put("33383000817","Apple");
        category.put("33383000848","Apple");
        category.put("33383001531","Apple");
        category.put("33383001562","Apple");
        category.put("33383004693","Apple");
        category.put("33383004716","Apple");
        category.put("33383005485","Apple");
        category.put("33383005492","Apple");
        category.put("33383007014","Apple");
        category.put("33383007038","Apple");
        category.put("33383007045","Apple");
        category.put("33383007410","Apple");
        category.put("33383007434","Apple");
        category.put("33383007441","Apple");
        category.put("33383010243","Apple");
        category.put("33383300504","Pear");
        category.put("33383300511","Pear");
        category.put("33383300528","Pear");
        category.put("33383310633","Cherry");
        category.put("33383310640","Cherry");
        category.put("33383310664","Cherry");
        category.put("33383310695","Cherry");
        category.put("33383310701","Cherry");

        for (Map.Entry<String, String> entry : category.entrySet()) {
            if (entry.getValue().equals("Apple")) {
                nutrientsFileHead += ";calories:0;130.0;;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";total_fat:0;0.0;g;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";total_carb:0;0.0;g;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";protein:0;1.0;g;;0;0;0" + entry.getKey() + ";\n";
            }
            if (entry.getValue().equals("Pear")) {
                nutrientsFileHead += ";calories:0;100.0;;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";total_fat:0;1.0;g;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";total_carb:0;25.0;g;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";protein:0;1.0;g;;0;0;0" + entry.getKey() + ";\n";
            }
            if (entry.getValue().equals("Cherry")) {
                nutrientsFileHead += ";calories:0;90.0;;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";total_fat:0;0.5;g;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";total_carb:0;22.0;g;;0;0;0" + entry.getKey() + ";\n";
                nutrientsFileHead += ";protein:0;2.0;g;;0;0;0" + entry.getKey() + ";\n";
            }
        }

        return nutrientsFileHead;
    }

    // getMedia = 0 => media
    // 1 => media container
    // 2 => product images
    public static List<List<String>> fakeMediaDataExtractor(int mediaType, String dimension) {
        List<List<String>> rows = new ArrayList<>();

        Map<String,String> pictures = new HashMap<>();
            pictures.put("33383000015","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383000015.jpg");
            pictures.put("33383000039","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383000015.jpg");
            pictures.put("33383000053","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383000015.jpg");
            pictures.put("33383000817","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383000817.jpg");
            pictures.put("33383000848","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383000817.jpg");
            pictures.put("33383001531","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383001531.jpg");
            pictures.put("33383001562","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383001531.jpg");
            pictures.put("33383004693","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383004693.jpg");
            pictures.put("33383004716","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383004693.jpg");
            pictures.put("33383005485","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383005485.jpg");
            pictures.put("33383005492","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383005492.jpg");
            pictures.put("33383007014","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383007014.jpg");
            pictures.put("33383007038","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383007014.jpg");
            pictures.put("33383007045","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383007014.jpg");
            pictures.put("33383007410","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383007410.jpg");
            pictures.put("33383007434","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383007410.jpg");
            pictures.put("33383007441","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383007410.jpg");
            pictures.put("33383010243","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383010243.jpg");
            pictures.put("33383300504","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383300504.jpg");
            pictures.put("33383300511","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383300511.jpg");
            pictures.put("33383300528","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383300528.jpg");
            pictures.put("33383310633","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383310633.jpg");
            pictures.put("33383310640","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383310633.jpg");
            pictures.put("33383310664","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383310633.jpg");
            pictures.put("33383310695","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383310695.jpg");
            pictures.put("33383310701","http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-003/0033383310695.jpg");

        if (mediaType == 0) {
            for (Map.Entry<String,String> entry : pictures.entrySet()) {
                List<String> row = new ArrayList<String>();

                row.add("$dimension");
                row.add("$dimension/" + entry.getKey() + ".image");
                row.add("");
                row.add("");
                row.add("images");
                row.add(entry.getValue());

                rows.add(row);
            }
        }
        else if (mediaType == 1) {
            for (Map.Entry<String,String> entry : pictures.entrySet()) {
                List<String> row = new ArrayList<String>();

                String imageId = entry.getKey() + ".image";

                row.add(imageId);
                row.add("90WX90H/" + imageId + ",600WX600H/" + imageId + ",200WX200H/" + imageId);
                row.add("");

                rows.add(row);
            }
        }
        else {
            for (Map.Entry<String,String> entry : pictures.entrySet()) {
                List<String> row = new ArrayList<String>();

                row.add(entry.getKey());
                row.add("");
                row.add(dimension + entry.getKey() + ".image");


                rows.add(row);
            }
        }

        return rows;
    }

}
