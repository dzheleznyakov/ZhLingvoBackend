import { assert } from 'chai';
import _ from 'lodash';

import QuizRunner from '../QuizRunner';
import { QUIZ, QUIZ_RECORDS } from './QuizRunnerTestData';
import quizRegimes from '../../../../../static/constants/quizRegimes';
import matchingRegimes from '../../../../../static/constants/matchingRegimes';

describe('QuizRunner', () => {
    describe('New QuizRunner', () => {
        test('should create new QuizRunner from Quiz', () => {
            const quizRunner = QuizRunner.fromQuiz(QUIZ());

            assert.exists(quizRunner);
            assert.instanceOf(quizRunner, QuizRunner);
        });

        test('should not include into run records with current score 1', () => {
            const quizRunner = QuizRunner.fromQuiz(QUIZ());

            quizRunner.initRun();
            const recordIds = quizRunner.records
                .map(record => record.id);

            const recordIdsUnordered = recordIds.reduce((map, id) => ({ ...map, [id]: '' }), {});
            const quizRecords = QUIZ_RECORDS();
            assert.deepEqual(recordIdsUnordered, {
                [quizRecords[0].id]: '',
                [quizRecords[1].id]: '',
                [quizRecords[2].id]: '',
            })
        });

        test('should shuffle records in the run', () => {
            let prevRunIds = null;
            let equalCount = 0;
            for (let i = 0; i < 1000; ++i) {
                const quizRunner = QuizRunner.fromQuiz(QUIZ());
                quizRunner.initRun();
                const recordIds = quizRunner.ids;
                if (prevRunIds == null)
                    prevRunIds = recordIds;
                else {
                    equalCount = _.isEqual(prevRunIds, recordIds) ? equalCount + 1 : equalCount;
                    prevRunIds = recordIds;
                }
            }
            assert.isBelow(equalCount, 999);
        });
    });

    describe('QuizRunner quiz regimes', () => {
        let recordsByIds;

        beforeAll(() => {
            recordsByIds = QUIZ_RECORDS()
                .reduce((map, record) => ({ ...map, [record.id]: record }), {});
        });

        test('under FORWARD quiz regime, should question all records in FORWARD direction', () => {
            const quiz = QUIZ();
            quiz.quizRegime = quizRegimes.FORWARD;

            const quizRunner = QuizRunner.fromQuiz(quiz);
            quizRunner.initRun();

            assertNextQuestionRegime(quizRunner, quizRegimes.FORWARD);
            assertNextQuestionRegime(quizRunner, quizRegimes.FORWARD);
            assertNextQuestionRegime(quizRunner, quizRegimes.FORWARD);
        });

        test('under BACKWARD quiz regime, should question all records in BACWARD direction', () => {
            const quiz = QUIZ();
            quiz.quizRegime = quizRegimes.BACKWARD;

            const quizRunner = QuizRunner.fromQuiz(quiz);
            quizRunner.initRun();

            assertNextQuestionRegime(quizRunner, quizRegimes.BACKWARD);
            assertNextQuestionRegime(quizRunner, quizRegimes.BACKWARD);
            assertNextQuestionRegime(quizRunner, quizRegimes.BACKWARD);
        });

        test('under ALTERNATING quiz regime, should question all records alternaiting FORWARD and BACWARD direction, starting from FORWARD', () => {
            const quiz = QUIZ();
            quiz.quizRegime = quizRegimes.ALTERNATING;

            const quizRunner = QuizRunner.fromQuiz(quiz);
            quizRunner.initRun();

            assertNextQuestionRegime(quizRunner, quizRegimes.FORWARD);
            assertNextQuestionRegime(quizRunner, quizRegimes.BACKWARD);
            assertNextQuestionRegime(quizRunner, quizRegimes.FORWARD);
        });

        test('under FORWARD quiz regime, should accept translation as correct answer', () => {
            const quiz = QUIZ();
            quiz.quizRegime = quizRegimes.FORWARD;
            
            const quizRunner = QuizRunner.fromQuiz(quiz);
            quizRunner.initRun();

            assertNextQuestionForwardEvaluationRegime(quizRunner);
            assertNextQuestionForwardEvaluationRegime(quizRunner);
            assertNextQuestionForwardEvaluationRegime(quizRunner);
        });

        test('under BACKWARD quiz regime, should accept word main form as correct answer', () => {
            const quiz = QUIZ();
            quiz.quizRegime = quizRegimes.BACKWARD;

            const quizRunner = QuizRunner.fromQuiz(quiz);
            quizRunner.initRun();

            assertNextQuestionBackwardEvaluationRegime(quizRunner);
            assertNextQuestionBackwardEvaluationRegime(quizRunner);
            assertNextQuestionBackwardEvaluationRegime(quizRunner);
        });

        test('under ALTERNATING quiz regime, should accept answers in FORWARD and BACWARD regimes in turns', () => {
            const quiz = QUIZ();
            quiz.quizRegime = quizRegimes.ALTERNATING;

            const quizRunner = QuizRunner.fromQuiz(quiz);
            quizRunner.initRun;

            assertNextQuestionForwardEvaluationRegime(quizRunner);
            assertNextQuestionBackwardEvaluationRegime(quizRunner);
            assertNextQuestionForwardEvaluationRegime(quizRunner);
        });

        const assertNextQuestionRegime = (quizRunner, expectedQuizRegime) => {
            const question = quizRunner.next();
            quizRunner.evaluate('');

            assert.strictEqual(question.quizRegime, expectedQuizRegime);
        };

        const assertNextQuestionForwardEvaluationRegime = (quizRunner) => {
            const question = quizRunner.next();
            const { recordId } = question;
            const record = recordsByIds[recordId];
            const answer = record.translations[0];

            const result = quizRunner.evaluate(answer);
            assert.isTrue(result);
        };

        const assertNextQuestionBackwardEvaluationRegime = (quizRunner) => {
            const question = quizRunner.next();
            const { recordId } = question;
            const record = recordsByIds[recordId];
            const answer = record.wordMainForm;

            const result = quizRunner.evaluate(answer);
            assert.isTrue(result);
        };
    });

    describe('QuizRunner matching regimes', () => {
        const quiz = QUIZ();
        const record = QUIZ_RECORDS()[0];
        const FIRST_WORD = 'acuna';
        const SECOND_WORD = 'matata';
        const THRID_WORD = 'pumba';
        const TRANSLATION_1_1 = 'Lorem';
        const TRANSLATION_1_2 = 'ipsum';
        const TRANSLATION_2_1 = 'dolor';
        const TRANSLATION_2_2 = 'sit';
        const TRANSLATION_2_3 = 'amet';
        const TRANSLATION_3_1 = 'consectetur';
        const TRANSLATION_3_2 = 'adipiscing';
        const TRANSLATION_3_3 = 'elit';

        beforeEach(() => {
            record.wordMainForm = `${FIRST_WORD} ${SECOND_WORD} ${THRID_WORD}`;
            record.translations = [
                `${TRANSLATION_1_1} ${TRANSLATION_1_2}`,
                `${TRANSLATION_2_1} ${TRANSLATION_2_2} ${TRANSLATION_2_3}`,
                `${TRANSLATION_3_1} ${TRANSLATION_3_2} ${TRANSLATION_3_3}`,
            ]
            quiz.records = [record];
        });

        const wordMainFormFullWordsAnswers = [FIRST_WORD, SECOND_WORD, THRID_WORD];
        const wordMainFormCombinedAnswersCore = [
            {
                answer: FIRST_WORD + ' ' + SECOND_WORD.substring(0, SECOND_WORD.length / 2),
                expected: true,
            }, 
            {
                answer: SECOND_WORD + ' ' + THRID_WORD.substring(0, THRID_WORD.length / 2),
                expected: true,
            },
            {
                answer: FIRST_WORD + ' ' + SECOND_WORD + ' ' + THRID_WORD.substring(0, THRID_WORD.length / 2),
                expected: true,
            },
            {
                answer: FIRST_WORD + ' ' + THRID_WORD.substring(0, THRID_WORD.length / 2),
                expected: false,
            },
            {
                answer: SECOND_WORD + ' abc',
                expected: false,
            },
            {
                answer: FIRST_WORD + SECOND_WORD,
                expected: false,
            },
            {
                answer: FIRST_WORD.substring(FIRST_WORD.length / 2) + ' ' + SECOND_WORD,
                expected: false,
            },
        ];
        const wordMainFormCombinedAnswersStrict = [
            ...wordMainFormCombinedAnswersCore,
            {
                answer: FIRST_WORD.substring(FIRST_WORD.length / 2),
                expected: false,
            },
        ];
        const wordMainFormCombinedAnswersRelaxed = [
            ...wordMainFormCombinedAnswersCore,
            {
                answer: SECOND_WORD.substring(0, SECOND_WORD.length / 2),
                expected: true,
            },
            {
                answer: FIRST_WORD.substring(0, 2),
                expected: false,
            },
        ];
        const translationsFullWordsAnswers = [TRANSLATION_1_1, TRANSLATION_3_2, TRANSLATION_1_1.toLowerCase()];
        const translationCombineAnswers = [
            {
                answer: TRANSLATION_1_1 + ' ' + TRANSLATION_1_2.substring(0, TRANSLATION_1_2.length / 2),
                expected: true,
            },
            {
                answer: TRANSLATION_2_2 + ' ' + TRANSLATION_2_3.substring(0, TRANSLATION_2_3.length / 2),
                expected: true,
            },
            {
                answer: TRANSLATION_3_1 + ' ' + TRANSLATION_3_2 + ' ' + TRANSLATION_3_3.substring(0, THRID_WORD.length / 2),
                expected: true,
            },
            {
                answer: TRANSLATION_2_1 + ' ' + TRANSLATION_2_3.substring(0, THRID_WORD.length / 2),
                expected: false,
            },
            {
                answer: TRANSLATION_2_2 + ' abc',
                expected: false,
            },
            {
                answer: TRANSLATION_3_1 + TRANSLATION_3_2,
                expected: false,
            },
            {
                answer: TRANSLATION_1_1.substring(TRANSLATION_1_1.length / 2) + ' ' + TRANSLATION_1_2,
                expected: false,
            },
            {
                answer: TRANSLATION_1_2 + ' ' + TRANSLATION_2_1,
                expected: true,
            },
        ];

        describe('STRICT matching regime', () => {
            beforeEach(() => {
                quiz.matchingRegime = matchingRegimes.STRICT;
            });

            describe.each(wordMainFormFullWordsAnswers)('The full word answer', answer => {
                it(`[${answer}] should be evaluated correctly under BACWARD quiz regime`, () => {
                    assertAnswerEvaluation(quizRegimes.BACKWARD, answer, true);
                });
            });

            describe.each(wordMainFormCombinedAnswersStrict)('An answer', input => {
                it(`[${input.answer}] should be evaluated correctly under BACWARD quiz regime`, () => {
                    const { answer, expected } = input;
                    assertAnswerEvaluation(quizRegimes.BACKWARD, answer, expected);
                });
            });

            describe.each(translationsFullWordsAnswers)('The full word answer', answer => {
                it(`[${answer}] should be evaluated correctly under FORWARD quiz regime`, () => {
                    assertAnswerEvaluation(quizRegimes.FORWARD, answer, true);
                });
            });

            describe.each(translationCombineAnswers)('The answer', input => {
                it(`[${input.answer}] should be evaluated correcly under FORWARD quiz regime`, () => {
                    const { answer, expected } = input;
                    assertAnswerEvaluation(quizRegimes.FORWARD, answer, expected);
                } );
            });
        });

        describe('RELAXED matching regime', () => {
            beforeEach(() => {
                quiz.matchingRegime = matchingRegimes.RELAXED;
            });

            describe.each(wordMainFormCombinedAnswersRelaxed)('The answer', input => {
                it(`[${input.answer}] should be evaluated correctly under BACWARD quiz regime`, () => {
                    const { answer, expected } = input;
                    assertAnswerEvaluation(quizRegimes.BACKWARD, answer, expected);
                });
            });
        });

        const assertAnswerEvaluation = (quizRegime, answer, expectedIsCorrect) => {
            const quizRunner = prepareEvaluation(quizRegime);

            const actualIsCorrect = quizRunner.evaluate(answer);

            assert.equal(actualIsCorrect, expectedIsCorrect, `Answer [${answer}] was mistakenly evaluated as ${
                expectedIsCorrect ? 'incorrect' : 'correct'
            }`);
        };

        const prepareEvaluation = (quizRegime) => {
            quiz.quizRegime = quizRegime;
            const quizRunner = QuizRunner.fromQuiz(quiz);
            quizRunner.initRun();
            quizRunner.next();

            return quizRunner;
        };
    });

    describe('Converting QuizRunner into QuizRun', () => {
        test('should have null id if QuizRunner is created from a Quiz', () => {
            const quiz = QUIZ();
            const quizRunner = QuizRunner.fromQuiz(quiz);
            quizRunner.initRun();

            const quizRun = quizRunner.toQuizRun();

            assert.isNull(quizRun.id);
        });

        test('should have only remaining records', () => {
            const quizRunner = QuizRunner.fromQuiz(QUIZ());
            quizRunner.initRun();

            const question = quizRunner.next();
            quizRunner.evaluate('');

            const quizRun = quizRunner.toQuizRun();

            const expectedRemainingRecordIds = QUIZ_RECORDS()
                .filter(record => record.currentScore < 1)
                .map(record => record.id)
                .filter(id => id !== question.recordId)
                .reduce((map, id) => ({ ...map, [id]: '' }), {});
            const actualRemainingRecordIds = quizRun.records
                .reduce((map, id) => ({ ...map, [id]: '' }), {});

            assert.deepEqual(actualRemainingRecordIds, expectedRemainingRecordIds);
        });

        test('should have processed records in doneRecords', () => {
            const quizRunner = QuizRunner.fromQuiz(QUIZ());
            quizRunner.initRun();

            const question = quizRunner.next();
            quizRunner.evaluate('');

            const quizRun = quizRunner.toQuizRun();

            const expectedDoneRecords = [{
                recordId: question.recordId,
                correct: false,
            }];
            const actualDoneRecords = quizRun.doneRecords;
            
            assert.deepEqual(actualDoneRecords, expectedDoneRecords);
        });
    });
});
