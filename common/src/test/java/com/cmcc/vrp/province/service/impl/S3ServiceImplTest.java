package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.S3Service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class S3ServiceImplTest {
    @InjectMocks
    S3Service service = new S3ServiceImpl();

    @Mock
    FileStoreService download;

    @Mock
    MockHttpServletResponse response;

    @Mock
    MockHttpServletRequest request;

    @Test
    public void testDownloadFromS31() throws FileNotFoundException {
        request = new MockHttpServletRequest();
        request.addHeader("USER-AGENT", "firefox");

        response = new MockHttpServletResponse();
        when(download.get(Mockito.anyString())).thenReturn(new BufferedInputStream(null));
        service.downloadFromS3(response, "123", "test.txt", request);
    }

    @Test
    public void testDownloadFromS32() throws FileNotFoundException {
        request = new MockHttpServletRequest();
        request.addHeader("USER-AGENT", "11");

        response = new MockHttpServletResponse();
        when(download.get(Mockito.anyString())).thenReturn(new BufferedInputStream(null));
        service.downloadFromS3(response, "123", "test.txt", request);
    }

    @Test
    public void testGetImageFromS3() throws FileNotFoundException {
        response = new MockHttpServletResponse();
        when(download.get(Mockito.anyString())).thenReturn(new BufferedInputStream(new FileInputStream("src/main/java/com/cmcc/vrp/province/service/impl/S3ServiceImpl.java")));
        service.getImageFromS3(response, "11");
    }

    @Test
    public void testCheckFile() throws IOException {
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        MockMultipartHttpServletRequest request1 = new MockMultipartHttpServletRequest();
        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile file = new MockMultipartFile("image", "image", "", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile file1 = new MockMultipartFile("licenceImage", "licenceImage.jpg", "", new BufferedInputStream(new FileInputStream(tempFile)));
        request.addFile(file);
        request1.addFile(file1);
        assertFalse(service.checkFile(request));
        assertTrue(service.checkFile(request1));
    }

    @Test
    public void testCheckFileSize() throws IOException {
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile file = new MockMultipartFile("image", "image", "", new BufferedInputStream(new FileInputStream(tempFile)));
        request.addFile(file);
        assertFalse(service.checkFileSize(request));
    }

}
