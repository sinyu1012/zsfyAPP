package com.fyzs.tool;

/**
 * Created by sinyu on 2017/3/25.
 */

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class HeadPortrait extends BmobObject {
    private BmobFile image;

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    private String xh;
    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

}