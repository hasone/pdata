package com.cmcc.vrp.util;

import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ProductService;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 数据库迁移时的工具类
 * <p>
 * Created by sunyiwei on 2016/6/30.
 */
@Ignore
public class TransitionUtilTest {
    @Autowired
    ProductService productService;

    //将初始化数据合并 到 一个文件 中
    @Ignore
    @Test
    public void testMerge() throws Exception {
        final String dirName = "C:/Users/Lenovo/Desktop/内蒙古/初始化数据";
        final String outputName = "C:/Users/Lenovo/Desktop/内蒙古/初始化数据/init_data.sql";

        final StringBuilder stringBuilder = new StringBuilder();
        Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String content = FileUtils.readFileToString(file.toFile(), Charsets.UTF_8);

                stringBuilder.append(content);
                stringBuilder.append(System.lineSeparator());

                return FileVisitResult.CONTINUE;
            }
        });

        FileUtils.write(new File(outputName), stringBuilder.toString(), Charsets.UTF_8);
    }

    @Ignore
    @Test
    public void testNMGProduct() throws Exception {
        final String fileName = "C:\\Users\\Lenovo\\Desktop\\product.txt";
        final String outputFilename = "C:\\Users\\Lenovo\\Desktop\\product.sql";

        processProduct(fileName, outputFilename, new LineContentProcessor() {
            @Override
            public void process(StringBuilder stringBuilder, String[] comps) {
                stringBuilder.append(format(removeQuote(comps[1]), removeQuote(comps[3]), NumberUtils.toLong(removeQuote(comps[8].substring(0, comps[8].indexOf("MB") + 1))),
                    NumberUtils.toDouble(removeQuote(comps[9]))));
            }
        });
    }

    @Ignore
    @Test
    public void testHljProduct() throws Exception {
        final String fileName = "C:\\Users\\Lenovo\\Desktop\\heilongjiang_product.txt";
        final String outputFilename = "C:\\Users\\Lenovo\\Desktop\\product.sql";

        processProduct(fileName, outputFilename, new LineContentProcessor() {
            @Override
            public void process(StringBuilder stringBuilder, String[] comps) {
                stringBuilder.append(format(removeQuote(comps[3]), removeQuote(comps[1]), NumberUtils.toLong(removeQuote(comps[8])), 0));
            }
        });
    }

    private void processProduct(String inputFilename, String outputFilename, LineContentProcessor processor) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        String content = StreamUtils.copyToString(new FileInputStream(new File(inputFilename)), Charsets.UTF_8);
        Scanner scanner = new Scanner(content);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] comps = line.split(";");

            processor.process(stringBuilder, comps);
            stringBuilder.append(System.lineSeparator());
        }

        FileUtils.write(new File(outputFilename), stringBuilder.toString(), Charsets.UTF_8);
    }

    private String format(String name, String code, long size, double price) {
        return String.format("INSERT INTO `product` VALUES (null, '%s', '2', '%s', '1', NOW(), NOW(), '0', '%d', '0', 'M', '全国', '全国', '%d');",
            code, name, (long) (price * 100), size * 1024);
    }

    private String removeQuote(String ori) {
        return ori.substring(1, ori.length() - 1);
    }

    private int parsePrice(String name) {
        int index = name.indexOf("元");
        return NumberUtils.toInt(name.substring(0, index));
    }

    @Ignore
    @Test
    public void generateSupplierProductsAndMap() throws Exception {
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
        final String srcFilename = "C:\\Users\\Lenovo\\Desktop\\flow_nmg.sql";
        final String dstFilename = "C:\\Users\\Lenovo\\Desktop\\heilongjiang.sql";

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

    private List<Product> readProductFile(String filename) throws IOException {
        List<Product> products = new LinkedList<Product>();

        String content = FileUtils.readFileToString(new File(filename), Charsets.UTF_8);
        Scanner scanner = new Scanner(content);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            products.add(parse(line));
        }

        return products;
    }

    private Product parse(String lineContent) {
        return null;
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

    private interface LineContentProcessor {
        void process(StringBuilder stringBuilder, String[] comps);
    }
}
