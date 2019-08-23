package com.dudu.model.bean;

import java.util.List;

public class SubjectInfo {


    /**
     * num_story : 12
     * title : 爱丽丝漫游奇境记（完）
     * total_pages : 2
     * result : success
     * data : [{"id":1322,"title":"第一章 掉进兔子洞","guide":"一生必读的经典系列。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1259.png","audio":"1259.mp3","like_num":126},{"id":1323,"title":"第二章 眼泪的池塘","guide":"爱丽丝哭出来的眼泪变成了一个池塘。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1260.png","audio":"1260.mp3","like_num":122},{"id":1324,"title":"第三章 会议式赛跑和长故事","guide":"听老鼠讲故事。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1261.png","audio":"1261.mp3","like_num":62},{"id":1325,"title":"第四章 兔子派来小比尔","guide":"爱丽丝又变小了。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1262.png","audio":"1262.mp3","like_num":73},{"id":1326,"title":"第五章 毛毛虫的建议","guide":"吃了毛毛虫给的蘑菇，爱丽丝变成了\u201c蛇\u201d。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1263.png","audio":"1263.mp3","like_num":68},{"id":1327,"title":"第六章 小猪和胡椒","guide":"爱丽丝见到了一只会笑的猫。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1264.png","audio":"1264.mp3","like_num":42},{"id":1328,"title":"第七章 发疯的茶会","guide":"爱丽丝和三月兔、帽匠、睡鼠的奇怪茶会。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1265.png","audio":"1265.mp3","like_num":39},{"id":1329,"title":"第八章  王后的槌球场","guide":"喜欢\u201c砍头\u201d的王后。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1266.png","audio":"1266.mp3","like_num":56},{"id":1330,"title":"第九章 素甲鱼的故事","guide":"关于素甲鱼海底学校的趣事。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1267.png","audio":"1267.mp3","like_num":58},{"id":1331,"title":"第十章 龙虾四组舞","guide":"素甲鱼和鹰头狮带着爱丽丝跳龙虾四组舞。","tags":[{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"picture":"1268.png","audio":"1268.mp3","like_num":56}]
     * picture : s018.png
     */

    private int num_story;
    private String title;
    private int total_pages;
    private String result;
    private String picture;
    private List<DataBean> data;

    public int getNum_story() {
        return num_story;
    }

    public void setNum_story(int num_story) {
        this.num_story = num_story;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1322
         * title : 第一章 掉进兔子洞
         * guide : 一生必读的经典系列。
         * tags : [{"id":2,"name":"3岁+"},{"id":31,"name":"魔法世界"},{"id":36,"name":"探险故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}]
         * picture : 1259.png
         * audio : 1259.mp3
         * like_num : 126
         */

        private int id;
        private String title;
        private String guide;
        private String picture;
        private String audio;
        private int like_num;
        private List<TagsBean> tags;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGuide() {
            return guide;
        }

        public void setGuide(String guide) {
            this.guide = guide;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class TagsBean {
            /**
             * id : 2
             * name : 3岁+
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
