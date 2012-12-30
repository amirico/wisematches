package wisematches.personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Alphabet {
    private final char[] letters;

    public Alphabet(char[] letters) {
        this.letters = letters;
    }

    public char[] getLetters() {
        return letters.clone();
    }

    public boolean contains(char letter) {
        for (char aChar : letters) {
            if (aChar == letter) {
                return true;
            }
        }
        return false;
    }

    public boolean validate(String word) {
        for (char c : word.toCharArray()) {
            if (!contains(c)) {
                return false;
            }
        }
        return true;
    }
}
