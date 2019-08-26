package com.dudu.model.bean;

import java.util.List;

public class LabelInfo {

    /**
     * story_list : [{"id":937,"title":"投鼠忌器","guide":"引导孩子去思考问题。","tags":[{"id":3,"name":"6岁+"},{"id":8,"name":"中国寓言故事"},{"id":9,"name":"成语故事"},{"id":16,"name":"生命教育"},{"id":18,"name":"情绪管理"},{"id":21,"name":"品格教育"},{"id":28,"name":"睡前故事"},{"id":54,"name":"责任"}],"picture":"891.png","audio":"","like_num":118},{"id":936,"title":"一人得道鸡犬升天","guide":"中国经典成语故事，提高思考理解能力。","tags":[{"id":3,"name":"6岁+"},{"id":9,"name":"成语故事"},{"id":10,"name":"中国神话故事"},{"id":26,"name":"科普故事"},{"id":28,"name":"睡前故事"}],"picture":"890.png","audio":"","like_num":54},{"id":927,"title":"见利反目","guide":"为了利益相互争夺最后只会两败俱伤。","tags":[{"id":2,"name":"3岁+"},{"id":8,"name":"中国寓言故事"},{"id":9,"name":"成语故事"},{"id":21,"name":"品格教育"},{"id":22,"name":"道德故事"},{"id":45,"name":"诚信"},{"id":47,"name":"友情"},{"id":49,"name":"团结"}],"picture":"869.png","audio":"869.mp3","like_num":67},{"id":919,"title":"望梅止渴","guide":"望梅止渴比喻愿望无法实现，用空想安慰自己。","tags":[{"id":3,"name":"6岁+"},{"id":8,"name":"中国寓言故事"},{"id":9,"name":"成语故事"},{"id":48,"name":"智慧"},{"id":59,"name":"思考"}],"picture":"882.png","audio":"","like_num":101},{"id":915,"title":"乐不思蜀","guide":"身处幸福中却忘记了过去的痛苦。","tags":[{"id":3,"name":"6岁+"},{"id":8,"name":"中国寓言故事"},{"id":9,"name":"成语故事"},{"id":48,"name":"智慧"},{"id":59,"name":"思考"}],"picture":"879.png","audio":"","like_num":40},{"id":910,"title":"上行下效","guide":"赞美和批评都应该一并接受。","tags":[{"id":3,"name":"6岁+"},{"id":8,"name":"中国寓言故事"},{"id":9,"name":"成语故事"},{"id":21,"name":"品格教育"},{"id":25,"name":"自我认知"},{"id":59,"name":"思考"}],"picture":"875.png","audio":"875.mp3","like_num":54},{"id":897,"title":"皮毛相依","guide":"中国成语故事，启发孩子的思考能力。","tags":[{"id":3,"name":"6岁+"},{"id":9,"name":"成语故事"},{"id":21,"name":"品格教育"},{"id":28,"name":"睡前故事"},{"id":48,"name":"智慧"},{"id":59,"name":"思考"}],"picture":"886.png","audio":"","like_num":24},{"id":894,"title":"与狐谋皮","guide":"中国成语故事，培养孩子对事物的认知能力。","tags":[{"id":3,"name":"6岁+"},{"id":9,"name":"成语故事"},{"id":25,"name":"自我认知"},{"id":28,"name":"睡前故事"},{"id":48,"name":"智慧"},{"id":59,"name":"思考"}],"picture":"883.png","audio":"","like_num":49},{"id":839,"title":"欲速则不达","guide":"做事不要单纯追求速度，不要贪图小利。","tags":[{"id":3,"name":"6岁+"},{"id":9,"name":"成语故事"},{"id":11,"name":"民间故事"},{"id":40,"name":"谦虚"},{"id":41,"name":"礼貌"},{"id":59,"name":"思考"}],"picture":"835.png","audio":"835.mp3","like_num":48},{"id":838,"title":"此地无银三百两","guide":"张三的三百两银子到了王二的手里。","tags":[{"id":3,"name":"6岁+"},{"id":9,"name":"成语故事"},{"id":11,"name":"民间故事"},{"id":48,"name":"智慧"}],"picture":"834.png","audio":"834.mp3","like_num":54}]
     * tag : 成语故事
     * total_pages : 15
     * story_num : 150
     * result : success
     */

    private String tag;
    private int total_pages;
    private int story_num;
    private String result;
    private List<StoryListBean> story_list;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getStory_num() {
        return story_num;
    }

    public void setStory_num(int story_num) {
        this.story_num = story_num;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<StoryListBean> getStory_list() {
        return story_list;
    }

    public void setStory_list(List<StoryListBean> story_list) {
        this.story_list = story_list;
    }

    public static class StoryListBean {
        /**
         * id : 937
         * title : 投鼠忌器
         * guide : 引导孩子去思考问题。
         * tags : [{"id":3,"name":"6岁+"},{"id":8,"name":"中国寓言故事"},{"id":9,"name":"成语故事"},{"id":16,"name":"生命教育"},{"id":18,"name":"情绪管理"},{"id":21,"name":"品格教育"},{"id":28,"name":"睡前故事"},{"id":54,"name":"责任"}]
         * picture : 891.png
         * audio :
         * like_num : 118
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
             * id : 3
             * name : 6岁+
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
