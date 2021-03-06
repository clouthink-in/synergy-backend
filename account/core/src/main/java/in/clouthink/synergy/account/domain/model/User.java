package in.clouthink.synergy.account.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.clouthink.synergy.shared.domain.model.StringIdentifier;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document(collection = "Users")
public class User extends StringIdentifier implements UserDetails {

    // 用户帐号名
    @Indexed(unique = true)
    private String username;

    // 联系电话
    @Indexed(unique = true)
    private String telephone;

    //省
    private String province;

    //市
    private String city;

    // 联系地址
    private String address;

    // 电子邮箱
    @Indexed(unique = true)
    private String email;

    // 头像对应的附件图片id
    private String avatarId;

    // 头像对应的附件图片url
    private String avatarUrl;

    // 显示名
    @Indexed
    private String displayName;

    // 显示名对应拼音
    @Indexed
    private String pinyin;

    // 性别
    private Gender gender;

    // 生日
    private Date birthday;

    @JsonIgnore
    private String password;

    private boolean expired = false;

    private boolean locked = false;

    private boolean enabled = true;

    private boolean archived = false;

    private Date archivedAt;

    private Date createdAt;

    private Date modifiedAt;

    @Transient
    private List<GrantedAuthority> authorities = new ArrayList<>();

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = trim(username);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = trim(province);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = trim(city);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = trim(address);
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = trim(telephone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = trim(email);
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = trim(displayName);
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Date getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(Date archivedAt) {
        this.archivedAt = archivedAt;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !expired;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
