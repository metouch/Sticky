package th.selection.bean;

/**
 * Created by me_touch on 2017/7/18.
 * 排序列表的左边
 */

public class IndexBean {

    public String firstSpell;
    public int position;

    public IndexBean(String firstSpell, int position){
        this.firstSpell = firstSpell;
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if(firstSpell == null || obj == null || !(obj instanceof IndexBean))
            return false;
        else
            return firstSpell.equals(((IndexBean) obj).firstSpell);
    }

    @Override
    public String toString() {
        return firstSpell + ", " + position;
    }
}
