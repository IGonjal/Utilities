package org.igm;

import org.igm.util.AnagramChecker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AnagramCheckerTest {

     @Test
    public void anagramTest() {
        assertTrue(AnagramChecker.getInstance().consideringPunctuation(false).isAnagram("asdfghj  klñ", "ñlkj  h g fds, . a"));
        assertFalse(AnagramChecker.getInstance().consideringPunctuation(true).isAnagram("asdfghj  klñ", "ñlkj  h g fds, . a"));

    }
}
