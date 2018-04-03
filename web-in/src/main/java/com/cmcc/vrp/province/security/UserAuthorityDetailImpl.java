package com.cmcc.vrp.province.security;

import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.service.AdministerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName: UserAuthorityDetailImpl
 * @Description: springsecurity使用，继承于UserDetails接口，实现getAuthorities()得到权限操作
 * @author: qihang
 * @date: 2015年4月3日 下午3:39:00
 */
public class UserAuthorityDetailImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String username = "";
    private String password = "";
    private boolean enabled = true;

    /**
     * UserDetails接口是可序列化的，这个类不支持序列化操作，因此加上transient关键字
     * Added by sunyiwei
     */
    private transient AdministerService administerService;

    public UserAuthorityDetailImpl() {

    }

    public UserAuthorityDetailImpl(String username, String password,
                                   boolean enabled, AdministerService administerService) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.administerService = administerService;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return long
     * @Title: getAuthorities
     * @Description: 继承与UserDetails接口的方法，从数据库通过电话号码取得用户拥有的权限
     * @author qihang
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> grantedList = new ArrayList<GrantedAuthority>();

        //得到登陆用户的拥有的所有权限
        List<Authority> authoritiesList = administerService.queryUserAuthoriesByMobile(username);
        if (authoritiesList != null && authoritiesList.size() > 0) {
            for (Authority auth : authoritiesList) {
                GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(
                    auth.getAuthorityName());
                grantedList.add(grantedAuthority);
            }
        }

        //所有用户得有的权限，可以修改个人信息。
        //原因是springsecurity必须保证权限不能为空，即至少有一个权限
        GrantedAuthority userInfoAuthority = new GrantedAuthorityImpl("ROLE_USER_INFO");
        grantedList.add(userInfoAuthority);
        return grantedList;
    }

    public AdministerService getAdministerService() {
        return administerService;
    }

    public void setAdministerService(AdministerService administerService) {
        this.administerService = administerService;
    }

}
