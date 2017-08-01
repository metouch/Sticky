package th.selection.analysier;

import android.support.annotation.NonNull;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import th.selection.bean.FullWordEntity;
import th.selection.bean.IndexBean;
import th.selection.spell.Word2SpellIMPL;

/**
 * Created by me_touch on 2017/7/18.
 * 数据容器，负责处理数据以及分发数据
 * 数据分为3层：
 * head层，每一个head数据对应一个view
 * middle层，特殊数据层，直接按照添加的数据进行排序，描述不固定，使用的view与body层相同
 * body层，主体层，按照A...Z对数据排序，描述词为对应数据拼音的首字母
 */

public class DataContainer {

    private List<FullWordEntity> decorationSet;
    private List<IndexBean> indexSet;
    private Word2SpellIMPL impl;

    private int indexOffset = 0;

    private int headDecorationCount = 0;
    private int headIndexCount = 0;

    private int middleDecorationCount = 0;
    private int middleIndexCount = 0;


    public DataContainer() {
        this.decorationSet = new ArrayList<>();
        this.indexSet = new ArrayList<>();
        this.impl = Word2SpellIMPL.getInstance();
    }

    public DataContainer(final Map<String, String[]> dict, @NonNull List<String> words){
        this();
        impl.addDict(dict);
        addData(words);
    }

    public DataContainer(@NonNull List<String> words) {
        this();
        addData(words);
//        for (int i = 0; i < words.size(); i++) {
//            String word = words.get(i);
//            String spell = impl.word2spell(word);
//            FullWordEntity entity = new FullWordEntity(spell);
//            entity.setWord(word);
//            decorationSet.add(entity);
//        }
//        Collections.sort(decorationSet);
//        for (int j = 0; j < decorationSet.size(); j++) {
//            FullWordEntity entity = decorationSet.get(j);
//            entity.setDifferentWithLast(j < 1 || !entity.firstSpell.equals(decorationSet.get(j - 1).firstSpell));
//            if (j == 0) {
//                indexSet.add(new IndexBean(decorationSet.get(j).firstSpell, 0));
//            } else {
//                String spell1 = decorationSet.get(j).firstSpell;
//                String spell2 = indexSet.get(indexSet.size() - 1).firstSpell;
//                if (!spell1.equals(spell2))
//                    indexSet.add(new IndexBean(spell1, j));
//            }
//        }
    }


    /**
     * 有序列表插入,
     * 另一种实现方式，可以直接在decorationSet末尾插入，重排序，然后重新生成indexSet，由于效率问题，弃用
     *
     * @param spell 待插入body数据
     * @return 插入点位置
     */
    public int addDecorationData(@NonNull String spell, @NonNull String word) {
        return addDecorationData(headDecorationCount + middleDecorationCount, spell, word);
    }

    /**
     * @param start 从 i 位置开始查找
     * @param spell 待插入body数据
     * @return 插入点位置
     */
    public int addDecorationData(int start, @NonNull String spell, @NonNull String word) {
        for (int i = start; i < decorationSet.size(); i++) {
            if (spell.compareTo(decorationSet.get(i).spell) <= 0) {
                FullWordEntity entity = new FullWordEntity(spell);
                entity.setWord(word);
                decorationSet.add(i, entity);
//                if (i == start && entity.compareTo(decorationSet.get(i).spell) != 0)
//                    entity.setDifferentWithLast(true);
                if (i - 1 >= start) {
                    FullWordEntity entity1 = decorationSet.get(i - 1);
                    entity.setDifferentWithLast(!entity.firstSpell.equals(entity1.firstSpell));
                }
                if (i + 1 < decorationSet.size()) {
                    FullWordEntity entity1 = decorationSet.get(i + 1);
                    entity1.setDifferentWithLast(!entity.firstSpell.equals(entity1.firstSpell));
                }
                return i;
            }
        }
        FullWordEntity entity = new FullWordEntity(spell);
        entity.setWord(word);
        if (decorationSet.size() == headDecorationCount + middleDecorationCount)
            entity.setDifferentWithLast(true);
        else {
            FullWordEntity entity1 = decorationSet.get(decorationSet.size() - 1);
            entity.setDifferentWithLast(!entity.firstSpell.equals(entity1.firstSpell));
        }
        decorationSet.add(entity);
        return decorationSet.size() - 1;
    }

    public int addIndexData(int start, @NonNull String firstSpell, int position) {
        for (int i = start; i < indexSet.size(); i++) {
            if (firstSpell.compareTo(indexSet.get(i).firstSpell) <= 0) {
                if (firstSpell.compareTo(indexSet.get(i).firstSpell) < 0)
                    indexSet.add(i, new IndexBean(firstSpell, position));
                for (int j = i + 1; j < indexSet.size(); j++) {
                    IndexBean bean = indexSet.get(j);
                    bean.position++;
                }
                return i;
            }
        }
        indexSet.add(new IndexBean(firstSpell, position));
        return indexSet.size() - 1;
    }

    public int addIndexData(@NonNull String firstSpell, int position) {
        return addIndexData(headIndexCount + middleIndexCount, firstSpell, position);
    }

    //添加body部分数据
    public void addData(@NonNull String word) {
        String spell = impl.word2spell(word);
        int start = addDecorationData(spell, word);
        addIndexData(spell.substring(0, 1), start);
    }

    //添加body部分数据
    public void addData(List<String> words) {
        int startDecoration = headDecorationCount + middleDecorationCount;
        int startIndex = headIndexCount + middleIndexCount;
        List<FullWordEntity> spells = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            String spell = impl.word2spell(word);
            FullWordEntity entity = new FullWordEntity(spell);
            entity.setWord(word);
            spells.add(entity);
        }
        Collections.sort(spells);
        for (int i = 0; i < words.size(); i++) {
            String spell = spells.get(i).spell;
            startDecoration = addDecorationData(startDecoration, spell, spells.get(i).word);
            startIndex = addIndexData(startIndex, spell.substring(0, 1), startDecoration);
        }
    }

    /**
     * 插入一个middle数据，
     *
     * @param word                 待插入middle数据
     * @param decorationFirstSpell 分类行显示的文字
     * @param index                导航栏显示文字
     * @param showDecoration 是否显示分隔栏
     */
    public void addMiddleData(@NonNull String word, @NonNull String decorationFirstSpell,
                              @NonNull String index, boolean showDecoration) {
        FullWordEntity entity = new FullWordEntity(word, index);
        entity.word = word;
        entity.text = decorationFirstSpell;
        entity.setDifferentWithLast(showDecoration);
        entity.firstSpell = index;
        decorationSet.add(headDecorationCount, entity);
        offsetIndexPosition(1);
        int position = headDecorationCount;
        indexSet.add(headIndexCount, new IndexBean(index, position));
        middleDecorationCount ++;
        middleIndexCount ++;
    }

    /**
     * 插入一组middle数据，
     *
     * @param words                待插入middle数据
     * @param decorationFirstSpell 分类行显示的文字
     * @param index                导航栏显示文字
     * @param showDecoration 是否显示分隔栏
     */
    public void addMiddleData(@NonNull List<String> words, @NonNull String decorationFirstSpell,
                              @NonNull String index, boolean showDecoration) {
        if (words.isEmpty()) return;
        for (int i = words.size() - 1; i >= 0; i--) {
            FullWordEntity entity = new FullWordEntity(words.get(i), index);
            entity.word = words.get(i);
            entity.text = showDecoration ? decorationFirstSpell : null;
            entity.setDifferentWithLast(i == 0 && showDecoration);
            entity.firstSpell = index;
            decorationSet.add(headDecorationCount, entity);
        }
        offsetIndexPosition(words.size());
        int position = headDecorationCount;
        indexSet.add(headIndexCount, new IndexBean(index, position));
        middleDecorationCount += words.size();
        middleIndexCount++;
    }

    /**
     * @param word
     *
     */
    public void addHeaderData(String word, String index) {
        FullWordEntity entity = new FullWordEntity(word, index);
        entity.setText(word);
        entity.word = word;
        entity.setDifferentWithLast(word != null && !word.isEmpty());
        decorationSet.add(0, entity);
        offsetIndexPosition(1);
        if(word == null || word.isEmpty()) return;
        indexSet.add(0, new IndexBean(index, 0));
        headIndexCount++;
        headDecorationCount++;
    }


    public void updateIndexOffset(int offset) {
        this.indexOffset += offset;
    }

    public void offsetIndexPosition(int offset){
        offsetIndexPosition(0, offset);
    }

    public void offsetIndexPosition(int start, int offset){
        int length = indexSet.size();
        for (int i = start; i < length; i++) {
            indexSet.get(i).position += offset;
        }
    }

    public int getIndexOffset() {
        return indexOffset;
    }

    public List<FullWordEntity> getDecorationSet() {
        return decorationSet;
    }

    public List<IndexBean> getIndexSet() {
        return indexSet;
    }

}
