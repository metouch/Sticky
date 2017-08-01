package th.selection.bean;

/**
 * Created by me_touch on 2017/6/15.
 *
 */

public class FullWordEntity implements Comparable<FullWordEntity>{

    public String word;
    public String spell;
    //与IndexBean的firstSpell相同
    public String firstSpell;
    //I分割每组数据时用于显示的字符
    public String text;
    private boolean differentWithLast = false; //区分与上一个数据的首字母是否相同

    public FullWordEntity(String spell){
        this.spell = spell;
        this.text = firstSpell = spell.substring(0, 1);
    }

    public FullWordEntity(String spell, String firstSpell){
        this.spell = spell;
        this.text = this.firstSpell = firstSpell;
    }

    public FullWordEntity(String word, String spell, String firstSpell){
        this.word = word;
        this.spell = spell;
        this.text = this.firstSpell = firstSpell;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setDifferentWithLast(boolean differentWithLast) {
        this.differentWithLast = differentWithLast;
    }

    public boolean isDifferentWithLast() {
        return differentWithLast;
    }

    @Override
    public int compareTo(FullWordEntity o) {
        return spell.compareTo(o.spell);
    }

    @Override
    public int hashCode() {
        return this.word == null ? 0 : word.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("this.spell = ")
                .append(spell)
                .append(",")
                .append("this.firstSpell = ")
                .append(firstSpell)
                .append(",")
                .append("this.differentWithLast = ")
                .append(differentWithLast)
                .toString();
    }

}
