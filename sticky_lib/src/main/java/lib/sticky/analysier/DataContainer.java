package lib.sticky.analysier;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lib.sticky.bean.FullEntity;
import lib.sticky.bean.IndexBean;
import lib.sticky.spell.IWord2Spell;


/**
 * Created by me_touch on 2017/7/18.
 * 数据容器，负责处理数据以及分发数据
 * 数据分为3层：
 * head层，每一个head数据对应一个view
 * middle层，特殊数据层，直接按照添加的数据进行排序，描述不固定，使用的view与body层相同
 * body层，主体层，按照A...Z对数据排序，描述词为对应数据拼音的首字母
 */

public class DataContainer<T extends IWord2Spell> {

    private List<FullEntity<T>> decorationSet;
    private List<IndexBean> indexSet;

    private int indexOffset = 0;

    private int headDecorationCount = 0;
    private int headIndexCount = 0;

    private int middleDecorationCount = 0;
    private int middleIndexCount = 0;


    public DataContainer() {
        this.decorationSet = new ArrayList<>();
        this.indexSet = new ArrayList<>();
    }

    public DataContainer(@NonNull List<T> entities) {
        this();
        addData(entities);
    }


    /**
     * 有序列表插入,
     * 另一种实现方式，可以直接在decorationSet末尾插入，重排序，然后重新生成indexSet，由于效率问题，弃用
     *
     * @param entity 待插入body数据
     * @return 插入点位置
     */
    public int addDecorationData(@NonNull T entity) {
        return addDecorationData(headDecorationCount + middleDecorationCount, entity);
    }

    /**
     * @param start 从 i 位置开始查找
     * @param entity 待插入body数据
     * @return 插入点位置
     */
    public int addDecorationData(int start, @NonNull T entity) {
        for (int i = start; i < decorationSet.size(); i++) {
            String spell = entity.word2spell();
            if (spell.compareTo(decorationSet.get(i).spell) < 0) {
                FullEntity<T> fullEntity = new FullEntity<T>(entity);
                fullEntity.analysisSpell();
                decorationSet.add(i, fullEntity);
                if(i == start)
                    fullEntity.setDifferentWithLast(true);
                if (i - 1 >= start) {
                    FullEntity<T> entity1 = decorationSet.get(i - 1);
                    fullEntity.setDifferentWithLast(!fullEntity.firstSpell.equals(entity1.firstSpell));
                }
                if (i + 1 < decorationSet.size()) {
                    FullEntity entity1 = decorationSet.get(i + 1);
                    entity1.setDifferentWithLast(!fullEntity.firstSpell.equals(entity1.firstSpell));
                }
                return i;
            }
        }
        FullEntity<T> fullEntity = new FullEntity<T>(entity);
        fullEntity.analysisSpell();
        if (decorationSet.size() == headDecorationCount + middleDecorationCount)
            fullEntity.setDifferentWithLast(true);
        else {
            FullEntity fullEntity1 = decorationSet.get(decorationSet.size() - 1);
            fullEntity.setDifferentWithLast(!fullEntity.firstSpell.equals(fullEntity1.firstSpell));
        }
        decorationSet.add(fullEntity);
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
    public void addData(@NonNull T entity) {
        String spell = entity.word2spell();
        int start = addDecorationData(entity);
        addIndexData(spell.substring(0, 1), start);
    }

    //添加body部分数据
    public void addData(List<T> entities) {
        int startDecoration = headDecorationCount + middleDecorationCount;
        int startIndex = headIndexCount + middleIndexCount;
        List<FullEntity<T>> spells = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            T entity = entities.get(i);
            FullEntity<T> fullEntity = new FullEntity<T>(entity);
            fullEntity.analysisSpell();
            spells.add(fullEntity);
        }
        Collections.sort(spells);
        for (int i = 0; i < entities.size(); i++) {
            String spell = spells.get(i).spell;
            startDecoration = addDecorationData(startDecoration, spells.get(i).entity);
            startIndex = addIndexData(startIndex, spell.substring(0, 1), startDecoration);
        }
    }

    /**
     * 插入一个middle数据，
     *
     * @param entity                 待插入middle数据
     * @param decorationFirstSpell 分类行显示的文字
     * @param index                导航栏显示文字
     * @param showDecoration 是否显示分隔栏
     */
    public void addMiddleData(@NonNull T entity, @NonNull String decorationFirstSpell,
                              @NonNull String index, boolean showDecoration) {
        FullEntity<T> fullEntity = new FullEntity<T>(entity);
        fullEntity.analysisSpell();
        fullEntity.text = showDecoration ? decorationFirstSpell : null;
        fullEntity.setDifferentWithLast(showDecoration);
        fullEntity.firstSpell = index;
        decorationSet.add(headDecorationCount, fullEntity);
        offsetIndexPosition(headIndexCount, 1);
        int position = headDecorationCount;
        indexSet.add(headIndexCount, new IndexBean(index, position));
        middleDecorationCount ++;
        middleIndexCount ++;
    }

    /**
     * 插入一组middle数据，
     *
     * @param entities               待插入middle数据
     * @param decorationFirstSpell 分类行显示的文字
     * @param index                导航栏显示文字
     * @param showDecoration 是否显示分隔栏
     */
    public void addMiddleData(@NonNull List<T> entities, @NonNull String decorationFirstSpell,
                              @NonNull String index, boolean showDecoration) {
        if (entities.isEmpty()) return;
        for (int i = entities.size() - 1; i >= 0; i--) {
            FullEntity<T> fullEntity = new FullEntity<T>(entities.get(i));
            fullEntity.analysisSpell();
            fullEntity.text = showDecoration ? decorationFirstSpell : null;
            fullEntity.setDifferentWithLast(i == 0 && showDecoration);
            fullEntity.firstSpell = index;
            decorationSet.add(headDecorationCount, fullEntity);
        }
        offsetIndexPosition(headIndexCount, entities.size());
        int position = headDecorationCount;
        indexSet.add(headIndexCount, new IndexBean(index, position));
        middleDecorationCount += entities.size();
        middleIndexCount++;
    }

    /**
     * @param word 分段标题
     *@param index 索引文字
     * 如果 word == null && index == null 则表示偏移所有index数据
     */
    public void addHeaderData(String word, String index) {
        FullEntity<T> fullEntity = new FullEntity<T>(word, index);
        fullEntity.setText(word);
        fullEntity.setDifferentWithLast(word != null && !word.isEmpty());
        decorationSet.add(headDecorationCount, fullEntity);
        int start = word == null && index == null ? 0 : headIndexCount;
        offsetIndexPosition(headIndexCount, 1);
        headDecorationCount++;
        if(word == null || word.isEmpty()) return;
        indexSet.add(headIndexCount, new IndexBean(index, headDecorationCount - 1));
        headIndexCount++;
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

    public List<FullEntity<T>> getDecorationSet() {
        return decorationSet;
    }

    public List<IndexBean> getIndexSet() {
        return indexSet;
    }

}
