package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.RuleTemplateMapper;
import com.cmcc.vrp.province.model.RuleTemplate;
import com.cmcc.vrp.province.model.json.RedPacketPage;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.RuleTemplateService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service("RuleTemplateService")
public class RuleTemplateServiceImpl implements RuleTemplateService {

    private static final Log LOGGER = LogFactory.getLog(RuleTemplateService.class);
    @Autowired
    RuleTemplateMapper mapper;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private AdminRoleService adminRoleService;


    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return false;
        }
        RuleTemplate entRedpacketTemplate = selectByPrimaryKey(id);
        entRedpacketTemplate.setDeleteFlag(Constants.DELETE_FLAG.DELETED.getValue());
        entRedpacketTemplate.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(entRedpacketTemplate) == 1;
    }

    @Override
    public boolean insert(RuleTemplate record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }

        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        //保存后默认为上架状态
        record.setStatus(Constants.ENTREDPACKET_STATUS.ON.getValue());
        record.setImageCnt(0);

        return mapper.insert(record) == 1;
    }

    @Override
    public boolean insertSelective(RuleTemplate record) {
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        //保存后默认为上架状态
        record.setStatus(Constants.ENTREDPACKET_STATUS.ON.getValue());
        record.setImageCnt(0);
        return mapper.insertSelective(record) > 0;
    }

    @Override
    public RuleTemplate selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(RuleTemplate record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        record.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(RuleTemplate record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        record.setUpdateTime(new Date());
        return mapper.updateByPrimaryKey(record) == 1;
    }

    @Override
    public Long count(QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (queryObject == null) {
            return 0L;
        }
        Long result = mapper.count(queryObject.toMap());
        return (result == null ? 0 : result);
    }

    @Override
    public List<RuleTemplate> listRuleTemplate(
        QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (queryObject == null) {
            return null;
        }
        return mapper.listRuleTemplate(queryObject.toMap());
    }

    @Override
    public String addValidate(RuleTemplate ruleTemplate,
                              QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (ruleTemplate == null || ruleTemplate.getName() == null) {
            return "传入参数不完整";
        }

        List<RuleTemplate> list = listRuleTemplate(queryObject);
        for (int i = 0; i < list.size(); ++i) {
            if (ruleTemplate.getId() != null) {
                if (ruleTemplate.getName().equals(list.get(i).getName())
                    ) {
                    return ruleTemplate.getName() + "规则已经存在该名称的短信模板！";
                }
            } else {
                if (ruleTemplate.getName().equals(list.get(i).getName())) {
                    return ruleTemplate.getName() + "规则已经存在该名称的短信模板！";
                }
            }
        }
        return null;
    }

    @Override
    public List<File> listFiles(long id) {
        // TODO Auto-generated method stub
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

    private File getFolder(long id) {
        String path = getRuleTemplateFilePath() + File.separator + id;
        return new File(path);
    }

    //排序
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

    @Override
    public int writeFile(long id, String filename, byte[] data)
        throws IOException {
        // TODO Auto-generated method stub
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

        // 更新资源文件个数
        updateResourcesCount(id, listFiles(id).size());
        return 0;
    }

    @Override
    public int updateResourcesCount(Long id, Integer imageCnt) {
        RuleTemplate ruleTemplate = new RuleTemplate();
        ruleTemplate.setId(id);
        ruleTemplate.setImageCnt(imageCnt);

        return mapper.updateByPrimaryKeySelective(ruleTemplate);
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
    public int updateFrontAndRearImage(long id, String image) {

        RuleTemplate ruleTemplate = selectByPrimaryKey(id);

        if (ruleTemplate == null) {
            return 0;
        }

        ruleTemplate.setId(id);
        ruleTemplate.setImage(image);
        //ruleTemplate.setRearImage(rearImage);

        return mapper.updateByPrimaryKeySelective(ruleTemplate);
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
    public List<RuleTemplate> getRuleTemplateByCreator(Long creatorId) {
        // TODO Auto-generated method stub
        if (creatorId == null) {
            return null;
        }
//        List<RuleTemplate> list = new ArrayList<RuleTemplate>();
        Long roleId = adminRoleService.getRoleIdByAdminId(creatorId);
        QueryObject queryObject = new QueryObject();

        if (roleId.intValue() == 1) {
            return mapper.getRuleTemplateByCreatorId(queryObject.toMap());
        } else {
            queryObject.getQueryCriterias().put("creatorId", creatorId);
            queryObject.getQueryCriterias().put("roleId", roleId);
            return mapper.getRuleTemplateByCreatorId(queryObject.toMap());
        }
    }

    @Override
    public List<RuleTemplate> getTemplateToCreateRedpacket(Long creatorId) {
        // TODO Auto-generated method stub
        if (creatorId == null) {
            return null;
        }
//        List<RuleTemplate> list = new ArrayList<RuleTemplate>();
        Long roleId = adminRoleService.getRoleIdByAdminId(creatorId);
        Integer status = 1;

        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("status", status);

        if (roleId.intValue() == 1) {
            return mapper.getRuleTemplateByCreatorId(queryObject.toMap());
        } else {
            queryObject.getQueryCriterias().put("creatorId", creatorId);
            queryObject.getQueryCriterias().put("roleId", roleId);
            return mapper.getRuleTemplateByCreatorId(queryObject.toMap());
        }
//        return list;
    }

    @Override
    public RuleTemplate getRedPacketRuleTempalteFromPage(
        RedPacketPage pageParams) {
        RuleTemplate ruleTemplate = new RuleTemplate();
        //生成该红包的模板
        ruleTemplate.setName(pageParams.getActivityName());
        ruleTemplate.setTitle(pageParams.getActivityName());
        ruleTemplate.setActivityDes(pageParams.getActivityDes());
        ruleTemplate.setDescription(pageParams.getDescription());
        ruleTemplate.setPeople(pageParams.getPeople());
        return ruleTemplate;
    }

    public String getRuleTemplateFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_RULE_TEMPLATE_FILE_PATH.getKey());
    }
}
