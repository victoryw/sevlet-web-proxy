package com.victoryw.picc;

import java.io.Serializable;

public class LifeUser extends User implements Serializable {

    private long lUserId = -1;
    private long lUserType = -1;
    private long authorizedUserId = -1;
    private String sUserName = null;
    private String sRealName = null;
    private String sOrganId = null;
    private String sEmpCode = null;

    private String sDeptId = null;
    private String sHeadId = null;
    private String sBranchId = null;
    private String sCertiCode = null;
    private String sCertiType =null;
    private String from = null;


    //add by bingham.liu 20090226
    //从界面取得case_type，区分个险、团险权限
    private String sPageCaseType = null;

    public LifeUser(long lUserId, long lUserType,String sUserName, String sRealName,
                    String sOrganId, String sEmpCode, String sDeptId, String sHeadId,
                    String sBranchId) {
        this.lUserId = lUserId;
        this.lUserType = lUserType;
        this.sUserName = sUserName;
        this.sRealName = sRealName;
        this.sEmpCode = sEmpCode;
        this.sOrganId = sOrganId;
        this.sDeptId = sDeptId;
        this.sHeadId = sHeadId;
        this.sBranchId = sBranchId;
    }

    public LifeUser(long lUserId, long lUserType,String sUserName, String sRealName,
                    String sOrganId, String sEmpCode, String sDeptId, String sHeadId,
                    String sBranchId,  String sCertiCode, String sCertiType, String sFingerprintStatus, String sFingerprintCollectStatus )
    {
        this.lUserId = lUserId;
        this.lUserType = lUserType;
        this.sUserName = sUserName;
        this.sRealName = sRealName;
        this.sEmpCode = sEmpCode;
        this.sOrganId = sOrganId;
        this.sDeptId = sDeptId;
        this.sHeadId = sHeadId;
        this.sBranchId = sBranchId;
        this.sCertiCode = sCertiCode;
        this.sCertiType = sCertiType;
        this.fingerprintStatus = sFingerprintStatus;
        this.fingerprintCollectStatus =sFingerprintCollectStatus;
    }


    // get methods
    public long getUserId(){ return lUserId; }
    public long getEmpId(){ return lUserId; }
    public long getUserType(){ return lUserType; }
    public String getUserName(){ return sUserName; }
    public String getRealName(){ return sRealName; }
    public String getOrganId(){ return sOrganId; }
    public String getEmpCode(){ return sEmpCode; }

    public String getDeptId(){ return sDeptId; }
    public String getHeadId(){ return sHeadId; }
    public String getBranchId(){ return sBranchId; }


    //为指纹系统添加
    //add by pengshixin 2010/3/24
    public String getCertiCode() { return this.sCertiCode;}
    public String getCertiType() { return this.sCertiType;}


    //指纹系统使用用户初始化方法
    //add by pengshixin 2010/3/24
    private String fingerprintStatus = null;
    public String getFingerprintStatus() {
        return fingerprintStatus;
    }
    public void setFingerprintStatus(String fingerprintStatus) {
        this.fingerprintStatus = fingerprintStatus;
    }

    private String fingerprintCollectStatus = null;
    private String visitMode;
    public String getFingerprintCollectStatus() {
        return fingerprintCollectStatus;
    }
    public void setFingerprintCollectStatus(String fingerprintCollectStatus) {
        this.fingerprintCollectStatus = fingerprintCollectStatus;
    }

    //add by bingham.liu 20090226 begin
    //从界面取得case_type，区分个险、团险权限
    public void setPageCaseType(String sPageCaseType)
    {
        this.sPageCaseType = sPageCaseType;
    }
    public String getPageCaseType(){ return sPageCaseType; }
    //add by bingham.liu 20090226 end

    public String show(){
        StringBuffer  sb = new StringBuffer();
        sb.append("\n<br>user_id = " + lUserId);
        sb.append("\n<br>type_id = " + lUserType);
        sb.append("\n<br>user_name = " + sUserName);
        sb.append("\n<br>real_name = " + sRealName);
        sb.append("\n<br>emp_code = " + sEmpCode);
        sb.append("\n<br>organ_id = " + sOrganId);
        sb.append("\n<br>dept_id = " + sDeptId);
        sb.append("\n<br>head_id = " + sHeadId);
        sb.append("\n<br>branch_id = " + sBranchId);
        sb.append("\n<br>page_case_type = " + sPageCaseType);
        sb.append("\n<br>certi_code = " + sCertiCode);
        sb.append("\n<br>certi_type = " + sCertiType);
        sb.append("\n<br>fingerprintStatus = " + fingerprintStatus);
        sb.append("\n<br>fingerprintCollectStatus = " + fingerprintCollectStatus);

        return sb.toString();
    }

    public long getAuthorizedUserId() {
        return authorizedUserId;
    }

    public void setAuthorizedUserId(long authorizedUserId) {
        this.authorizedUserId = authorizedUserId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    public void setVisitMode(String visitMode) {
        this.visitMode=visitMode;
    }
    public String getVisitMode(){
        return this.visitMode;
    }
}
