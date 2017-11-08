package com.yaping.leisureTime.duanzia.Bean;

/**
 * 功能：
 *
 * @创建： Created by yaping on 2017/11/2 0002.
 */

public  class GroupBean {

    /**
     * user : {"is_living":false,"user_id":5743418029,"name":"瑾色残年62514365","followings":0,"user_verified":false,"ugc_count":4,"avatar_url":"http://p3.pstatp.com/medium/1dcf00074f9ddf81c992","followers":2,"is_following":false,"is_pro_user":false,"medals":[]}
     * content : 昨天睡觉前去厕所 可能看手机看太久有点懵了，就直奔厨房去了………到了厨房发现走错地方了 正准备往回走…看见我爸也在厨房 ，，他就问我找什么。。。我说找厕所。。。。。他后来就每隔几分钟进我房间看看我是否还正常
     */

    private UserBean user;
    private String content;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static class UserBean {
        /**
         * is_living : false
         * user_id : 5743418029
         * name : 瑾色残年62514365
         * followings : 0
         * user_verified : false
         * ugc_count : 4
         * avatar_url : http://p3.pstatp.com/medium/1dcf00074f9ddf81c992
         * followers : 2
         * is_following : false
         * is_pro_user : false
         * medals : []
         */

        private String name;
        private String avatar_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }
}
