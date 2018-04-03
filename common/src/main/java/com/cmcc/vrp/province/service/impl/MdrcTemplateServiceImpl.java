package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.MdrcTemplateType;
import com.cmcc.vrp.province.dao.MdrcTemplateMapper;
import com.cmcc.vrp.province.mdrc.model.MdrcFileInfo;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("mdrcTemplateService")
public class MdrcTemplateServiceImpl implements MdrcTemplateService {

    private static final Log LOGGER = LogFactory
        .getLog(MdrcTemplateService.class);

    @Autowired
    MdrcTemplateMapper mdrcTemplateMapper;

    @Autowired
    FileStoreService fileStoreService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public MdrcTemplate selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return mdrcTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MdrcTemplate> list(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        return mdrcTemplateMapper.list(queryObject.toMap());
    }

    @Override
    public int count(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        return mdrcTemplateMapper.count(queryObject.toMap());
    }

    @Override
    public int isExist(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        return mdrcTemplateMapper.isExist(queryObject.toMap());
    }

    @Override
    public int updateResourcesCount(Long id, Integer resourcesCount) {
        MdrcTemplate mdrcTemplate = new MdrcTemplate();
        mdrcTemplate.setId(id);
        mdrcTemplate.setResourcesCount(resourcesCount);

        return mdrcTemplateMapper.updateByPrimaryKeySelective(mdrcTemplate);
    }

    @Override
    public boolean insert(MdrcTemplate mdrcTemplate) {
        return mdrcTemplateMapper.insert(mdrcTemplate) > 0;
    }
    
    @Override
    public boolean insertSelective(MdrcTemplate mdrcTemplate){
        return mdrcTemplateMapper.insertSelective(mdrcTemplate) > 0;
    }

    @Override
    public List<String> listThemes() {
        return mdrcTemplateMapper.selectThemes();
    }

    @Override
    public List<File> listFiles(long id) {

        // 组装指定标识的模板文件目录
        File folder = getFolder(id);
        // 校验目录
        File[] folderFiles = folder.listFiles();
        if (folder.exists() && folder.isDirectory() && folderFiles != null) {
            List<File> files = listSort(Arrays.asList(folderFiles));
            if (files != null) {
                return files;
            } else {
                return null;
            }
        }
        return null;
    }


    @Override
    public List<MdrcFileInfo> listFilesS3(long id) {
        List<MdrcFileInfo> list = new ArrayList<MdrcFileInfo>();

        try {
            List<String> listNames = fileStoreService.getKeysStartWith("mdrc_" + id);
            for (String name : listNames) {
                MdrcFileInfo info = new MdrcFileInfo(name);
                if (info != null) {
                    list.add(info);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }

        return list;
    }

    private File getFolder(long id) {
        String path = getTemplateFilePath() + File.separator + id;
        return new File(path);
    }

    @Override
    public int writeFile(long id, String filename, byte[] data)
        throws IOException {

        File folder = getFolder(id);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 保存到磁盘，先校验是否已经存在该文件
        File[] folderFiles = folder.listFiles();
        if (folderFiles != null) {
            for (File f : folderFiles) {
                if (f != null && f.getName().equals(filename)) {
                    return 1;
                }
            }
        }

        FileUtils.writeByteArrayToFile(new File(folder.getAbsolutePath()
            + File.separator + filename), data);

        return 0;
    }

    @Override
    public void deleteFile(long id, String filename) {
        LOGGER.info("delete File id=" + id + ", filename=" + filename);
        File folder = getFolder(id);

        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }

        File file = new File(folder.getAbsolutePath() + File.separator
            + filename);
        if (file.delete()) {
            // 更新资源文件个数
            updateResourcesCount(id, listFiles(id).size());
        }
    }

    @Override
    public void minusResourceCount(long id) {
        MdrcTemplate template = selectByPrimaryKey(id);

        if (template == null) {
            return;
        }

        updateResourcesCount(id, template.getResourcesCount() - 1);

    }


    @Override
    public int updateFrontAndRearImage(long id, String frontImage,
                                       String rearImage) {

        MdrcTemplate mdrcTemplate = selectByPrimaryKey(id);

        if (mdrcTemplate == null) {
            return 0;
        }

        mdrcTemplate.setId(id);
        mdrcTemplate.setFrontImage(frontImage);
        mdrcTemplate.setRearImage(rearImage);

        return mdrcTemplateMapper.updateByPrimaryKeySelective(mdrcTemplate);
    }

    @Override
    public void compress(long id, String outputFolder) {

        MdrcTemplate mdrcTemplate = selectByPrimaryKey(id);

        if (mdrcTemplate == null) {
            return;
        }
        File input = getFolder(id);

        String filename = outputFolder + File.separator
            + mdrcTemplate.getName() + ".zip";

        if (input.exists()) {
            ZipUtils.compress(input, new File(filename));
        }
    }
    
    
    

    @Override
    public Map<String, List<MdrcTemplate>> getThemeTemplatesByProdSize(Long prodSize) {
        // TODO Auto-generated method stub
        Map<String, List<MdrcTemplate>> map = new LinkedHashMap<String, List<MdrcTemplate>>();

        HashMap<String, String> searchMap = new HashMap<String, String>();
        searchMap.put("productSize", prodSize.toString());
        searchMap.put("templateType", MdrcTemplateType.COMMON_TEMPLATE.getCode().toString());
        List<MdrcTemplate> orderedList = mdrcTemplateMapper
            .selectByMap(searchMap);
        if (orderedList != null && orderedList.size() > 0) {
            for (MdrcTemplate template : orderedList) {
                String theme = template.getTheme();
                List<MdrcTemplate> list = map.get(theme);

                if (list == null) {
                    list = new LinkedList<MdrcTemplate>();
                    map.put(theme, list);
                }
                list.add(template);
            }
        }

        return map;
    }
    
    

    @Override
    public Map<String, List<MdrcTemplate>> themeTemplates() {

        Map<String, List<MdrcTemplate>> map = new LinkedHashMap<String, List<MdrcTemplate>>();

        List<MdrcTemplate> orderedList = mdrcTemplateMapper
            .selectAllTemplateByTheme();
        if (orderedList != null && orderedList.size() > 0) {
            for (MdrcTemplate template : orderedList) {
                String theme = template.getTheme();
                List<MdrcTemplate> list = map.get(theme);

                if (list == null) {
                    list = new LinkedList<MdrcTemplate>();
                    map.put(theme, list);
                }
                list.add(template);
            }
        }

        return map;
    }

    @Override
    public byte[] getFile(long id, String filename) throws IOException {

        LOGGER.info("getFile id=" + id + ", filename=" + filename);

        File folder = getFolder(id);

        String path = folder.getAbsolutePath() + File.separator + filename;

        LOGGER.info("get file from " + path);

        return FileUtils.readFileToByteArray(new File(path));
    }

    @Override
    public Boolean checkUnique(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        List<MdrcTemplate> templates = mdrcTemplateMapper.selectAll();
        for (MdrcTemplate temp : templates) {
            if (temp.getName().replaceAll(" ", "").equalsIgnoreCase(name)) {
                return false;
            }
        }

        return true;
    }

    //排序
    @Override
    public List<File> listSort(List<File> list) {
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }

                }
            });

        }
        return list;
    }

    public String getTemplateFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_TEMPLATE_FILE_PATH.getKey());
    }

    @Override
    public boolean updateByIdSeletive(MdrcTemplate mdrcTemplate) {
        // TODO Auto-generated method stub
        return mdrcTemplateMapper.updateByPrimaryKeySelective(mdrcTemplate)>=0;
    }
    
    
    
}
