package org.nachain.core.crypto.bip39;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dictionary {

    private final List<String> words = new ArrayList<>();

    public Dictionary(Language language) throws IOException {
        InputStream wordStream = this.getClass().getClassLoader()
                .getResourceAsStream("wordlists/" + language.name().toLowerCase() + ".txt");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(wordStream), StandardCharsets.UTF_8));
        String word;

        while ((word = reader.readLine()) != null) {
            words.add(word);
        }
    }

    public String getWord(int wordIdx) {
        return words.get(wordIdx);
    }

    public int indexOf(String word) {
        return words.indexOf(word);
    }
}
