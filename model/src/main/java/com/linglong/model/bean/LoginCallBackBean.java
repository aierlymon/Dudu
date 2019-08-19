package com.linglong.model.bean;

public class LoginCallBackBean {

    /**
     * user : {"id":29,"phone":"13060600609","password":"123456","nick":"用户7898917","name":null,"card":null,"clientType":null,"createTime":1563955874,"activeTime":1563955874,"channelId":1,"status":1,"ip":"127.0.0.1"}
     * token :
     */

    private UserBean user;
    private String token;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class UserBean {
        /**
         * id : 29
         * phone : 13060600609
         * password : 123456
         * nick : 用户7898917
         * name : null
         * card : null
         * clientType : null
         * createTime : 1563955874
         * activeTime : 1563955874
         * channelId : 1
         * status : 1
         * ip : 127.0.0.1
         */

        private int id;
        private String phone;
        private String password;
        private String nick;
        private Object name;
        private Object card;
        private Object clientType;
        private int createTime;
        private int activeTime;
        private int channelId;
        private int status;
        private String ip;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getCard() {
            return card;
        }

        public void setCard(Object card) {
            this.card = card;
        }

        public Object getClientType() {
            return clientType;
        }

        public void setClientType(Object clientType) {
            this.clientType = clientType;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public int getActiveTime() {
            return activeTime;
        }

        public void setActiveTime(int activeTime) {
            this.activeTime = activeTime;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }
}
