package zh.lingvo.data.fixtures;

import zh.lingvo.data.model.Quiz;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.data.model.QuizRun;

import static zh.lingvo.util.CollectionsHelper.getListSafely;

public class QuizRunEvaluator {
    private final Quiz quiz;
    private final QuizRun quizRun;
    private final Integer maxScore;

    public QuizRunEvaluator(Quiz quiz, QuizRun quizRun) {
        this.quiz = quiz;
        this.quizRun = quizRun;
        maxScore = this.quiz.getMaxScore();
    }

    public void evaluate() {
        getListSafely(quiz::getQuizRecords)
                .stream()
                .filter(qr -> quizRun.getDoneRecords().containsKey(qr.getId()))
                .forEach(this::updateStats);
    }

    private void updateStats(QuizRecord quizRecord) {
        Long id = quizRecord.getId();
        boolean success = quizRun.getDoneRecords().get(id);
        if (success)
            updateStatsForSuccess(quizRecord);
        else
            updateStatsForFailure(quizRecord);
    }

    private void updateStatsForSuccess(QuizRecord quizRecord) {
        quizRecord.setNumberOfRuns(quizRecord.getNumberOfRuns() + 1);
        quizRecord.setNumberOfSuccesses(quizRecord.getNumberOfSuccesses() + 1);
        float currentScore = Math.min(quizRecord.getCurrentScore() + 1f / maxScore, 1.0f);
        quizRecord.setCurrentScore(currentScore);
    }

    private void updateStatsForFailure(QuizRecord quizRecord) {
        quizRecord.setNumberOfRuns(quizRecord.getNumberOfRuns() + 1);
        float currentScore = quizRecord.getCurrentScore() - 1f / maxScore;
        quizRecord.setCurrentScore(currentScore);
    }
}
