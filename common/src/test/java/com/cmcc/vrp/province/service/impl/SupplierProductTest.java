package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ProductService;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

/**
 * Created by sunyiwei on 2016/6/23.
 */
@Ignore
public class SupplierProductTest {
    @Autowired
    ProductService productService;

    @Ignore
    @Test
    public void testProductService() throws Exception {
        final String supplierProductFilename = "C:\\Users\\Lenovo\\Desktop\\supplier_product.sql";
        final String supplierProductMapFilename = "C:\\Users\\Lenovo\\Desktop\\supplier_product_map.sql";

        StringBuilder supplierProduct = new StringBuilder();
        StringBuilder supplierProductMap = new StringBuilder();
        List<Product> products = productService.selectAllProducts();
        for (Product product : products) {
            supplierProduct.append(parseSupplierProduct(product));
            supplierProduct.append(System.lineSeparator());

            supplierProductMap.append(parseSupplierMap(product));
            supplierProductMap.append(System.lineSeparator());
        }

        FileUtils.writeStringToFile(new File(supplierProductFilename), supplierProduct.toString());
        FileUtils.writeStringToFile(new File(supplierProductMapFilename), supplierProductMap.toString());
    }

    @Ignore
    @Test
    public void testInsert() throws Exception {
        final String srcFilename = "C:\\Users\\Lenovo\\Desktop\\tietong.sql";
        final String dstFilename = "C:\\Users\\Lenovo\\Desktop\\insert.sql";

        StringBuilder stringBuilder = new StringBuilder();
        String oriContent = FileUtils.readFileToString(new File(srcFilename), "utf-8");
        Scanner scanner = new Scanner(oriContent);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith("INSERT INTO")) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
        }

        FileUtils.writeStringToFile(new File(dstFilename), stringBuilder.toString(), "utf-8");
    }

    private String parseSupplierMap(Product product) {
        StringBuilder stringBuilder = new StringBuilder();

        Formatter formatter = new Formatter(stringBuilder);
        formatter.format("INSERT INTO `supplier_product_map` VALUES (null, '%s', '%s', now(), now(), '0');",
            product.getId(),
            product.getId());

        return formatter.toString();
    }

    private String parseSupplierProduct(Product product) {
        StringBuilder stringBuilder = new StringBuilder();

        Formatter formatter = new Formatter(stringBuilder);
        formatter.format("INSERT INTO `supplier_product` VALUES ('%s', '%s', '%s', '1', '%s', '%s', '%s', '%s', '%s', '', '1', NOW(), NOW(), '0');",
            product.getId(),
            product.getName(),
            product.getIsp(),
            product.getProductCode(),
            product.getProductSize(),
            product.getOwnershipRegion(),
            product.getRoamingRegion(),
            product.getPrice());

        return formatter.toString();
    }
}
