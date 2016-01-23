package application;

/**
 * Created by Max on 16-01-22.
 */
public abstract class Line {
    int lineNumber;
    private String sceneNumber;
    private String actNumber;

    public abstract boolean isSpeech();

    public int getLineNumber() {
        return lineNumber;
    }

    public String getSceneNumber() {
        return sceneNumber;
    }

    public void setSceneNumber(String sceneNumber) {
        this.sceneNumber = sceneNumber;
    }

    public String getActNumber() {
        return actNumber;
    }

    public void setActNumber(String actNumber) {
        this.actNumber = actNumber;
    }
}
