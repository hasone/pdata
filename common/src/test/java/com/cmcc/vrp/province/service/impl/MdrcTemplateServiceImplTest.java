package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.MdrcTemplateMapper;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.ZipUtils;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * <p>Title:MdrcTemplateServiceImplTest </p> <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月2日
 */
@RunWith(PowerMockRunner.class)
public class MdrcTemplateServiceImplTest {

    @InjectMocks
    MdrcTemplateService mtService = new MdrcTemplateServiceImpl();

    @Mock
    MdrcTemplateMapper mdrcTemplateMapper;

    @Mock
    FileStoreService fileStoreService;

    @Mock
    GlobalConfigService globalConfigService;

    @Test
    public void testSelectByPrimaryKey() {
        Long id = 1L;
        MdrcTemplate record = new MdrcTemplate();
        when(mdrcTemplateMapper.selectByPrimaryKey(id)).thenReturn(record);
        assertSame(record, mtService.selectByPrimaryKey(id));
        Mockito.verify(mdrcTemplateMapper, Mockito.times(1)).selectByPrimaryKey(id);
    }

    @Test
    public void testList() {
        QueryObject queryObject = new QueryObject();
        List<MdrcTemplate> list = new ArrayList();
        when(mdrcTemplateMapper.list(queryObject.toMap())).thenReturn(list);
        assertNull(mtService.list(null));
        assertSame(list, mtService.list(queryObject));
        Mockito.verify(mdrcTemplateMapper, Mockito.times(1)).list(queryObject.toMap());

    }

    @Test
    public void testCount() {
        QueryObject queryObject = new QueryObject();
        int result = 4;
        when(mdrcTemplateMapper.count(queryObject.toMap())).thenReturn(result);
        assertSame(0, mtService.count(null));
        assertSame(result, mtService.count(queryObject));
        Mockito.verify(mdrcTemplateMapper, Mockito.times(1)).count(queryObject.toMap());
    }

    @Test
    public void testIsExist() {
        QueryObject queryObject = new QueryObject();
        int result = 1;
        when(mdrcTemplateMapper.isExist(queryObject.toMap())).thenReturn(result);
        assertSame(0, mtService.isExist(null));
        assertSame(result, mtService.isExist(queryObject));
        Mockito.verify(mdrcTemplateMapper, Mockito.times(1)).isExist(queryObject.toMap());
    }

    @Test
    public void testUpdateResourcesCount() {
        Long id = 1L;
        Integer resourcesCount = 1;
        int result = 5;
        when(mdrcTemplateMapper.updateByPrimaryKeySelective(Mockito.any(MdrcTemplate.class))).thenReturn(result);
        assertSame(result, mtService.updateResourcesCount(id, resourcesCount));
        Mockito.verify(mdrcTemplateMapper, Mockito.times(1)).updateByPrimaryKeySelective(Mockito.any(MdrcTemplate.class));
    }

    @Test
    public void testInsert() {
        MdrcTemplate mdrcTemplate = new MdrcTemplate();
        when(mdrcTemplateMapper.insert(mdrcTemplate)).thenReturn(0).thenReturn(1);
        assertFalse(mtService.insert(mdrcTemplate));
        assertTrue(mtService.insert(mdrcTemplate));
        Mockito.verify(mdrcTemplateMapper, Mockito.times(2)).insert(mdrcTemplate);
    }

    @Test
    public void testListThemes() {
        List<String> themes = new ArrayList();
        when(mdrcTemplateMapper.selectThemes()).thenReturn(themes);
        assertSame(themes, mtService.listThemes());
        Mockito.verify(mdrcTemplateMapper, Mockito.times(1)).selectThemes();
    }

    @Ignore
    @Test
    @PrepareForTest(MdrcTemplateServiceImpl.class)
    public void testListFiles() throws Exception {
        long id = 1L;
        File folder = PowerMockito.mock(File.class);
        File[] folderFiles = {folder, folder};
        when(globalConfigService.get(GlobalConfigKeyEnum.MDRC_TEMPLATE_FILE_PATH.getKey())).thenReturn("");
        PowerMockito.whenNew(File.class).withArguments("\\1").thenReturn(folder);
        PowerMockito.when(folder.listFiles()).thenReturn(null);
        PowerMockito.when(folder.exists()).thenReturn(false).thenReturn(true);
        PowerMockito.when(folder.isDirectory()).thenReturn(false).thenReturn(true);
        assertNull(mtService.listFiles(id));
        assertNull(mtService.listFiles(id));
        assertNull(mtService.listFiles(id));
        PowerMockito.when(folder.listFiles()).thenReturn(folderFiles);
        assertNotNull(mtService.listFiles(id));
    }

    @Test
    public void testListFilesS3() {
        long id = 1L;
        List<String> listNames = new ArrayList();
        listNames.add("aaa");
        listNames.add("bbb");
        when(fileStoreService.getKeysStartWith(Mockito.anyString())).thenReturn(listNames);
        assertNotNull(mtService.listFilesS3(id));
        when(fileStoreService.getKeysStartWith(Mockito.anyString())).thenThrow(new RuntimeException());
    }

    @Test
    @PrepareForTest(FileUtils.class)
    public void testWriteFile() throws Exception {
        long id = 1L;
        String filename = "aaa";
        byte[] data = {1, 1};
        File folder = PowerMockito.mock(File.class);
        File[] folderFiles = {};
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.MDRC_TEMPLATE_FILE_PATH.getKey())).thenReturn("");
        PowerMockito.whenNew(File.class).withArguments("\\1").thenReturn(folder);
        PowerMockito.when(folder.exists()).thenReturn(false).thenReturn(true);
        PowerMockito.when(folder.listFiles()).thenReturn(null).thenReturn(folderFiles);
        PowerMockito.mockStatic(FileUtils.class);
//	FileUtils.writeByteArrayToFile(Mockito.any(File.class), Mockito.any(byte[].class)/), null;
        PowerMockito.doNothing().when(FileUtils.class, "writeByteArrayToFile", Mockito.any(File.class), Mockito.any(byte[].class));
        assertSame(0, mtService.writeFile(id, filename, data));
        assertSame(0, mtService.writeFile(id, filename, data));
    }

    @Test
    public void testDeleteFile() throws Exception {
        long id = 1L;
        String filename = "aaa";
        File folder = PowerMockito.mock(File.class);
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.MDRC_TEMPLATE_FILE_PATH.getKey())).thenReturn("");
        PowerMockito.whenNew(File.class).withArguments("\\1").thenReturn(folder);
        PowerMockito.when(folder.exists()).thenReturn(false).thenReturn(true);
        PowerMockito.when(folder.isDirectory()).thenReturn(false).thenReturn(true);
        mtService.deleteFile(id, filename);
        mtService.deleteFile(id, filename);

        File file = PowerMockito.mock(File.class);
        PowerMockito.when(folder.getAbsolutePath()).thenReturn("");
        PowerMockito.whenNew(File.class).withArguments("\\aaa").thenReturn(file);
        PowerMockito.when(file.delete()).thenReturn(false).thenReturn(true);

        when(mdrcTemplateMapper.updateByPrimaryKeySelective(Mockito.any(MdrcTemplate.class))).thenReturn(1);
        File[] folderFiles = {};
        PowerMockito.when(folder.listFiles()).thenReturn(folderFiles);
        mtService.deleteFile(id, filename);
        mtService.deleteFile(id, filename);

    }

    @Test
    public void testMinusResourceCount() {
        long id = 1L;
        MdrcTemplate template = new MdrcTemplate();
        template.setResourcesCount(1);
        when(mdrcTemplateMapper.selectByPrimaryKey(id)).thenReturn(null).thenReturn(template);
        when(mdrcTemplateMapper.updateByPrimaryKeySelective(Mockito.any(MdrcTemplate.class))).thenReturn(1);
        mtService.minusResourceCount(id);
        mtService.minusResourceCount(id);

    }

    @Test
    public void testUpdateFrontAndRearImage() {
        long id = 1L;
        String frontImage = "aaa";
        String rearImage = "bbb";
        int result = 1;
        MdrcTemplate mdrcTemplate = new MdrcTemplate();
        when(mdrcTemplateMapper.selectByPrimaryKey(id)).thenReturn(null).thenReturn(mdrcTemplate);
        when(mdrcTemplateMapper.updateByPrimaryKeySelective(mdrcTemplate)).thenReturn(result);
        assertSame(0, mtService.updateFrontAndRearImage(id, frontImage, rearImage));
        assertSame(result, mtService.updateFrontAndRearImage(id, frontImage, rearImage));

    }

    @Test
    @PrepareForTest(ZipUtils.class)
    public void testCompress() throws Exception {
        long id = 1L;
        String outputFolder = "aaa";
        MdrcTemplate mdrcTemplate = new MdrcTemplate();

        when(mdrcTemplateMapper.selectByPrimaryKey(id)).thenReturn(null).thenReturn(mdrcTemplate);
        File input = PowerMockito.mock(File.class);
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.MDRC_TEMPLATE_FILE_PATH.getKey())).thenReturn("");
        PowerMockito.whenNew(File.class).withArguments("\\1").thenReturn(input);
        PowerMockito.when(input.exists()).thenReturn(false).thenReturn(true);
        PowerMockito.mockStatic(ZipUtils.class);
        PowerMockito.doNothing().when(ZipUtils.class, "compress", Mockito.any(File.class), Mockito.any(File.class));
        mtService.compress(id, outputFolder);
        mtService.compress(id, outputFolder);
        mtService.compress(id, outputFolder);

    }

    @Test
    public void testThemeTemplates() {
        List<MdrcTemplate> orderedList = new ArrayList();
        when(mdrcTemplateMapper.selectAllTemplateByTheme()).thenReturn(null).thenReturn(orderedList);
        assertNotNull(mtService.themeTemplates());
        assertNotNull(mtService.themeTemplates());
        MdrcTemplate mt = new MdrcTemplate();
        mt.setTheme("aaa");
        orderedList.add(mt);
        assertNotNull(mtService.themeTemplates());
    }

    @Test
    @PrepareForTest(FileUtils.class)
    public void testGetFile() throws Exception {
        long id = 1L;
        String filename = "aaa";
        File folder = PowerMockito.mock(File.class);
        byte[] result = {1, 1};
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.MDRC_TEMPLATE_FILE_PATH.getKey())).thenReturn("");
        PowerMockito.whenNew(File.class).withArguments("\\1").thenReturn(folder);
        PowerMockito.when(folder.getAbsolutePath()).thenReturn("");
        PowerMockito.mockStatic(FileUtils.class);
        PowerMockito.when(FileUtils.class, "readFileToByteArray", Mockito.any(File.class)).thenReturn(result);
        assertSame(result, mtService.getFile(id, filename));

    }

    @Test
    public void testCheckUnique() {
        String name = "aaa";
        List<MdrcTemplate> templates = new ArrayList();
        MdrcTemplate mt = new MdrcTemplate();
        mt.setName("aaa ");
        when(mdrcTemplateMapper.selectAll()).thenReturn(templates);
        assertFalse(mtService.checkUnique(null));
        assertTrue(mtService.checkUnique(name));
        templates.add(mt);
        assertFalse(mtService.checkUnique(name));

    }

    @Test
    public void testListSort() {
        List<File> list1 = new ArrayList<File>();
        File file1 = new File("/test1.txt");
        File file2 = new File("/test2.txt");
        list1.add(file1);
        list1.add(file2);
        Assert.assertEquals(list1, mtService.listSort(list1));
    }
}
