package com.cmcc.vrp.province.security;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.AdministerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: UserGrantedServiceImpl
 * @Description: springsecurity使用，继承于UserDetailsService接口，实现loadUserByUsername()方法填充UserDetails
 * @author: qihang
 * @date: 2015年4月3日 下午3:39:00
 */
public class UserGrantedServiceImpl implements UserDetailsService {

    private Logger logger = Logger.getLogger(UserDetailsService.class);

    private Map<String, UserDetails> usersinfo = new HashMap<String, UserDetails>();


    @Autowired
    private AdministerService administerService;

    /*
     * UserDetailsService接口的方法，返回UserDetails类型，必须包括登陆姓名，密码，enabled(是否可用，这里设为一直可用)
     */
    @Override
    public UserDetails loadUserByUsername(String mobilephone)
        throws UsernameNotFoundException {

        logger.info("loadUserByUsername with parameter mobilephone=" + mobilephone);

        //通过电话号码从数据库查询的管理员
        Administer administer = administerService.selectByMobilePhone(mobilephone);

        //没有找到管理员
        if (administer == null) {
            throw new UsernameNotFoundException("账户信息错误，请核对姓名及密码是否正确", new Object[]{mobilephone});
        }

        UserDetails userDetails = new UserAuthorityDetailImpl(mobilephone, administer.getPassword(), true, administerService);
        return userDetails;

    }

}
