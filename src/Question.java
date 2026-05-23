package phase1;

public class Question {

    // ==============================
    // 1. Question Data
    // ==============================
    private String question;
    private String[] options;
    private String correctAnswer;

    // ==============================
    // 2. Constructor
    // ==============================
    public Question(String question, String[] options, String correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // ==============================
    // 3. Getters
    // ==============================
    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // ==============================
    // 4. Check Answer
    // ==============================
    public boolean isCorrect(String answer) {
        return correctAnswer.equals(answer);
    }
}