package th.selection.spell;

import java.util.Map;

/**
 * Created by me_touch on 2017/6/15.
 *
 */

public interface IWord2Spell {

    String word2spell(final Map<String, String[]> dict, String word);
    String word2spell(String word);
}
