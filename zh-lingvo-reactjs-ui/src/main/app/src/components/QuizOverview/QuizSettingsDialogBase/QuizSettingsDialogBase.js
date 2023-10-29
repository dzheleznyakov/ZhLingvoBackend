import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import _ from 'lodash';

import { Form, formInputTypes, Spinner, validators } from '../../UI';
import { useAutofocus, useConditionalActionOnMount } from '../../../hooks';
import * as actions from '../../../store/actions/';
import { matchingRegimesSelector, quizRegimeSelector, quizSettingsSelector } from '../../../store/selectors';

const MIN_MAX_SCORE = 3;
const NULL_SETTINGS = {
    maxScore: MIN_MAX_SCORE,
    quizRegime: '',
    matchingRegime: '',
};   

const QuizSettingsDialogBase = props => {
    const { close, quizId } = props;
    const dispatch = useDispatch();
    const matchingRegimes = useSelector(matchingRegimesSelector);
    const quizRegimes = useSelector(quizRegimeSelector);
    const settingsMap = useSelector(quizSettingsSelector);
    const settings = settingsMap ? settingsMap[quizId] || NULL_SETTINGS : NULL_SETTINGS;
    const loading = matchingRegimes.length === 0 
        || quizRegimes.length === 0 
        || settings === NULL_SETTINGS;

    useConditionalActionOnMount(
        actions.fetchMatchingRegimes(), 
        matchingRegimes.length === 0,
        matchingRegimes);
    useConditionalActionOnMount(
        actions.fetchQuizRegimes(),
        quizRegimes.length === 0,
        quizRegimes);
    useConditionalActionOnMount(
        actions.fetchQuizSettings(quizId),
        settings === NULL_SETTINGS,
        settings);
    
    const settingsGroup = {
        key: 'settings',
        label: 'Quiz Settings',
    };
    
    const maxScoreRef = useRef();
    const maxScoreField = {
        key: 'maxSxore',
        label: 'Max. Score',
        type: formInputTypes.NUMBER,
        defaultValue: settings.maxScore || MIN_MAX_SCORE,
        groupKey: settingsGroup.key,
        forwardRef: maxScoreRef,
        validation: [{
            validate: validators.minValue(MIN_MAX_SCORE),
            failureMessage: 'Maximal score should be at least 3',
        }]        
    };

    const defaultQuizRegime = (quizRegimes.find(({ code }) => code === settings.quizRegime) || {}).value;
    const quizRegimeRef = useRef();
    const quizRegimeField = {
        key: 'quizRegime',
        label: 'Quiz Regime',
        type: formInputTypes.SELECT,
        values: quizRegimes.map(({ value }) => value),
        defaultValue: defaultQuizRegime,
        groupKey: settingsGroup.key,
        forwardRef: quizRegimeRef,
    };

    const defaultMatchingRegime = (matchingRegimes.find(({ code }) => code === settings.matchingRegime) || {}).value;
    const matchingRegimeRef = useRef();
    const matchingRegimeField = {
        key: 'matchingRegime',
        label: 'Matching Regime',
        type: formInputTypes.SELECT,
        values: matchingRegimes.map(({ value }) => value),
        defaultValue: defaultMatchingRegime,
        groupKey: settingsGroup.key,
        forwardRef: matchingRegimeRef,
    };

    useAutofocus(maxScoreRef);

    const onConfirm = () => {
        const maxScore = +maxScoreRef.current.value;
        const quizRegime = quizRegimes.find(qr => qr.value === quizRegimeRef.current.value).code;
        const matchingRegime = matchingRegimes.find(mr => mr.value === matchingRegimeRef.current.value).code;
        const updatedSettings = {
            quizId,
            maxScore,
            quizRegime,
            matchingRegime,
        };
        if (!_.isEqual(settings, updatedSettings))
            dispatch(actions.updateQuizSettings(quizId, updatedSettings));
    };

    return loading ? <Spinner /> : <Form 
        groups={[settingsGroup]}
        fields={[maxScoreField, quizRegimeField, matchingRegimeField]}
        canceled={close}
        confirmed={onConfirm}
    />;
};

QuizSettingsDialogBase.propTypes = {
    close: PropTypes.func.isRequired,
    quizId: PropTypes.number.isRequired,
};

export default QuizSettingsDialogBase;
