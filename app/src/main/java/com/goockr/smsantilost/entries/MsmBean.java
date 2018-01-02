package com.goockr.smsantilost.entries;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LJN on 2017/12/19.
 */
@Entity
public class MsmBean implements Serializable{
    private static final long serialVersionUID=-11L;
    @Id
    private Long id;
    @Property(nameInDb = "smsTitle")
    private String smsTitle = "";
    @Property(nameInDb = "smsTime")
    private String smsTime = "";
    @Property(nameInDb = "smsStr")
    private String smsStr = "";
    @Property(nameInDb = "isShow")
    private boolean isShow = false;
    @Property(nameInDb = "isCheck")
    private boolean isCheck = false;
    //指定与之关联的其他类的id
    @ToMany(referencedJoinProperty = "mid")
    private List<ContentBean> contentBeans;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1597336777)
    private transient MsmBeanDao myDao;
    @Generated(hash = 515286209)
    public MsmBean(Long id, String smsTitle, String smsTime, String smsStr, boolean isShow,
            boolean isCheck) {
        this.id = id;
        this.smsTitle = smsTitle;
        this.smsTime = smsTime;
        this.smsStr = smsStr;
        this.isShow = isShow;
        this.isCheck = isCheck;
    }
    @Generated(hash = 914537230)
    public MsmBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSmsTitle() {
        return this.smsTitle;
    }
    public void setSmsTitle(String smsTitle) {
        this.smsTitle = smsTitle;
    }
    public String getSmsTime() {
        return this.smsTime;
    }
    public void setSmsTime(String smsTime) {
        this.smsTime = smsTime;
    }
    public boolean getIsShow() {
        return this.isShow;
    }
    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }
    public boolean getIsCheck() {
        return this.isCheck;
    }
    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 502399384)
    public List<ContentBean> getContentBeans() {
        if (contentBeans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContentBeanDao targetDao = daoSession.getContentBeanDao();
            List<ContentBean> contentBeansNew = targetDao
                    ._queryMsmBean_ContentBeans(id);
            synchronized (this) {
                if (contentBeans == null) {
                    contentBeans = contentBeansNew;
                }
            }
        }
        return contentBeans;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1728947903)
    public synchronized void resetContentBeans() {
        contentBeans = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    public String getSmsStr() {
        return this.smsStr;
    }
    public void setSmsStr(String smsStr) {
        this.smsStr = smsStr;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 364814425)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMsmBeanDao() : null;
    }
}
