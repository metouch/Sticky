package th.selection;

import com.github.promeg.pinyinhelper.Pinyin;

import lib.sticky.spell.IWord2Spell;

/**
 * Created by me_touch on 2017/8/2.
 *
 */

public class CityBean implements IWord2Spell {

    private int id;
    private String name;

    public CityBean(int id, String name){
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String word2spell() {
        return Pinyin.toPinyin(name, ",");
    }
}
