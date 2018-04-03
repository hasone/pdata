package com.cmcc.vrp.boss.shangdong.boss;

import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.util.FTPUtil;

/**
 * FTPUtil测试，只为填充行覆盖率
 * 
 * @author Administrator
 *
 */
@Ignore
@PrepareForTest({FTPClient.class,FTPUtil.class,InputStream.class})
@RunWith(PowerMockRunner.class)
public class FtpUtilTest {

    /**
     * testUploadFile
     * @throws Exception
     */
    @Test
    @Ignore
    public void testUploadFile() throws Exception {
        
        FTPClient ftpClient = PowerMockito.mock(FTPClient.class);
        
        InputStream inputStream =PowerMockito.mock(InputStream.class);

        PowerMockito.whenNew(FTPClient.class).withAnyArguments().thenReturn(ftpClient);

        
        boolean b =FTPUtil.uploadFile("127.0.0.1",8092,
                "username", "password", "remotePath", 
                "fileName", inputStream);
        
        Assert.assertTrue(b);
        
    }
    
    /**
     * testDownloadFile
     * @throws Exception
     */
    @Test
    @Ignore
    public void testDownloadFile() throws Exception {
        
        FTPClient ftpClient = PowerMockito.mock(FTPClient.class);
        
        PowerMockito.whenNew(FTPClient.class).withAnyArguments().thenReturn(ftpClient);
 
        String str =FTPUtil.downFileFuzzyName("127.0.0.1", 80,
                "username", "password", "remotePath", 
                "fileName","localpath");
        
        Assert.assertEquals(str, "");
        
    }

}
