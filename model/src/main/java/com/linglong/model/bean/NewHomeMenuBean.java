package com.linglong.model.bean;

import java.util.List;

public class NewHomeMenuBean {

    /**
     * loanCategories : [{"id":1,"name":"新口子","icon":"/group1/default/20190701/18/09/8/1.png","createTime":1561904630},{"id":2,"name":"闪电下款","icon":"/group1/default/20190701/18/10/8/4.png","createTime":1561904644},{"id":3,"name":"高通过率","icon":"/group1/default/20190701/18/10/8/3.png","createTime":1561946855},{"id":4,"name":"品牌大额","icon":"/group1/default/20190701/18/10/8/2.png","createTime":1561947388}]
     * page : 1
     * pageCount : 5
     */

    private int page;
    private int pageCount;
    private List<LoanCategoriesBean> loanCategories;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<LoanCategoriesBean> getLoanCategories() {
        return loanCategories;
    }

    public void setLoanCategories(List<LoanCategoriesBean> loanCategories) {
        this.loanCategories = loanCategories;
    }

    public static class LoanCategoriesBean {
        /**
         * id : 1
         * name : 新口子
         * icon : /group1/default/20190701/18/09/8/1.png
         * createTime : 1561904630
         */

        private int id;
        private String name;
        private String icon;
        private int createTime;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }
}
