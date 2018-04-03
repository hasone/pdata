package com.cmcc.vrp.province.mdrc.service.impl;

import com.cmcc.vrp.province.dao.AdministerMapper;
import com.cmcc.vrp.province.dao.MdrcCardmakerMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:40:44
*/
@Service("mdrcCardmarkerService")
public class MdrcCardmakerServiceImpl implements MdrcCardmakerService {

    @Autowired
    AdministerMapper administerMapper;
    @Autowired
    private MdrcCardmakerMapper mdrcCardmakerMapper;

    @Override
    public List<MdrcCardmaker> selectAllCardmaker() {
        // TODO Auto-generated method stub
        return mdrcCardmakerMapper.selectAllCardmaker();
    }

    @Override
    public MdrcCardmaker selectByPrimaryKey(Long id) {
        return id != null ? mdrcCardmakerMapper.selectByPrimaryKey(id) : null;
    }

    @Override
    public boolean insert(MdrcCardmaker mdrcCardmaker) {

        // 生成密钥对
        KeyPair keyPair = RSA.initKey();

        String publicKey = RSA.base64Encode(keyPair.getPublic().getEncoded());
        String privateKey = RSA.base64Encode(keyPair.getPrivate().getEncoded());

        mdrcCardmaker.setPrivateKey(privateKey);
        mdrcCardmaker.setPublicKey(publicKey);

        return mdrcCardmakerMapper.insert(mdrcCardmaker) > 0;
    }

    @Override
    public int count(QueryObject queryObject) {
        if (queryObject == null) {
            return 0;
        }
        return mdrcCardmakerMapper.count(queryObject.toMap());
    }

    @Override
    public List<MdrcCardmaker> list(QueryObject queryObject) {
        if (queryObject == null) {
            return null;
        }
        return mdrcCardmakerMapper.select(queryObject.toMap());
    }

    @Override
    public boolean update(MdrcCardmaker mdrcCardmaker) {
        return mdrcCardmakerMapper.updateByPrimaryKeySelective(mdrcCardmaker) > 0;
    }

    @Override
    public int getNewSerialNumber() {
        Integer max = mdrcCardmakerMapper.selectMaxSerialNumber();
        if (max == null) {
            return 1;
        }
        return max.intValue() + 1;
    }

    @Override
    public int countByName(String name) {
        return mdrcCardmakerMapper.countByName(name);
    }

    @Override
    public MdrcCardmaker selectWithKeys(Long id) {
        return selectWithKeys(id, null);
    }

    @Override
    public MdrcCardmaker selectWithKeys(Long id, Long creatorId) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", id);
        map.put("creatorId", creatorId);
        return mdrcCardmakerMapper.selectWithKeys(map);
    }

    @Override
    public List<Administer> selectUnboundCardmaker(String authCode) {
        return administerMapper.selectUnboundCardmaker(authCode);
    }

    @Override
    public boolean deleteById(Long id) {
        return mdrcCardmakerMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public MdrcCardmaker selectByOperatorId(Long operatorId) {
        // TODO Auto-generated method stub
        return mdrcCardmakerMapper.selectByOperatorId(operatorId);
    }

    @Override
    public boolean checkUnique(MdrcCardmaker m) {
        if (m == null) {
            return false;
        }
        List<MdrcCardmaker> list = mdrcCardmakerMapper.checkUnique(m);
        if(list == null || list.size() == 0){
            return true;
        }else{
            if(list.size() == 1 && list.get(0).getId().equals(m.getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public MdrcCardmaker selectByPrimaryKeyForshow(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mdrcCardmakerMapper.selectByPrimaryKeyForshow(id);
        }
        return null;
    }

    @Override
    public boolean deleteCardmaker(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mdrcCardmakerMapper.deleteCardmaker(id)>=0;
        }
        return false;
    }
    
    

}
