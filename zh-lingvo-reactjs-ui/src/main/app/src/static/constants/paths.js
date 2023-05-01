export const ROOT = '/';
export const AUTH = '/auth';

export const DICTIONARIES_ROOT = '/dictionaries';
export const DICTIONARY = `${DICTIONARIES_ROOT}/:id/:wordMainForm?`;

export const TUTOR_ROOT = '/tutor';
export const TUTOR_QUIZ = '/tutor/quiz/:qid/:rid?';
export const TUTOR_QUIZ_RUN = '/quiz/:qid/run';
export const TUTOR_QUIZ_RUN_RESULT = `${TUTOR_QUIZ_RUN}/result`;
