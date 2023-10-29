import React, { useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { 
    actionButtonTypes,
    DialogBase, 
    FormBase, 
    formInputTypes, 
    validators, 
} from '../../../UI';
import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import { MEANING_TO_QUIZ_RECORD__CHOOSE_QUIZ } from '../../../../static/constants/wordEditModalTypes';
import { useAutofocus } from '../../../../hooks';

const MeaningToQuizRecordDialog_NewQuiz = () => {
    const { language } = useSelector(selectors.loadedDictionarySelector);
    const path = useSelector(selectors.wordEditPathSelector);
    const [valid, setValid] = useState(false);
    const [creatingNewQuiz, setCreatingNewQuiz] = useState(false);
    const dispatch = useDispatch();

    const quizGroup = {
        key: 'quiz',
        label: 'New Quiz',
    };

    const nameRef = useRef();
    const quizNameField = {
        key: 'name',
        label: 'Quiz name',
        type: formInputTypes.TEXT,
        defaultValue: '',
        groupKey: quizGroup.key,
        forwardRef: nameRef,
        disabled: creatingNewQuiz,
        validation: [{
            validate: validators.minLength(3),
            failureMessage: 'Quiz name should contain at least 3 characters',
        }],
    };

    const langRef = useRef(actions.setWordEditModalType);
    const langField = {
        key: 'lang',
        label: 'Target language',
        type: formInputTypes.SELECT,
        defaultValue: language.name,
        values: [language.name],
        groupKey: quizGroup.key,
        forwardRef: langRef,
        disabled: true,
    };

    useAutofocus(nameRef);

    const onBack = () => dispatch(actions.setWordEditModalType(MEANING_TO_QUIZ_RECORD__CHOOSE_QUIZ, path));
    const onOk = () => {
        setCreatingNewQuiz(true);
        const name = nameRef.current.value;
        dispatch(actions.createQuizForMeaningToQuizRecord(name, language));
    };
    const onCancel = () => dispatch(actions.shouldShowWordEditModal(false));
    const { CONFIRM, CANCEL } = actionButtonTypes;
    const buttons = [
        { type: CANCEL, onClicked: onBack, disabled: creatingNewQuiz, label: 'Back' },
        { type: CONFIRM, onClicked: onOk, disabled: !valid || creatingNewQuiz, label: 'OK' },
        { type: CANCEL, onClicked: onCancel, disabled: creatingNewQuiz, label: 'Cancel' },
    ];

    return (
        <DialogBase buttons={buttons}>
            <FormBase 
                groups={[quizGroup]}
                fields={[quizNameField, langField]}
                setValid={setValid}
            />
        </DialogBase>
    );
};

export default MeaningToQuizRecordDialog_NewQuiz;
