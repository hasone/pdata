package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.service.impl.BaseTest;
import com.cmcc.vrp.util.S3Until;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.omg.CORBA.portable.InputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * s3文件存储服务的UT
 *
 * Created by sunyiwei on 2017/4/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class S3FileStoreServiceImplTest extends BaseTest {
    @InjectMocks
    S3FileStoreServiceImpl s3FileStoreService = new S3FileStoreServiceImpl();

    @Mock
    private S3Until s3Until;

    /**
     * 测试存储服务, save(InputStream inputStream)
     */
    @Test
    public void testSaveInputStream() throws Exception {
        doNothing().when(s3Until)
                .uploadFile(anyString(), any(InputStream.class));

        assertNull(s3FileStoreService.save((InputStream) null));
        assertNotNull(s3FileStoreService.save(
                new BufferedInputStream(new FileInputStream(createTempFile()))
        ));

        //verify
        verify(s3Until, times(1)).uploadFile(anyString(), any(InputStream.class));
    }


    /**
     * 测试文件存储服务, save(File file)
     */
    @Test
    public void testSaveFile() throws Exception {
        assertNull(s3FileStoreService.save((File) null));
        assertNull(s3FileStoreService.save(new File("nonExistFile")));

        String key = randStr();
        S3FileStoreServiceImpl spy = spy(s3FileStoreService);
        doReturn(null).doReturn(key).when(spy).save(any(InputStream.class));

        assertNull(spy.save(createTempFile()));
        assertEquals(key, spy.save(createTempFile()));

        verify(spy, times(2)).save(any(InputStream.class));
    }

    /**
     * 测试文件存储服务, save(String key, File file)
     */
    @Test
    public void testSaveKeyAndFile() throws Exception {
        assertFalse(s3FileStoreService.save(null, createTempFile()));
        assertFalse(s3FileStoreService.save("", createTempFile()));
        assertFalse(s3FileStoreService.save(randStr(), null));
        assertFalse(s3FileStoreService.save(randStr(), new File("nonExistFile")));

        doNothing().when(s3Until).uploadFile(anyString(), any(InputStream.class));

        assertTrue(s3FileStoreService.save(randStr(), createTempFile()));

        verify(s3Until, times(1)).uploadFile(anyString(), any(InputStream.class));
    }

    /**
     * 测试删除文件服务
     */
    @Test
    public void testDeleteKeysStartWith() throws Exception {
        assertFalse(s3FileStoreService.deleteKeysStartWith(null));
        assertFalse(s3FileStoreService.deleteKeysStartWith(""));

        doNothing().when(s3Until).deleteObjectsWithPrefix(anyString());
        assertTrue(s3FileStoreService.deleteKeysStartWith(randStr()));
        verify(s3Until, times(1)).deleteObjectsWithPrefix(anyString());
    }

    /**
     * 测试文件是否存在
     */
    @Test
    public void testExist() throws Exception {
        assertFalse(s3FileStoreService.exist(null));
        assertFalse(s3FileStoreService.exist(""));

        doReturn(false).doReturn(true).when(s3Until).checkExist(anyString());

        assertFalse(s3FileStoreService.exist(randStr()));
        assertTrue(s3FileStoreService.exist(randStr()));

        verify(s3Until, times(2)).checkExist(anyString());
    }


    /**
     * 测试带有指定前缀的key
     */
    @Test
    public void testGetKeysStartWith() throws Exception {
        assertNull(s3FileStoreService.getKeysStartWith(null));
        assertNull(s3FileStoreService.getKeysStartWith(""));

        doReturn(null).doReturn(new LinkedList<String>())
                .when(s3Until).listStartWithKeys(anyString());

        assertNull(s3FileStoreService.getKeysStartWith(randStr()));
        assertNotNull(s3FileStoreService.getKeysStartWith(randStr()));

        verify(s3Until, times(2)).listStartWithKeys(anyString());
    }

    /**
     * 测试获取文件
     */
    @Test
    public void testGet() throws Exception {
        assertNull(s3FileStoreService.get(null));
        assertNull(s3FileStoreService.get(""));

        doReturn(null)
                .doReturn(new BufferedInputStream(new FileInputStream(createTempFile())))
                .when(s3Until).readFileFromS3(anyString());

        assertNull(s3FileStoreService.get(randStr()));
        assertNotNull(s3FileStoreService.get(randStr()));

        verify(s3Until, times(2)).readFileFromS3(anyString());
    }
}