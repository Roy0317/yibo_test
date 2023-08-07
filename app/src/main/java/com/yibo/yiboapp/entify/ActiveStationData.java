package com.yibo.yiboapp.entify;

import java.util.List;

public class ActiveStationData {
    String activeLogo;
    List<Aactivebean> active;

    public String getActiveLogo() {
        return activeLogo;
    }

    public void setActiveLogo(String activeLogo) {
        this.activeLogo = activeLogo;
    }

    public List<Aactivebean> getActive() {
        return active;
    }

    public void setActive(List<Aactivebean> active) {
        this.active = active;
    }

    public class Aactivebean {
        String img;
        int id;
        String title;
        boolean isChecke;

        public boolean isChecke() {
            return isChecke;
        }

        public void setChecke(boolean checke) {
            isChecke = checke;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

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
    }
}
