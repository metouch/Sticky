package th.selection.spell;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Map;

/**
 * Created by me_touch on 2017/7/17.
 * 实现汉字转拼音
 */

public class Word2SpellIMPL implements IWord2Spell {

    private static volatile Word2SpellIMPL impl;

    public static Word2SpellIMPL getInstance() {
        if (null == impl) {
            synchronized (Word2SpellIMPL.class) {
                if (null == impl)
                    impl = new Word2SpellIMPL();
            }
        }
        return impl;
    }

    @Override
    public String word2spell(final Map<String, String[]> dict, String word) {
        if (word == null || 0 == word.length())
            throw new NullPointerException("word cannot be null");
        return Pinyin.toPinyin(word, ",");
    }

    @Override
    public String word2spell(String word) {
        return word2spell(null, word);
    }
}
