package com.cmcc.vrp.boss.shangdong.boss;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import com.cmcc.vrp.util.UncompressFileGZIP;

/**
 * UncompressFileGZIP
 *
 */
/*@PrepareForTest({FTPClient.class,UncompressFileGZIP.class,
    FileOutputStream.class,GZIPInputStream.class,FileInputStream.class})
@RunWith(PowerMockRunner.class)*/
@Ignore
public class UncompressFileGZIPTest {

    /**
     * testUncompressFileGZIP
     */
    @Test
    @Ignore
    public void testUncompressFileGZIP() throws Exception {
        FileInputStream fis = PowerMockito.mock(FileInputStream.class);
        
        GZIPInputStream gis = PowerMockito.mock(GZIPInputStream.class);
        
        FileOutputStream fos = PowerMockito.mock(FileOutputStream.class);
        
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fis);
        
        PowerMockito.whenNew(GZIPInputStream.class).withAnyArguments().thenReturn(gis);
        
        PowerMockito.whenNew(FileOutputStream.class).withAnyArguments().thenReturn(fos);
        
        Assert.assertTrue(UncompressFileGZIP.doUncompressFile("DBOSS_3009_20160823011140.0000074259.gz"));
    }

}
