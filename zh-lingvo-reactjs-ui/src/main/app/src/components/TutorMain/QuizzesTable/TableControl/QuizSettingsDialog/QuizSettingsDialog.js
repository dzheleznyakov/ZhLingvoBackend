import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import { selectedQuizSelector } from '../../../../../store/selectors';
import QuizSettingsDialogBase from '../../../../QuizOverview/QuizSettingsDialogBase/QuizSettingsDialogBase';

const QuizSettingsDialog = props => {
    const { close } = props;
    const quiz = useSelector(selectedQuizSelector);
    const quizId = quiz.id;

    return <QuizSettingsDialogBase
        close={close}
        quizId={quizId} 
    />;
};

QuizSettingsDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default QuizSettingsDialog;
