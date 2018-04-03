package com.cmcc.vrp.province.service;

import com.google.common.base.Charsets;

import com.cmcc.vrp.province.service.impl.BaseTest;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 使用本地文件系统存储文件
 *
 * Created by sunyiwei on 2017/4/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocalFileSystemFileStoreServiceImplTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileSystemFileStoreServiceImpl.class);

    @InjectMocks
    private LocalFileSystemFileStoreServiceImpl localFileSystemFileStoreService;

    @Mock
    private GlobalConfigService globalConfigService;

    /**
     * 测试save(key, file)
     */
    @Test
    public void testSaveKeyAndFile() throws Exception {
        assertFalse(localFileSystemFileStoreService.save(null, createTempFile()));
        assertFalse(localFileSystemFileStoreService.save("", createTempFile()));
        assertFalse(localFileSystemFileStoreService.save(randStr(), null));

        String path = "/tmp/";
        String name = randStr();
        String key = randStr();
        when(globalConfigService.get(anyString())).thenReturn(null).thenReturn(path);

        if (mkfile(path, name)) {
            assertFalse(localFileSystemFileStoreService.save(key, new File(path, name)));
            assertTrue(localFileSystemFileStoreService.save(key, new File(path, name)));

            rmfile(path, name);
        }

        verify(globalConfigService, times(2)).get(anyString());
    }


    /**
     * 测试save(InputStream inputStream)
     */
    @Test
    public void testSaveInputStream() throws Exception {
        assertNull(localFileSystemFileStoreService.save((InputStream) null));

        String path = "/tmp/";
        String name = randStr();
        String key = randStr();
        when(globalConfigService.get(anyString())).thenReturn(null).thenReturn(path);

        if (mkfile(path, name)) {
            assertFalse(localFileSystemFileStoreService.save(key, new File(path, name)));
            assertTrue(localFileSystemFileStoreService.save(key, new File(path, name)));

            rmfile(path, name);
        }

        verify(globalConfigService, times(2)).get(anyString());
    }

    /**
     * 测试获取文件
     */
    @Test
    public void testGet() throws Exception {
        assertNull(localFileSystemFileStoreService.get(null));
        assertNull(localFileSystemFileStoreService.get(""));

        String path = "/tmp";
        String filename = randStr();
        when(globalConfigService.get(GlobalConfigKeyEnum.LOCAL_FILESYSTEM_STORE_PATH.getKey()))
                .thenReturn(null).thenReturn(path);

        //没有指定全局参数
        assertNull(localFileSystemFileStoreService.get(filename));

        //文件不存在
        assertNull(localFileSystemFileStoreService.get(filename));

        //文件存在但不是文件类型
        if (mkdir(path, filename)) {
            assertNull(localFileSystemFileStoreService.get(filename));
            rmdir(path, filename);
        }

        //happy path
        if (mkfile(path, filename)) {
            assertNotNull(localFileSystemFileStoreService.get(filename));
            rmfile(path, filename);
        }

        //verify
        verify(globalConfigService, times(4)).get(anyString());
    }


    private void rmfile(String path, String filename) {
        try {
            FileUtils.forceDelete(new File(path, filename));
        } catch (IOException e) {
            LOGGER.error("删除文件夹时出错, 错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }
    }

    private boolean mkfile(String path, String filename) {
        try {
            //创建文件夹
            FileUtils.forceMkdir(new File(path));

            String content = randStr(32);
            IOUtils.write(content, new BufferedOutputStream(new FileOutputStream(new File(path, filename))));
            return true;
        } catch (IOException e) {
            LOGGER.error("创建文件时出错, 错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }

        return false;
    }

    private void rmdir(String path, String filename) {
        try {
            FileUtils.forceDelete(new File(path, filename));
        } catch (IOException e) {
            LOGGER.error("删除文件夹时出错, 错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }
    }

    private boolean mkdir(String path, String filename) {
        try {
            FileUtils.forceMkdir(new File(path, filename));
            return true;
        } catch (IOException e) {
            LOGGER.error("创建文件夹时出错, 错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }

        return false;
    }

    /**
     * 测试save(key, file)
     */
    @Test
    public void testSaveFile() throws Exception {
        assertNull(localFileSystemFileStoreService.save((File) null));
        assertNull(localFileSystemFileStoreService.save(new File("nonExistFile")));

        LocalFileSystemFileStoreServiceImpl spy = spy(localFileSystemFileStoreService);

        String key = randStr();
        doReturn(null).doReturn(key).when(spy).save(any(InputStream.class));

        assertNull(spy.save(createTempFile()));
        assertEquals(key, spy.save(createTempFile()));

        verify(spy, times(2)).save(any(InputStream.class));
    }

    /**
     * 测试是否存在
     */
    @Test
    public void testExist() throws Exception {
        assertFalse(localFileSystemFileStoreService.exist(null));
        assertFalse(localFileSystemFileStoreService.exist(""));

        String path = "/tmp/";
        String name = randStr();
        when(globalConfigService.get(anyString()))
                .thenReturn(null)
                .thenReturn(path);

        if (mkfile(path, name)) {
            assertFalse(localFileSystemFileStoreService.exist(name));
            assertTrue(localFileSystemFileStoreService.exist(name));

            rmfile(path, name);
        }

        verify(globalConfigService, times(2)).get(anyString());
    }

    /**
     * 获取以key开头的文件
     */
    @Test
    public void testGetKeysStartWith() throws Exception {
        assertNull(localFileSystemFileStoreService.getKeysStartWith(null));
        assertNull(localFileSystemFileStoreService.getKeysStartWith(""));

        String path = "/tmp/";
        String prefix = randStr();
        when(globalConfigService.get(anyString()))
                .thenReturn(null)
                .thenReturn(path);

        assertNull(localFileSystemFileStoreService.getKeysStartWith(prefix));

        Info info = createTempFiles(path, prefix);
        List<String> actualResult = localFileSystemFileStoreService.getKeysStartWith(prefix);
        assertTrue(actualResult != null && actualResult.size() == info.getPrefixCount());

        //clear
        clear(path, info.getNames());

        verify(globalConfigService, times(2)).get(anyString());
    }

    /**
     * 删除以指定key开头的文件
     */
    @Test
    public void testDeleteKeysStartWith() throws Exception {
        assertFalse(localFileSystemFileStoreService.deleteKeysStartWith(null));
        assertFalse(localFileSystemFileStoreService.deleteKeysStartWith(""));

        String path = "/tmp/";
        String prefix = randStr();
        when(globalConfigService.get(anyString()))
                .thenReturn(null)
                .thenReturn(path);

        assertFalse(localFileSystemFileStoreService.deleteKeysStartWith(prefix));

        Info info = createTempFiles(path, prefix);
        assertTrue(localFileSystemFileStoreService.deleteKeysStartWith(prefix));

        //clear
        clear(path, info.getNames());
        verify(globalConfigService, times(2)).get(anyString());
    }

    private void clear(String dir, List<String> names) {
        for (String name : names) {
            try {
                FileUtils.forceDelete(new File(dir, name));
            } catch (IOException e) {
                LOGGER.error("删除文件[name={}]时抛出异常,错误信息为{}.", name, e.getMessage());
            }
        }
    }

    //创建临时文件,返回以指定前缀命名的文件数量及全部的文件名
    private Info createTempFiles(String dir, String prefix) {
        Random r = new Random();
        int count = r.nextInt(100) + 10;
        String content = randStr(32);

        int prefixCount = 0;
        List<String> names = new LinkedList<String>();

        for (int i = 0; i < count; i++) {
            boolean flag = r.nextBoolean();

            String filename = flag ? prefix + randStr() : randStr();
            prefixCount += (flag ? 1 : 0);
            names.add(filename);

            try {
                FileUtils.writeStringToFile(new File(dir, filename), content, Charsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error("往文件[name={}]写入数据时抛出异常,错误信息为{}.", filename, e.getMessage());
            }
        }

        return new Info(prefixCount, names);
    }

    private class Info {
        private int prefixCount;
        private List<String> names;

        public Info(int prefixCount, List<String> names) {
            this.prefixCount = prefixCount;
            this.names = names;
        }

        public int getPrefixCount() {
            return prefixCount;
        }

        public void setPrefixCount(int prefixCount) {
            this.prefixCount = prefixCount;
        }

        public List<String> getNames() {
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }
    }
}