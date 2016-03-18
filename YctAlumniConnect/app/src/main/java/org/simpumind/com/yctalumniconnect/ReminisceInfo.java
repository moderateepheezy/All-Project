package org.simpumind.com.yctalumniconnect;

/**
 * Created by simpumind on 12/23/15.
 */
public class ReminisceInfo {
    protected String slang;
    protected String pronunciation;
    protected String meaning;

    public ReminisceInfo(String slang, String pronunciation, String meaning) {
        this.slang = slang;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
    }

    public String getSlang() {
        return slang;
    }

    public void setSlang(String slang) {
        this.slang = slang;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
