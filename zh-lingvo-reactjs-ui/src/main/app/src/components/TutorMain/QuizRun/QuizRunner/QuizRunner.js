import quizRegimes from "../../../../static/constants/quizRegimes";
import matchingRegimes from "../../../../static/constants/matchingRegimes";

/*
type QuizRecord = {
    readonly id: number,
    readonly wordMainForm: string,
    readonly translations: string[],
    currentScore: number,
    numberOfRuns: number,
    numberOfSuccesses: number,
};

type Quiz = {
    readonly id: number,
    readonly maxScore: number,
    readonly quizRegime: string,
    readonly matchingRegime: string,
    records: QuizRecord[],
};

type DoneRecordEntry = {
    recordId: number,
    correct: boolean,
};

type QuizRun = {
    readonly id: number | null,
    readonly quizId: number | null,
    readonly quizRegime: string,
    readonly matchingRegime: string,
    readonly records: number[],
    readonly doneRecords: DoneRecordEntry[],
};

type Question = {
    quizRegime: string,
    recordId: number,
    done: boolean,
};

type Input = {
    id: number | null,
    quizId: number | null,
    records: QuizRecord[],
    quizRegime: string,
    matchingRegime: string,
    doneRecords: DoneRecordEntry[],
};

function shuffle<T>(array: T[]): void {
    let currentIndex = array.length, randomIndex;
  
    while (currentIndex != 0) {
      randomIndex = Math.floor(Math.random() * currentIndex);
      --currentIndex;
  
      [array[currentIndex], array[randomIndex]] = [
        array[randomIndex], array[currentIndex]];
    }
};

export default class QuizRunner {
    records: QuizRecord[];
    doneRecords: DoneRecordEntry[];
    nextIndex: number;
    quizRegime: string;
    matchingRegime: string;
    quizId: number | null;
    id: number | null;

    private constructor ({ records, quizRegime, matchingRegime, id, quizId, doneRecords }: Input) {
        this.records = records;
        this.doneRecords = doneRecords;
        this.nextIndex = 0;
        this.quizRegime = quizRegime;
        this.matchingRegime = matchingRegime;
        this.quizId = quizId;
        this.id = id;
    }

    public static fromQuiz(quiz: Quiz): QuizRunner {
        const records = quiz.records
            .filter(record => record.currentScore < 1)
        return new this({
            records,
            doneRecords: [],
            quizRegime: quiz.quizRegime,
            matchingRegime: quiz.matchingRegime,
            quizId: quiz.id,
            id: null,
        });
    }

    public static fromQuizRun(quizRun: QuizRun, quiz: Quiz): QuizRunner {
        const records = quiz.records
            .filter(record => quizRun.records.includes(record.id));
        return new this({
            records: records,
            doneRecords: quizRun.doneRecords,
            quizRegime: quizRun.quizRegime,
            matchingRegime: quizRun.matchingRegime,
            quizId: quizRun.quizId,
            id: quizRun.id,
        });
    }

    public setId(id: number) {
        this.id = id;
    }

    public initRun(): void {
        shuffle(this.records);
        this.nextIndex = 0;
    }

    public next(): Question {
        return {
            quizRegime: this.getNextQuizRegime(),
            recordId: this.records[this.nextIndex].id,
            done: this.nextIndex === this.records.length,
        };
    }

    public evaluate(answer: string): boolean {
        const quizRegime = this.getNextQuizRegime();
        const record = this.records[this.nextIndex];

        let isCorrect = false;
        switch (quizRegime) {
            case quizRegimes.FORWARD:
                const { translations } = record;
                isCorrect = this.match(translations, answer);
                break;
            case quizRegimes.BACKWARD:
                const { wordMainForm } = record;
                isCorrect = this.match([wordMainForm], answer);
                break;
        }

        ++this.nextIndex;
        this.doneRecords.push({
            recordId: record.id,
            correct: isCorrect,
        });
        return isCorrect;
    }

    private getNextQuizRegime(): string {
        if (this.quizRegime === quizRegimes.ALTERNATING)
            return this.nextIndex % 2 === 0 ? quizRegimes.FORWARD : quizRegimes.BACKWARD;
        return this.quizRegime;
    }

    private match(rightAnswers: string[], answer: string): boolean {
        answer = answer.trim();
        switch (this.matchingRegime) {
            case matchingRegimes.STRICT:
                return this.matchStrict(rightAnswers, answer);
            case matchingRegimes.RELAXED:
                return this.matchRelaxed(rightAnswers, answer);
            case matchingRegimes.LOOSENED:
                return this.matchLoosened(rightAnswers, answer);
            default: 
                return false;
        }
    }


    private matchStrict(rightAnswers: string[], answer: string): boolean {
        const quizRegime = this.getNextQuizRegime();
        const allAnswersInString = rightAnswers.join(' ');
        let tokens = [] as string[];
        for (let i = 0, len = rightAnswers.length; i < len; ++i) {
            const group = rightAnswers[i]
            tokens = tokens.concat(group.split(' '));
        }
        const flags = quizRegime === quizRegimes.FORWARD ? 'i' : undefined;
        const answerCoversAtLeastOneToken = tokens.reduce(
            (disj, token) => disj || answer.match(new RegExp(`^${token}`, flags)) != null, 
            false
        );
        const regExpStr = `(^${answer}| ${answer})`;
        const answerIsContainedInGroup = !!allAnswersInString.match(new RegExp(regExpStr, flags));
        const matched = answerCoversAtLeastOneToken && answerIsContainedInGroup;
        return matched;
    }

    private matchRelaxed(rightAnswers: string[], answer: string): boolean {
        let tokens = [] as string[];
        for (let i = 0, len = rightAnswers.length; i < len; ++i) {
            const group = rightAnswers[i]
            tokens = tokens.concat(group.split(' '));
        }

        const answerCoincideWithSomeWord = tokens.reduce(
            (disj, token) => disj || answer === token, 
            false,
        );
        if (answerCoincideWithSomeWord)
            return true;

        if (answer.length < 3)
            return false;

        const quizRegime = this.getNextQuizRegime();
        const allAnswersInString = rightAnswers.join(' ');
        const flags = quizRegime === quizRegimes.FORWARD ? 'i' : undefined;
        const regExpStr = `(^${answer}| ${answer})`;
        const answerIsContainedInGroup = !!allAnswersInString.match(new RegExp(regExpStr, flags));
        const matched = answerIsContainedInGroup;
        return matched;
    }

    private matchLoosened(rightAnswers: string[], answer: string): boolean {
        const quizRegime = this.getNextQuizRegime();
        return quizRegime === quizRegimes.BACKWARD
            ? this.matchStrict(rightAnswers, answer)
            : this.matchRelaxed(rightAnswers, answer);
    }

    public toQuizRun(): QuizRun {
        const remainingRecordIds = this.records.slice(this.nextIndex)
            .map(record => record.id);
        return {
            id: this.id,
            quizId: this.quizId,
            quizRegime: this.quizRegime,
            matchingRegime: this.matchingRegime,
            records: remainingRecordIds,
            doneRecords: this.doneRecords,
        };
    }
}   
*/

function shuffle(array) {
    let currentIndex = array.length, randomIndex;
  
    while (currentIndex != 0) {
      randomIndex = Math.floor(Math.random() * currentIndex);
      --currentIndex;
  
      [array[currentIndex], array[randomIndex]] = [
        array[randomIndex], array[currentIndex]];
    }
};

export default class QuizRunner {
    records;
    doneRecords;
    nextIndex;
    quizRegime;
    matchingRegime;
    quizId;
    id;

    constructor ({ records, quizRegime, matchingRegime, quizId, id, doneRecords }) {
        this.records = records;
        this.doneRecords = doneRecords;
        this.nextIndex = 0;
        this.quizRegime = quizRegime;
        this.matchingRegime = matchingRegime;
        this.quizId = quizId;
        this.id = id;
    }

    static fromQuiz(quiz) {
        const records = quiz.records
            .filter(record => record.currentScore < 1)
        return new this({
            records: records,
            doneRecords: [],
            quizRegime: quiz.quizRegime,
            matchingRegime: quiz.matchingRegime,
            id: null,
            quizId: quiz.id,
        });
    }

    static fromQuizRun(quizRun, quiz) {
        const records = quiz.records
            .filter(record => quizRun.records.includes(record.id));
        return new this({
            records: records,
            doneRecords: quizRun.doneRecords,
            quizRegime: quizRun.quizRegime,
            matchingRegime: quizRun.matchingRegime,
            quizId: quizRun.quizId,
            id: quizRun.id,
        });
    }

    setId(id) {
        this.id = id;
    }

    initRun() {
        shuffle(this.records);
        this.nextIndex = 0;
    }

    next() {
        return {
            quizRegime: this.getNextQuizRegime(),
            recordId: this.records[this.nextIndex].id,
            done: this.nextIndex === this.records.length - 1,
        };
    }

    evaluate(answer) {
        const quizRegime = this.getNextQuizRegime();
        const record = this.records[this.nextIndex];

        let isCorrect = false;
        switch (quizRegime) {
            case quizRegimes.FORWARD:
                const { translations } = record;
                isCorrect = this.match(translations, answer);
                break;
            case quizRegimes.BACKWARD:
                const { wordMainForm } = record;
                isCorrect = this.match([wordMainForm], answer);
                break;
        }

        ++this.nextIndex;
        this.doneRecords.push({
            recordId: record.id,
            correct: isCorrect,
        });
        return isCorrect;
    }

    getNextQuizRegime() {
        if (this.quizRegime === quizRegimes.ALTERNATING)
            return this.nextIndex % 2 === 0 ? quizRegimes.FORWARD : quizRegimes.BACKWARD;
        return this.quizRegime;
    }

    match(rightAnswers, answer) {
        answer = answer.trim();
        switch (this.matchingRegime) {
            case matchingRegimes.STRICT:
                return this.matchStrict(rightAnswers, answer);
            case matchingRegimes.RELAXED:
                return this.matchRelaxed(rightAnswers, answer);
            case matchingRegimes.LOOSENED:
                return this.matchLoosened(rightAnswers, answer);
            default: 
                return false;
        }
    }


    matchStrict(rightAnswers, answer) {
        const quizRegime = this.getNextQuizRegime();
        const allAnswersInString = rightAnswers.join(' ');
        let tokens = [];
        for (let i = 0, len = rightAnswers.length; i < len; ++i) {
            const group = rightAnswers[i]
            tokens = tokens.concat(group.split(' '));
        }
        const flags = quizRegime === quizRegimes.FORWARD ? 'i' : undefined;
        const answerCoversAtLeastOneToken = tokens.reduce(
            (disj, token) => disj || answer.match(new RegExp(`^${token}`, flags)) != null, 
            false
        );
        const regExpStr = `(^${answer}| ${answer})`;
        const answerIsContainedInGroup = !!allAnswersInString.match(new RegExp(regExpStr, flags));
        const matched = answerCoversAtLeastOneToken && answerIsContainedInGroup;
        return matched;
    }

    matchRelaxed(rightAnswers, answer) {
        let tokens = [];
        for (let i = 0, len = rightAnswers.length; i < len; ++i) {
            const group = rightAnswers[i]
            tokens = tokens.concat(group.split(' '));
        }

        const answerCoincideWithSomeWord = tokens.reduce(
            (disj, token) => disj || answer === token, 
            false,
        );
        if (answerCoincideWithSomeWord)
            return true;

        if (answer.length < 3)
            return false;

        const quizRegime = this.getNextQuizRegime();
        const allAnswersInString = rightAnswers.join(' ');
        const flags = quizRegime === quizRegimes.FORWARD ? 'i' : undefined;
        const regExpStr = `(^${answer}| ${answer})`;
        const answerIsContainedInGroup = !!allAnswersInString.match(new RegExp(regExpStr, flags));
        const matched = answerIsContainedInGroup;
        return matched;
    }

    matchLoosened(rightAnswers, answer) {
        const quizRegime = this.getNextQuizRegime();
        return quizRegime === quizRegimes.BACKWARD
            ? this.matchStrict(rightAnswers, answer)
            : this.matchRelaxed(rightAnswers, answer);
    }

    toQuizRun() {
        const remainingRecordIds = this.records.slice(this.nextIndex)
            .map(record => record.id);
        return {
            id: this.id,
            quizId: this.quizId,
            quizRegime: this.quizRegime,
            matchingRegime: this.matchingRegime,
            records: remainingRecordIds,
            doneRecords: this.doneRecords,
        };
    }
}   
