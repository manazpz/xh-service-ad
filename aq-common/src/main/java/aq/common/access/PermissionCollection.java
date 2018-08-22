package aq.common.access;

import java.util.*;

/**
 * Created by ywb on 2017-02-16.
 * 权限集合
 */
public class PermissionCollection {

    //权限集合
    protected static Map<String,Permission> mapPermission = null;

    //所有权限
    protected List<Permission> allPermission;

    //权限值
    protected HashSet<String> pvSet;

    //构造函数(无参)
    public PermissionCollection(){

    }

    //构造函数(有参)
    public PermissionCollection(HashMap<String,Permission> map){
      this.pvSet = new HashSet<String>();
      mapPermission  = map;
      this.allPermission = null;
    }

    //添加权限值
    protected void setPvSet(HashSet<String> hashSet){
        this.pvSet = hashSet;
    }

    //获取权限值
    protected HashSet<String> getPvSet(){
        return  this.pvSet;
    }

    //Set 权限集合
    protected void setMapPermission(HashMap<String,Permission> map){
        mapPermission = map;
    }

    //Get 权限集合
    protected Map<String ,Permission> getMapPermission(){
        return mapPermission;
    }

    //Get 所有权限
    public List<Permission> getAllPermission(){
        if (this.allPermission==null){
            this.allPermission = new ArrayList<>();
            if (mapPermission!=null&&mapPermission.size()>0){
                mapPermission.forEach((k,v)->{
                    this.allPermission.add(new Permission(v.getIndex(),v.getPermissionType(),v.getApp(),v.getModule(),v.getPermissionName(),v.getPv()));
                });
            }
        }
        return this.allPermission;
    }

    //匹配任意一个权限值
    public boolean containAny(String[] pvs){
        if (this.getPvSet()==null||this.getPvSet().size()==0){
            return false;
        }
        for (String pv:pvs){
            if (this.getPvSet().contains(pv)) return true;
        }
        return false;
    }

    //匹配任意一个权限值
    public boolean containAny(PermissionCollection pc){
        for (String pv : pc.pvSet){
            if (this.pvSet.contains(pv)) return true;
        }
        return false;
    }

    //匹配所有权限
    public boolean containAll(PermissionCollection pc){
        if (pc==null||pc.pvSet.isEmpty()) return false;
        for (String pv : pc.pvSet){
            if (!this.pvSet.contains(pv)) return false;
        }
        return true;
    }

    //获取不在给定权限集合中的权限集合
    public List<Permission> getNotIn(PermissionCollection pc){
        final List<Permission> listPermission  = null;
        for (final String pv :pc.pvSet){
            if (!this.pvSet.contains(pv)){
                Permission item = null;
                this.allPermission.forEach(p->{
                    if (p.getPv().trim() == pv)  listPermission.add(p);
                });
            }
        }
        return  listPermission;
    }

    //添加所有权限值
    public void addAllPermission(){
      this.getAllPermission().forEach(p->{
          this.pvSet.add(p.getPv());
      });
    }

    //添加权限值
    public void addPermission(String pv){
        this.getAllPermission().forEach(p->{
            if (p.getPv() == pv) this.pvSet.add(pv);
        });
    }

    //添加多个权限
    public  void  addRangePermission(String[] pvs){
        //是否需要校验权限值是否包含在allPermission中？
        for (String v : pvs){
            this.pvSet.add(v);
        }
    }

}
