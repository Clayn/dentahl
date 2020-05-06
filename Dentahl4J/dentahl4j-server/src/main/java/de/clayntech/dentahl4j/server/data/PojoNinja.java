package de.clayntech.dentahl4j.server.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import de.clayntech.dentahl4j.domain.Element;
import de.clayntech.dentahl4j.domain.Ninja;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class PojoNinja {

    @SerializedName("released")
    @Expose
    private String released;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iId")
    @Expose
    private String iId;
    @SerializedName("szName")
    @Expose
    private String szName;
    @SerializedName("szNickname")
    @Expose
    private String szNickname;
    @SerializedName("iNid")
    @Expose
    private String iNid;
    @SerializedName("szPicUrl")
    @Expose
    private String szPicUrl;
    @SerializedName("szPicUrl2")
    @Expose
    private String szPicUrl2;
    @SerializedName("szPicUrl3")
    @Expose
    private String szPicUrl3;
    @SerializedName("szAttr")
    @Expose
    private String szAttr;
    @SerializedName("szBasicAttr")
    @Expose
    private String szBasicAttr;
    @SerializedName("iStar")
    @Expose
    private String iStar;
    @SerializedName("szOrg")
    @Expose
    private String szOrg;
    @SerializedName("iOySkill")
    @Expose
    private String iOySkill;
    @SerializedName("iPgSkill")
    @Expose
    private String iPgSkill;
    @SerializedName("iBdSkill1")
    @Expose
    private String iBdSkill1;
    @SerializedName("iBdSkill2")
    @Expose
    private String iBdSkill2;
    @SerializedName("iBdSkill3")
    @Expose
    private String iBdSkill3;
    @SerializedName("szEffect")
    @Expose
    private String szEffect;
    @SerializedName("szEffectChase")
    @Expose
    private String szEffectChase;
    @SerializedName("szOyTp")
    @Expose
    private String szOyTp;
    @SerializedName("szPgTp")
    @Expose
    private String szPgTp;
    @SerializedName("szBdTp1")
    @Expose
    private String szBdTp1;
    @SerializedName("szBdTp2")
    @Expose
    private String szBdTp2;
    @SerializedName("szBdTp3")
    @Expose
    private String szBdTp3;
    @SerializedName("iStatus")
    @Expose
    private String iStatus;
    @SerializedName("szGetWay")
    @Expose
    private String szGetWay;
    @SerializedName("szPos")
    @Expose
    private String szPos;

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIId() {
        return iId;
    }

    public void setIId(String iId) {
        this.iId = iId;
    }

    public String getSzName() {
        return szName;
    }

    public void setSzName(String szName) {
        this.szName = szName;
    }

    public String getSzNickname() {
        return szNickname;
    }

    public void setSzNickname(String szNickname) {
        this.szNickname = szNickname;
    }

    public String getINid() {
        return iNid;
    }

    public void setINid(String iNid) {
        this.iNid = iNid;
    }

    public String getSzPicUrl() {
        return szPicUrl;
    }

    public void setSzPicUrl(String szPicUrl) {
        this.szPicUrl = szPicUrl;
    }

    public String getSzPicUrl2() {
        return szPicUrl2;
    }

    public void setSzPicUrl2(String szPicUrl2) {
        this.szPicUrl2 = szPicUrl2;
    }

    public String getSzPicUrl3() {
        return szPicUrl3;
    }

    public void setSzPicUrl3(String szPicUrl3) {
        this.szPicUrl3 = szPicUrl3;
    }

    public String getSzAttr() {
        return szAttr;
    }

    public void setSzAttr(String szAttr) {
        this.szAttr = szAttr;
    }

    public String getSzBasicAttr() {
        return szBasicAttr;
    }

    public void setSzBasicAttr(String szBasicAttr) {
        this.szBasicAttr = szBasicAttr;
    }

    public String getIStar() {
        return iStar;
    }

    public void setIStar(String iStar) {
        this.iStar = iStar;
    }

    public String getSzOrg() {
        return szOrg;
    }

    public void setSzOrg(String szOrg) {
        this.szOrg = szOrg;
    }

    public String getIOySkill() {
        return iOySkill;
    }

    public void setIOySkill(String iOySkill) {
        this.iOySkill = iOySkill;
    }

    public String getIPgSkill() {
        return iPgSkill;
    }

    public void setIPgSkill(String iPgSkill) {
        this.iPgSkill = iPgSkill;
    }

    public String getIBdSkill1() {
        return iBdSkill1;
    }

    public void setIBdSkill1(String iBdSkill1) {
        this.iBdSkill1 = iBdSkill1;
    }

    public String getIBdSkill2() {
        return iBdSkill2;
    }

    public void setIBdSkill2(String iBdSkill2) {
        this.iBdSkill2 = iBdSkill2;
    }

    public String getIBdSkill3() {
        return iBdSkill3;
    }

    public void setIBdSkill3(String iBdSkill3) {
        this.iBdSkill3 = iBdSkill3;
    }

    public String getSzEffect() {
        return szEffect;
    }

    public void setSzEffect(String szEffect) {
        this.szEffect = szEffect;
    }

    public String getSzEffectChase() {
        return szEffectChase;
    }

    public void setSzEffectChase(String szEffectChase) {
        this.szEffectChase = szEffectChase;
    }

    public String getSzOyTp() {
        return szOyTp;
    }

    public void setSzOyTp(String szOyTp) {
        this.szOyTp = szOyTp;
    }

    public String getSzPgTp() {
        return szPgTp;
    }

    public void setSzPgTp(String szPgTp) {
        this.szPgTp = szPgTp;
    }

    public String getSzBdTp1() {
        return szBdTp1;
    }

    public void setSzBdTp1(String szBdTp1) {
        this.szBdTp1 = szBdTp1;
    }

    public String getSzBdTp2() {
        return szBdTp2;
    }

    public void setSzBdTp2(String szBdTp2) {
        this.szBdTp2 = szBdTp2;
    }

    public String getSzBdTp3() {
        return szBdTp3;
    }

    public void setSzBdTp3(String szBdTp3) {
        this.szBdTp3 = szBdTp3;
    }

    public String getIStatus() {
        return iStatus;
    }

    public void setIStatus(String iStatus) {
        this.iStatus = iStatus;
    }

    public String getSzGetWay() {
        return szGetWay;
    }

    public void setSzGetWay(String szGetWay) {
        this.szGetWay = szGetWay;
    }

    public String getSzPos() {
        return szPos;
    }

    public void setSzPos(String szPos) {
        this.szPos = szPos;
    }

    private static boolean numberAvailable(String txt) {
        return getNumber(txt)!=null;
    }

    private static Integer getNumber(String txt) {
        try{
            return txt==null||txt.isBlank()?-1:Integer.parseInt(txt);
        }catch (Exception ex) {
            return null;
        }
    }


    private static final Logger LOG= LoggerFactory.getLogger(PojoNinja.class);
    public Ninja toNinja(String imageBase, String image3DBase) throws MalformedURLException {
        if(!imageBase.endsWith("/")) {
            imageBase+="/";
        }
        Ninja n=new Ninja();
        n.setId(getNumber(getINid()));
        for(Element el:Element.values()) {
            if(getSzAttr().contains(el.name().toLowerCase())) {
                n.setElementType(el);
                break;
            }
        }
        n.setName(getSzName());
        int iId=numberAvailable(getIId())?getNumber(getIId()):-1;
        if(iId<=5&&iId>=1) {
            n.setMain(n.getElement()+1);
        }
        n.setImage(new URL(imageBase+getSzPicUrl()+".png"));
        return n;
    }

}