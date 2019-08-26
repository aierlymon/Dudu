package com.dudu.model.bean;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public class AllStoryBean {

    /**
     * classification : [{"id":1,"name":"年龄段","tag_list":[{"id":1,"name":"0~2岁"},{"id":2,"name":"3岁+"},{"id":3,"name":"6岁+"}],"num_of_story":1691},{"id":2,"name":"故事体裁","tag_list":[{"id":4,"name":"安徒生童话"},{"id":5,"name":"格林童话"},{"id":7,"name":"伊索寓言"},{"id":8,"name":"中国寓言故事"},{"id":9,"name":"成语故事"},{"id":10,"name":"中国神话故事"},{"id":11,"name":"民间故事"},{"id":12,"name":"名人故事"},{"id":13,"name":"现代故事"},{"id":71,"name":"寓言故事"},{"id":73,"name":"童话故事"},{"id":74,"name":"绘本故事"}],"num_of_story":1359},{"id":3,"name":"教育意义","tag_list":[{"id":15,"name":"安全教育"},{"id":16,"name":"生命教育"},{"id":17,"name":"益智故事"},{"id":18,"name":"情绪管理"},{"id":19,"name":"语言表达"},{"id":20,"name":"习惯养成"},{"id":21,"name":"品格教育"},{"id":22,"name":"道德故事"},{"id":23,"name":"礼仪故事"},{"id":24,"name":"艺术修养"},{"id":25,"name":"自我认知"},{"id":26,"name":"科普故事"},{"id":27,"name":"社交能力"},{"id":28,"name":"睡前故事"}],"num_of_story":1390},{"id":4,"name":"故事风格","tag_list":[{"id":31,"name":"魔法世界"},{"id":32,"name":"历史故事"},{"id":33,"name":"校园故事"},{"id":34,"name":"公主故事"},{"id":35,"name":"动物故事"},{"id":36,"name":"探险故事"},{"id":37,"name":"世界名著"},{"id":38,"name":"悬疑推理"},{"id":70,"name":"王子故事"},{"id":72,"name":"幽默故事"}],"num_of_story":677},{"id":5,"name":"价值观","tag_list":[{"id":39,"name":"独立"},{"id":40,"name":"谦虚"},{"id":41,"name":"礼貌"},{"id":42,"name":"坚强"},{"id":43,"name":"勇敢"},{"id":44,"name":"善良"},{"id":45,"name":"诚信"},{"id":46,"name":"亲情"},{"id":47,"name":"友情"},{"id":48,"name":"智慧"},{"id":49,"name":"团结"},{"id":50,"name":"勤劳"},{"id":51,"name":"乐观"},{"id":52,"name":"分享"},{"id":53,"name":"自信"},{"id":54,"name":"责任"},{"id":55,"name":"爱心"},{"id":56,"name":"帮助"},{"id":57,"name":"奉献"},{"id":58,"name":"坚持"},{"id":59,"name":"思考"},{"id":60,"name":"探索"},{"id":61,"name":"干净"},{"id":62,"name":"信任"},{"id":63,"name":"专注"},{"id":64,"name":"诚实"},{"id":65,"name":"感恩"}],"num_of_story":1410}]
     * result : success
     */

    private String result;
    private List<ClassificationBean> classification;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ClassificationBean> getClassification() {
        return classification;
    }

    public void setClassification(List<ClassificationBean> classification) {
        this.classification = classification;
    }

    public static class ClassificationBean {
        /**
         * id : 1
         * name : 年龄段
         * tag_list : [{"id":1,"name":"0~2岁"},{"id":2,"name":"3岁+"},{"id":3,"name":"6岁+"}]
         * num_of_story : 1691
         */

        private int id;
        private String name;
        private int num_of_story;
        private List<TagListBean> tag_list;

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

        public int getNum_of_story() {
            return num_of_story;
        }

        public void setNum_of_story(int num_of_story) {
            this.num_of_story = num_of_story;
        }

        public List<TagListBean> getTag_list() {
            return tag_list;
        }

        public void setTag_list(List<TagListBean> tag_list) {
            this.tag_list = tag_list;
        }

        public static class TagListBean {
            /**
             * id : 1
             * name : 0~2岁
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
