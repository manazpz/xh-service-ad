package aq.common.access;


/**
 * Created by ywb on 2017-02-16.
 * 权限对象
 */
public class Permission {

    //构造函数(无参)
    public Permission(){

    }

    //构造函数(有参)
    public Permission(int index,String type,String app,String module,String name,String value){
        this.index = index;
        this.pv = value;
        this.app = app;
        this.module = module;
        this.permissionType = type;
        this.permissionName = name;
    }

    //序号
    public int index;

    //权限值
    public String pv;

    //所属应用
    public String app;

    //所属模块
    public String module;

    //权限类型
    public String permissionType;

    //权限名称
    public String permissionName;

    public int getIndex(){
        return this.index;
    }

    public String getPv(){
        return this.pv;
    }

    public String getApp(){
        return this.app;
    }

    public String getModule(){
        return this.module;
    }

    public String getPermissionType(){
        return this.permissionType;
    }

    public String getPermissionName(){
        return this.permissionName;
    }
}
