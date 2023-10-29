import React from 'react';
import PropTypes from 'prop-types';
import QuizSettingsDialogBase from '../../../QuizSettingsDialogBase/QuizSettingsDialogBase';

const QuizSettingsDialog = props => {
    const { close, quizId } = props;
    
    return <QuizSettingsDialogBase
        close={close}
        quizId={+quizId}
    />;
};

QuizSettingsDialog.propTypes = {
    close: PropTypes.func.isRequired,
    quizId: PropTypes.string.isRequired,
};

export default QuizSettingsDialog;
